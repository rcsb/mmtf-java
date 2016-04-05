package org.rcsb.mmtf.arraydecompressors;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.dataholders.MmtfBean;

/**
 * Decode string arrays that have been run length encoded.
 * The input values are in pairs. The first value in each pair
 * is the string to be used in the output array. The second value
 * is the number of repetitions of that value.
 * @author Anthony Bradley
 *
 */
public class RunLengthDecodeString implements StringArrayDeCompressorInterface {


  /**
   * Runlength decode an int[] and return as a char[].
   * @param inputIntArray An input array of integers.
   * @return a char array of values runlength decoded.
   */
  public final char[] intArrayToCharArray(final int[] inputIntArray) {
    int totNum = 0;
    // Define a default Char value
    char outChar =  MmtfBean.UNAVAILABLE_CHAR_VALUE;
    // If it's only one long - just take the char
    if (inputIntArray.length == 1) {
      char[] outArray = new char[1];
      if (Character.toChars(inputIntArray[0])[0] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
        outChar = '?';
      } else {
        outChar = Character.toChars(inputIntArray[0])[0];
      }
      outArray[0] = outChar;
      return outArray;
    }
    for (int i = 0; i < inputIntArray.length; i += 2) {
      totNum += inputIntArray[i + 1];
    }

    char[] outArray = new char[totNum];
    int totCounter = 0;


    for (int i = 0; i < inputIntArray.length; i += 2) {
      if (inputIntArray[i] == MmtfBean.UNAVAILABLE_CHAR_VALUE) {
        outChar = "?".charAt(0);
      } else {
    	// Otherwise get this char
        outChar = Character.toChars(inputIntArray[i])[0];
      }
      int numString = inputIntArray[i + 1];
      for (int j = 0; j < numString; j++) {
        outArray[totCounter] = outChar;
        totCounter++;
      }
    }
    return outArray;
  }

  @Override
  public final List<String> deCompressStringArray(final List<String> inArray) {
    // Make the output array
    List<String> outArray =  new ArrayList<String>();
    for (int i = 0; i < inArray.size(); i += 2) {
      String outString = inArray.get(i);
      int numString = Integer.parseInt(inArray.get(i + 1));

      for (int j = 0; j < numString; j++) {
        outArray.add(outString);
      }
    }
    return outArray;
  }



}
