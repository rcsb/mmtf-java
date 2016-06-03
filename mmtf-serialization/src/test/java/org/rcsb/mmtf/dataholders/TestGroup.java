package org.rcsb.mmtf.dataholders;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the {@link Group} class.
 * @author Anthony Bradley
 *
 */
public class TestGroup {

	
	private Group groupOne;
	private Group groupTwo;
	
	/**
	 * Initialise to objects before the test.
	 */
	@Before
	public void beforeTest() {
		
		groupOne = makegroup();
		groupTwo = makegroup();
		
	}
	
	
	private Group makegroup() {
		Group group = new Group();
		group.setFormalChargeList(new int[] {1,1});
		group.setAtomNameList(new String[] {"A","B"});
		group.setBondAtomList(new int[] {0,1});
		group.setBondOrderList(new int[] {1});
		group.setChemCompType("POLT");
		group.setElementList(new String[] {"A","B"});
		group.setGroupName("MET");
		group.setSingleLetterCode('A');
		return group;
	}


	/**
	 * Check if A==A
	 */
	@Test
	public void testgroupEqualsItstelf() {
		assertTrue(groupOne.equals(groupOne));
	}


	/**
	 * Check if A==B
	 */
	@Test
	public void testgroupEquals() {
		assertTrue(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if A has different charge
	 */
	@Test
	public void testgroupNotEqualsCharge() {
		groupOne.setFormalChargeList(new int[] {0,1});
		assertFalse(groupOne.equals(groupTwo));
	}
	/**
	 * Check if A!=B if A has different names
	 */
	@Test
	public void testgroupNotEqualsName() {
		groupOne.setAtomNameList(new String[] {"A","C"});
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if A has different bonds
	 */
	@Test
	public void testgroupNotEqualsBond() {
		groupOne.setBondAtomList(new int[] {1,1});
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if A has different bond orders
	 */
	@Test
	public void testgroupNotEqualsOrder() {
		groupOne.setBondOrderList(new int[] {0});
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if A has different chem comp type
	 */
	@Test
	public void testgroupNotEqualsChem() {
		groupOne.setChemCompType("LL");
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if A has different elements
	 */
	@Test
	public void testgroupNotEqualsEle() {
		groupOne.setElementList(new String[] {"A","BC"});
		assertFalse(groupOne.equals(groupTwo));
	}	
	
	/**
	 * Check if A!=B if A has different PDB group name
	 */
	@Test
	public void testgroupNotEqualsGroupName() {
		groupOne.setGroupName("MES");
		assertFalse(groupOne.equals(groupTwo));
	}	
	
	/**
	 * Check if A!=B if A has different single letter amino acid code
	 */
	@Test
	public void testgroupNotEqualsSingle() {
		groupOne.setSingleLetterCode('B');
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=null
	 */
	@Test
	public void testgroupNotEqualsNull() {
		assertFalse(groupOne.equals(null));
	}
	
	/**
	 * Check if A!=B if B is a different object
	 */
	@Test
	public void testgroupNotEqulasOtherObj() {
		assertFalse(groupOne.equals(new ArrayList<Integer>()));
	}
	
	/**
	 * Check if A!=B if B has null chem comp
	 */
	@Test
	public void testgroupNotEqualsNullChem() {
		groupTwo.setChemCompType(null);
		assertFalse(groupOne.equals(groupTwo));
	}
	
	/**
	 * Check if A!=B if B has null group name
	 */
	@Test
	public void testgroupNotEqulasNullType() {
		groupTwo.setGroupName(null);
		assertFalse(groupOne.equals(groupTwo));
	}
}
