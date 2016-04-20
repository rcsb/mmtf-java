package org.rcsb.mmtf.decoder;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


import java.io.IOException;
import java.nio.file.Paths;

/**
 * Test the reader utils class functions work.
 * @author Anthony Bradley
 *
 */
public class TestReaderUtils {
	
	/**
	 * Test we can decompress a gzipped byte array.
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
	public void testReadFromFile() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		MmtfBean mmtfBean = ReaderUtils.getDataFromFile(Paths.get(classLoader.getResource("mmtf/4cup.mmtf").getFile()));
		assertNotEquals(mmtfBean, null);
		assertEquals(mmtfBean.getDepositionDate(), "2014-03-21");
	}
}
