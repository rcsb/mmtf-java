package org.rcsb.mmtf.encoder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.CharCodecs;
import org.rcsb.mmtf.codec.FloatCodecs;
import org.rcsb.mmtf.codec.IntCodecs;
import org.rcsb.mmtf.codec.OptionParser;
import org.rcsb.mmtf.codec.StringCodecs;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioAssemblyTransformation;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.Group;
import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * A class of static utility functions to aid encoding of data.
 * Convert from {@link StructureDataInterface} into a format useable by
 * the {@link MmtfStructure}.
 * @author Anthony Bradley
 *
 */
public class EncoderUtils {

	/** The Null parameter for encoding (i.e. when no params are set). */
	public static final int NULL_PARAM = 0;
	/**
	 * Regenerate a group map from the {@link StructureDataInterface}.
	 * @param structureDataInterface the input interface
	 * @return a list of all the groups in the molecule
	 */
	public static Group[] generateGroupMap(StructureDataInterface structureDataInterface) {
		int[] groupTypeIndices = structureDataInterface.getGroupTypeIndices();
		if(groupTypeIndices.length==0){
			return new Group[0];
		}
		int maxIndex = Arrays.stream(groupTypeIndices).max().getAsInt();
		Group[] outGroupList = new Group[maxIndex+1];
		for (int i=0; i<maxIndex+1; i++) {
			// Generate this PDBGroup
			Group pdbGroup = new Group();
			pdbGroup.setFormalChargeList(structureDataInterface.getGroupAtomCharges(i));
			pdbGroup.setAtomNameList(structureDataInterface.getGroupAtomNames(i));
			pdbGroup.setBondAtomList(structureDataInterface.getGroupBondIndices(i));
			pdbGroup.setBondOrderList(structureDataInterface.getGroupBondOrders(i));
			pdbGroup.setChemCompType(structureDataInterface.getGroupChemCompType(i));
			pdbGroup.setElementList(structureDataInterface.getGroupElementNames(i));
			pdbGroup.setGroupName(structureDataInterface.getGroupName(i));
			pdbGroup.setSingleLetterCode(structureDataInterface.getGroupSingleLetterCode(i));
			outGroupList[i] = pdbGroup;
		}
		return outGroupList;
	}

	/**
	 * Find the bioassembly information as a list from the {@link StructureDataInterface}.
	 * @param structureDataInterface the interface from where to find the data
	 * @return a list of bioassembly information to be stored in the MMTF data structure.
	 */
	public static List<BioAssemblyData> generateBioassemblies(StructureDataInterface structureDataInterface) {
		int numBioassemblies = structureDataInterface.getNumBioassemblies();
		List<BioAssemblyData> outList = new ArrayList<>();
		for (int i=0; i<numBioassemblies; i++) {
			BioAssemblyData bioassembly = new BioAssemblyData(structureDataInterface.getBioassemblyName(i));
			outList.add(bioassembly);
			int numTrans = structureDataInterface.getNumTransInBioassembly(i);
			for (int j=0; j<numTrans; j++) {
				addTransform(bioassembly, structureDataInterface.getChainIndexListForTransform(i, j),
						structureDataInterface.getMatrixForTransform(i, j));
			}
		}
		return outList;
	}

	/**
	 * Add a transform to a given {@link BioAssemblyData} object.
	 * @param bioassembly the {@link BioAssemblyData} object
	 * @param chainIndices the integer list of chain indices to add
	 * @param transformation the list of doubles describing a transformation
	 */
	private static void addTransform(BioAssemblyData bioassembly, int[] chainIndices, double[] transformation) {
		BioAssemblyTransformation bioAssemblyTrans = new BioAssemblyTransformation();
		bioassembly.getTransformList().add(bioAssemblyTrans);
		bioAssemblyTrans.setChainIndexList(chainIndices);
		bioAssemblyTrans.setMatrix(transformation);		
	}


	/**
	 * Generate the entity level information from the {@link StructureDataInterface}.
	 * @param structureDataInterface the input interface
	 * @return the list of entities
	 */
	public static Entity[] generateEntityList(StructureDataInterface structureDataInterface) {
		int numEntities =  structureDataInterface.getNumEntities();
		Entity[] outList = new Entity[numEntities];
		for (int i=0; i<numEntities;i++) {
			Entity entity = new Entity();
			entity.setChainIndexList(structureDataInterface.getEntityChainIndexList(i));
			entity.setDescription(structureDataInterface.getEntityDescription(i));
			entity.setSequence(structureDataInterface.getEntitySequence(i));
			entity.setType(structureDataInterface.getEntityType(i));
			outList[i] = entity;
		}
		return outList;
	}
	
