package org.rcsb.mmtf.encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.rcsb.mmtf.sedeserializers.MmtfBeanSeDeMessagePackImpl;
import org.rcsb.mmtf.sedeserializers.MmtfBeanSeDerializerInterface;

public class WriterUtils {

	/**
	 * Function to write data to a file.
	 * @param writerToEncoder the writer to encoder instance
	 * @param path the full path to write to
	 * @throws IOException 
	 */
	public static void writeDataToFile(WriterToEncoder writerToEncoder, String path) throws IOException {
		byte[] byteArray = getDataAsByteArr(writerToEncoder);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(byteArray);
		fos.close();
	}
	
	
	/**
	 * Function to take data from a writer and return as a byte array (MessagePacked serialized).
	 * @param writerToEncoder the writer to encoder instance
	 * @return a byte array of the data
	 * @throws IOException
	 */
	public static byte[] getDataAsByteArr(WriterToEncoder writerToEncoder) throws IOException {
		MmtfBeanSeDerializerInterface mmtfBeanSeDerializerInterface = new MmtfBeanSeDeMessagePackImpl();
		// Get to bean
		DefaultEncoder getToBean = new DefaultEncoder(writerToEncoder);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mmtfBeanSeDerializerInterface.serialize(getToBean.getMmtfBean(), bos);
		return bos.toByteArray();
	}

}
