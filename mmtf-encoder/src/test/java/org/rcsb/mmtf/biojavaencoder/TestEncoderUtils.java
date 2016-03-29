package org.rcsb.mmtf.biojavaencoder;


import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;

public class TestEncoderUtils {


	@Test
	public void microHeterogenity() throws IOException, StructureException {
		EncoderUtils encoderUtils = new EncoderUtils();
		AtomCache cache = encoderUtils.setUpBioJava();
		Structure inputStructure = StructureIO.getStructure("4ck4");
		
	}
}
