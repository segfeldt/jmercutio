package jmercutio.terminfo;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import jmercutio.terminfo.escape.EscapeSequence;

public class TermInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private final TerminalNames terminalNames;
	
	private final Map<String, EscapeSequence> escapeSequenceCapabilities;
	private final Map<String, Integer> numbericalCapabilities;
	private final EnumSet<BooleanCapability> booleanCapabilities;

	public TermInfo(TerminalNames terminalNames) {
		this.terminalNames = terminalNames;
		escapeSequenceCapabilities = new HashMap<>();
		numbericalCapabilities = new HashMap<>();
		booleanCapabilities = EnumSet.noneOf(BooleanCapability.class);
	}

	public void addCapability(String capabilityName, EscapeSequence escapeSequence) {
		escapeSequenceCapabilities.put(capabilityName, escapeSequence);
	}

	public void addCapability(BooleanCapability capability, boolean value) {
		if (value) {
			booleanCapabilities.add(capability);
		} else {
			booleanCapabilities.remove(capability);
		}
	}

	public void addCapability(String capabilityName, int value) {
		numbericalCapabilities.put(capabilityName, value);
	}

	public TerminalNames getTerminalNames() {
		return terminalNames;
	}

	@Override
	public String toString() {
		return "TermInfo [" + terminalNames + " " + escapeSequenceCapabilities
				+ ", numbers" + numbericalCapabilities + ", boolean" + booleanCapabilities
				+ "]";
	}
}
