package org.rcsb.mmtf.arraydecompressors;

import java.util.ArrayList;
import java.util.List;

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
   * Runlength decode a string list and return as a char[].
   * @param inputArray An input string array.
   * @return a char array of  the strings
   * rather than a string array.
   */
  public final char[] stringArrayToChar(final ArrayList<String> inputArray) {
    int totNum = 0;
    // Define an array to hold chars
    char[] outChars = new char[1];
    char outChar = "l".charAt(0);
    // If it's only one long - just take the char
    if (inputArray.size() == 1) {
      char[] outArray = new char[1];
      if (inputArray.get(0) == null) {
        outChar = "?".charAt(0);
      } else {
        String outString = inputArray.get(0);
        outString.getChars(0, 1, outChars, 0);
        outChar = outChars[0];
      }
      outArray[0] = outChar;
      return outArray;
    }
    for (int i = 0; i < inputArray.size(); i += 2) {
      totNum += Integer.parseInt(inputArray.get(i + 1));
    }

    char[] outArray = new char[totNum];
    int totCounter = 0;


    for (int i = 0; i < inputArray.size(); i += 2) {
      if (inputArray.get(i) == null) {
        outChar = "?".charAt(0);
      } else {
        String outString = inputArray.get(i);
        outString.getChars(0, 1, outChars, 0);
        outChar = outChars[0];
      }
      int numString = Integer.parseInt(inputArray.get(i + 1));
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
