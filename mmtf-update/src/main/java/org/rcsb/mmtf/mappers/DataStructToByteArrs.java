package org.rcsb.mmtf.mappers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.rcsb.mmtf.biojavaencoder.BiojavaEncoderImpl;
import org.rcsb.mmtf.encoder.EncoderInterface;
import org.rcsb.mmtf.encoder.EncoderUtils;
import org.rcsb.mmtf.sedeserializers.BioDataStruct;
import org.rcsb.mmtf.sedeserializers.CalphaDistBean;
import org.rcsb.mmtf.sedeserializers.HeaderBean;

import scala.Tuple2;

/**
 * A class to generate the three (or more) byte arrays from the data structure parsed by the MMCIF.
 * Returns three byte arrays per PDB id.
 * @author Anthony Bradley
 *
 */
public class DataStructToByteArrs  implements PairFlatMapFunction<Tuple2<String, BiojavaEncoderImpl>, String, byte[]>{

	private static final long serialVersionUID = 2066093446043635571L;
	
	@Override
	public Iterable<Tuple2<String, byte[]>> call(Tuple2<String, BiojavaEncoderImpl> t) throws IOException, IllegalAccessException, InvocationTargetException {
		// First generate the list to return
		List<Tuple2<String, byte[]>> outList = new ArrayList<Tuple2<String, byte[]>>();
		EncoderUtils cm = new EncoderUtils();
		EncoderInterface cbs = t._2;
		String pdbCode = t._1;
		// Now get the header too
		HeaderBean headerData = cbs.getHeaderStruct();
		BioDataStruct thisBS = cbs.getBioStruct();
		CalphaDistBean calphaDistStruct = cm.compressCalpha(cbs.getCalphaStruct(), cbs.getHeaderStruct());
		// NOW JUST WRITE THE KEY VALUE PAIRS HERE
		byte[] totBytes = cm.getMessagePack(cm.compressToMmtfBean(thisBS, headerData));
		byte[] headerBytes = cm.getMessagePack(headerData);
		byte[] calphaBytes = cm.getMessagePack(calphaDistStruct);
		// Add the total data
		outList.add(new Tuple2<String, byte[]>(pdbCode+"_total", totBytes));
		// Add the header
		outList.add(new Tuple2<String, byte[]>(pdbCode+"_header", headerBytes));
		// Add the calpha
		outList.add(new Tuple2<String, byte[]>(pdbCode+"_calpha", calphaBytes));
		return outList;	
		
	}
}