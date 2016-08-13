package org.rcsb.mmtf.decoder;

/**
 * A class of array decoders.
 * @author Anthony Bradley
 *
 */
public class ArrayDecoders {

	/**
	 * Delta decode an array of integers.
	 * @param intArray the input array of integers
	 * @return a decoded integer array.
	 */
	public static int[] deltaDecode(int[] intArray) {
		int[] outArray = new int[intArray.length];
		System.arraycopy(intArray, 0, outArray, 0, intArray.length);
		for (int i = 1; i < outArray.length; i++)  {
			outArray[i] = outArray[i-1] + outArray[i];
		}
		return outArray;

	}

	/**
	 * Run length decode an array of integers.
	 * @param integerArray the input data
	 * @return the decoded integer array
	 */
	public static int[] runlengthDecode(int[] integerArray) {
		// Calculate the length
		int totCount =0;
		for(int i=0; i<integerArray.length;i+=2){
			totCount+=integerArray[i+1];
		}
		int[] outArray = new int[totCount];
		int index = 0;
		for (int i=0; i<integerArray.length; i+=2) {
			int currentInt = integerArray[i];
			int currentCount = integerArray[i+1];
			for (int j=0; j<currentCount;j++){
				outArray[index] = currentInt;
				index++;
			}
		}
		return outArray;
	}
}
