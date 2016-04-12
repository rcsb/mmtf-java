package org.rcsb.mmtf.mappers;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.StructureImpl;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureReader;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureWriter;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.rcsb.mmtf.decoder.BeanToDataApi;
import org.rcsb.mmtf.decoder.DataApiToReader;
import org.rcsb.mmtf.deserializers.MessagePackDeserializer;
import org.rcsb.mmtf.encoder.WriterToDataApi;
import org.rcsb.mmtf.encoder.WriterUtils;

/**
 * A class to preserve the log if the functions in mappers. 
 * Mappers should not contain logic - as they are hard to test.
 * @author Anthony Bradley
 *
 */
public class MapperUtils implements Serializable{
	
	private static final long serialVersionUID = -4717807367698811030L;

	/**
	 * Converts a byte array of the messagepack (mmtf) to a Biojava structure. 
	 * @param pdbCodePlus The pdb code is the first four characters. Additional characters can be used.
	 * @param inputByteArr The message pack bytre array to be decoded.
	 * @return
	 * @throws IOException 
	 */
	public static Structure byteArrToBiojavaStruct(String pdbCodePlus, byte[] inputByteArr) throws IOException { 
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		// Get the reader - this is the bit that people need to implement.
		MmtfStructureReader mmtfStructureReader = new MmtfStructureReader();
		// Set up the inflator
		DataApiToReader getToInflator = new DataApiToReader();
		Structure newStruct;
		try{
			// Do the inflation
			getToInflator.read(new BeanToDataApi( messagePackDeserializer.deserialize(inputByteArr)), mmtfStructureReader);
			// Get the structue
			newStruct =  mmtfStructureReader.getStructure();;
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println(pdbCodePlus);
			Structure thisStruct = new StructureImpl();
			return thisStruct;
		}
		return newStruct;
	}
	
	/**
	 * PDB RDD gnerateor. Converts a list of pdb ids to a writeable RDD
	 * @param sparkContext
	 * @return
	 */
	public JavaPairRDD<Text, BytesWritable> generateRDD(JavaSparkContext sparkContext, List<String> inputList, String inputUrl) {
		// Set up Biojava appropriateyl	
		MmtfUtils.setUpBioJava();
		return sparkContext.parallelize(inputList)
		.mapToPair(new PdbIdToMmtf())
		.mapToPair(new StringByteToTextByteWriter());
	}

	/**
	 * Get the available data as a byte array
	 * @param pdbId
	 * @return the data as a byte array
	 * @throws StructureException 
	 * @throws IOException 
	 */
	public static byte[] getByteArray(String pdbId) throws IOException, StructureException {
		Structure structure = StructureIO.getStructure(pdbId);
		// Set up this writer
		WriterToDataApi inflatorToGet = new WriterToDataApi();
		// Get the writer - this is what people implement
		MmtfStructureWriter mmtfStructureWriter = new MmtfStructureWriter(structure);
		// Now pass to the get API
		mmtfStructureWriter.write(inflatorToGet);
		// Now write this dat to file
		return WriterUtils.getDataAsByteArr(inflatorToGet);
	}
}
