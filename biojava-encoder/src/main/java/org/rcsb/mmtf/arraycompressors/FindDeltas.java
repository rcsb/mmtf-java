package org.rcsb.mmtf.arraycompressors;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to encode an integer array with deltas.
 *
 * @author Anthony Bradley
 */
public class FindDeltas implements IntArrayCompressor, Serializable {


  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -8404400061650470813L;

  /* (non-Javadoc)
   * @see org.rcsb.mmtf.arraycompressors.IntArray
   * Compressor#compressIntArray(java.util.ArrayList)
   */
  public final ArrayList<Integer> compressIntArray(final ArrayList<Integer> inArray) {
    // 
    ArrayList<Integer> outArray =  new ArrayList<Integer>();
    int oldInt = 0;
    for (int i = 0; i < inArray.size(); i++) {
      // Get the value out here
      int numInt = inArray.get(i);
      if (i==0){
        oldInt = numInt;
        outArray.add(numInt);
      }
      else{
        int this_int = numInt - oldInt;
        outArray.add((int) this_int);
        oldInt = numInt;
      }
    }
    return outArray;
  }


}
