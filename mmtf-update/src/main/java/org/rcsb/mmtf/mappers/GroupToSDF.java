package org.rcsb.mmtf.mappers;


import org.apache.spark.api.java.function.PairFunction;
import org.biojava.nbio.structure.Group;


import scala.Tuple2;

public class GroupToSDF implements PairFunction<Tuple2<String, Group>,String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Tuple2<String, String> call(Tuple2<String, Group> t) throws Exception {
		// Now just return the group as the SDF
		return new Tuple2<String, String>(t._1, t._2.toSDF());
	}

}

