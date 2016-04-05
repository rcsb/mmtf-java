package org.rcsb.mmtf.arraydecompressors;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Run length decode a list of integers.
 * @author Anthony Bradley
 *
 */
public class RunLengthDecodeInt {

  /**
   * The number of bytes in a four byte integers.
   */
  private static final int BIG_INT_BYTES = 4;

  /**
   * Decompress a byte array that is run length encoded.
   * @param inArray The input byte array. Integers as 4 bytes long
   * @return The decompressed integer array
   * @throws IOException The byte array does not contain the
   * information requested.
   */
  public final int[] decompressByteArray(final byte[] inArray)
      throws IOException {
    // The length of each of the 4 byte integer arrays
    int lengthOfBigIntArr = inArray.length / (BIG_INT_BYTES * 2);
    // Array to store all the different numbers
    int[] numArr = new int[lengthOfBigIntArr];
    int[] countArr = new int[lengthOfBigIntArr];
    // Get the size
    int totCount = 0;
    DataInputStream bis = new DataInputStream(new
        ByteArrayInputStream(inArray));
    for (int i = 0; i < lengthOfBigIntArr; i++) {
      // Get the number
      int getNum = bis.readInt();
      int getCount = bis.readInt();
      totCount += getCount;
      numArr[i] = getNum;
      countArr[i] = getCount;
    }
    // Now set this output array
    int[] outArr = new int[totCount];
    int totCounter = 0;
    for (int i = 0; i < numArr.length; i++) {
      int thisAns = numArr[i];
      for (int j = 0; j < countArr[i]; j++) {
        // Annd then add t is to the array
        outArr[totCounter] = thisAns;
        // Now add to the counter
        totCounter++;

      }

    }
    return outArr;
  }

}
