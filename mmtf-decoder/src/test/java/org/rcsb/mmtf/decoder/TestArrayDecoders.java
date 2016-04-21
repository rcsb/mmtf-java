package org.rcsb.mmtf.decoder;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the array decoders.
 * @author Anthony Bradley
 *
 */
public class TestArrayDecoders {
	
	/**
	 * Run length decode int test.
	 */
	@Test
	public final void runLengthDecodeTest() {
		// Allocate the byte array
		int[] inputData =  {15,3,100,2,111,4,10000,6};
		int[] outputDataTest = {15,15,15,100,100,111,111,111,111,10000,10000,10000,10000,10000,10000};
		int[] outputData = ArrayDecoders.runlengthDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
	
	/**
	 * Check run length encoding on empty arrays
	 */
	@Test
	public final void emptyRunLengthDecodeTest() {
		// Allocate the byte array
		int[] inputData =  {};
		int[] outputDataTest = {};
		int[] outputData = ArrayDecoders.runlengthDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
	
	/**
	 * Delta decode int test.
	 */
	@Test
	public final void deltaDecodeTest() {
		// Allocate the byte array
		int[] inputData =  {15,3,100,-1,11,4};
		int[] outputDataTest = {15,18,118,117,128,132};
		int[] outputData = ArrayDecoders.deltaDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
	
	
	/**
	 * Check delta decoding on empty array
	 */
	@Test
	public final void emptyDeltaDecodeTest() {
		// Allocate the byte array
		int[] inputData =  {};
		int[] outputDataTest = {};
		int[] outputData = ArrayDecoders.deltaDecode(inputData);
		assertArrayEquals(outputDataTest, outputData);
	}
}
