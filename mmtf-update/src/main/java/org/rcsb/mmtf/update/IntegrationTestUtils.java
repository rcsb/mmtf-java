package org.rcsb.mmtf.update;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class IntegrationTestUtils {

	public static final String[] TEST_CASES = new String[] {

			// THESE CURRENTLY FAIL...
//			// Weird NMR structure
//			"1o2f",
//			// NMR structure with multiple models - one of which has chain missing
//			"1msh",
//			// A weird case with incorrect residue numbers.
//			"3th3",
//			// Calpha atom is missing (not marked as calpha)
//			"1lpv",
			// Standard structure
			"4cup",
			// Stanadard NMR structure
			"2n2z",
			// Another weird structure (jose's suggestion) 
			"3zyb",
			// B-DNA structure
			"1bna", 
			// DNA structure
			"4y60",
			// Sugar structure
			"1skm",
			// Ribosome
			"4v5a",
			// Biosynthetic protein
			"5emg",

			// No ATOM records just HETATM records (in PDB). Opposite true for MMCif. It's a D-Peptide.
			"1r9v",
			// Micro heterogenity
			"4ck4",
			// Negative residue numbers
			"5esw",
			};

	public Path returnTempDir() {
		Path tmpDir;
		String uuid = UUID.randomUUID().toString();
		try {
			tmpDir = Files.createTempDirectory(uuid);
		} catch (IOException e) {
			System.err.println("Error in making temp directory");
			e.printStackTrace();
			throw new RuntimeException();
		} 
		return tmpDir;
	}
}
