package org.rcsb.mmtf.integrationtest;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.io.mmtf.MmtfStructureWriter;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.junit.Test;
import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.decoder.BeanToDataApi;
import org.rcsb.mmtf.encoder.DataApiToBean;
import org.rcsb.mmtf.encoder.WriterToDataApi;
import org.rcsb.mmtf.update.TestingUtils;



public class TestRoundTrip {


	@Test
	public void testApiRoundTrip() throws IOException, StructureException, IllegalArgumentException{
		// First set up Biojava
		MmtfUtils.setUpBioJava();
		Structure structure = StructureIO.getStructure("1O2F");
		MmtfStructureWriter mmtfStructureWriter = new MmtfStructureWriter(structure);
		WriterToDataApi mmtfApi = new WriterToDataApi();
		mmtfStructureWriter.write(mmtfApi);
		DataApiToBean getToBean = new DataApiToBean(mmtfApi);
		MmtfDecodedDataInterface beanToGet = new BeanToDataApi(getToBean.getMmtfBean());
		assertArrayEquals(beanToGet.getGroupTypeIndices(), mmtfApi.getGroupTypeIndices());
		assertArrayEquals(beanToGet.getGroupAtomNames(3), mmtfApi.getGroupAtomNames(3));
		assertArrayEquals(beanToGet.getGroupIds(), mmtfApi.getGroupIds());
		assertArrayEquals(beanToGet.getInterGroupBondOrders(), mmtfApi.getInterGroupBondOrders());
		assertArrayEquals(beanToGet.getInterGroupBondIndices(), mmtfApi.getInterGroupBondIndices());
		assertEquals(beanToGet.getNumBioassemblies(),mmtfApi.getNumBioassemblies());
		for(int i=0; i<beanToGet.getNumBioassemblies(); i++) {
			assertEquals(beanToGet.getNumTransInBioassembly(i),mmtfApi.getNumTransInBioassembly(i));
		}
	}

	@Test
	public void testBiojavaRoundTrip() throws IOException, StructureException {
		TestingUtils.testSingleStructure("4cup");
	}
}
