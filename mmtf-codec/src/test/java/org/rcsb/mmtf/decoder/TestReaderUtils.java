package org.rcsb.mmtf.decoder;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfStructure;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import java.io.IOException;
import java.text.ParseException;
import org.rcsb.mmtf.codec.Utils;

/**
 * Test the reader utils class functions work.
 * @author Anthony Bradley
 *
 */
public class TestReaderUtils {
	
	/**
	 * Test we can decompress a gzipped byte array.
	 * @throws IOException accessing the byte array	
	 */
	@Test
	public void testGzipDecompress() throws IOException {
		
		// Data derived using Python to get bytes and gzip to compres
		byte[] uncompressed = new byte[] {65, 66, 67, 68, 10};
		byte[] compressed = new byte[] {31, (byte) 139,
               8,
               8,
               45,
               (byte)  174,
               15,
               87,
               0,
               3,
               109,
               101,
               46,
               116,
               120,
               116,
               0,
               115,
               116,
               114,
               118,
               (byte)  225,
               2,
               0,
               (byte)  212,
               70,
               (byte)  176,
               (byte)  148,
               5,
               0,
               0,
               0};
		// Check they  are the same
		assertArrayEquals(ReaderUtils.deflateGzip(compressed), uncompressed);
		
	}
	
	/**
	 * Test that we can't gzip decompress non-gzipped data.
	 * @throws IOException deflating the byte array
	 */
	@Test(expected=IOException.class)
	public void testGzipDecompressText() throws IOException {
		// Test that decomprss text returns null
		byte[] uncompressed = new byte[] {65, 66, 67, 68, 10};
		ReaderUtils.deflateGzip(uncompressed);
		//assertEquals(null,ReaderUtils.deflateGzip(uncompressed));
	}

	
	/**
	 * Test that we can read an mmtf file into an MmmtfBean
	 * @throws IOException error accesing the file
	 */
	@Test 
	public void testReadFromFile() throws IOException, ParseException {
		ClassLoader classLoader = getClass().getClassLoader();
		MmtfStructure mmtfBean = ReaderUtils.getDataFromFile(Utils.getResource("/mmtf/4CUP.mmtf"));
		assertNotEquals(mmtfBean, null);
		assertEquals("2014-03-21", mmtfBean.getDepositionDate());
		assertEquals(1107, mmtfBean.getNumAtoms());
		assertEquals(980, mmtfBean.getNumBonds());
	}
	
	/**
	 * Test that we can read an mmtf file from the MMTF web server
	 * @throws IOException error accessing the data
	 */
	@Test 
	public void testReadFromUrl() throws IOException, ParseException {
		MmtfStructure mmtfBean = ReaderUtils.getDataFromUrl("4CUP");
		assertNotEquals(mmtfBean, null);
		assertEquals("2014-03-21", mmtfBean.getDepositionDate());
		assertEquals(1107, mmtfBean.getNumAtoms());
		assertEquals(980, mmtfBean.getNumBonds());
	}
	
	/**
	 * Test that we can read an mmtf file from the MMTF web server using HTTPS
	 * @throws IOException error accessing the data
	 */
	@Test 
	public void testReadFromHttpsUrl() throws IOException, ParseException {
		MmtfStructure mmtfBean = ReaderUtils.getDataFromUrl("4CUP", true, false);
		assertNotEquals(mmtfBean, null);
		assertEquals("2014-03-21", mmtfBean.getDepositionDate());
		assertEquals(1107, mmtfBean.getNumAtoms());
		assertEquals(980, mmtfBean.getNumBonds());
	}
	
	/**
	 * Test that we can read a reduced mmtf file from the MMTF web server using HTTP
	 * @throws IOException error accessing the data
	 */
	@Test 
	public void testReadFromReducedUrl() throws IOException, ParseException {
		MmtfStructure mmtfBean = ReaderUtils.getDataFromUrl("4CUP", true, true);
		assertNotEquals(mmtfBean, null);
		assertEquals("2014-03-21", mmtfBean.getDepositionDate());
		assertEquals(139, mmtfBean.getNumAtoms());
		assertEquals(21, mmtfBean.getNumBonds());
	}
	
	/**
	 * Test that we can read a reduced mmtf file from the MMTF web server using HTTPS
	 * @throws IOException error accessing the data
	 */
	@Test 
	public void testReadFromReducedHttpsUrl() throws IOException, ParseException {
		MmtfStructure mmtfBean = ReaderUtils.getDataFromUrl("4CUP", true, true);
		assertNotEquals(mmtfBean, null);
		assertEquals("2014-03-21", mmtfBean.getDepositionDate());
		assertEquals(139, mmtfBean.getNumAtoms());
		assertEquals(21, mmtfBean.getNumBonds());
	}
}
