package org.rcsb.mmtf.dataholders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the PDBGroup object
 * @author Anthony Bradley
 *
 */
public class TestPdbGroup {

	
	private Group pdbGroupOne;
	private Group pdbGroupTwo;
	
	/**
	 * Initialise to objects before the test.
	 */
	@Before
	public void beforeTest() {
		
		pdbGroupOne = makePdbGroup();
		pdbGroupTwo = makePdbGroup();
		
	}
	
	
	private Group makePdbGroup() {
		Group pdbGroup = new Group();
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


	/**
	 * Check if A==A
	 */
	@Test
	public void testPdbGroupEqualsItstelf() {
		assertTrue(pdbGroupOne.equals(pdbGroupOne));
	}


	/**
	 * Check if A==B
	 */
	@Test
	public void testPdbGroupEquals() {
		assertTrue(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if A has different charge
	 */
	@Test
	public void testPdbGroupNotEqualsCharge() {
		pdbGroupOne.setAtomChargeList(new int[] {0,1});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	/**
	 * Check if A!=B if A has different names
	 */
	@Test
	public void testPdbGroupNotEqualsName() {
		pdbGroupOne.setAtomNameList(new String[] {"A","C"});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if A has different bonds
	 */
	@Test
	public void testPdbGroupNotEqualsBond() {
		pdbGroupOne.setBondAtomList(new int[] {1,1});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if A has different bond orders
	 */
	@Test
	public void testPdbGroupNotEqualsOrder() {
		pdbGroupOne.setBondOrderList(new int[] {0});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if A has different chem comp type
	 */
	@Test
	public void testPdbGroupNotEqualsChem() {
		pdbGroupOne.setChemCompType("LL");
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if A has different elements
	 */
	@Test
	public void testPdbGroupNotEqualsEle() {
		pdbGroupOne.setElementList(new String[] {"A","BC"});
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}	
	
	/**
	 * Check if A!=B if A has different PDB group name
	 */
	@Test
	public void testPdbGroupNotEqualsGroupName() {
		pdbGroupOne.setGroupName("MES");
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}	
	
	/**
	 * Check if A!=B if A has different single letter amino acid code
	 */
	@Test
	public void testPdbGroupNotEqualsSingle() {
		pdbGroupOne.setSingleLetterCode('B');
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=null
	 */
	@Test
	public void testPdbGroupNotEqualsNull() {
		assertFalse(pdbGroupOne.equals(null));
	}
	
	/**
	 * Check if A!=B if B is a different object
	 */
	@Test
	public void testPdbGroupNotEqulasOtherObj() {
		assertFalse(pdbGroupOne.equals(new ArrayList<Integer>()));
	}
	
	/**
	 * Check if A!=B if B has null chem comp
	 */
	@Test
	public void testPdbGroupNotEqualsNullChem() {
		pdbGroupTwo.setChemCompType(null);
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
	
	/**
	 * Check if A!=B if B has null group name
	 */
	@Test
	public void testPdbGroupNotEqulasNullType() {
		pdbGroupTwo.setGroupName(null);
		assertFalse(pdbGroupOne.equals(pdbGroupTwo));
	}
}
