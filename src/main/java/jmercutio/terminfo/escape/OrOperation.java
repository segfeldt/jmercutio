package jmercutio.terminfo.escape;

class OrOperation implements EscapeSequence {

	private static final long serialVersionUID = 1L;

	@Override
	public void apply(TerminfoState state) {
		TerminfoParameterStack stack = state.stack();
		stack.pushNumber(stack.popNumber() | stack.popNumber());
	}

	@Override
	public String toString() {
		return "%|";
	}
}
