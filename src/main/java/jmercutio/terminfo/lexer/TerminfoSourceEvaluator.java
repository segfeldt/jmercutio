package jmercutio.terminfo.lexer;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

import jmercutio.terminfo.BooleanCapability;
import jmercutio.terminfo.TerminalNames;
import jmercutio.terminfo.parser.TerminfoSourceParser;

class TerminfoSourceEvaluator {

	private TerminfoSourceParser parser = new TerminfoSourceParser();

	private Pattern pattern = Pattern.compile("[ A-Z]");

	public enum FieldType {
		TERMINAL_NAME,
		CAPABILITY
	}
	
	void processField(FieldType fieldType, byte[] field, int fieldLength) {
		
		switch (fieldType) {
		case TERMINAL_NAME:
			processTerminalNames(field, fieldLength);
			return;
		case CAPABILITY:
			processCapability(field, fieldLength);
			return;
		}
	}

	private void processTerminalNames(byte[] field, int fieldLength) {
		String s = new String(field, 0, fieldLength, StandardCharsets.US_ASCII);
		String[] names = s.split("\\|");

		if (names.length == 0) {
			throw new IllegalStateException("empty terminal name");
		}

		int numNames = names.length;

		String description = names[numNames-1];
		if (pattern.matcher(description).find()) {
			// this is a descriptive name, exclude from the aliases
			--numNames;
		}

		String name = names[0];
		Set<String> aliases = null;
		for (int i = 1; i < numNames; i++) {
			if (aliases == null) {
				aliases = new LinkedHashSet<>();
			}
			aliases.add(names[i]);
		}
		TerminalNames terminalNames = new TerminalNames(name, aliases, description);
		
		parser.startEntry(terminalNames);
	}

	private void processCapability(byte[] field, int fieldLength) {
		
		// This handles the following:
		// "Sometimes individual capabilities must be commented out. To do this, put a period before the capability name."
		if (field[0] == '.') {
			return;
		}
		
		for (int i = 0; i < fieldLength; i++) {
			if (field[i] == '#') {
				processNumericalCapability(field, fieldLength, i);
				return;
			} else if (field[i] == '=') {
				processStringCapability(field, fieldLength, i);
				return;
			}
		}
		processBooleanCapability(field, fieldLength);
	}

	private void processNumericalCapability(byte[] field, int fieldLength, int separatorPos) {
		String capabilityName = new String(field, 0, separatorPos, StandardCharsets.US_ASCII);
		// e.g. cols#80
		//      0123456
		// fieldLength = 7
		// separatorPos = 4
		// offset = separatorPos + 1 = 4 + 1 = 5
		// length = fieldLength - offset = 7 - 5 = 2
		int offset = separatorPos + 1;
		int length = fieldLength - offset;
		String numberString = new String(field, offset, length, StandardCharsets.US_ASCII);
		
		int value;
		if (numberString.startsWith("0")) {
			value = Integer.parseInt(numberString, 8);
		} else if (numberString.startsWith("0x")) {
			value = Integer.parseInt(numberString.substring(2), 16);
		} else {
			value = Integer.parseInt(numberString, 10);
		}
		
		parser.addNumericalCapability(capabilityName, value);
	}

	private void processStringCapability(byte[] field, int fieldLength, int separatorPos) {
		String capabilityName = new String(field, 0, separatorPos, StandardCharsets.US_ASCII);
		
		int offset = separatorPos + 1;
		int length = fieldLength - offset;

		// TODO delay processing, parameter processing
		
		byte[] value = new byte[length];
		System.arraycopy(field, offset, value, 0, length);
		parser.addStringCapability(capabilityName, value);
	}

	private void processBooleanCapability(byte[] field, int fieldLength) {
		boolean value = field[fieldLength-1] != '@';		
		String capabilityName;
		if (value == true) {
			capabilityName = new String(field, 0, fieldLength, StandardCharsets.US_ASCII);
		} else {
			capabilityName = new String(field, 0, fieldLength-1, StandardCharsets.US_ASCII);
		}
		
		BooleanCapability cap = BooleanCapability.fromTerminfo(capabilityName);
		if (cap == null) {
			throw new RuntimeException("Unknown boolean capability " + capabilityName);
		}
		parser.addBooleanCapability(cap, value);
	}

	public void finish() {
		parser.finish();
	}
}
