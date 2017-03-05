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

import junit.framework.TestCase;
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
public class MessagePackTest extends TestCase {

	private int n = 10; // how many structures should be tested
	private List<String> testCodes;

	public MessagePackTest() {
		testCodes = getTestCodes();
	}

	private List<String> getTestCodes() {
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

	public Path getResource(String p) throws IOException {
		File f = new File(getClass().getResource(p).getFile());
		return Paths.get(f.getAbsolutePath());
	}

	public List<String> getAllPdbCodes() {
		try {
			Path p = getResource("pdb_entry_type.txt");
			List<String> codes = new ArrayList<>();
			LineFile lf = new LineFile(p.toFile());
			List<String> lines = lf.readLines();
			for (String line : lines) {
				StringTokenizer st = new StringTokenizer(line, " \t");
				String code = st.nextToken();
				codes.add(code);
			}
			return codes;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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

	private byte[] fetchMmtf(String code) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			byte[] chunk = new byte[4096];
			int bytesRead;
			String url = "http://mmtf.rcsb.org/v1.0/full/" + code + ".mmtf.gz";
			InputStream is = new URL(url).openStream();
			while ((bytesRead = is.read(chunk)) > 0) {
				outputStream.write(chunk, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return outputStream.toByteArray();
	}

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
