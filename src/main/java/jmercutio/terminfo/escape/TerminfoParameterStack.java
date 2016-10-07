package jmercutio.terminfo.escape;

import java.util.Stack;

public class TerminfoParameterStack {

	private static final byte[] emptyStr = new byte[0];
	
	
	private class Element {
		final int number;
		final byte[] str;
		
		Element(int number) {
			super();
			this.number = number;
			this.str = emptyStr;
		}
		
		Element(byte[] str) {
			super();
			this.number = 0;
			this.str = str;
		}
		
	}
	
	private final Stack<Element> stack = new Stack<>();

	public void pushNumber(boolean b) {
		stack.push(new Element(b ? 1 : 0));
	}
	
	public void pushNumber(int number) {
		stack.push(new Element(number));
	}

	public int popNumber() {
		return stack.pop().number;
	}

	public void pushString(byte[] bytes) {
		stack.push(new Element(bytes));
	}

	public byte[] popString() {
		return stack.pop().str;
	}
	
}
