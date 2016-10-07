package jmercutio.terminfo.lexer;

import java.io.IOException;
import java.io.InputStream;

import jmercutio.terminfo.lexer.TerminfoSourceEvaluator.FieldType;

public class TerminfoSourceScanner {

	public final TerminfoSourceEvaluator evaluator = new TerminfoSourceEvaluator();

	public final String terminfoResourceName;

	public final int tabsize = 7;

	public final boolean nullAs0200 = true;

	boolean skipLeadingWhitespace = true;

	boolean inComment = false;
	boolean inEscape = false;
	int octalBytes = 0;
	int octalValue = 0;
	boolean ctrl = false;

	boolean hasLeadingWhitespace = false;
	byte[] field = new byte[4096];
	int fieldLength = 0;

	private int lineNumber = 1;

	public TerminfoSourceScanner() {
		this("/terminfo.src", true);
	}

	public TerminfoSourceScanner(String terminfoResourceName, boolean nullAs0200) {
		this.terminfoResourceName = terminfoResourceName;
	}

	public void scan() throws IOException {
		byte[] buffer = new byte[4096];

		try (InputStream is = terminfoSource()) {

			do {
				// read into buffer
				int bytesRead = is.read(buffer);
				if (bytesRead == -1) {
					// EOF
					evaluator.finish();
					return;
				}

				for (int i = 0; i < bytesRead; i++) {
					byte b = buffer[i];
					processByte(b);
				}

			} while (true);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to compile on line " + lineNumber, ex);
		}
	}

	private void processByte(byte b) {
		if (skipLeadingWhitespace) {
			if (b != '\n' && Character.isWhitespace(b)) {
				// terminal names begin in first column, so need to make note of leading whitespace
				hasLeadingWhitespace = true;
				return; // leading whitespace, nothing else to process
			}

			// found non-whitespace
			skipLeadingWhitespace = false;
			// comments can only be at beginning of line in terminfo.src, so check for these here:
			if (b == '#') {
				inComment = true;
				return;
			}

			// continue to process anything else
		}

		if (b == '\n') {
			skipLeadingWhitespace = true;
			inComment = false;
			hasLeadingWhitespace = false;
			++lineNumber;
		} else if (ctrl) {
			addToField((byte)(b & 0x1F));
			ctrl = false;
		} else if (inComment) {
			// do nothing until newline
		} else if (!inEscape && b == '\\') {
			inEscape = true;
			octalBytes = 0;
			octalValue = 0;
		} else if (!inEscape && b == '^') {
			ctrl = true;
		} else if (!inEscape && b == ',') {
			FieldType fieldType = hasLeadingWhitespace ? FieldType.CAPABILITY : FieldType.TERMINAL_NAME; 
			evaluator.processField(fieldType, field, fieldLength);
			fieldLength = 0;
		} else if (fieldLength == 0 && Character.isWhitespace(b)) {
			// skip whitespace after separator
		} else if (inEscape) {
			if (b >= '0' && b <= '9') {
				// TODO handle octal escape sequence properly
				octalBytes++;
				octalValue = (octalValue * 8) + (b-'0');
				if (octalBytes == 3) {
					inEscape = false;
					addOctalToField(octalValue);
				}
			} else {
				inEscape = false;
				// finish up the octal escape value, as we have hit a non-digit
				// then process this non-digit character
				if (octalBytes > 0) {
					// by the spec, we can only ever have either a three-digit octal or \0
					if (octalValue != 0) {
						throw new RuntimeException("Incomplete octal value " + Integer.toOctalString(octalValue));
					}
					addOctalToField(octalValue);
					processByte(b);
					return;
				}

				switch (b) {
				case '^':
				case '\\':
				case ',':
				case ':':
					addToField(b);
					break;
				case 'e':
				case 'E':
					addToField(0x27);
					break;
				case 'a':
					addToField(0x07);
					break;
				case 'b':
					addToField(0x08);
					break;
				case 't':
					addToField('\t');
					break;
				case 'r':
					addToField('\r');
					break;
				case 'n':
					addToField('\n');
					break;
				case 'f':
					addToField('\f');
					break;
				case 's':
					addToField(' ');
					break;
				default:
					throw new RuntimeException("Unhandled escape character \\" + (char)b);
				}
			}
		} else {
			addToField(b);
		}
	}

	private void addOctalToField(int octal) {
		// we could just let \0 be NULL 0, rather than 0200, in spite of specification.
		// this is because we are producing our own internal representation and don't need
		// to be compatible with SVr4 binary terminfo. However, we allow the specified behaviour
		// just in case.
		if (octal == 0 && nullAs0200) {
			addToField(0200);
		} else if (octal < 0 || octal > 255) {
			throw new RuntimeException("Octal out of range");
		} else {
			addToField(octal);
		}
	}

	private void addToField(int b) {
		if (fieldLength == field.length) {
			throw new RuntimeException("Field too long: " + new String(field));
		}
		field[fieldLength++] = (byte)b;
	}

	private InputStream terminfoSource() {
		return this.getClass().getResourceAsStream(terminfoResourceName);
	}

}
