package org.rcsb.mmtf.update;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerUtils {

	
	/**
	 * General function to a ping a generic server.
	 * @param host
	 * @param port
	 * @return
	 */
	public boolean pingServer(String host, int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 100);
			return true;
		} catch (IOException e) {
			return false; // Either timeout or unreachable or failed DNS lookup.
		}

	}
}
