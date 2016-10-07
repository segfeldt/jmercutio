package jmercutio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuiShell implements Command {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// This delay is based on Internet folklore that the vim timeout is 25ms, and this works reliably even
	// over slow connections. This shell will wait for up to this delay in time before assuming that the
	// escape key sequence is really a series of human key presses
	private static final int ESCAPE_DELAY_MILLIS = 25;

	private InputStream in;
	private OutputStream out;
	private ExitCallback exitCallback;
	private KeyboardHandler keyboardHandler = new KeyboardHandler() {
		
		public void character(char ch) {
			logger.info("Character: {}", (char)ch);
			try {
				out.write(ch);
				out.flush();
			} catch (Exception ex) {				
			}
		}
		
		public void asciiControl(AsciiControlCharacter asciiControlCharacter) {
			logger.info("ASCII Control character: {}", asciiControlCharacter);
			if (asciiControlCharacter == AsciiControlCharacter.EOT) {
				exitCallback.onExit(0, "ctrl+d received");
			}
		}
		
		public void ansiEscapeSequence(int[] escapeSequence, int length) {
			logger.info("ANSI Escape Code: {}", Arrays.copyOf(escapeSequence, length));
		}

		public void nonCsiControl(char code) {
			logger.info("non-CSI Code: ESC {}", code);
		}
	};

	private Thread inputThread;

	public void start(Environment env) throws IOException {
		env.addSignalListener(new TuiSignalListener());
		Runnable inputThreadRunner = new InputThreadRunner();

		inputThread = new Thread(inputThreadRunner);
		inputThread.setDaemon(true);
		inputThread.start();
	}

	public void destroy() throws Exception {
		inputThread = null;
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

	public void setErrorStream(OutputStream err) {
	}

	public void setExitCallback(ExitCallback callback) {
		this.exitCallback = callback;
	}

	public class InputThreadRunner implements Runnable {
		
		private int escape = 0;
		private int[] escapeSequence = new int[100];
		
		private void handleSingleCode(int ch) {
			if (Character.isISOControl(ch)) {
				if (ch < 128) {
					AsciiControlCharacter asciiControlCharacter = AsciiControlCharacter.fromCodepoint(ch);
					keyboardHandler.asciiControl(asciiControlCharacter);				
				} else {
					logger.info("C1 control character: {}", ch);
				}
			}
			else {
				keyboardHandler.character((char)ch);
			}
		}
		
		public void run() {	

			try {

				do {
					if (escape > 0) {
						for (int i = 0; in.available() == 0 && i < ESCAPE_DELAY_MILLIS; i++) {
							Thread.sleep(1);
						}
						if (in.available() == 0) {
							// escape sequence didn't complete in time, assume individual key presses
							for (int i = 0; i < escape; i++) {
								handleSingleCode(escapeSequence[i]);
							}
							escape = 0;
						}
					}

					int ch = in.read();
					
					if (ch == -1) {
						logger.info("input thread exiting");
						return;
					}

					if (ch == 27) {
						// two escapes in a row, treat the first as a key press
						if (escape == 1) {
							handleSingleCode(escapeSequence[0]);
							escape = 0;
						}
						escapeSequence[escape++] = ch;
						continue;
					}

					if (escape == 1) {
						
						if (ch == 91) {
							// this is a CSI, expect further data in escape sequence
							escapeSequence[escape++] = ch;
						} else if (ch >= 64 && ch <= 95){
							// two-character non-CSI escape sequence
							keyboardHandler.nonCsiControl((char)ch);
							escape = 0;
						} else {
							// not a valid escape escape, assume individual key presses
							handleSingleCode(escapeSequence[0]);
							handleSingleCode(ch);
							escape = 0;
						}
					}
					else if (escape > 1) {						
						// continue with CSI escape sequence
						
						if (escape >= escapeSequence.length - 1) {
							logger.warn("Escape sequence too long, ignoring");
							escape = 0;
							continue;
						}
						
						escapeSequence[escape++] = ch;
						
						if (ch >= 64 && ch <=126) {
							// final character of CSI escape sequence received; process now
							keyboardHandler.ansiEscapeSequence(escapeSequence, escape);
							escape = 0;
						}
					}
					else {
						handleSingleCode(ch);
					}
				}
				while (true);
			} catch (Exception ex) {
				logger.error("input thread exception", ex);
			}
		}
	}

}
