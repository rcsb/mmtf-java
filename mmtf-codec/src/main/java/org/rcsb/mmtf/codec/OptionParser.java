package org.rcsb.mmtf.codec;

import java.nio.ByteBuffer;

/**
 * Class to parse the options for a given byte[]
 * @author Anthony Bradley
 *
 */
public class OptionParser {

	/** The byte array of the data without the header.*/
	public byte[] data;
	/** The  number of the method. */
	public Integer methodNumber;
	/** The length of the output array. */
	public Integer outputLength;
	/** The integer encoding the parameter. */
	public Integer param;

	private static final int offset = 12;
	
	/**
	 * Constructor to take the options from the byte array and allow 
	 * access to the options and the raw data.
	 * @param inputData the input byte array (with 12 byte header)
	 */
	public OptionParser(byte[] inputData) {
		ByteBuffer buffer = ByteBuffer.wrap(inputData);
		methodNumber = buffer.getInt();
		outputLength = buffer.getInt();
		param = buffer.getInt();
		data = new byte[inputData.length-offset];
		buffer.get(data, 0, inputData.length-offset);
	}

	/**
	 * Construct the option header from the input variables.
	 * @param methodNumber the number of the method
	 * @param outputLength the length of the output
	 * @param param the parameter
	 */
	public OptionParser(Integer methodNumber, Integer outputLength, Integer param) {
		this.methodNumber = methodNumber;
		this.outputLength = outputLength; 
		this.param = param;
	}
	
	/**
	 * Get the header for this option as a byte array.
	 * @return a byte array specifying the header for the given encoding strategy.
	 */
	public byte[] getHeader() {
		ByteBuffer buffer = ByteBuffer.allocate(12);
		if(methodNumber!=null){
			buffer.putInt(methodNumber);
		}
		else{
			throw new NullPointerException("Method number cannot be null");
		}
		if(outputLength != null){
			buffer.putInt(outputLength);
		}
		else{
			throw new NullPointerException("Outputlength cannot be null");
		}
		if(param != null){
			buffer.putInt(param);
		}
		else{
			throw new NullPointerException("Param cannot be null");
		}
		return buffer.array();
	}


}
