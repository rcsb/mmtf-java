package org.rcsb.mmtf.encoder;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.codec.CharCodecs;
import org.rcsb.mmtf.codec.FloatCodecs;
import org.rcsb.mmtf.codec.IntCodecs;
import org.rcsb.mmtf.codec.OptionParser;
import org.rcsb.mmtf.codec.StringCodecs;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioAssemblyTransformation;

/**
 * Tests for the {@link EncoderUtils} class of static methods.
 * @author Anthony Bradley
 *
 */
public class TestEncoderUtils {

	/**
	 * Test that all of the codecs can handle and empty input array.
	 */
	@Test
	public void testEmptyArrs() {

		for (FloatCodecs inputCodec : FloatCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new float[] {}, 0), 
					inputCodec.getCodecId());
		}

		for (CharCodecs inputCodec : CharCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new char[] {}, 0), 
					inputCodec.getCodecId());
		}

		for (IntCodecs inputCodec : IntCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new int[] {}, 0), 
					inputCodec.getCodecId());
		}

		for (StringCodecs inputCodec : StringCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new String[] {}, 0), 
					inputCodec.getCodecId());
		}
	}

	@Test
	public void testGenerateBioassemblies() {
		List<BioAssemblyData> bioAssemblyData = new ArrayList<>();
		BioAssemblyData bioAssemblyOne = new BioAssemblyData("1"); 
		bioAssemblyData.add(bioAssemblyOne);
		List<BioAssemblyTransformation> bioAssemblyOneTransforms = new ArrayList<>();
		BioAssemblyTransformation bioassOneTransOne = new BioAssemblyTransformation();
		bioassOneTransOne.setChainIndexList(new int[]{1,2,3,4});
		bioassOneTransOne.setMatrix(new double[]{1.0,2.0,3.0,4.0});
		bioAssemblyOneTransforms.add(bioassOneTransOne);
		BioAssemblyTransformation bioassOneTransTwo = new BioAssemblyTransformation();
		bioassOneTransTwo.setChainIndexList(new int[]{5,7,11});
		bioassOneTransTwo.setMatrix(new double[]{5.0,2.0,8.0,4.0});
		bioAssemblyOneTransforms.add(bioassOneTransTwo);
		bioAssemblyOne.setTransformList(bioAssemblyOneTransforms);
		AdapterToStructureData adapterToStructureData = new AdapterToStructureData();
		adapterToStructureData.initStructure(0,0,0,0,0,"DUMY");
		for (int i=0; i< bioAssemblyData.size(); i++){
			for (int j=0; j< bioAssemblyData.get(i).getTransformList().size();j++)
			adapterToStructureData.setBioAssemblyTrans(i, 
					bioAssemblyData.get(i).getTransformList().get(j).getChainIndexList(), 
					bioAssemblyData.get(i).getTransformList().get(j).getMatrix(), 
					bioAssemblyData.get(i).getName());
		}
		List<BioAssemblyData> generateBioass = EncoderUtils.generateBioassemblies(adapterToStructureData);
		
		assertEquals(bioAssemblyData.get(0).getName(), generateBioass.get(0).getName());
		assertArrayEquals(bioAssemblyData.get(0).getTransformList().get(0).getChainIndexList(),
				generateBioass.get(0).getTransformList().get(0).getChainIndexList());
		assertArrayEquals(bioAssemblyData.get(0).getTransformList().get(0).getMatrix(),
				generateBioass.get(0).getTransformList().get(0).getMatrix(),0.0);
	}
	

	private void testOutput(byte[] encodeByteArr, int codecId) {
		assertArrayEquals(encodeByteArr, new OptionParser(codecId, 0, 0).getHeader());
	}
}
