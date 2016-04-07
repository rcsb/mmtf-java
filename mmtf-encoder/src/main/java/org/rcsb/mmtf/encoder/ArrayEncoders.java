package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

/**
 * A class of methods that can be used to encode arrays.
 * @author Anthony Bradley
 *
 */
public class ArrayEncoders {

	
	  public static List<Integer> deltaEncode(final List<Integer> inArray) {
		    List<Integer> outArray =  new ArrayList<Integer>();
		    int oldInt = 0;
		    for (int i = 0; i < inArray.size(); i++) {
		      // Get the value out here
		      int numInt = inArray.get(i);
		      // TODO Remove the if statement.
		      if (i==0){
		        oldInt = numInt;
		        outArray.add(numInt);
		      }
		      else{
		        int this_int = numInt - oldInt;
		        outArray.add((int) this_int);
		        oldInt = numInt;
		      }
		    }
		    return outArray;
		  }
	  
	  
	  public static  List<Integer> runlengthEncodeIntegers(List<Integer> inArray) {

			List<Integer> outArray =  new ArrayList<Integer>();
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
	  
	  
	  public static List<String> runlengthEncodeStrings(List<String> inArray) {
		    List<String> outArray =  new ArrayList<String>();
		    String oldVal = "";
		    int counter = 0;
		    // Loop through the vals
		    for (int i = 0; i < inArray.size(); i++) {
		      // Get the value out here
		      String inString = inArray.get(i);
		      if (inString != oldVal){
		        if(oldVal != ""){
		          // Add the counter to the array
		          outArray.add(Integer.toString(counter));
		        }
		        // If it's a new number add it to the array
		        outArray.add(inString);
		        counter = 1;
		        oldVal = inString;
		      } else {
		        counter += 1;
		      }
		    }
		    outArray.add(Integer.toString(counter));
		    return outArray;
		  }
}
