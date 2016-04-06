package org.rcsb.mmtf.encoder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.arraycompressors.FindDeltas;
import org.rcsb.mmtf.arraycompressors.IntArrayCompressor;
import org.rcsb.mmtf.arraycompressors.RunLengthEncode;
import org.rcsb.mmtf.arraycompressors.RunLengthEncodeString;
import org.rcsb.mmtf.arraycompressors.StringArrayCompressor;
import org.rcsb.mmtf.biocompressors.BioCompressor;
import org.rcsb.mmtf.biocompressors.CompressDoubles;
import org.rcsb.mmtf.dataholders.BioDataStruct;
import org.rcsb.mmtf.dataholders.CalphaBean;
import org.rcsb.mmtf.dataholders.CalphaDistBean;
import org.rcsb.mmtf.dataholders.CoreSingleStructure;
import org.rcsb.mmtf.dataholders.HeaderBean;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.NoFloatDataStruct;
import org.rcsb.mmtf.dataholders.NoFloatDataStructBean;
import org.rcsb.mmtf.dataholders.PDBGroup;
import org.rcsb.mmtf.gitversion.GetRepoState;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * A class of functions to help encoding of mmCIF data to other data structures.
 *
 * @author Anthony Bradley
 */
public class EncoderUtils implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 376413981384858130L;

	/** The class to get the git repo start */
	private GetRepoState grs = new GetRepoState();

	/** A converter of doubles to ints. */
	private BioCompressor doublesToInts = new CompressDoubles();

	/** The delta compressor of arrays. */
	private IntArrayCompressor deltaComp = new FindDeltas();

	/** The run length compressor of arrays. */
	private IntArrayCompressor runLengthComp = new RunLengthEncode();

	/**
	 * Take a list of integers (as List<Integer>) and return as byte array.
	 * Each integer is stored as four bytes.
	 * @param inputList the input integer array
	 * @return the byte array output. Each integer is stored as four bytes.
	 * @throws IOException Occurred writing the int to the stream.
	 */
	public byte[] integersToBytes(List<Integer> inputList) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(Integer i: inputList)
		{
			dos.writeInt(i);
		}
		return baos.toByteArray();
	}

	/**
	 * Get a messagepack from an input object using the getters and setters.
	 * @param inputObject the input object. Field names will be set using the getters and setters for private variable.s
	 * @return the message pack as a byte array.
	 * @throws JsonProcessingException the json processing exception - most likely related 
	 * to serialization.
	 */
	public final byte[] getMessagePack(Object inputObject) throws JsonProcessingException{
		com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new MessagePackFactory());
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		byte[] inBuf = objectMapper.writeValueAsBytes(inputObject);
		return inBuf;
	}


	/**
	 * Compress the biological and header data into a combined data structure.
	 * @param inStruct the BiodataStruct holding the structure data
	 * @param inHeader the header data
	 * @return the MmtfBean of the combined (compressed) data.
	 * @throws IOException reading byte array
	 */
	public final MmtfBean compressToMmtfBean(BioDataStruct inStruct, HeaderBean inHeader) throws IOException {
		EncoderUtils cm = new EncoderUtils();
		// Compress the data.
		CoreSingleStructure strucureData = compressInputData(inStruct);
		// Now set up the output MMTF dataholder
		MmtfBean outputMmtfBean = new MmtfBean();
		NoFloatDataStructBean bioBean = (NoFloatDataStructBean) strucureData.findDataAsBean();
		// Copy these things
		outputMmtfBean.setDepositionDate(convertToIsoTime(inHeader.getDepDate()));
		outputMmtfBean.setStructureId(bioBean.getPdbCode());
		outputMmtfBean.setInsCodeList(convertRunLengthStringListToIntArray(bioBean.get_atom_site_pdbx_PDB_ins_code()));
		outputMmtfBean.setAltLocList(convertRunLengthStringListToIntArray(bioBean.get_atom_site_label_alt_id()));
		// Set this experimental data
		outputMmtfBean.setResolution(inHeader.getResolution());
		outputMmtfBean.setrFree(inHeader.getrFree());
		outputMmtfBean.setrWork(inHeader.getrWork());
		// Copy the asym data
		outputMmtfBean.setChainIdList(inHeader.getAsymChainList());
		outputMmtfBean.setChainsPerModel(inHeader.getAsymChainsPerModel());
		outputMmtfBean.setGroupsPerChain(inHeader.getAsymGroupsPerChain());
		// Set the entity information
		outputMmtfBean.setEntityList(inHeader.getEntityList());
		// Get the seqres information
		outputMmtfBean.setSequenceIdList(cm.integersToBytes(runLengthComp.compressIntArray(deltaComp.compressIntArray(inHeader.getSeqResGroupIds()))));
		outputMmtfBean.setExperimentalMethods(inHeader.getExperimentalMethods());
		// Now get this list
		outputMmtfBean.setBondAtomList(cm.integersToBytes(inStruct.getInterGroupBondInds()));
		outputMmtfBean.setBondOrderList(cm.integersToSmallBytes(inStruct.getInterGroupBondOrders()));
		// Now get these from the headers
		outputMmtfBean.setChainNameList(inHeader.getChainList());
		outputMmtfBean.setNumAtoms(inHeader.getNumAtoms());
		outputMmtfBean.setNumBonds(inHeader.getNumBonds());
		// Now get the crystalographic info from this header
		outputMmtfBean.setSpaceGroup(inHeader.getSpaceGroup());
		outputMmtfBean.setGroupList(genGroupList(inStruct.getGroupMap()));
		outputMmtfBean.setUnitCell(inHeader.getUnitCell());
		outputMmtfBean.setBioAssemblyList(inHeader.getBioAssembly());
		// Now set this extra header information
		outputMmtfBean.setTitle(inHeader.getTitle());
		// Now add the byte arrays to the bean
		addByteArrs(outputMmtfBean, bioBean);
		// Now set the version
		outputMmtfBean.setMmtfProducer("RCSB-PDB Generator---version: "+grs.getCurrentVersion());
		return outputMmtfBean;
	}
	
	/**
	 * Convert the a run length encoded List of strings to an integer array.
	 * The values are in pairs. The first value of each pair encodes the character.
	 * The second is a string representing the number of repeats. 
	 * @param inputStringArray The list of strings. Values are in pairs. The first value is a String of length 1.
	 * The second value encodes an integer as a string.
	 * @return An integer array of the values having been converted to integers.
	 */
	private final int[] convertRunLengthStringListToIntArray(List<String> inputStringArray) {
		// The output integer array
		int[] outArray = new int[inputStringArray.size()];
		// Now loop through the array first value is char. 
		for (int i=0; i<inputStringArray.size(); i+=2) {
			if ( inputStringArray.get(i)==null){
				outArray[i] = MmtfBean.UNAVAILABLE_CHAR_VALUE;
			}
			else{
				outArray[i] = inputStringArray.get(i).charAt(0);
			}
			
		}
		// The second value is an integer.
		for (int i=1; i<inputStringArray.size(); i+=2) {
			outArray[i] = Integer.parseInt(inputStringArray.get(i));
		}		
		
		return outArray;
	}


	/**
	 * Covert a Date object to ISO time format.
	 * @param inputDate The input date object
	 * @return the time in ISO time format
	 */
	private final String convertToIsoTime(Date inputDate) {
		DateFormat dateStringFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateStringFormat.format(inputDate);
	}


	/**
	 * Returns a PDBGroupList from a GroupMap. Uses the key of the map as the index in the list.
	 * @param groupMap the input map of Integer -> PDBGroup
	 * @return a list of PDBGroups, where the previous keys are used as indices.
	 */
	private final PDBGroup[] genGroupList(Map<Integer, PDBGroup> groupMap) {
		PDBGroup[] outGroupList = new PDBGroup[Collections.max(groupMap.keySet())+1];
		for (int key : groupMap.keySet()) {
			outGroupList[key] = groupMap.get(key);
		}
		return outGroupList;
	}


	/**
	 * Add the required bytearrays to an mmtfbean.
	 * @param inputMmtfBean the mmtf bean to which the arrays are to be added.
	 * @param inputDataBean the bean holding the coordinate information.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private final void addByteArrs(MmtfBean inputMmtfBean, NoFloatDataStructBean inputDataBean) throws IOException {
		EncoderUtils cm = new EncoderUtils();
		// Convert the x,y,z coordinate arrays as 4 and 2 byte arrays.
		List<byte[]> retArr = splitListIntsToByteArrays(inputDataBean.get_atom_site_Cartn_xInt());
		inputMmtfBean.setxCoordBig(retArr.get(0));
		inputMmtfBean.setxCoordSmall(retArr.get(1));
		retArr = splitListIntsToByteArrays(inputDataBean.get_atom_site_Cartn_yInt());
		inputMmtfBean.setyCoordBig(retArr.get(0));
		inputMmtfBean.setyCoordSmall(retArr.get(1));
		retArr = splitListIntsToByteArrays(inputDataBean.get_atom_site_Cartn_zInt());
		inputMmtfBean.setzCoordBig(retArr.get(0));
		inputMmtfBean.setzCoordSmall(retArr.get(1));
		// Set the bfactor arrays as 4 and 2 byte arrays
		retArr = splitListIntsToByteArrays(inputDataBean.get_atom_site_B_iso_or_equivInt());
		inputMmtfBean.setbFactorBig(retArr.get(0));
		inputMmtfBean.setbFactorSmall(retArr.get(1));
		// Now the occupancy
		inputMmtfBean.setOccupancyList(cm.integersToBytes(inputDataBean.get_atom_site_occupancyInt()));
		// System.out.println(Collections.max(bioBean.getResOrder()));
		inputMmtfBean.setGroupTypeList((cm.integersToBytes(inputDataBean.getResOrder())));
		inputMmtfBean.setAtomIdList(cm.integersToBytes(inputDataBean.get_atom_site_id()));
		// Now the secondary structure
		inputMmtfBean.setSecStructList(cm.integersToSmallBytes(inputDataBean.getSecStruct()));
		// Now set the group num list
		inputMmtfBean.setGroupIdList(cm.integersToBytes(inputDataBean.get_atom_site_auth_seq_id()));
	}

	/**
	 * Write a list of integers to 1 byte integers.
	 * @param inputList the input list of integers.
	 * @return the byte array, each byte is a different integer.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final byte[] integersToSmallBytes(List<Integer> inputList) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i: inputList)
		{
			dos.writeByte(i);
		}
		return baos.toByteArray();
	}

	/**
	 * Split a list of integers into small (2 byte) and big (4 byte) integers.
	 * @param inputList the input list of 4 byte integers.
	 * @return a list (length two) of byte arrays. The first array is the four byte integers.
	 * The second is the two byte integers.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final List<byte[]> splitListIntsToByteArrays(List<Integer> inputList) throws IOException{
		// Initialise the list to return
		List<byte[]> outputList = new ArrayList<>();
		// The counter keeps track of  every short (two byte int) that has been added after a four byte int.
		int counter = 0;
		// Generate the output streams and data output streams.
		ByteArrayOutputStream twoByteOutputStream = new ByteArrayOutputStream();
		DataOutputStream twoByteDataOutputStream = new DataOutputStream(twoByteOutputStream);
		ByteArrayOutputStream fourByteOutputStream = new ByteArrayOutputStream();
		DataOutputStream fourByteDataOutputStream = new DataOutputStream(fourByteOutputStream);
		// Iterate through the input list.
		for(int i=0;i<inputList.size();i++){
			// First number always goes as a four byte integer.
			if(i==0){
				fourByteDataOutputStream.writeInt(inputList.get(i));
			}
			// If the number is greater than the maximum for a two byte integer. Store as a four byte integer.
			else if(Math.abs(inputList.get(i))>Short.MAX_VALUE){
				// Add the counter to the four byte list.
				fourByteDataOutputStream.writeInt(counter);
				// Add the number to the four byte list.
				fourByteDataOutputStream.writeInt(inputList.get(i));
				// Set the counter to zero.
				counter = 0;
			}
			else{
				// Add the two byte integer to the two byte list.
				twoByteDataOutputStream.writeShort(inputList.get(i));
				// Add one to the counter.
				counter+=1;
			}
		}
		// Finally add the final counter to the four byte list.
		fourByteDataOutputStream.writeInt(counter);
		// Add the two byte arrays to the out put list.
		outputList.add(fourByteOutputStream.toByteArray());
		outputList.add(twoByteOutputStream.toByteArray());
		return outputList;
	}

	/**
	 * Utility function to gzip compress a byte[].
	 * @param inputArray the input array
	 * @return the byte[]
	 */
	public final byte[] gzipCompress(byte[] inputArray){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try{
			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			gzipOutputStream.write(inputArray);
			gzipOutputStream.close();
		} catch(IOException e){
			throw new RuntimeException(e);
		}
		System.out.printf("Compression %f\n", (1.0f * inputArray.length/byteArrayOutputStream.size()));
		return byteArrayOutputStream.toByteArray();
	}


	/**
	 * Set the chain id of a given chain to the byte array comprising the chain ids (as chars).
	 * Chain ids can be up to four chars long. If they are shorter they are padded using 0 bytes.
	 * @param chainId the string chain id. 
	 * @param charChainList the char chain list. The current chain list of chars.
	 * @param chainIndex the chain index. The current chain index.
	 */
	public void setChainId(String chainId, byte[] charChainList, int chainIndex) {
		// A char array to store the chars
		char[] outChar = new char[4];
		// The length of this chain id
		int chainIdLen =  chainId.length();
		chainId.getChars(0, chainIdLen, outChar, 0);
		// Set the bytrarray - chain ids can be up to 4 chars - pad with empty bytes
		charChainList[chainIndex*4+0] = (byte) outChar[0];
		if(chainIdLen>1){
			charChainList[chainIndex*4+1] = (byte) outChar[1];
		}
		else{
			charChainList[chainIndex*4+1] = (byte) 0;
		}
		if(chainIdLen>2){
			charChainList[chainIndex*4+2] = (byte) outChar[2];
		}				
		else{
			charChainList[chainIndex*4+2] = (byte) 0;
		}
		if(chainIdLen>3){
			charChainList[chainIndex*4+3] = (byte) outChar[3];
		}				
		else{
			charChainList[chainIndex*4+3] =  (byte) 0;
		}		
	}

	
	/**
	 * Function to compress the input biological data.
	 * @param inputBioDataStruct the input data structure
	 * @return a core single structure
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 * @throws Exception The bean data copying didn't work - weird.
	 */
	public final CoreSingleStructure compressInputData(BioDataStruct inputBioDataStruct) {
		// Convert the arrays to integers.
		NoFloatDataStruct inStruct = (NoFloatDataStruct) doublesToInts.compresStructure(inputBioDataStruct);
		// Get the lists of coordinates.
		List<Integer> cartnX = inStruct.get_atom_site_Cartn_xInt();
		List<Integer> cartnY = inStruct.get_atom_site_Cartn_yInt();
		List<Integer> cartnZ = inStruct.get_atom_site_Cartn_zInt();
		// Get the number of models
		inStruct.set_atom_site_Cartn_xInt(deltaComp.compressIntArray(cartnX));
		inStruct.set_atom_site_Cartn_yInt(deltaComp.compressIntArray(cartnY));
		inStruct.set_atom_site_Cartn_zInt(deltaComp.compressIntArray(cartnZ));		
		// Compress the b factors using delta compression.
		inStruct.set_atom_site_B_iso_or_equivInt(deltaComp.compressIntArray(inStruct.get_atom_site_B_iso_or_equivInt()));
		// Run length compress the occupanct
		inStruct.set_atom_site_occupancyInt(runLengthComp.compressIntArray(inStruct.get_atom_site_occupancyInt()));
		// Now the sequential numbers - huge gain - new order of good compressors
		// Now runlength encode the residue order
		inStruct.setResOrder(inStruct.getResOrder());
		// Check for negative counters
		inStruct.set_atom_site_auth_seq_id(runLengthComp.compressIntArray(deltaComp.compressIntArray(inStruct.get_atom_site_auth_seq_id())));
		inStruct.set_atom_site_label_entity_poly_seq_num(runLengthComp.compressIntArray(deltaComp.compressIntArray(inStruct.get_atom_site_label_entity_poly_seq_num())));
		inStruct.set_atom_site_id(runLengthComp.compressIntArray(deltaComp.compressIntArray(inStruct.get_atom_site_id())));
		// Now run length decode the strings
		StringArrayCompressor stringRunEncode = new RunLengthEncodeString();
		inStruct.set_atom_site_label_alt_id(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_label_alt_id()));
		//inStruct.set_atom_site_label_entity_id(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_label_entity_id()));
		inStruct.set_atom_site_pdbx_PDB_ins_code(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_pdbx_PDB_ins_code()));
		return inStruct;
	}

	/**
	 * Compress Calpha data to the output format.
	 * @param calphaData the calpha struct
	 * @param inHeader the header data
	 * @return the compressed calpha data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final CalphaDistBean compCAlpha(CalphaBean calphaData, HeaderBean inHeader) throws IOException {
		EncoderUtils cm = new  EncoderUtils();
		// Create the object to leave
		CalphaDistBean calphaOut = new CalphaDistBean();
		calphaOut.setMmtfProducer("RCSB-PDB Generator---version: "+grs.getCurrentVersion());
		// The PDBCode
		calphaOut.setPdbId(inHeader.getPdbCode());
		// The title of the structure
		calphaOut.setTitle(inHeader.getTitle());
		// String for the space group
		calphaOut.setSpaceGroup(inHeader.getSpaceGroup());
		// The unit cell information
		calphaOut.setUnitCell(inHeader.getUnitCell());
		// A map of Bioassembly -> new class so serializable
		calphaOut.setBioAssembly(inHeader.getBioAssembly());
		// Now set the number of bonds
		calphaOut.setNumBonds(calphaData.getNumBonds());
		calphaOut.setGroupsPerChain(calphaData.getGroupsPerChain());
		// Set this header info
		calphaOut.setChainsPerModel(inHeader.getChainsPerModel());
		calphaOut.setGroupsPerChain(calphaData.getGroupsPerChain());
		calphaOut.setChainIdList(inHeader.getChainList());
		calphaOut.setNumAtoms(calphaData.getNumAtoms());
		// Write the secondary stucture out
		calphaOut.setSecStructList(cm.integersToSmallBytes(calphaData.getSecStruct()));
		calphaOut.setGroupMap(calphaData.getGroupMap());
		calphaOut.setGroupTypeList(cm.integersToBytes(calphaData.getResOrder()));
		// Get the input structure
		List<Integer> cartnX = calphaData.getCartn_x();
		List<Integer> cartnY = calphaData.getCartn_y();
		List<Integer> cartnZ = calphaData.getCartn_z();
		// Now add the X coords
		List<byte[]> bigAndLittleX = splitListIntsToByteArrays(deltaComp.compressIntArray(cartnX));
		calphaOut.setxCoordBig(bigAndLittleX.get(0));
		calphaOut.setxCoordSmall(bigAndLittleX.get(1));
		//  No add they Y coords
		List<byte[]> bigAndLittleY = splitListIntsToByteArrays(deltaComp.compressIntArray(cartnY));
		calphaOut.setyCoordBig(bigAndLittleY.get(0));
		calphaOut.setyCoordSmall(bigAndLittleY.get(1));
		// Now add the Z coords
		List<byte[]> bigAndLittleZ = splitListIntsToByteArrays(deltaComp.compressIntArray(cartnZ));
		calphaOut.setzCoordBig(bigAndLittleZ.get(0));
		calphaOut.setzCoordSmall(bigAndLittleZ.get(1));	
		// THESE ONES CAN BE RUN LENGTH ON DELTA
		calphaOut.setGroupIdList(cm.integersToBytes(runLengthComp.compressIntArray(deltaComp.compressIntArray(calphaData.get_atom_site_auth_seq_id()))));
		return calphaOut;
	}

}
