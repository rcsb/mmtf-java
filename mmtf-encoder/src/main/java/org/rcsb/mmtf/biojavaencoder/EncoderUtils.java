package org.rcsb.mmtf.biojavaencoder;




import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
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
import org.rcsb.mmtf.gitversion.GetRepoState;

import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * This class finds an mmCIF file and saves it as a csv file .
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
	 * Take a list of integers (as List<Integer>).
	 *
	 * @param inputList the input integer array
	 * @return the byte[] output 
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
	 * Get a messagepack from a bean.
	 *
	 * @param inputObject the input object
	 * @return the message pack
	 * @throws JsonProcessingException the json processing exception - most likely related 
	 * to serialization.
	 */
	public byte[] getMessagePack(Object inputObject) throws JsonProcessingException{
		com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new MessagePackFactory());
		byte[] inBuf = objectMapper.writeValueAsBytes(inputObject);
		return inBuf;
	}


	/**
	 * Compress the biological and header data into a combined data structure.
	 *
	 * @param inStruct the in struct
	 * @param inHeader the in header
	 * @return the byte array for the compressed data
	 * @throws IOException reading byte array
	 * @throws Exception 
	 */
	public MmtfBean compressMainData(BioDataStruct inStruct, HeaderBean inHeader) throws IOException {
		EncoderUtils cm = new EncoderUtils();
		// Compress the protein 
		CoreSingleStructure strucureData = compressHadoopStruct(inStruct);
		// NOW SET UP THE 
		MmtfBean thisDistBeanTot = new MmtfBean();
		NoFloatDataStructBean bioBean = (NoFloatDataStructBean) strucureData.findDataAsBean();
		// Copt these things
		thisDistBeanTot.setPdbId(bioBean.getPdbCode());
		thisDistBeanTot.setInsCodeList(bioBean.get_atom_site_pdbx_PDB_ins_code());
		thisDistBeanTot.setAltLabelList(bioBean.get_atom_site_label_alt_id());
		// Set this experimental data
		thisDistBeanTot.setResolution(inHeader.getResolution());
		thisDistBeanTot.setrFree(inHeader.getrFree());
		thisDistBeanTot.setrWork(inHeader.getrWork());
		// Copy the asym data
		thisDistBeanTot.setChainIdList(inHeader.getAsymChainList());
		thisDistBeanTot.setChainsPerModel(inHeader.getAsymChainsPerModel());
		thisDistBeanTot.setGroupsPerChain(inHeader.getAsymGroupsPerChain());
		// Get
		thisDistBeanTot.setEntityList(inHeader.getEntityList());
		// Get the seqres information
		thisDistBeanTot.setSeqResIdList(cm.integersToBytes(runLengthComp.compressIntArray(deltaComp.compressIntArray((ArrayList<Integer>) inHeader.getSeqResGroupIds()))));
		thisDistBeanTot.setChainSeqList(inHeader.getSequence());
		thisDistBeanTot.setExperimentalMethods(inHeader.getExperimentalMethods());
		// Now get this list
		thisDistBeanTot.setBondAtomList(cm.integersToBytes(inStruct.getInterGroupBondInds()));
		thisDistBeanTot.setBondOrderList(cm.integersToSmallBytes(inStruct.getInterGroupBondOrders()));
		// Now get these from the headers
		thisDistBeanTot.setChainNameList(inHeader.getChainList());
		thisDistBeanTot.setNumAtoms(inHeader.getNumAtoms());
		thisDistBeanTot.setNumBonds(inHeader.getNumBonds());
		// Now get the Xtalographic info from this header
		thisDistBeanTot.setSpaceGroup(inHeader.getSpaceGroup());
		thisDistBeanTot.setGroupMap(inStruct.getGroupMap());
		thisDistBeanTot.setUnitCell(inHeader.getUnitCell());
		thisDistBeanTot.setBioAssembly(inHeader.getBioAssembly());
		// Now set this extra header information
		thisDistBeanTot.setTitle(inHeader.getTitle());
		// Now add the byte arrays to the bean
		addByteArrs(thisDistBeanTot, bioBean);
		// Now set the version
		thisDistBeanTot.setMmtfProducer("RCSB-PDB Generator---version: "+grs.getCurrentVersion());
		return thisDistBeanTot;
	}

	/**
	 * Add the required bytearrays to an mmtfbean.
	 *
	 * @param thisDistBeanTot the this dist bean tot
	 * @param bioBean the bio bean
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addByteArrs(MmtfBean thisDistBeanTot, NoFloatDataStructBean bioBean) throws IOException {
		EncoderUtils cm = new EncoderUtils();
		// X,Y and Z and Bfactors - set these arrays
		List<byte[]> retArr = getBigAndLittle(bioBean.get_atom_site_Cartn_xInt());
		thisDistBeanTot.setxCoordBig(retArr.get(0));
		thisDistBeanTot.setxCoordSmall(retArr.get(1));
		retArr = getBigAndLittle(bioBean.get_atom_site_Cartn_yInt());
		thisDistBeanTot.setyCoordBig(retArr.get(0));
		thisDistBeanTot.setyCoordSmall(retArr.get(1));
		retArr = getBigAndLittle(bioBean.get_atom_site_Cartn_zInt());
		thisDistBeanTot.setzCoordBig(retArr.get(0));
		thisDistBeanTot.setzCoordSmall(retArr.get(1));
		retArr = getBigAndLittle(bioBean.get_atom_site_B_iso_or_equivInt());
		thisDistBeanTot.setbFactorBig(retArr.get(0));
		thisDistBeanTot.setbFactorSmall(retArr.get(1));
		// Now the occupancy
		thisDistBeanTot.setOccList(cm.integersToBytes(bioBean.get_atom_site_occupancyInt()));
		// System.out.println(Collections.max(bioBean.getResOrder()));
		thisDistBeanTot.setGroupTypeList((cm.integersToBytes(bioBean.getResOrder())));
		thisDistBeanTot.setAtomIdList(cm.integersToBytes(bioBean.get_atom_site_id()));

		// Now the secondary structure
		thisDistBeanTot.setSecStructList(cm.integersToSmallBytes(bioBean.getSecStruct()));
		// Now set the group num list
		thisDistBeanTot.setGroupIdList(cm.integersToBytes(bioBean.get_atom_site_auth_seq_id()));
	}


	/**
	 * Write a list of integers to 1 byte integers.
	 *
	 * @param values the values
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] integersToSmallBytes(List<Integer> values) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		for(int i: values)
		{
			dos.writeByte(i);
		}
		return baos.toByteArray();
	}

	/**
	 * Split an array into small (2 byte) and big (4 byte) integers.
	 *
	 * @param inArr the in arr
	 * @return the big and little
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<byte[]> getBigAndLittle(List<Integer> inArr) throws IOException{
		List<byte[]>outArr = new ArrayList<byte[]>();
		int counter = 0;
		ByteArrayOutputStream littleOS = new ByteArrayOutputStream();
		DataOutputStream littleDOS = new DataOutputStream(littleOS);
		ByteArrayOutputStream bigOS = new ByteArrayOutputStream();
		DataOutputStream bigDOS = new DataOutputStream(bigOS);
		for(int i=0;i<inArr.size();i++){
			if(i==0){
				// First number goes in big list
				bigDOS.writeInt(inArr.get(i));
			}
			else if(Math.abs(inArr.get(i))>30000){
				// Counter added to the big list
				bigDOS.writeInt(counter);
				// Big number added to big list
				bigDOS.writeInt(inArr.get(i));
				// Counter set to zero
				counter = 0;
			}
			else{
				// Little number added to little list
				littleDOS.writeShort(inArr.get(i));
				// Add to the counter
				counter+=1;
			}
		}
		// Finally add the counter to the big list 
		bigDOS.writeInt(counter);

		outArr.add(bigOS.toByteArray());
		outArr.add(littleOS.toByteArray());
		return outArr;
	}

	/**
	 * Utility function to gzip compress a byte[].
	 *
	 * @param inputArray the input array
	 * @return the byte[]
	 */
	public byte[] gzipCompress(byte[] inputArray){
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
	 * Function to compress the input biological data.
	 *
	 * @param inputBioDataStruct the input data structure
	 * @return a core single structure
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 * @throws Exception The bean data copying didn't work - weird.
	 */
	public CoreSingleStructure compressHadoopStruct(BioDataStruct inputBioDataStruct) {

		CoreSingleStructure outStruct;
		outStruct = doublesToInts.compresStructure(inputBioDataStruct);
		// Get the input structure
		NoFloatDataStruct inStruct =  (NoFloatDataStruct) outStruct;
		ArrayList<Integer> cartnX = (ArrayList<Integer>) inStruct.get_atom_site_Cartn_xInt();
		ArrayList<Integer> cartnY = (ArrayList<Integer>) inStruct.get_atom_site_Cartn_yInt();
		ArrayList<Integer> cartnZ = (ArrayList<Integer>) inStruct.get_atom_site_Cartn_zInt();

		// Get the number of models
		inStruct.set_atom_site_Cartn_xInt(deltaComp.compressIntArray(cartnX));
		inStruct.set_atom_site_Cartn_yInt(deltaComp.compressIntArray(cartnY));
		inStruct.set_atom_site_Cartn_zInt(deltaComp.compressIntArray(cartnZ));		
		//		// Now the occupancy and BFACTOR -> VERY SMALL GAIN
		inStruct.set_atom_site_B_iso_or_equivInt(deltaComp.compressIntArray((ArrayList<Integer>) inStruct.get_atom_site_B_iso_or_equivInt()));
		// SMALL GAIN
		inStruct.set_atom_site_occupancyInt(runLengthComp.compressIntArray((ArrayList<Integer>) inStruct.get_atom_site_occupancyInt()));
		// Now the sequential numbers - huge gain - new order of good compressors
		// Now runlength encode the residue order
		inStruct.setResOrder(inStruct.getResOrder());
		// THESE ONES CAN BE RUN LENGTH ON DELTA

		// Check for negative counters
		inStruct.set_atom_site_auth_seq_id(runLengthComp.compressIntArray(deltaComp.compressIntArray((ArrayList<Integer>) inStruct.get_atom_site_auth_seq_id())));
		inStruct.set_atom_site_label_entity_poly_seq_num(runLengthComp.compressIntArray(deltaComp.compressIntArray((ArrayList<Integer>) inStruct.get_atom_site_label_entity_poly_seq_num())));
		inStruct.set_atom_site_id(runLengthComp.compressIntArray(deltaComp.compressIntArray((ArrayList<Integer>) inStruct.get_atom_site_id())));
		//// NOW THE STRINGS  - small gain
		StringArrayCompressor stringRunEncode = new RunLengthEncodeString();
		inStruct.set_atom_site_label_alt_id(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_label_alt_id()));
		//inStruct.set_atom_site_label_entity_id(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_label_entity_id()));
		inStruct.set_atom_site_pdbx_PDB_ins_code(stringRunEncode.compressStringArray((ArrayList<String>) inStruct.get_atom_site_pdbx_PDB_ins_code()));
		return inStruct;
	}

	/**
	 * Comp c alpha.
	 *
	 * @param calphaStruct the calpha struct
	 * @param inHeader the in header
	 * @return the calpha dist bean
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public CalphaDistBean compCAlpha(CalphaBean calphaStruct, HeaderBean inHeader) throws IOException {
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
		calphaOut.setNumBonds(calphaStruct.getNumBonds());
		calphaOut.setGroupsPerChain(calphaStruct.getGroupsPerChain());
		// Set this header info
		calphaOut.setChainsPerModel(inHeader.getChainsPerModel());
		calphaOut.setGroupsPerChain(calphaStruct.getGroupsPerChain());
		calphaOut.setChainList(inHeader.getChainList());
		calphaOut.setNumAtoms(calphaStruct.getNumAtoms());
		// Write the secondary stucture out
		calphaOut.setSecStructList(cm.integersToSmallBytes(calphaStruct.getSecStruct()));
		calphaOut.setGroupMap(calphaStruct.getGroupMap());
		calphaOut.setGroupTypeList(cm.integersToBytes(calphaStruct.getResOrder()));
		// Get the input structure
		ArrayList<Integer> cartnX = (ArrayList<Integer>) calphaStruct.getCartn_x();
		ArrayList<Integer> cartnY = (ArrayList<Integer>) calphaStruct.getCartn_y();
		ArrayList<Integer> cartnZ = (ArrayList<Integer>) calphaStruct.getCartn_z();
		// Now add the X coords
		List<byte[]> bigAndLittleX = getBigAndLittle(deltaComp.compressIntArray(cartnX));
		calphaOut.setxCoordBig(bigAndLittleX.get(0));
		calphaOut.setxCoordSmall(bigAndLittleX.get(1));
		//  No add they Y coords
		List<byte[]> bigAndLittleY = getBigAndLittle(deltaComp.compressIntArray(cartnY));
		calphaOut.setyCoordBig(bigAndLittleY.get(0));
		calphaOut.setyCoordSmall(bigAndLittleY.get(1));
		// Now add the Z coords
		List<byte[]> bigAndLittleZ = getBigAndLittle(deltaComp.compressIntArray(cartnZ));
		calphaOut.setzCoordBig(bigAndLittleZ.get(0));
		calphaOut.setzCoordSmall(bigAndLittleZ.get(1));	
		// THESE ONES CAN BE RUN LENGTH ON DELTA
		calphaOut.setGroupNumList(cm.integersToBytes(runLengthComp.compressIntArray(deltaComp.compressIntArray((ArrayList<Integer>) calphaStruct.get_atom_site_auth_seq_id()))));
		return calphaOut;
	}

	/**
	 * Set up the configuration parameters for BioJava.
	 */
	public void setUpBioJava() {
		// Set up the atom cache etc
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		params.setUseInternalChainId(true);
		DownloadChemCompProvider cc = new DownloadChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
	}

}
