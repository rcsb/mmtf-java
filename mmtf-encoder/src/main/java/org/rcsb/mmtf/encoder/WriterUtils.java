package org.rcsb.mmtf.encoder;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.rcsb.mmtf.serialization.MessagePackSerialization;

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
	public static void writeDataToFile(AdapterToStructureData writerToEncoder, Path path) throws IOException {
		byte[] byteArray = getDataAsByteArr(writerToEncoder);
		OutputStream fos = Files.newOutputStream(path); 
		fos.write(byteArray);
		fos.close();
	}
	
	
	/**
	 * Function to take data from a writer and return as a byte array (MessagePacked serialized).
	 * @param writerToEncoder the writer to encoder instance
	 * @return a byte array of the data
	 * @throws IOException an error related to byte array transfers
	 */
	public static byte[] getDataAsByteArr(AdapterToStructureData writerToEncoder) throws IOException {
		MessagePackSerialization mmtfBeanSeDerializerInterface = new MessagePackSerialization();
		// Get to bean
		DefaultEncoder getToBean = new DefaultEncoder(writerToEncoder);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mmtfBeanSeDerializerInterface.serialize(getToBean.getMmtfEncodedStructure(), bos);
		return bos.toByteArray();
	}

}
