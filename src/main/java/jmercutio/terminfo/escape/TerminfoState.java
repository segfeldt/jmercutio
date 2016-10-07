package jmercutio.terminfo.escape;

public class TerminfoState {

	private final TerminfoParameterStack stack = new TerminfoParameterStack();

	public TerminfoParameterStack stack() {
		return stack;
	}
}
