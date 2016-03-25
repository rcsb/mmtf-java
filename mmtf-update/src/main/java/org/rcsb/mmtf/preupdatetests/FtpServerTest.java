package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.rcsb.mmtf.update.PullFtpData;
import org.rcsb.mmtf.update.ServerUtils;


public class FtpServerTest {

	/**
	 * Can we get to the ftp site and download the data
	 * First argument is the URL
	 * Second argument is the file to write out
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String url = args[0];
		String outPutFile = args[1];
		ServerUtils serverUtils = new ServerUtils();
		FtpServerTest ftpServerTest = new FtpServerTest();
		PullFtpData pullFtpData = new PullFtpData(url);
		// Get the host and port
		// First check we can ping the server
		assertTrue(serverUtils.pingServer(url));
		// Check this is not empty
		String[] currentEntries;
		currentEntries = pullFtpData.getAllCurrentEntries();
		ftpServerTest.checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		// Now check the others aren't null
		currentEntries = pullFtpData.getAllCurrentModels();
		ftpServerTest.checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);		
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		currentEntries = pullFtpData.getAllObsoleteEntries();
		ftpServerTest.checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		// Check there is something to be updated
		assertNotEquals(currentEntries, null);
		currentEntries = pullFtpData.getAdded();
		assertNotEquals(currentEntries.length, 0);
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
	}


	private void checkAllPdbIds(String[] inputData) {
		// Check they all equal 4
		for (String inputId : inputData) {
			assertEquals(inputId.length(), 4);
			assertTrue(StringUtils.isAlphanumeric(inputId));
		}
	}

}
