package org.rcsb.mmtf.arraycompressors;

import java.util.ArrayList;

import org.junit.Test;
import org.rcsb.mmtf.arraycompressors.FindDeltas;
import org.rcsb.mmtf.arraycompressors.RunLengthEncode;
import org.rcsb.mmtf.arraycompressors.RunLengthEncodeString;

import static org.junit.Assert.*;

public class TestArrayCompressors {

	@Test
	public void deltaTest() {
		// Generate the array
		FindDeltas fd = new FindDeltas();
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
		ArrayList<Integer> deltadArray = fd.compressIntArray(inArray);
		// Now check 
		assertEquals(deltadArray, testArray);

	}

	@Test
	public void runLengthTest() {
		// Generate the array
		RunLengthEncode rle = new RunLengthEncode();
		// Create the two arrays
		ArrayList<Integer> inArray = new ArrayList<Integer>();
		ArrayList<Integer> testArray = new ArrayList<Integer>();
		int length = 1000;
		for(int i=0; i<length;i++){
			inArray.add(1);
			if(i==0){
				testArray.add(1);
			}
		}
		testArray.add(length);
		// Compress it
		ArrayList<Integer> runlenghtdArray = rle.compressIntArray(inArray);
		// Now check 
		assertEquals(runlenghtdArray, testArray);
	}

	

	@Test
	public void runLengthStringTest() {
		// Generate the array
		RunLengthEncodeString rle = new RunLengthEncodeString();
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
		ArrayList<String> runlenghtdArray = rle.compressStringArray(inArray);
		// Now check 
		assertEquals(runlenghtdArray, testArray);
	}
}
