package org.rcsb.mmtf.update;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestFTPServer {
	
	/**
	 * Can we get to the ftp site and download the data
	 * @throws MalformedURLException 
	 */
	@Test
	public void testReadFtpSite() throws MalformedURLException {
		PullFtpData pullFtpData = new PullFtpData();
		// First check we can ping the server
		if(pullFtpData.pingServer()) {
		// Check this is not empty
		String[] currentEntries;
		currentEntries = pullFtpData.getAllCurrentEntries();
		checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		// Now check the others aren't null
		currentEntries = pullFtpData.getAllCurrentModels();
		checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);		
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		currentEntries = pullFtpData.getAllObsoleteEntries();
		checkAllPdbIds(currentEntries);
		assertNotEquals(currentEntries, null);
		assertNotEquals(currentEntries.length, 0);
		assertNotEquals(currentEntries.length, 1);
		// Check there is something to be updated
		assertNotEquals(currentEntries, null);
		currentEntries = pullFtpData.getAdded();
		assertNotEquals(currentEntries.length, 0);
		}
		else {
			System.out.println("Can't reach ftp server");
		}
	}
	
	
	private void checkAllPdbIds(String[] inputData) {
		// Check they all equal 4
		for (String inputId : inputData) {
			assertEquals(inputId.length(), 4);
			assertTrue(StringUtils.isAlphanumeric(inputId));
		}
	}

}
