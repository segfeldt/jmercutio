package jmercutio.terminfo.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jmercutio.terminfo.BooleanCapability;
import jmercutio.terminfo.TermInfo;
import jmercutio.terminfo.TerminalNames;
import jmercutio.terminfo.escape.EscapeSequence;
import jmercutio.terminfo.escape.EscapeSequenceFactory;
import jmercutio.util.FlyweightFactory;

public class TerminfoSourceParser {

	private Map<String, TermInfo> termInfos = new HashMap<>();
	
	private TermInfo termInfo;

	private FlyweightFactory<String> capabilities = new FlyweightFactory<>();
	
	private EscapeSequenceFactory escapeSequenceFactory = new EscapeSequenceFactory();
	
	public void startEntry(TerminalNames terminalNames) {
		finishCurrent();
		this.termInfo = new TermInfo(terminalNames);
	}

	public void addNumericalCapability(String capabilityName, int value) {
		capabilityName = capabilities.get(capabilityName);
		
		// TODO null check
		termInfo.addCapability(capabilityName, value);
	}

	public void addStringCapability(String capabilityName, byte[] value) {
		capabilityName = capabilities.get(capabilityName);
		
		capabilities.get(capabilityName);
		EscapeSequence escapeSequence = escapeSequenceFactory.literal(value);
		
		// TODO null check
		termInfo.addCapability(capabilityName, escapeSequence);
	}

	public void addBooleanCapability(BooleanCapability capabilityName, boolean value) {
		
		// TODO null check
		termInfo.addCapability(capabilityName, value);
	}

	private void finishCurrent() {
		if (termInfo != null) {
			// TODO collision reporting
			termInfos.putIfAbsent(termInfo.getTerminalNames().getName(), termInfo);
			for (String alias : termInfo.getTerminalNames().getAliases()) {
				termInfos.putIfAbsent(alias, termInfo);	
			}
			
			System.out.println(termInfo);
		}		
		termInfo = null;
	}

	public void finish() {
		finishCurrent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			oos.writeObject(termInfos);
			oos.flush();
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.out.println();
		System.out.println("size: " + bos.size());
		System.out.println();
	}
}
