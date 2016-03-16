package org.rcsb.mmtf.arraydecompressors;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.rcsb.mmtf.arraydecompressors.DeltaDeCompress;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeInt;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the array decompressor library.
 * @author Anthony Bradley
 *
 */
public class TestArrayDecompressor {


  /** The Constant NUMBER_INTS. */
  private static final int NUMBER_INTS = 100;

  /** The Constant REPEITITONS. */
  private static final int REPEITITONS = 1000;

  /** The Constant NUMBER_CHARS. */
  private static final int NUMBER_CHARS = 100;

  /** The Constant INITIAL_INT. */
  private static final int INITIAL_INT = 3000;

  /** The Constant TOTAL_LENGTH. */
  private static final int TOTAL_LENGTH = 100;
  /**
   * Run length decode int test.
   */
  @Test
  public final void runLengthDecodeIntTest() {

    RunLengthDecodeInt rlds = new RunLengthDecodeInt();
    // Set the size and character

    // Build the test array
    ArrayList<Integer> testArray = new ArrayList<Integer>();
    for (int i = 0; i < NUMBER_INTS; i++) {
      testArray.add(REPEITITONS);
    }
    // Add the lists
    ArrayList<Integer> otherArray = new ArrayList<Integer>();
    otherArray.add(REPEITITONS);
    otherArray.add(NUMBER_INTS);
    // Check theyr'e the same
    assertEquals(testArray, rlds.decompressIntArray(otherArray));
  }

  /**
   * Run lenght decode string test.
   */
  @Test
  public final void runLenghtDecodeStringTest() {

    RunLengthDecodeString rlds = new RunLengthDecodeString();
    // Set the size and character
    String charRep = "A";
    // Build the test array
    ArrayList<String> testArray = new ArrayList<String>();
    for (int i = 0; i < NUMBER_CHARS; i++) {
      testArray.add(charRep);
    }
    // Add the lists
    ArrayList<String> otherArray = new ArrayList<String>();
    otherArray.add(charRep);
    otherArray.add(Integer.toString(NUMBER_CHARS));
    // Check theyr'e the same
    assertEquals(testArray, rlds.deCompressStringArray(otherArray));
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

    // Set the size of the start and lenght of the aray

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
