package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.rcsb.mmtf.update.ServerUtils;


public class SandboxAccessTest {
	public static void main(String[] args) throws MalformedURLException {
		String url = args[0];
		ServerUtils serverUtils = new ServerUtils();

		// Assert that we can reach this server
		assertTrue(serverUtils.pingServer(url));
	}
}
