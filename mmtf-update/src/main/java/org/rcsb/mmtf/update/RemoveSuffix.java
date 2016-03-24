package org.rcsb.mmtf.update;


import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class RemoveSuffix implements PairFunction<Tuple2<String,byte[]>, String,byte[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8038296891190383974L;

	@Override
	public Tuple2<String, byte[]> call(Tuple2<String, byte[]> t) throws Exception {
		// Now return the array with just the PDB code
		// And gzip compress the byte arr
		return new Tuple2<String,byte[]>(t._1.substring(0,4),t._2);
	}


}
