package org.rcsb.mmtf.serialization.mp;

import org.rcsb.mmtf.utils.Lines;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;
import org.rcsb.mmtf.serialization.MessagePackSerialization;

import org.unitils.reflectionassert.ReflectionAssert;

/**
 * Tests if both MessagePack decoding methods, Jackson and the manual object
 * creation gives identical data.
 *
 */
public class MessagePackSerializationTest {

	private final int n = 10; // how many structures should be tested
	private final List<String> testCodes;

	public MessagePackSerializationTest() throws IOException {
		testCodes = getTestCodes();
	}

	private List<String> getTestCodes() throws IOException {		
		List<String> result = new ArrayList<>();
		Random random = new Random();
		int seed = random.nextInt();		
		System.out.println("Using seed " + seed + " to select " + n + " random "
			+ "structures for MessagePack testing.");
		random = new Random(seed);
		List<String> codes = getAllPdbCodes();
		for (int i = 0; i < Math.min(n, codes.size()); i++) {
			int r = random.nextInt(codes.size());
			String code = codes.get(r);
			codes.remove(r);
			result.add(code);
		}
		return result;
	}

	public List<String> getAllPdbCodes() throws IOException {
		List<String> codes = new ArrayList<>();
		for (String line : Lines.readResource("/mmtf/pdb_codes.gz")) {
			String code = line.trim().substring(0, 4);
			codes.add(code);
		}
		return codes;
	}

	/**
	 * Decodes the MMTF from the MessagePack data.
	 */
	private StructureDataInterface parse(byte[] bytes) throws IOException, ParseException {
		MmtfStructure mmtf = ReaderUtils.getDataFromInputStream(
			new ByteArrayInputStream(bytes));
		GenericDecoder gd = new GenericDecoder(mmtf);
		return gd;
	}

	private byte[] fetchMmtf(String code) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String url = "http://mmtf.rcsb.org/v1.0/full/" + code + ".mmtf.gz";
		try (InputStream is = new URL(url).openStream()) {
			byte[] chunk = new byte[4096];
			int bytesRead;
			while ((bytesRead = is.read(chunk)) > 0) {
				outputStream.write(chunk, 0, bytesRead);
			}
		}
		return outputStream.toByteArray();
	}

	@Test
	public void testByComparisonWithJackson() throws IOException {

		for (String code : testCodes) {
			try {
				byte[] zipped = fetchMmtf(code);
				byte[] bytes = ReaderUtils.deflateGzip(zipped);

				MessagePackSerialization.setJackson(false);
				StructureDataInterface sdiJmol = parse(bytes);

				MessagePackSerialization.setJackson(true);
				StructureDataInterface sdiJackson = parse(bytes);

				ReflectionAssert.assertReflectionEquals(sdiJackson, sdiJmol);
			} catch (Exception ex) {
				throw new RuntimeException(code, ex);
			}
		}
	}

}
