package org.rcsb.mmtf.biojavaencoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

public class EncodeStructure {

  /**
   * Get a byte array of the compressed messagepack MMTF data
   * from an input PDB id
   * @param pdbId
   * @return a byte array of compressed data
   */
	public byte[] getCompressedMessagePackFromPdbId(String pdbId) {
		// Get the utility class to get the strucutes
		ParseFromBiojava parsedDataStruct = new ParseFromBiojava();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		parsedDataStruct.createFromJavaStruct(pdbId, totMap);
		// Compress the data and get it back out
    return buildFromDataStructure(parsedDataStruct);
	}
	
	/**
	 * Generate the compressed messagepack MMTF data from a biojava structure
	 * @param bioJavaStruct
	 * @return a byte array of compressed data
	 */
	public byte[] encodeFromBiojava(Structure bioJavaStruct){
    // Get the utility class to get the strucutes
		ParseFromBiojava parsedDataStruct = new ParseFromBiojava();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		parsedDataStruct.generateDataStructuresFromBioJavaStructure(bioJavaStruct, totMap);
		return buildFromDataStructure(parsedDataStruct);
	}
	
	/**
	 * Build up a byte array from the parsed data
	 * @param parsedDataStruct
	 * @return a byte array of compressed data
	 */
	private byte[] buildFromDataStructure(ParseFromBiojava parsedDataStruct) {
	   EncoderUtils eu = new EncoderUtils();
	   // Compress the data and get it back out
    try {
      MmtfBean mmtfBean = eu.compressMainData(parsedDataStruct.getBioStruct(), parsedDataStruct.getHeaderStruct());
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
	public byte[] encodeBackBoneFromPdbId(String pdbId){
		// Get the two utility classes
		EncoderUtils eu = new EncoderUtils();
		ParseFromBiojava cbs = new ParseFromBiojava();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		// Parse the data into the basic data structure
		cbs.createFromJavaStruct(pdbId, totMap);
		// Compress the data and get it back out
		try {
      return eu.getMessagePack(eu.compCAlpha(cbs.getCalphaStruct(), cbs.getHeaderStruct()));
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Error in reading or writing byte array");
      throw new RuntimeException(e);
    }
	}
	
	
	
}
