package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.rcsb.mmtf.update.ServerUtils;

public class SandboxAccessTest {
	public static void main(String[] args) throws IOException {
		String url = args[0];
		String outPutFile = args[1];
		// Assert that we can reach this server
		assertTrue(ServerUtils.pingServer(url));
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
	}
}
