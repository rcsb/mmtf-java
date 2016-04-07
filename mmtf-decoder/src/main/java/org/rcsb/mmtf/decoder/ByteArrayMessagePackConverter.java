package org.rcsb.mmtf.decoder;

import java.io.IOException;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.api.ByteArrayToObjectConverterInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to covert a byte array to an object using message pack conversion.
 * @author Anthony Bradley
 *
 */
public class ByteArrayMessagePackConverter implements ByteArrayToObjectConverterInterface {

	@Override
	public MmtfBean convert(byte[] byteArray) {
		MmtfBean mmtfBean = null;
		try {
			mmtfBean = new ObjectMapper(new MessagePackFactory()).readValue(byteArray, MmtfBean.class);
		} catch (IOException e) {
			System.err.println("Error converting Byte array to message pack. IOError");
			e.printStackTrace();
			throw new RuntimeException();
		}
		return mmtfBean;
	}

}
