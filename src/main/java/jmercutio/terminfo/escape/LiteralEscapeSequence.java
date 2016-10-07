package jmercutio.terminfo.escape;

import java.util.Arrays;

class LiteralEscapeSequence implements EscapeSequence {
	
	private static final long serialVersionUID = 1L;

	private final byte[] sequence;

	LiteralEscapeSequence(byte[] sequence) {
		super();
		this.sequence = sequence;
	}

	@Override
	public void apply(TerminfoState state) {
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(sequence);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LiteralEscapeSequence other = (LiteralEscapeSequence) obj;
		if (!Arrays.equals(sequence, other.sequence))
			return false;
		return true;
	}

	/**
	 * 
	 * string representation for display purposes only.
	 * 
	 * ASCII printable characters are included as-is, sequences of non-printable characters are displayed as hexadecimal digits in
	 * parentheses, zero-padded to 2 digits for each octet.
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		boolean startedBinary = false;
		boolean inBinary = false;
		for (byte octet : sequence) {
			if (octet >= 32 && octet <= 127) {
				if (inBinary) {
					string.append(')');
				}
				string.append((char)octet);
				startedBinary = false;
				inBinary = false;
			} else {
				if (!startedBinary) {
					string.append("(");
					startedBinary = true;
					inBinary = true;
				}
				String hex = Integer.toHexString(octet);
				if (hex.length() == 1) {
					string.append('0');
				}				
				string.append(hex);
			}
		}
		if (inBinary) {
			string.append(')');
		}
		return string.toString();
	}

}
