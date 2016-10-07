package jmercutio.test;

import org.junit.Test;

import jmercutio.terminfo.lexer.TerminfoSourceScanner;

public class TerminfoSourceParserTests {

	@Test
	public void test() throws Exception {

		TerminfoSourceScanner scanner = new TerminfoSourceScanner();

		scanner.scan();

	}
}
