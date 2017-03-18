package org.rcsb.mmtf.serialization.mp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Allows to read all lines of a gzipped text file into memory.
 */
public class GzTxtFile {

	private static List<String> readLines(BufferedReader br) throws IOException {
		List<String> lines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

	private static BufferedReader convert(InputStream is) throws IOException {
		return new BufferedReader(new InputStreamReader(new GZIPInputStream(
			is)));
	}

	public static List<String> readLines(File f) throws IOException {
		try (FileInputStream fis = new FileInputStream(f)) {
			return readLines(convert(fis));
		}
	}

	public static List<String> readLines(URL url) throws IOException {
		try (InputStream is = url.openStream()) {
			return readLines(convert(is));
		}
	}
}
