package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.rcsb.mmtf.update.ServerUtils;


public class SandboxAccessTest {
	public static void main(String[] args) throws IOException {
		String url = args[0];
		String outPutFile = args[1];
		ServerUtils serverUtils = new ServerUtils();
		// Assert that we can reach this server
		assertTrue(serverUtils.pingServer(url));
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
	}
}
