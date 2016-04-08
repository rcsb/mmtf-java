package org.rcsb.mmtf.decoder;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the array decompressor library.
 * @author Anthony Bradley
 *
 */
public class TestArrayDecoders {

	/**
	 * 
	 */
	
	
	/**
	 * Run length decode int test.
	 */
	@Test
	public final void runLengthDecodeIntTest() {
		// Allocate the byte array
		int[] inputData =  {15,3,100,2,111,4};
		int[] outputDataTest = {15,15,15,100,100,111,111,111,111};
		int[] outputData = ArrayDecoders.runlengthDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
	
	/**
	 * Delta decode int test.
	 */
	@Test
	public final void deltaDecodeIntTest() {
		// Allocate the byte array
		int[] inputData =  {15,3,100,-1,11,4};
		int[] outputDataTest = {15,18,118,117,128,132};
		int[] outputData = ArrayDecoders.deltaDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
}
