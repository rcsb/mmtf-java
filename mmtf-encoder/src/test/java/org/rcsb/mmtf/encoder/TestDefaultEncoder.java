package org.rcsb.mmtf.encoder;

import java.io.IOException;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

public class TestDefaultEncoder {

	
	
	/**
	 * Test whether calling all the set methods gives a none null get
	 * @throws IOException
	 */
	@Test
	public void testEncodeAllFields() throws IOException {
		DummyApiImpl dummyApiImpl = new DummyApiImpl();
		DefaultEncoder defaultEncoder = new DefaultEncoder(dummyApiImpl);
		ReflectionAssert.assertPropertiesNotNull("Some properties null after encoding", defaultEncoder.getMmtfBean());
	}
	
	
	@Test
	public void testWriter() {
		WriterToEncoder writerToEncoder = new WriterToEncoder();
		writerToEncoder.initStructure(1, 1, 1, 1, 1, "ABC");
		writerToEncoder.setModelInfo(0, 1);
		writerToEncoder.setChainInfo("A","A",10);
		writerToEncoder.setGroupInfo("HET", 1, 'a', "D", 1, 1, 'A', 0, -1);
		writerToEncoder.setAtomInfo("A", 1, 'a', 1.0f, 1.0f, 1.0f,1.0f, 1.0f, "A", 1);
		writerToEncoder.setEntityInfo(new int[1], "A", "A", "A");
		writerToEncoder.setGroupBond(0, 0, 1);
		writerToEncoder.setHeaderInfo(1.0f, 1.0f, 1.0f, "A", "A", "A", new String[1]);
		writerToEncoder.setInterGroupBond(0, 0, 1);
		writerToEncoder.setBioAssemblyTrans(0, new int[1], new double[6]);
		writerToEncoder.setXtalInfo("A", new float[6]);
		writerToEncoder.finalizeStructure();
		ReflectionAssert.assertPropertiesNotNull("Some properties null after writing", writerToEncoder);
		
	}
}
