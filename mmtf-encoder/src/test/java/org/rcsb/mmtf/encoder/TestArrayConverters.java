package org.rcsb.mmtf.encoder;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.junit.Test;

public class TestArrayConverters {


	/**
	 * Test the decoding of the chain list
	 */
	@Test 
	public final void testSetChain() {
		String[] stringList = {"A","ABC","abc"};
		byte[] testByteArray = {'A','\0','\0','\0',
				'A','B','C','\0',
				'a','b','c','\0'};
		byte[] byteArray = ArrayConverters.encodeChainList(stringList);
		assertArrayEquals(testByteArray, byteArray);
	}
	
	
	/**
	 * Test the conversion of the integer array to a float 
	 */
	@Test 
	public final void testConvertFloatToInt() {
		float[] floatArray = {10.001f,100.203f,124.542f};
		int[] testIntArray = {10001,100203,124542};
		float multiplier = 1000.0f;
		int[] intArray = ArrayConverters.convertFloatsToInts(floatArray, multiplier);
		assertArrayEquals(testIntArray, intArray);
	}
	
	
	/**
	 * Test the conversion of byte arrays to one byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void integersToOneByteTest() throws IOException {
		int[] intArray = {12,123,24};
		byte[] testByteArray = {(byte) 12,(byte) 123, (byte) 24};
		byte[] byteArray = ArrayConverters.convertIntegersToBytes(intArray);
		assertArrayEquals(testByteArray, byteArray);
	}

	/**
	 * Test the conversion of byte arrays to two byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void integersToTwoByteTest() throws IOException {
		int[] intArray = {1000,1002,546};
		byte[] testByteArray = getByteArray(intArray,2);
		byte[] byteArray = ArrayConverters.convertIntegersToTwoBytes(intArray);
		assertArrayEquals(testByteArray, byteArray);
	}

	/**
	 * Test the conversion of byte arrays to four byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void integersToFourByteTest() throws IOException {	
		int[] intArray = {32403,11200,100090};
		byte[] testByteArray = getByteArray(intArray,4);
		byte[] byteArray = ArrayConverters.convertIntegersToFourByte(intArray);
		assertArrayEquals(testByteArray, byteArray);
	}


	/**
	 * Test the conversion of integer arrays to char arrays.
	 */
	@Test
	public final void splitArraysTest() {
		// Initialise the two input arrays
		int[] testTwoByteIntArray = {1,2,5,4,50,0};
		int[] testFourByteIntArray = {10002,4,1303040,2};
		// The expected output
		int[] combinedArray = {10002,1,2,5,4,1303040,50,0};
		List<int[]> outVal = ArrayConverters.splitIntegers(combinedArray);
		assertArrayEquals(testFourByteIntArray, outVal.get(0));
		assertArrayEquals(testTwoByteIntArray, outVal.get(1));
	}


	/**
	 * Test the conversion of integer arrays to char arrays.
	 */
	@Test
	public final void charToIntTest() {
		int[] testIntArray =  {66,63,67};
		char[] charArray= {'B','?','C'};
		int[] intArray = ArrayConverters.convertCharToIntegers(charArray);
		assertArrayEquals(testIntArray, intArray);
	}
	
	/**
	 * Utiliy function to get a byte array. I don't really like this but at least
	 * it's an orthogonal approach.
	 * @param inArray the input int array
	 * @param numBytes the number of bytes per integer
	 * @return the output byte array
	 */
	private byte[] getByteArray(int[] inArray, int numBytes) {
		byte[] outBytes = new byte[inArray.length*numBytes];
		for(int i=0; i<inArray.length;i++){
			byte[] intBytes;
			if (numBytes==4){
				intBytes = ByteBuffer.allocate(numBytes).putInt(inArray[i]).array();
			}
			else if (numBytes==2){
				intBytes = ByteBuffer.allocate(numBytes).putShort((short) inArray[i]).array();
			}
			else if (numBytes==1){
				intBytes = ByteBuffer.allocate(numBytes).put((byte) inArray[i]).array();
			}
			else{
				intBytes = new byte[numBytes];
			}
			for(int j=0; j<numBytes; j++){
				outBytes[i*numBytes+j] = intBytes[j];
			}
		}
		return outBytes;
	}
}
