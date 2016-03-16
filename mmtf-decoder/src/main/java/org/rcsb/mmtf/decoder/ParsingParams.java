package org.rcsb.mmtf.decoder;

/**
 * The Class ParsingParams.
 */
public class ParsingParams {

  /**   Whether to use internal chain ids or not. */
  private boolean parseInternal;

  /**
   * Instantiates a new parsing params.
   */
  public ParsingParams() {
    parseInternal = false;
  }

  /**
   * Checks if is parses the internal chain ids.
   *
   * @return true, if is parses the internal
   */
  public final boolean isParseInternal() {
    return parseInternal;
  }

  /**
   * Sets whether to parse the internal chain ids.
   *
   * @param ifParseInternal the new parses the internal
   */
  public final void setParseInternal(final boolean ifParseInternal) {
    this.parseInternal = ifParseInternal;
  }



}
