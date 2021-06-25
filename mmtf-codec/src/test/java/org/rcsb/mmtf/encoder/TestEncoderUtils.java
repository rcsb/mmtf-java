package org.rcsb.mmtf.encoder;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.api.StructureAdapterInterface;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.CharCodecs;
import org.rcsb.mmtf.codec.FloatCodecs;
import org.rcsb.mmtf.codec.IntCodecs;
import org.rcsb.mmtf.codec.OptionParser;
import org.rcsb.mmtf.codec.StringCodecs;
import org.rcsb.mmtf.codec.Utils;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioAssemblyTransformation;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.Group;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;

/**
 * Tests for the {@link EncoderUtils} class of static methods.
 *
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
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new float[]{}, 0),
				inputCodec.getCodecId());
		}

		for (CharCodecs inputCodec : CharCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new char[]{}, 0),
				inputCodec.getCodecId());
		}

		for (IntCodecs inputCodec : IntCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new int[]{}, 0),
				inputCodec.getCodecId());
		}

		for (StringCodecs inputCodec : StringCodecs.values()) {
			testOutput(EncoderUtils.encodeByteArr(inputCodec, new String[]{}, 0),
				inputCodec.getCodecId());
		}
	}

	/**
	 * Test that Bioassemblies can be generated correctly form a
	 * {@link StructureDataInterface} to a {@link StructureAdapterInterface}
	 */
	@Test
	public void testGenerateBioassemblies() {
		List<BioAssemblyData> bioAssemblyData = new ArrayList<>();
		BioAssemblyData bioAssemblyOne = new BioAssemblyData("1");
		bioAssemblyData.add(bioAssemblyOne);
		List<BioAssemblyTransformation> bioAssemblyOneTransforms = new ArrayList<>();
		BioAssemblyTransformation bioassOneTransOne = new BioAssemblyTransformation();
		bioassOneTransOne.setChainIndexList(new int[]{1, 2, 3, 4});
		bioassOneTransOne.setMatrix(new double[]{1.0, 2.0, 3.0, 4.0});
		bioAssemblyOneTransforms.add(bioassOneTransOne);
		BioAssemblyTransformation bioassOneTransTwo = new BioAssemblyTransformation();
		bioassOneTransTwo.setChainIndexList(new int[]{5, 7, 11});
		bioassOneTransTwo.setMatrix(new double[]{5.0, 2.0, 8.0, 4.0});
		bioAssemblyOneTransforms.add(bioassOneTransTwo);
		bioAssemblyOne.setTransformList(bioAssemblyOneTransforms);
		AdapterToStructureData adapterToStructureData = new AdapterToStructureData();
		adapterToStructureData.initStructure(0, 0, 0, 0, 0, "DUMMY");
		for (int i = 0; i < bioAssemblyData.size(); i++) {
			for (int j = 0; j < bioAssemblyData.get(i).getTransformList().size(); j++) {
				adapterToStructureData.setBioAssemblyTrans(i,
					bioAssemblyData.get(i).getTransformList().get(j).getChainIndexList(),
					bioAssemblyData.get(i).getTransformList().get(j).getMatrix(),
					bioAssemblyData.get(i).getName());
			}
		}
		List<BioAssemblyData> generateBioass = EncoderUtils.generateBioassemblies(adapterToStructureData);
		assertEquals(bioAssemblyData.get(0).getName(), generateBioass.get(0).getName());
		assertArrayEquals(bioAssemblyData.get(0).getTransformList().get(0).getChainIndexList(),
			generateBioass.get(0).getTransformList().get(0).getChainIndexList());
		assertArrayEquals(bioAssemblyData.get(0).getTransformList().get(0).getMatrix(),
			generateBioass.get(0).getTransformList().get(0).getMatrix(), 0.0);
	}

	/**
	 * Test that the entity type can be retrieved from a chain index
	 * @throws java.io.IOException
	 */
	@Test
	public void testGetEntityType() throws IOException, ParseException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 0), "polymer");
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 1), "non-polymer");
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 2), "non-polymer");
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 3), "non-polymer");
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 4), "non-polymer");
		assertEquals(EncoderUtils.getTypeFromChainId(structureDataInterface, 5), "water");
	}

	/**
	 * Test that the entityList can be generated correctly.
	 * @throws java.io.IOException
	 */
	@Test
	public void testGenerateEntityList() throws IOException, ParseException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		Entity[] entities = EncoderUtils.generateEntityList(structureDataInterface);
		assertEquals(entities.length, 4);
		assertArrayEquals(entities[0].getChainIndexList(), new int[]{0});
		assertArrayEquals(entities[1].getChainIndexList(), new int[]{1});
		assertArrayEquals(entities[2].getChainIndexList(), new int[]{2, 3, 4});
		assertArrayEquals(entities[3].getChainIndexList(), new int[]{5});

		assertEquals("BROMODOMAIN ADJACENT TO ZINC FINGER DOMAIN PROTEIN 2B", entities[0].getDescription());
		assertEquals("4-Fluorobenzamidoxime", entities[1].getDescription());
		assertEquals("METHANOL", entities[2].getDescription());
		assertEquals("water", entities[3].getDescription());

		assertEquals("SMSVKKPKRDDSKDLALCSMILTEMETHEDAWPFLLPVNLKLVPGYKKVIKKPMDFSTIREKLSSGQYPNLETFALDVRLVFDNCETFNEDDSDIGRAGHNMRKYFEKKWTDTFKVS", entities[0].getSequence());
		assertEquals("", entities[1].getSequence());
		assertEquals("", entities[2].getSequence());
		assertEquals("", entities[3].getSequence());

		assertEquals("polymer", entities[0].getType());
		assertEquals("non-polymer", entities[1].getType());
		assertEquals("non-polymer", entities[2].getType());
		assertEquals("water", entities[3].getType());

	}

	/**
	 * Test that the groupList can be generated correctly
	 * @throws java.io.IOException
	 */
	@Test
	public void testGenerateGroupMap() throws IOException, ParseException {
		StructureDataInterface structureDataInterface = getDefaultFullData();
		Group[] groupList = EncoderUtils.generateGroupList(structureDataInterface);
		assertEquals(groupList.length, 29);
	}

	/**
	 * Get the default data for the full format.
	 *
	 * @return a {@link StructureDataInterface} for the full data.
	 */
	private StructureDataInterface getDefaultFullData() throws IOException, ParseException {
		Path inFile = Utils.getResource("/mmtf/4CUP.mmtf");
		return new GenericDecoder(ReaderUtils.getDataFromFile(inFile));
	}

	private void testOutput(byte[] encodeByteArr, int codecId) {
		assertArrayEquals(encodeByteArr, new OptionParser(codecId, 0, 0).getHeader());
	}
}
