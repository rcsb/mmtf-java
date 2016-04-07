package org.rcsb.mmtf.api;

public interface ObjectToByteArrayConverterInterface {
	
	/**
	 * Convert an object to a byte array using reflection.
	 * @param object an object with getters and setters
	 * @return a byte array of the serialised object
	 */
	byte[] convert(Object object);
}
