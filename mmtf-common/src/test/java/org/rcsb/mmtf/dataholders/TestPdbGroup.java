package org.rcsb.mmtf.dataholders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestPdbGroup {

	
	private PDBGroup pdbGroupOne;
	private PDBGroup pdbGroupTwo;
	
	@Before
	public void beforeTest() {
		
		pdbGroupOne = makePdbGroup();
		pdbGroupTwo = makePdbGroup();
		
	}
	
	
	private PDBGroup makePdbGroup() {
		PDBGroup pdbGroup = new PDBGroup();
		pdbGroup.setAtomChargeList(new int[] {1,1});
		pdbGroup.setAtomNameList(new String[] {"A","B"});
		pdbGroup.setBondAtomList(new int[] {0,1});
		pdbGroup.setBondOrderList(new int[] {1});
		pdbGroup.setChemCompType("POLT");
		pdbGroup.setElementList(new String[] {"A","B"});
		pdbGroup.setGroupName("MET");
		pdbGroup.setSingleLetterCode('A');
		return pdbGroup;
	}


	@Test
	public void testPdbGroupEqualsItstelf() {
		assertTrue(pdbGroupOne.equals(pdbGroupOne));
	}


	@Test
	public void testPdbGroupEquals() {
		assertTrue(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsCharge() {
		pdbGroupOne.setAtomChargeList(new int[] {0,1});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsName() {
		pdbGroupOne.setAtomNameList(new String[] {"A","C"});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsBond() {
		pdbGroupOne.setBondAtomList(new int[] {1,1});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsOrder() {
		pdbGroupOne.setBondOrderList(new int[] {0});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsChem() {
		pdbGroupOne.setChemCompType("LL");
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	
	@Test
	public void testPdbGroupNotEqualsEle() {
		pdbGroupOne.setElementList(new String[] {"A","BC"});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}	
	@Test
	public void testPdbGroupNotEqualsGroupName() {
		pdbGroupOne.setGroupName("MES");
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}	
	@Test
	public void testPdbGroupNotEqualsSingle() {
		pdbGroupOne.setSingleLetterCode('B');
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqualsNull() {
		assertFalse(pdbGroupOne.equals(null));
	}
	
	@Test
	public void testPdbGroupNotEqulasOtherObj() {
		assertFalse(pdbGroupOne.equals(new ArrayList<Integer>()));
	}
	
	@Test
	public void testPdbGroupNotEqualsNullChem() {
		pdbGroupTwo.setChemCompType(null);
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	@Test
	public void testPdbGroupNotEqulasNullType() {
		pdbGroupTwo.setGroupName(null);
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
}
