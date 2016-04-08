package org.rcsb.mmtf.api;

public interface SerializerInterface {

	/**
	 * Convert a byte array to an object to then be procesed.
	 * e.g. a messagepack byte array can be coverted to an MMTFBean.
	 * @param byteArray the input byte data
	 * @return the object with defined getters and setters
	 */
	Object deserialize(byte[] byteArray);
	
	
	/**
	 * Convert an object to a byte array using reflection.
	 * @param object an object with getters and setters
	 * @return a byte array of the serialised object
	 */
	byte[] serialize(Object object);
}
