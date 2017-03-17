package org.rcsb.mmtf.serialization.mp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ZippedTextFile {

	public static List<String> readLines(File f) throws IOException {
		List<String> lines = new ArrayList<>();
		String line;
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(f))) {
			// set the position in stream to the first and only file
			zis.getNextEntry();
			BufferedReader br = new BufferedReader(new InputStreamReader(zis));
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			zis.closeEntry();
			return lines;
		}
	}

}
