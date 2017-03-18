package org.rcsb.mmtf.serialization.mp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

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
public class MessagePackTest {

	private int n = 10; // how many structures should be tested
	private List<String> testCodes;

	public MessagePackTest() throws IOException {
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
		URL url = getClass().getResource("pdb_entry_type.gz");
		List<String> codes = new ArrayList<>();
		for (String line : GzTxtFile.readLines(url)) {
			StringTokenizer st = new StringTokenizer(line, " \t");
			String code = st.nextToken();
			codes.add(code);
		}
		return codes;
	}

	/**
	 * Decodes the MMTF from the MessagePack data.
	 */
	private StructureDataInterface parse(byte[] bytes) throws IOException {
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
			System.out.println("Testing " + code);
			byte[] zipped = fetchMmtf(code);
			byte[] bytes = ReaderUtils.deflateGzip(zipped);

			MessagePackSerialization.setJackson(false);
			StructureDataInterface sdiJmol = parse(bytes);

			MessagePackSerialization.setJackson(true);
			StructureDataInterface sdiJackson = parse(bytes);

			ReflectionAssert.assertReflectionEquals(sdiJackson, sdiJmol);
			System.out.println("OK");
		}
	}

}
