package org.rcsb.mmtf.mappers;


import org.apache.spark.api.java.function.PairFunction;
import org.biojava.nbio.structure.io.mmtf.MmtfActions;

import scala.Tuple2;

/**
 * Generate the internal data structure (using biojava) from a PDB code.
 * @author Anthony Bradley
 *
 */
public class PdbIdToMmtf implements PairFunction<String, String, byte[]>{

	private static final long serialVersionUID = 786599975302506694L;	

	@Override
	public Tuple2<String, byte[]> call(String t) throws Exception {
		return new Tuple2<String,byte[]>(t, MmtfActions.getByteArray(t));
	}


}

