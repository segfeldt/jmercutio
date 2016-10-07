package jmercutio;

import org.apache.sshd.server.Signal;
import org.apache.sshd.server.SignalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TuiSignalListener implements SignalListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void signal(Signal signal) {
		// TODO Auto-generated method stub
		logger.info("---- SIGNAL: {}", signal);
	}

}
