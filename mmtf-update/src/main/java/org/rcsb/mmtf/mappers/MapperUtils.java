package org.rcsb.mmtf.mappers;

import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureImpl;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureDecoder;
import org.rcsb.mmtf.biojavaencoder.BiojavaUtils;
import org.rcsb.mmtf.decoder.GetApiToInflatorInterface;

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
	 */
	public Structure byteArrToBiojavaStruct(String pdbCodePlus, byte[] inputByteArr) { 
		MmtfStructureDecoder bjs = new MmtfStructureDecoder();
		Structure newStruct;
		try{
			GetApiToInflatorInterface ds = new GetApiToInflatorInterface(inputByteArr);
		ds.getStructFromByteArray(bjs);
		newStruct = bjs.getStructure();
		newStruct.setPDBCode(pdbCodePlus.substring(0,4));}
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
		BiojavaUtils biojavaUtils = new BiojavaUtils();
		biojavaUtils.setUpBioJava(inputUrl);
		return sparkContext.parallelize(inputList)
		.mapToPair(new PdbIdToDataStruct())
		.flatMapToPair(new DataStructToByteArrs())
		.mapToPair(new StringByteToTextByteWriter());
	}
}
