package org.rcsb.mmtf.arraydecompressors;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.rcsb.mmtf.arraydecompressors.DeltaDeCompress;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeInt;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeString;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the array decompressor library.
 * @author Anthony Bradley
 *
 */
public class TestArrayDecompressor {

	/** The Constant INITIAL_INT. */
	private static final int INITIAL_INT = 3000;

	/** The Constant TOTAL_LENGTH. */
	private static final int TOTAL_LENGTH = 100;


	/**
	 * Run length decode int test.
	 * @throws IOException 
	 */
	@Test
	public final void runLengthDecodeIntTest() throws IOException {
		// Allocate the byte array
		byte[] inputData =  ByteBuffer.allocate(48).putInt(15).putInt(3).putInt(100).putInt(2).putInt(111).putInt(4).array();
		int[] outPutDataTest = {15,15,15,100,100,111,111,111,111};
		RunLengthDecodeInt runLengthDecodeInt = new RunLengthDecodeInt();
		int[] outPutData = runLengthDecodeInt.decompressByteArray(inputData);
		assertArrayEquals(outPutDataTest, outPutData);
	}

	/**
	 * Run length decode string test.
	 */
	@Test
	public final void runLenghtDecodeStringTest() {
		int[] inputData =  {66,4,63,2,67,1};
		char[] outPutDataTest = {'B','B','B','B','?','?','C'};
		RunLengthDecodeString runLengthDecodeString = new RunLengthDecodeString();
		char[] outPutData = runLengthDecodeString.intArrayToCharArray(inputData);
		assertArrayEquals(outPutDataTest, outPutData);
	}

	/**
	 * Delta decompressor test.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public final void deltaDecompressorTest() throws IOException {

		DeltaDeCompress ddc = new DeltaDeCompress();
		// Now let's generate the byte arrays for the test data
		ByteArrayOutputStream bigBos = new ByteArrayOutputStream();
		DataOutputStream bigDos = new DataOutputStream(bigBos);
		ByteArrayOutputStream littleBos = new ByteArrayOutputStream();
		DataOutputStream littleDos = new DataOutputStream(littleBos);
		// Make the big byte array
		bigDos.writeInt(INITIAL_INT);
		bigDos.writeInt(TOTAL_LENGTH);
		// Now write the shorts
		for (int i = 0; i < TOTAL_LENGTH; i++) {
			littleDos.writeShort(1);
		}
		// Get the test array
		int[] testArray = new int[TOTAL_LENGTH + 1];
		testArray[0] = INITIAL_INT;
		int incrementorInt = INITIAL_INT;
		for (int i = 1; i < TOTAL_LENGTH + 1; i++) {
			incrementorInt += 1;
			testArray[i] = incrementorInt;
		}
		// Now proccess these
		int[] outArray = ddc.decompressByteArray(bigBos.toByteArray(),
				littleBos.toByteArray());
		// Check if there the same
		assertArrayEquals(outArray, testArray);
	}

}
