package jmercutio.terminfo.escape;

class SubtractOperation implements EscapeSequence {

	private static final long serialVersionUID = 1L;

	@Override
	public void apply(TerminfoState state) {
		TerminfoParameterStack stack = state.stack();
		int y = stack.popNumber();
		int x = stack.popNumber();
		stack.pushNumber(x - y);
	}

	@Override
	public String toString() {
		return "%-";
	}
}
