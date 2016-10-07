package jmercutio.terminfo.escape;

public class JoinedEscapeSequence implements EscapeSequence {

	private static final long serialVersionUID = 1L;

	private final EscapeSequence[] escapeSequences;
	
	public JoinedEscapeSequence(EscapeSequence[] escapeSequences) {
		super();
		this.escapeSequences = escapeSequences;
	}

	@Override
	public void apply(TerminfoState state) {
		for (EscapeSequence escapeSequence : escapeSequences) {
			escapeSequence.apply(state);
		}
	}
}
