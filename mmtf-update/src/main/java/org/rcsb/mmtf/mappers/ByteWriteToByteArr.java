package org.rcsb.mmtf.mappers;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * Maps the input of a Hadoop sequence file (Text/Bytes Writeable) to a String, byte[]
 * @author Anthony Bradley
 *
 */
public class ByteWriteToByteArr implements PairFunction<Tuple2<Text, BytesWritable>,String, byte[]> {


	private static final long serialVersionUID = 1466772536507675533L;

	@Override
	public Tuple2<String, byte[]> call(Tuple2<Text, BytesWritable> t) throws Exception {
		// Simply return the byte array
		return new Tuple2<String, byte[]>(t._1.toString(), t._2.copyBytes());
	}

}
