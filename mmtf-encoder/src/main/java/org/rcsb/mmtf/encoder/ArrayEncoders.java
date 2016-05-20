package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.utils.CodecUtils;

/**
 * A class of methods to encode arrays.
 * e.g. using delta encoding.
 * @author Anthony Bradley
 *
 */
public class ArrayEncoders {

	/**
	 * Delta encode an array of integers.
	 * @param intArray the input array
	 * @return the encoded array
	 */
	public static Integer[] deltaEncode(Integer[] intArray) {
		Integer[] out = new Integer[intArray.length];
		System.arraycopy(intArray, 0, out, 0, intArray.length);
		for (int i = out.length-1; i > 0; i--) {
			out[i] = out[i] - out[i-1];
		}
		return out;
	}

	/**
	 * Run length encode an array of integers.
	 * @param intArray the input array
	 * @return the encoded integer array
	 */
	public static Integer[] runlengthEncode(Integer[] intArray) {
		// If it's length zero
		if (intArray.length==0){
			return new Integer[0];
		}
		// We don't know the length so use
		List<Integer> outList = new ArrayList<>();
		int lastInt = intArray[0];
		int counter = 1;
		for (int i=1; i<intArray.length; i++) {
			if (intArray[i]==lastInt){
				counter++;
			}
			else{
				// Add the integer that's being 
				// encoded and the number of repeats
				outList.add(lastInt);
				outList.add(counter);
				// Reset the counter
				counter=1;
				lastInt = intArray[i];
			}
		}
		// Now add the last two
		outList.add(lastInt);
		outList.add(counter);
		return outList.toArray(new Integer[outList.size()]);
	}
}
