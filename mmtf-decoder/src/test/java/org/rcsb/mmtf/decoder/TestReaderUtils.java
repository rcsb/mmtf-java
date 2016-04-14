package org.rcsb.mmtf.decoder;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;


public class TestReaderUtils {
	
	
	@Test
	public void testGzipDecompress() {
		
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
	
	@Test
	public void testGzipDecompressText() {
		// Test that decomprss text returns null
		byte[] uncompressed = new byte[] {65, 66, 67, 68, 10};
		assertEquals(null,ReaderUtils.deflateGzip(uncompressed));
	}

	
	@Test 
	public void testReadFromFile() throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		ReaderUtils.getDataFromFile(classLoader.getResource("mmtf/4cup.mmtf").getPath());
	}
}
