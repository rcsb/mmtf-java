package org.rcsb.mmtf.preupdatetests;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.rcsb.mmtf.update.PullFtpData;
import org.rcsb.mmtf.update.ServerUtils;


public class FtpServerTest {

	/**
	 * Can we get to the ftp site and download the data
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) {
		String url = args[0];
		ServerUtils serverUtils = new ServerUtils();
		FtpServerTest ftpServerTest = new FtpServerTest();
		PullFtpData pullFtpData = new PullFtpData(url);
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

		// First check we can ping the server
		assertTrue(serverUtils.pingServer(host, port));
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
	}


	private void checkAllPdbIds(String[] inputData) {
		// Check they all equal 4
		for (String inputId : inputData) {
			assertEquals(inputId.length(), 4);
			assertTrue(StringUtils.isAlphanumeric(inputId));
		}
	}

}
