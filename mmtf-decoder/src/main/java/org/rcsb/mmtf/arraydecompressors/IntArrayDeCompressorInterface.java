package org.rcsb.mmtf.arraydecompressors;

import java.util.List;

/**
 * Specify the functions in an decompressort of integer arrays.
 * @author Anthony Bradley
 *
 */
public interface IntArrayDeCompressorInterface {

  /**
   * Decompress an integer array and return an other integer array.
   * @param inArray An input list of integers.
   * @return A list of Integers.
   */
  List<Integer> decompressIntArray(List<Integer> inArray);
}
