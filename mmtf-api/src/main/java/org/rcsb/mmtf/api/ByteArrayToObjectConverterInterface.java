package org.rcsb.mmtf.api;

public interface ByteArrayToObjectConverterInterface {

	/**
	 * Convert a byte array to an object to then be procesed.
	 * e.g. a messagepack byte array can be coverted to an MMTFBean.
	 * @param byteArray the input byte data
	 * @return the object with defined getters and setters
	 */
	Object convert(byte[] byteArray);
}
