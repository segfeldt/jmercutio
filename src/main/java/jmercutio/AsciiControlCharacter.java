package jmercutio;

public enum AsciiControlCharacter {
	NUL("^@", 0, "Null"),
	SOH("^A", 1, "Start of Heading"),
	STX("^B", 2, "Start of Text"),
	ETX("^C", 3, "End of Text"),
	EOT("^D", 4, "End of Transmission"),
	ENQ("^E", 5, "Enquiry"),
	ACK("^F", 6, "Acknowledge"),
	BEL("^G", 7, "Bell"),
	BS("^H", 8, "Backspace"),
	HT("^I", 9, "Horizontal Tabulation"),
	LF("^J", 10, "Line Feed"),
	VT("^K", 11, "Vertical Tabulation"),
	FF("^L", 12, "Form Feed"),
	CR("^M", 13, "Carriage Return"),
	SO("^N", 14, "Shift Out"),
	SI("^O", 15, "Shift In"),
	DLE("P^", 16, "Data Link Escape"),
	DC1("^Q", 17, "XON"),
	DC2("^R", 18, "Device Control 2"),
	DC3("^S", 19, "XOFF"),
	DC4("^T", 20, "Device Control 4"),
	NAK("^U", 21, "Negative Acknowledge"),
	SYN("^V", 22, "Synchronous Idle"),
	ETB("^W", 23, "End of Transmission Block"),
	CAN("^X", 24, "Cancel"),
	EM("^Y", 25, "End of medium"),
	SUB("^Z", 26, "Substitute"),
	ESC("^[", 27, "Escape"),
	FS("^\\", 28, "File Separator"),
	GS("^]", 29, "Group Separator"),
	RS("^^", 30, "Record Separator"),
	US("^_", 31, "Unit Separator"),
	DEL("^?", 127, "Delete");
	
	private final String caret;
	private final int codepoint;
	private final String fullName;
	
	private AsciiControlCharacter(
			String caret,
			int codepoint,
			String fullName) {
		this.caret = caret;
		this.codepoint = codepoint;
		this.fullName= fullName;
	}
	
	public String caretString() {
		return this.caret;
	}
	
	public char caretChar() {
		return caret.charAt(1);
	}
	
	public int codepoint() {
		return codepoint;
	}
	
	public String fullName() {
		return fullName;
	}
	
	public static AsciiControlCharacter fromCodepoint(int codepoint) {
		if (codepoint <= 31) {
			return values()[codepoint];
		} else if (codepoint == 127) {
			return DEL;
		} else {
			throw new IllegalArgumentException("not an ASCII control codepoint: " + codepoint);
		}
	}
}
