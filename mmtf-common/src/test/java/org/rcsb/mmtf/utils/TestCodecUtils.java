package org.rcsb.mmtf.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class of functions to test if codec utils wor
 * @author Anthony Bradley
 *
 */
public class TestCodecUtils {

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
	
	@Test
	public void testFindMaxInIntArray() {
		int[] intArray = {1,2,2020,1,2,567,203,-10200,-304,20};
		int maxValue = CodecUtils.findMaxInIntArray(intArray);
		assertEquals(2020, maxValue);
	}
}
