package org.rcsb.mmtf.mappers;

import org.apache.spark.api.java.function.PairFunction;
import org.biojava.nbio.structure.Structure;

import scala.Tuple2;

/**
 * Maps a String byte[] of a message pack to a String Structure of the biojava structure
 * @author Anthony Bradley
 *
 */
public class ByteArrayToBioJavaStructMapper implements PairFunction<Tuple2<String, byte[]>,String, Structure> {

	private static final long serialVersionUID = -1671280971380509379L;
	MapperUtils mapperUtils = new MapperUtils();


	@Override
	public Tuple2<String, Structure> call(Tuple2<String, byte[]> t) throws Exception {
		// Now return this
		return new Tuple2<String, Structure>(t._1, mapperUtils.byteArrToBiojavaStruct(t._1, t._2));
	}
}
