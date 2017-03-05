package org.rcsb.mmtf.serialization.mp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineFile {

	private File file;

	public LineFile(File f) {
		this.file = f;
	}

	public List<String> readLines() {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			List<String> lines = new ArrayList<>();
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void writeLine(String line) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
			bw.write(line + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
