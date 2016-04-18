package org.rcsb.mmtf.encoder;

import java.io.FileOutputStream;
import java.io.IOException;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.serializers.MessagePackSerializer;

/**
 * A class of static utility methods to aid writing of data.
 * @author Anthony Bradley
 *
 */
public class WriterUtils {

	/**
	 * Function to write data to a file.
	 * @param writerToEncoder the writer to encoder instance
	 * @param path the full path to write to
	 * @throws IOException an error related to byte array transfers
	 */
	public static void writeDataToFile(StructureDataInterface writerToEncoder, String path) throws IOException {
		byte[] byteArray = getDataAsByteArr(writerToEncoder);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(byteArray);
		fos.close();
	}
	
	
	/**
	 * Function to take data from a writer and return as a byte array (MessagePacked serialized).
	 * @param writerToEncoder the writer to encoder instance
	 * @return a byte array of the data
	 * @throws IOException an error related to byte array transfers
	 */
	public static byte[] getDataAsByteArr(StructureDataInterface writerToEncoder) throws IOException {
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		// Get to bean
		DefaultEncoder getToBean = new DefaultEncoder(writerToEncoder);
		return messagePackSerializer.serialize(getToBean.getMmtfBean());
	}

}
