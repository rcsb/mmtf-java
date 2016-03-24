package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.*;

import org.rcsb.mmtf.update.ServerUtils;


public class SandboxAccessTest {
	public static void main(String[] args) {
		String url = args[0];
		ServerUtils serverUtils = new ServerUtils();
		// Get the host and port
		int doubleslash = url.indexOf("//");
		if(doubleslash == -1)
			doubleslash = 0;
		else
			doubleslash += 2;
		int end = url.indexOf('/', doubleslash);
		end = end >= 0 ? end : url.length();
		int port = url.indexOf(':', doubleslash);
		end = (port > 0 && port < end) ? port : end;
		String host = url.substring(doubleslash, end);
		port = Integer.parseInt(url.split(":")[1].split("/")[0]);
		// Assert that we can reach this server
		assertTrue(serverUtils.pingServer(host, port));
	}
}
