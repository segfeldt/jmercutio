package jmercutio;

public interface KeyboardHandler {

	void asciiControl(AsciiControlCharacter asciiControlCharacter);
	void nonCsiControl(char code);
	void ansiEscapeSequence(int[] escapeSequence, int length);
	void character(char ch);
}
