package org.rcsb.mmtf.testutils;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureReader;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureWriter;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.decoder.BeanToDataApi;
import org.rcsb.mmtf.decoder.DataApiToReader;
import org.rcsb.mmtf.deserializers.MessagePackDeserializer;
import org.rcsb.mmtf.encoder.DataApiToBean;
import org.rcsb.mmtf.encoder.WriterToDataApi;
import org.rcsb.mmtf.encoder.WriterUtils;

public class Utils {

	public static Structure readFromByteArr(byte[] inputByteArr) throws IOException {
		// Get the reader - this is the bit that people need to implement.
		MmtfStructureReader mmtfStructureReader = new MmtfStructureReader();
		// Set up the inflator
		DataApiToReader getToInflator = new DataApiToReader();
		// Now do the deserializer
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		// Do the inflation
		getToInflator.read(new BeanToDataApi(messagePackDeserializer.deserialize(inputByteArr)), mmtfStructureReader);
		// Get the structue
		return mmtfStructureReader.getStructure();
	}
	
	
	public static byte[] writeToByteArr(Structure structure) throws IOException {
		// Set up this writer
		WriterToDataApi inflatorToGet = new WriterToDataApi();
		// Get the writer - this is what people implement
		MmtfStructureWriter mmtfStructureWriter = new MmtfStructureWriter(structure);
		// Now pass to the get API
		mmtfStructureWriter.write(inflatorToGet);
		return WriterUtils.getDataAsByteArr(inflatorToGet);
	}
	
	/**
	 * Round trip a biojava structuree
	 * @param structure
	 * @return
	 * @throws IOException 
	 */
	public static Structure roundTrip(Structure structure) throws IOException {
		return readFromByteArr(writeToByteArr(structure));
	}
	

	/**
	 * Get an MMTF bean from a pdb ID
	 * @param pdbId
	 * @return
	 * @throws StructureException 
	 * @throws IOException 
	 */
	public static MmtfBean getBean(String pdbId) throws IOException, StructureException {
		Structure structure = StructureIO.getStructure(pdbId);
		// Set up this writer
		WriterToDataApi inflatorToGet = new WriterToDataApi();
		// Get the writer - this is what people implement
		MmtfStructureWriter mmtfStructureWriter = new MmtfStructureWriter(structure);
		mmtfStructureWriter.write(inflatorToGet);
		DataApiToBean dataApiToBean = new DataApiToBean(inflatorToGet);
		return dataApiToBean.getMmtfBean();
		
	}

}
