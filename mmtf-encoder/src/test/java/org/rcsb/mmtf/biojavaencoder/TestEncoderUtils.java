package org.rcsb.mmtf.biojavaencoder;


import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;

public class TestEncoderUtils {


	@Test
	public void microHeterogenity() throws IOException, StructureException {
		EncoderUtils encoderUtils = new EncoderUtils();
		encoderUtils.setUpBioJava();
		Structure inputStructure = StructureIO.getStructure("4ck4");
		// Count the number of groups
		Group before = inputStructure.getChains().get(0).getAtomGroup(17);
		assertTrue(inputStructure.getChains().get(0).getAtomGroup(17).hasAltLoc());
	    List<Atom> totalAtoms = new ArrayList<>(encoderUtils.getAllAtoms(inputStructure));
		int totGroups = 0;
		int totAtomsCounter = 0;
		Set<Atom> totAtoms = new HashSet<>();
		for (Chain c : inputStructure.getChains()) {
			totGroups += c.getAtomGroups().size();
			for (Group g: c.getAtomGroups() ){
				totAtomsCounter+=g.getAtoms().size();
				totAtoms.addAll(g.getAtoms());
				for (Group alt : g.getAltLocs()) {
					totAtomsCounter+=alt.getAtoms().size();
					totAtoms.addAll(alt.getAtoms());
				}
			}
		}
		// Now "fix" the microheterogenity
		encoderUtils.fixMicroheterogenity(inputStructure);
		
		assertEquals(before, inputStructure.getChains().get(0).getAtomGroup(17));
		assertFalse(inputStructure.getChains().get(0).getAtomGroup(17).hasAltLoc());
		assertFalse(inputStructure.getChains().get(0).getAtomGroup(18).hasAltLoc());
		int totGroupsAfter = 0;
		int totAtomsCounterAfter = 0;
		Set<Atom> totAtomsAfter = new HashSet<>();
		for (Chain c : inputStructure.getChains()) {
			totGroupsAfter += c.getAtomGroups().size();
			for (Group g: c.getAtomGroups() ){
				totAtomsCounterAfter+=g.getAtoms().size();
				totAtomsAfter.addAll(g.getAtoms());
				for (Group alt : g.getAltLocs()) {
					totAtomsAfter.addAll(alt.getAtoms());
					totAtomsCounterAfter+=alt.getAtoms().size();
				}
			}
		}
		
	    List<Atom> totalAtomsAfter = new ArrayList<>(encoderUtils.getAllAtoms(inputStructure));
		System.out.println("Before: "+ totalAtoms.size());
		System.out.println("After: "+ totalAtomsAfter.size());
		System.out.println("Unique before: "+ totAtoms.size());
		System.out.println("Unique after: "+ totAtomsAfter.size());
		// Get all of the duplicate atoms
		Set<Atom> duplicates = findDuplicates(totalAtomsAfter);
		System.out.println(duplicates.size());
		for (Atom a : duplicates) {
			System.out.println(a);
		}
		// There should be no duplicates
		assertEquals(duplicates.size(), 0);
	    assertEquals(totalAtoms.size(), totalAtomsAfter.size());
		// Check there are two more groups afterwards
		assertEquals(totGroupsAfter-2, totGroups);
		// Check there are no more atoms afterwards
		assertEquals(totAtomsAfter.size(), totAtoms.size());
		// Check the counter are the same too
		assertEquals(totAtomsCounterAfter, totAtomsCounter);
		
	}
	
	private Set<Atom> findDuplicates(List<Atom> listContainingDuplicates)
	{ 
	  final Set<Atom> setToReturn = new HashSet<>(); 
	  final Set<Atom> set1 = new HashSet<>();

	  for (Atom yourInt : listContainingDuplicates)
	  {
	   if (!set1.add(yourInt))
	   {
	    setToReturn.add(yourInt);
	   }
	  }
	  return setToReturn;
	}
}

