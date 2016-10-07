package jmercutio;

import java.io.File;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;

public class SecureTuiMain {

	public static void main(String[] args) throws Exception {
		
		SshServer sshd = SshServer.setUpDefaultServer();
		
		File hostKeyFile = new File("hostkey.ser");
		
		sshd.setPort(2222);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(hostKeyFile));
		
		sshd.setShellFactory(new TuiShellFactory());
		
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {

			public boolean authenticate(String username, String password, ServerSession session)
					throws PasswordChangeRequiredException {
				if (username.equals("trousers") && password.equals("trousers")) {
					return true;
				}
				return false;
			}
			
		});
		
		sshd.start();
		Thread.sleep(Long.MAX_VALUE);
		sshd.close();
	}

}
