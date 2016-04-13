package org.rcsb.mmtf.dataholders;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestDataHolders {
	
	@Test
	public void testBeans(){
		PodamFactory factory = new PodamFactoryImpl();
		// Tests if setters are set appropriately
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(MmtfBean.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyData.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyTrans.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(Entity.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(PDBGroup.class));
	}


	@Test
	public void testDsspType() {		
		
		assertEquals(DsspType.dsspTypeFromString("pi Helix"), DsspType.dsspTypeFromInt(0));
		assertEquals(DsspType.PI_HELIX, DsspType.dsspTypeFromInt(0));
		
		assertEquals(DsspType.dsspTypeFromString("Bend"), DsspType.dsspTypeFromInt(1));
		assertEquals(DsspType.BEND, DsspType.dsspTypeFromInt(1));
		
		assertEquals(DsspType.dsspTypeFromString("alpha Helix"), DsspType.dsspTypeFromInt(2));
		assertEquals(DsspType.ALPHA_HELIX, DsspType.dsspTypeFromInt(2));
		
		assertEquals(DsspType.dsspTypeFromString("Extended"), DsspType.dsspTypeFromInt(3));
		assertEquals(DsspType.EXTENDED, DsspType.dsspTypeFromInt(3));
		
		assertEquals(DsspType.dsspTypeFromString("3-10 Helix"), DsspType.dsspTypeFromInt(4));
		assertEquals(DsspType.HELIX_3_10, DsspType.dsspTypeFromInt(4));
		
		assertEquals(DsspType.dsspTypeFromString("Bridge"), DsspType.dsspTypeFromInt(5));
		assertEquals(DsspType.BRIDGE, DsspType.dsspTypeFromInt(5));
		
		assertEquals(DsspType.dsspTypeFromString("Turn"), DsspType.dsspTypeFromInt(6));
		assertEquals(DsspType.TURN, DsspType.dsspTypeFromInt(6));
		
		assertEquals(DsspType.dsspTypeFromString("Coil"), DsspType.dsspTypeFromInt(7));
		assertEquals(DsspType.COIL, DsspType.dsspTypeFromInt(7));
		
		assertEquals(DsspType.dsspTypeFromString("NA"), DsspType.dsspTypeFromInt(-1));
		assertEquals(DsspType.NULL_ENTRY, DsspType.dsspTypeFromInt(-1));
		
	}
	
	
	@Test
	public void testPdbGroupType() {
		
		PDBGroup pdbGroupOne = new PDBGroup();
		pdbGroupOne.setAtomChargeList(new int[] {1,1});
		pdbGroupOne.setAtomNameList(new String[] {"A","B"});
		pdbGroupOne.setBondAtomList(new int[] {0,1});
		pdbGroupOne.setBondOrderList(new int[] {1});
		pdbGroupOne.setChemCompType("POLT");
		pdbGroupOne.setElementList(new String[] {"A","B"});
		pdbGroupOne.setGroupName("MET");
		pdbGroupOne.setSingleLetterCode('A');
		
		PDBGroup pdbGroupTwo = new PDBGroup();
		pdbGroupTwo.setAtomChargeList(new int[] {1,1});
		pdbGroupTwo.setAtomNameList(new String[] {"A","B"});
		pdbGroupTwo.setBondAtomList(new int[] {0,1});
		pdbGroupTwo.setBondOrderList(new int[] {1});
		pdbGroupTwo.setChemCompType("POLT");
		pdbGroupTwo.setElementList(new String[] {"A","B"});
		pdbGroupTwo.setGroupName("MET");
		pdbGroupTwo.setSingleLetterCode('A');
		assertTrue(pdbGroupOne.equals(pdbGroupTwo));
		
	}
}
