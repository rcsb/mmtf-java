package org.rcsb.mmtf.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Perform delta decompression on byte arrays.
 * The first array is of four byte integers found in pairs. The first in
 * each pair is a number to be used in the output array. The second indicates
 * the number of two byte integers to read from the second array.
 * @author Anthony Bradley
 *
 */
public class DeltaDeCompress {

  /**
   * The number of bytes in a four byte integers.
   */
  private static final int BIG_INT_BYTES = 4;
  /**
   * Decompress two byte arrays - one containing 4 byte and one 2 byte integers.
   * @param fourByteInts An array of four byte integers.
   * @param twoByteInts An array of two byte integers.
   * @return A decompressed integer array.
   * @throws IOException The byte array does not contain
   * the information requested.
   */
  public final int[] decompressByteArray(final byte[] fourByteInts,
      final byte[] twoByteInts) throws IOException {
    // Get these data streams
    DataInputStream bigStream = new DataInputStream(new
        ByteArrayInputStream(fourByteInts));
    // Get the length of the array
    int lengthOfBigIntArray = fourByteInts.length / (BIG_INT_BYTES * 2);
    // Integers used in the output
    int[] fourByteIntArr = new int[lengthOfBigIntArray];
    // Integers used to count from the two byte array
    int[] counterInts = new int[lengthOfBigIntArray];
    DataInputStream smallStream = new DataInputStream(new
        ByteArrayInputStream(twoByteInts));
    int totNum = 0;
    // Loop through these and take every other int
    for (int i = 0; i < lengthOfBigIntArray; i++) {
      int bigNum =  bigStream.readInt();
      int counterNum =  bigStream.readInt();
      // Now writ thei sout
      totNum++;
      // Now add to the counter
      totNum += counterNum;
      fourByteIntArr[i] = bigNum;
      counterInts[i] = counterNum;
    }
    // Now loop over the total number of ints
    int[] outArr = new int[totNum];
    int totCounter = 0;
    for (int i = 0; i < fourByteIntArr.length; i++) {
      // Now add this to the out array
      if (i == 0) {
        outArr[totCounter] = fourByteIntArr[i];
        } else {
        outArr[totCounter] = outArr[totCounter - 1] + fourByteIntArr[i];
      }
      totCounter++;
      // Now loop through this
      for (int j = 0; j < counterInts[i]; j++) {
        // Now add this as a short
        int currentInt = (int) smallStream.readShort();
        outArr[totCounter] = outArr[totCounter - 1] + currentInt;
        totCounter++;
      }
    }
    return outArr;
  }
}
