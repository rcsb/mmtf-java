package org.rcsb.mmtf.integrationtest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.io.mmtf.MmtfActions;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.junit.Test;
import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.decoder.BeanToGet;
import org.rcsb.mmtf.encoder.GetToBean;
import org.rcsb.mmtf.update.TestingUtils;



public class TestRoundTrip {


	@Test
	public void testApiRoundTrip() throws IOException, StructureException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// First set up Biojava
		MmtfUtils.setUpBioJava();
		Structure structure = StructureIO.getStructure("1O2F");
		MmtfDecodedDataInterface mmtfApi = MmtfActions.getApi(structure);
		GetToBean getToBean = new GetToBean(mmtfApi);
		MmtfDecodedDataInterface beanToGet = new BeanToGet(getToBean.getMmtfBean());
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
