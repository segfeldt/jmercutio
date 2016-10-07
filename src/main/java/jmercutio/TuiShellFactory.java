package jmercutio;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

public class TuiShellFactory implements Factory<Command> {

	public Command create() {
		return new TuiShell();
	}

}
