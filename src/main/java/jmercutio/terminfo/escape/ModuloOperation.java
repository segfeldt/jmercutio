package jmercutio.terminfo.escape;

class ModuloOperation implements EscapeSequence {

	private static final long serialVersionUID = 1L;

	@Override
	public void apply(TerminfoState state) {
		TerminfoParameterStack stack = state.stack();
		int y = stack.popNumber();
		int x = stack.popNumber();
		stack.pushNumber(y == 0 ? (x % y) : 0);
	}

	@Override
	public String toString() {
		return "%m";
	}
}
