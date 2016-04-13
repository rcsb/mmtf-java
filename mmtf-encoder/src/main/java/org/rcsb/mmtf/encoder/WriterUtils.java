package org.rcsb.mmtf.encoder;

import java.io.FileOutputStream;
import java.io.IOException;

import org.rcsb.mmtf.serializers.MessagePackSerializer;

public class WriterUtils {

	/**
	 * Function to write data to a file
	 * @param inflatorToGet
	 * @param path
	 * @throws IOException 
	 */
	public static void writeDataToFile(WriterToEncoder inflatorToGet, String path) throws IOException {
		byte[] byteArray = getDataAsByteArr(inflatorToGet);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(byteArray);
		fos.close();
	}
	
	
	public static byte[] getDataAsByteArr(WriterToEncoder inflatorToGet) throws IOException {
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		// Get to bean
		DefaultEncoder getToBean = new DefaultEncoder(inflatorToGet);
		return messagePackSerializer.serialize(getToBean.getMmtfBean());
	}

}
