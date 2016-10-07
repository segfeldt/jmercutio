package jmercutio.terminfo.escape;

import java.util.HashMap;
import java.util.Map;

import jmercutio.util.FlyweightFactory;

public class EscapeSequenceFactory {

	private final Map<String, EscapeSequence> singleSymbolOperations = new HashMap<>();

	private FlyweightFactory<LiteralEscapeSequence> literals = new FlyweightFactory<>();
	
	public EscapeSequenceFactory() {
		operation(new AddOperation());
		operation(new AndOperation());
		operation(new DivideOperation());
		operation(new EqualsOperation());
		operation(new GreaterThanOperation());
		operation(new LessThanOperation());
		operation(new ModuloOperation());
		operation(new MultiplyOperation());
		operation(new OrOperation());
		operation(new SubtractOperation());
		operation(new XorOperation());
	}

	private void operation(EscapeSequence escapeSequence) {
		singleSymbolOperations.put(escapeSequence.toString(), escapeSequence);
	}
	
	/**
	 * Looks up the operation by printf-style escape characters, only covers those consisting of
	 * a single symbol e.g. %+, %^ ...
	 * @param op printf-style encoding, including the %
	 * @return translated escape sequence, or null if there is no match
	 */
	public EscapeSequence singleSymbolOperation(String op) {
		return singleSymbolOperations.get(op);
	}

	public EscapeSequence literal(byte[] sequence) {
		return literals.get(new LiteralEscapeSequence(sequence));
	}
}
