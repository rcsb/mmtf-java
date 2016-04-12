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
	public static void writeDataToFile(WriterToDataApi inflatorToGet, String path) throws IOException {
		byte[] byteArray = getDataAsByteArr(inflatorToGet);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(byteArray);
		fos.close();
	}
	
	
	public static byte[] getDataAsByteArr(WriterToDataApi inflatorToGet) throws IOException {
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		// Get to bean
		DataApiToBean getToBean = new DataApiToBean(inflatorToGet);
		return messagePackSerializer.serialize(getToBean.getMmtfBean());
	}

}
