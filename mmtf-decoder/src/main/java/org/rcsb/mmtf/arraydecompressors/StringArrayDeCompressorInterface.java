package org.rcsb.mmtf.arraydecompressors;

import java.util.List;

/**
 * Generic functions to decompress a list of Strings to another list of Strings.
 * @author Anthony Bradley
 *
 */
public interface StringArrayDeCompressorInterface {

  /**
   * Generic function to decompress a list of Strings to another
   * list of Strings.
   * @param inArray An input list of strings
   * @return A list of strings
   */
  List<String> deCompressStringArray(List<String> inArray);

}
