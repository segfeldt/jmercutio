package jmercutio.terminfo.escape;

import java.io.Serializable;

public interface EscapeSequence extends Serializable {

	void apply(TerminfoState state);
}