	/**
	 * Get the type of a given chain index.
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 * @param chainInd the index of the relevant chain
	 * @return the {@link String} describing the chain 
	 */
	public static String getTypeFromChainId(StructureDataInterface structureDataInterface, int chainInd) {
		for(int i=0; i<structureDataInterface.getNumEntities(); i++){
			for(int chainIndex : structureDataInterface.getEntityChainIndexList(i)){
				if(chainInd==chainIndex){
					return structureDataInterface.getEntityType(i);
				}
			}
		}
		System.err.println("ERROR FINDING ENTITY FOR CHAIN: "+chainInd);
		return "NULL";
	}
	
	/**
	 * Method to prepend a byte array with a byte.
	 * @param inputCodec the codec to be used
	 * @param inputData the array to encode and prepend
	 * @param param the input parameter - an integer to be used in the encoding
	 * @return the updated array
	 */
	public static byte[] encodeByteArr(FloatCodecs inputCodec, float[] inputData, int param){
		byte[] prepend = new OptionParser(inputCodec.getCodecId(), inputData.length, param).getHeader();
		byte[] outputArr = inputCodec.encode(inputData,param);
		return joinArrays(prepend, outputArr);
	}



	/**
	 * Method to prepend a byte array with a byte.
	 * @param inputCodec the codec to be used
	 * @param inputData the array to encode and prepend
	 * @param param the input parameter - an integer to be used in the encoding
	 * @return the updated array
	 */
	public static byte[] encodeByteArr(IntCodecs inputCodec, int[] inputData, int param){
		byte[] prepend = new OptionParser(inputCodec.getCodecId(), inputData.length, param).getHeader();
		byte[] outputArr = inputCodec.encode(inputData, param);
		return joinArrays(prepend, outputArr);
	}
	
	/**
	 * Method to prepend a byte array with a byte.
	 * @param inputData the array to encode and prepend
	 * @param inputCodec the codec to be used
	 * @param param the input parameter - an integer to be used in the encoding
	 * @return the updated array
	 */
	public static byte[] encodeByteArr(CharCodecs inputCodec, char[] inputData, int param){
		byte[] prepend = new OptionParser(inputCodec.getCodecId(), inputData.length, param).getHeader();
		byte[] outputArr = inputCodec.encode(inputData, param);
		return joinArrays(prepend, outputArr);

	}
	
	/**
	 * Method to prepend a byte array with a byte.
	 * @param inputData the array to encode and prepend
	 * @param inputCodec the codec to be used
	 * @param param the input parameter - an integer to be used in the encoding
	 * @return the updated array
	 */
	public static byte[] encodeByteArr(StringCodecs inputCodec, String[] inputData, int param){
		byte[] prepend = new OptionParser(inputCodec.getCodecId(), inputData.length, param).getHeader();
		byte[] outputArr = inputCodec.encode(inputData, param);
		return joinArrays(prepend, outputArr);

	}

	private static byte[] joinArrays(byte[] prepend, byte[] outputArr) {
		ByteBuffer buffer = ByteBuffer.allocate(prepend.length+outputArr.length);
		return buffer.put(prepend, 0, prepend.length).put(outputArr, 0, outputArr.length).array();
	}

	/**
	 * Transfer bioassembly data from a {@link StructureDataInterface} to an {@link AdapterToStructureData}.
	 * @param structureDataInterface the input {@link StructureDataInterface}
	 * @param adapterToStructureData the {@link AdapterToStructureData} to pass the data to
	 */
	public static void transferBioassembly(StructureDataInterface structureDataInterface,
			AdapterToStructureData adapterToStructureData) {
		for(int i=0; i<structureDataInterface.getNumBioassemblies(); i++) {
			for(int j =0; j<structureDataInterface.getNumTransInBioassembly(i); j++){
				adapterToStructureData.setBioAssemblyTrans(i, 
						structureDataInterface.getChainIndexListForTransform(i, j), 
						structureDataInterface.getMatrixForTransform(i, j),
						structureDataInterface.getBioassemblyName(i));
			}
		}
		
	}
}
