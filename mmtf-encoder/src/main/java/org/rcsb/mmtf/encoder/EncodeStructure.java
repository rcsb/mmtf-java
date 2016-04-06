package org.rcsb.mmtf.encoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

public class EncodeStructure {

	/**
	 * Get a byte array of the compressed messagepack MMTF data
	 * from an input PDB id
	 * @param pdbId
	 * @return a byte array of compressed data
	 */
	public final byte[] getCompressedMessagePackFromPdbId(String pdbId, EncoderInterface encoderInterface) {
		// Get the utility class to get the strucutes
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		encoderInterface.generateDataStructuresFromPdbId(pdbId, totMap);
		// Compress the data and get it back out
		return buildFromDataStructure(encoderInterface);
	}

	/**
	 * Generate the compressed messagepack MMTF data from a biojava structure
	 * @param bioJavaStruct
	 * @return a byte array of compressed data
	 */
	public final byte[] encodeFromPdbId(String pdbId, EncoderInterface encoderInterface){
		// Get the utility class to get the strucutes
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		encoderInterface.generateDataStructuresFromPdbId(pdbId, totMap);
		return buildFromDataStructure(encoderInterface);
	}

	/**
	 * Build up a byte array from the parsed data
	 * @param parsedDataStruct
	 * @return a byte array of compressed data
	 */
	private final byte[] buildFromDataStructure(EncoderInterface parsedDataStruct) {
		EncoderUtils eu = new EncoderUtils();
		// Compress the data and get it back out
		try {
			MmtfBean mmtfBean = eu.compressToMmtfBean(parsedDataStruct.getBioStruct(), parsedDataStruct.getHeaderStruct());
			return eu.getMessagePack(mmtfBean);
		} catch (IOException e) {
			// Here we've failed to read or write a byte array
			e.printStackTrace();
			System.err.println("Error reading or writing byte array - file bug report");
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generate the compressed messagepack of the calpha, phospohate and ligand data.
	 * @param pdbId The input pdb id
	 * @return a byte array of compressed calpha data
	 */
	public final byte[] encodeBackBoneFromPdbId(String pdbId, EncoderInterface encoderInterface){
		// Get the two utility classes
		EncoderUtils eu = new EncoderUtils();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		encoderInterface.generateDataStructuresFromPdbId(pdbId, totMap);
		// Compress the data and get it back out
		try {
			return eu.getMessagePack(eu.compressCalpha(encoderInterface.getCalphaStruct(), encoderInterface.getHeaderStruct()));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error in reading or writing byte array");
			throw new RuntimeException(e);
		}
	}



}
