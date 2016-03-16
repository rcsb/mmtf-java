package org.rcsb.mmtf.arraycompressors;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to run length encode an integer array.
 * @author Anthony Bradley
 *
 */
public class RunLengthEncode implements IntArrayCompressor, Serializable  {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -793325266722283046L;

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.arraycompressors.IntArrayCompressor#compressIntArray(java.util.ArrayList)
	 */
	public ArrayList<Integer> compressIntArray(ArrayList<Integer> inArray) {

		ArrayList<Integer> outArray =  new ArrayList<Integer>();
		int oldVal = 0;
		boolean inSwitch = false;
		int counter = 0;
		// Loop through the vals
		for (int i = 0; i < inArray.size(); i++) {
			// Get the value out here
			int num_int = inArray.get(i);


			if(inSwitch==false){
				inSwitch=true;
				// If it's a new number add it to the array
				outArray.add(num_int);
				counter=1;
				oldVal=num_int;	
			}
			else if (num_int!=oldVal){
				// Add the counter to the array
				outArray.add(counter);
				if(counter<0){
					System.out.println("THIS ERROR - "+counter);	
				}
				// If it's a new number add it to the array
				outArray.add(num_int);
				counter=1;
				oldVal=num_int;
			}
			else{
				counter+=1;
			}
		}
		outArray.add(counter);
		return outArray;
	}



}
