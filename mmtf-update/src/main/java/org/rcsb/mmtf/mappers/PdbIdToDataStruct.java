package org.rcsb.mmtf.mappers;


import java.util.HashMap;
import java.util.Map;
import org.apache.spark.api.java.function.PairFunction;
import org.rcsb.mmtf.biojavaencoder.GenDataStructFromMmcif;
import org.rcsb.mmtf.dataholders.PDBGroup;

import scala.Tuple2;

/**
 * Generate the internal data structure (using biojava) from a PDB code.
 * @author Anthony Bradley
 *
 */
public class PdbIdToDataStruct implements PairFunction<String, String, GenDataStructFromMmcif>{

	private static final long serialVersionUID = 786599975302506694L;	

	@Override
	public Tuple2<String, GenDataStructFromMmcif> call(String t) throws Exception {
		GenDataStructFromMmcif cbs = new GenDataStructFromMmcif();
		Map<Integer, PDBGroup> totMap = new HashMap<Integer, PDBGroup>();
		try{
			cbs.createFromJavaStruct(t, totMap);
		}
		catch(Exception e){
			// Just return the object
			System.out.println(e+"  ::  "+t);
			System.out.println(e.getMessage());
			return new Tuple2<String, GenDataStructFromMmcif>(t,cbs);
		}
		// If it doesn't fail also return the object
		return new Tuple2<String, GenDataStructFromMmcif>(t,cbs);
	}


}

