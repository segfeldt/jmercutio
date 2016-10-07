package jmercutio.terminfo.escape;

class AnsiIncrementOperation implements EscapeSequence {

	private static final long serialVersionUID = 1L;

	@Override
	public void apply(TerminfoState state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "%i";
	}
}
