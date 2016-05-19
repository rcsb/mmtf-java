package org.rcsb.mmtf.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of functions to test if codec utils work.
 * @author Anthony Bradley
 *
 */
public class TestCodecUtils {

	/**
	 * Test that converting a list of ints to an array of ints works.
	 */
	@Test
	public void testConvertToIntArray() {
		int[] testIntegerArray = {1,4,3,2,6,7,2,200};
		List<Integer> integerList = new ArrayList<>();
		for (int inputInt : testIntegerArray) {
			integerList.add(inputInt);
		}
		int[] integerArray = CodecUtils.convertToIntArray(integerList);
		assertArrayEquals(testIntegerArray, integerArray);
	}
	
	/**
	 * Test that finding the maximum value in an array works.
	 */
	@Test
	public void testFindMaxInIntArray() {
		int[] intArray = {1,2,2020,1,2,567,203,-10200,-304,20};
		int maxValue = CodecUtils.findMaxInIntArray(intArray);
		assertEquals(2020, maxValue);
	}
	
	/**
	 * Test that an empty array returns -1.
	 */
	@Test
	public void testFindMaxEmptyArray() {
		int[] intArray = {};
		int maxValue = CodecUtils.findMaxInIntArray(intArray);
		assertEquals(-1, maxValue);
		
	}
}
