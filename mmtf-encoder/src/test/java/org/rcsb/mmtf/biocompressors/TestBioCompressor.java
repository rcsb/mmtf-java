package org.rcsb.mmtf.biocompressors;

import java.util.ArrayList;

import org.junit.Test;
import org.rcsb.mmtf.biocompressors.CompressDoubles;

import static org.junit.Assert.*;


public class TestBioCompressor {

	@Test
	public void floatToInt() {
		// 
		CompressDoubles cd = new CompressDoubles();
		ArrayList<Float> inArray = new ArrayList<Float>();
		ArrayList<Integer> testArray = new ArrayList<Integer>();
		for(int i=0; i<1000;i++){
			inArray.add((float) 100.12);
			testArray.add(10012);

		}
		assertEquals(cd.getIntArrayFromFloat(inArray, (float) 100.0),testArray);

	}


	@Test
	public void doubleToInt() {

		// 
		CompressDoubles cd = new CompressDoubles();
		ArrayList<Double> inArray = new ArrayList<Double>();
		ArrayList<Integer> testArray = new ArrayList<Integer>();
		for(int i=0; i<1000;i++){
			inArray.add(100.12);
			testArray.add(10012);

		}
		assertEquals(cd.getIntArrayFromDouble(inArray, 100.0),testArray);

	}

}
