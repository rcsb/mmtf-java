package org.rcsb.mmtf.update;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class RemoveSuffixAndGzip implements PairFunction<Tuple2<String,byte[]>, String,byte[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8038296891190383974L;

	@Override
	public Tuple2<String, byte[]> call(Tuple2<String, byte[]> t) throws Exception {
		// Now return the array with just the PDB code
		// And gzip compress the byte arr
		return new Tuple2<String,byte[]>(t._1.substring(0,4),gzipCompress(t._2));
	}

	private byte[] gzipCompress(byte[] dataToCompress) throws IOException {
		// Function to gzip compress the data for the hashmaps
		ByteArrayOutputStream byteStream =
		new ByteArrayOutputStream(dataToCompress.length);
		try
		{
			GZIPOutputStream zipStream =
					new GZIPOutputStream(byteStream);
			try
			{
				zipStream.write(dataToCompress);
			}
			finally
			{
				zipStream.close();
			}
		}
		finally
		{
			byteStream.close();
		}

		byte[] compressedData = byteStream.toByteArray();
		return compressedData;

	}

}
