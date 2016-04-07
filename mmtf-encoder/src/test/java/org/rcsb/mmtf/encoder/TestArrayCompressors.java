package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrayCompressors {

	@Test
	public void deltaTest() {
		// Generate the array
		
		// Create the two arrays
		ArrayList<Integer> inArray = new ArrayList<Integer>();
		ArrayList<Integer> testArray = new ArrayList<Integer>();
		for(int i=0; i<1000;i++){
			inArray.add(i);
			if(i==0){
				testArray.add(i);
			}
			else{
				testArray.add(1);
			}
		}
		// Compress it
		List<Integer> deltadArray = ArrayEncoders.deltaEncode(inArray);
		// Now check 
		assertEquals(deltadArray, testArray);

	}

	@Test
	public void runLengthTest() {
		// Generate the array
		// Create the two arrays
		List<Integer> inArray = new ArrayList<Integer>();
		List<Integer> testArray = new ArrayList<Integer>();
		int length = 1000;
		for(int i=0; i<length;i++){
			inArray.add(1);
			if(i==0){
				testArray.add(1);
			}
		}
		testArray.add(length);
		// Compress it
		List<Integer> runlenghtdArray = ArrayEncoders.runlengthEncodeIntegers(inArray);
		// Now check 
		assertEquals(runlenghtdArray, testArray);
	}

	

	@Test
	public void runLengthStringTest() {
		// Generate the array
		// Create the two arrays
		ArrayList<String> inArray = new ArrayList<String>();
		ArrayList<String> testArray = new ArrayList<String>();
		int length = 1000;
		for(int i=0; i<length;i++){
			inArray.add("A");
			if(i==0){
				testArray.add("A");
			}
		}
		testArray.add(Integer.toString(length));
		// Compress it
		List<String> runlenghtdArray = ArrayEncoders.runlengthEncodeStrings(inArray);
		// Now check 
		assertEquals(runlenghtdArray, testArray);
	}
}
