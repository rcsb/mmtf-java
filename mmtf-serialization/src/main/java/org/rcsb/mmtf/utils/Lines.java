package org.rcsb.mmtf.utils;

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
 * @author Antonin Pavelka
 *
 * Allows to load all lines from a gzipped data source into memory.
 */
public class Lines {

	/**
	 * Read lines from a resource specified by absolute path, e.g.
	 * /org/rcsb/mmtf/pdb_entry_type.gz. The location of the root of the path is
	 * something like project_dir/src/main/resources
	 *
	 * @param name Name of the resouce.
	 * @return Array containing all lines in the gzipped resouce.
	 * @throws java.io.IOException
	 */
	public static String[] readResource(String name) throws IOException {
		URL url = Lines.class.getResource(name);
		try (InputStream is = url.openStream()) {
			return readLines(is);
		}
	}

	/**
	 * Read lines from a file.
	 *
	 * @param f The input gzipped text file.
	 * @return List of all lines in the gzipped resouce.
	 * @throws java.io.IOException
	 */
	public static String[] readFile(File f) throws IOException {
		try (FileInputStream fis = new FileInputStream(f)) {
			return readLines(fis);
		}
	}

	private static String[] readLines(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
			new GZIPInputStream(is)));
		List<String> lines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		String[] a = lines.toArray(new String[lines.size()]);
		return a;
	}

}
