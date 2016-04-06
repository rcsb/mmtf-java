package org.rcsb.mmtf.testutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Bond;
import org.biojava.nbio.structure.Calc;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.io.mmcif.model.ChemComp;
import org.biojava.nbio.structure.io.mmcif.model.ChemCompAtom;
import org.biojava.nbio.structure.io.mmcif.model.ChemCompBond;
import org.biojava.nbio.structure.rcsb.GetRepresentatives;
import org.rcsb.mmtf.biojavaencoder.BiojavaUtils;

public class CheckBonds {

	private StringBuilder stringBuilder;
	private Structure structure;

	private static final double MAX_NUCLEOTIDE_BOND_LENGTH = 2.2;
	private static final double MAX_PEPTIDE_BOND_LENGTH = 1.9;

	public void testAllConsistency() throws IOException, StructureException {

		// Set up biojava
		BiojavaUtils biojavaUtils = new BiojavaUtils();
		biojavaUtils.setUpBioJava();
		for (String testCase : GetRepresentatives.getAll()) {
			Structure structure = StructureIO.getStructure(testCase);
			biojavaUtils.fixMicroheterogenity(structure);
			checkIfBondsExist(structure);
		}
	}

	/**
	 * Test to see if bonds exist for all atoms on a chain
	 * @return 
	 */
	public String checkIfBondsExist(Structure inputStructure) {
		// Set the structure as the input to the function
		structure = inputStructure;
		stringBuilder = new StringBuilder();
		// ONLY DO THE TEST FOR THE FIRST MODEL
		for(int i=0;i<1;i++){
			List<Chain> chainsOne = structure.getChains(i);
			// Now loop over
			for(int j=0; j<chainsOne.size();j++){
				Chain chainOne = chainsOne.get(j);
				List<Group> groupsOne = chainOne.getAtomGroups();
				for(int k=0; k<groupsOne.size();k++){
					Group groupOne = groupsOne.get(k);
					// All atoms should have bonds (apart from atoms alone in a group)
					checkAllAtomsHaveBonds(groupOne);
					// All intra group bonds that can be made should be made
					checkIfIntraGroupBondsAreCreated(groupOne);
					// Peptide and nucleotide bonds should be made
					checkPeptideBondsAreMade(groupOne, groupsOne, k);
					checkNucleotideBondsAreMade(groupOne, groupsOne, k);
					// Metal bonds shouldn't be made
					checkMetalBondsArentMade(groupOne);
				}

			}
		}
		return stringBuilder.toString(); 
	}

	/**
	 * If it's a metal (or water alone) it shouldn't have bonds
	 * @param groupOne
	 */
	private void checkMetalBondsArentMade(Group groupOne) {
		// Loop through the atoms
		for(Atom a : groupOne.getAtoms()) {
			// Check that the bond list is null
			if (a.getElement().isMetal()) {
				assertEquals(a.getBonds(), null);
			}
		}


	}

	/**
	 * Ensure a group that is part of a nucleotide chain has a nucleotide bond to it's next nucleotide (if the group and atom exists).
	 * @param groupOne
	 * @param groupsOne
	 * @param k
	 */
	private void checkNucleotideBondsAreMade(Group groupOne, List<Group> groupsOne, int k) {
		// It has to be a nucleotide
		if (!groupOne.getType().equals(GroupType.NUCLEOTIDE)){
			return;
		}
		// Find the P
		Atom groupOneP = groupOne.getAtom("P");
		if (groupOneP==null) {
			return;
		}
		// Find the neighhbouring O3P
		// Now find the neighbouring Ns
		if(k>0){
			Atom otherO3P = groupsOne.get(k-1).getAtom("O3'");
			if(otherO3P!=null){
				checkNucleotideBond(groupOneP, otherO3P);
			}

		}
		if(k<groupsOne.size()-1){
			Atom otherO3P = groupsOne.get(k+1).getAtom("O3'");
			if(otherO3P!=null){
				checkNucleotideBond(groupOneP, otherO3P);
			}
		}

	}

	/**
	 * Check that there are bonds between nucleotides - but only between close ones.
	 * @param groupOneP
	 * @param otherO3P
	 */
	private void checkNucleotideBond(Atom groupOneP, Atom otherO3P) {
		// Check the distance is below a certain level
		if (Calc.getDistance(groupOneP, otherO3P) > MAX_NUCLEOTIDE_BOND_LENGTH) {
			// It shouldn't have a bond
			checkBonded(groupOneP, otherO3P, 1, false);
		}
		else {
			// It should have a bond
			checkBonded(groupOneP, otherO3P, 1, true);
		}

	}

	/**
	 * Ensure a group that is part of a polypeptide has a peptide bond to it's next amino acid (if the group and atom exists).
	 * @param groupOne
	 * @param k 
	 * @param groupsOne 
	 */
	private void checkPeptideBondsAreMade(Group groupOne, List<Group> groupsOne, int k) {
		// It has to be an amino acid
		if (!groupOne.getType().equals(GroupType.AMINOACID)){
			return;
		}
		// Find the calpha
		Atom groupOneCalpha = groupOne.getAtom("CA");
		// An amino acid without a calpha - skip
		if (groupOneCalpha==null) {
			return;
		}
		// Now find the neighbouring Ns
		if(k>0){
			Atom otherN = groupsOne.get(k-1).getAtom("N");
			if(otherN!=null){
				checkPeptideBond(groupOneCalpha, otherN);
			}

		}
		if(k<groupsOne.size()-1){
			Atom otherN = groupsOne.get(k+1).getAtom("N");
			if(otherN!=null){
				checkPeptideBond(groupOneCalpha, otherN);
			}
		}



	}

	/**
	 * Function to check to see if a calpha and N are bonded if they're close enough
	 * @param groupOneCalpha
	 * @param otherN
	 */
	private void checkPeptideBond(Atom groupOneCalpha, Atom otherN) {
		// Check the distance is below a certain level
		if (Calc.getDistance(groupOneCalpha, otherN) > MAX_PEPTIDE_BOND_LENGTH) {
			// It shouldn't have a bond
			checkBonded(groupOneCalpha, otherN, 1, false);
		}
		else {
			// It should have a bond
			checkBonded(groupOneCalpha, otherN, 1, true);
		}
	}

	/**
	 * Check that all atoms in a given group have bonds.
	 * @param groupOne
	 */
	private void checkAllAtomsHaveBonds(Group groupOne) {
		List<Atom> atomsOne = new ArrayList<>(groupOne.getAtoms());
		for(Group altLocOne: groupOne.getAltLocs()){
			for(Atom atomAltLocOne: altLocOne.getAtoms()){
				atomsOne.add(atomAltLocOne);
			}
		}
		// If it's just a single atom
		if (groupOne.getAtoms().size()==1){
			return;
		}
		// If it's a water
		if (groupOne.isWater()) {
			return;
		}
		// If the sum of the occupancy is less than or equal to 1.00
		float occ = (float) 0.0;
		for (Atom a: groupOne.getAtoms()) {
			occ += a.getOccupancy();
		}
		if (occ <= 1.0) {
			return;
		}

		// Now let's check to see if all atoms have bonds... 
		for(int l=0;l<atomsOne.size();l++){
			List<String> atomNameList = new ArrayList<>();
			for(ChemCompAtom  thisAtom: groupOne.getChemComp().getAtoms()) {
				atomNameList.add(thisAtom.getAtom_id());
			}
			if (atomsOne.get(l).getBonds()==null){
				// Check if the ATOM is not in the CCD
				if(!atomNameList.contains(atomsOne.get(l).getName())){
					stringBuilder.append(structure.getPDBCode()+": ### ATOM NOT IN CCD AND HAS NO BONDS ->> "+atomsOne.get(l).toPDB()+"\n");
					continue;
				}
			}
			// Check that in all cases the other groups are found in this list...
			if(!atomNameList.contains(atomsOne.get(l).getName())){
				stringBuilder.append(structure.getPDBCode()+": ### ATOM NOT IN CCD BUT HAS "+atomsOne.get(l).getBonds().size()+" BONDS ->> "+atomsOne.get(l).toPDB()+"\n");
			}
			// Bonds should not be null (but they currently are - have this here to remind us so...
			if (atomsOne.get(l).getBonds()==null){
				stringBuilder.append(structure.getPDBCode()+": ATOM IN CCD BUT HAS NULL BONDS ->> "+atomsOne.get(l).toPDB()+"\n");
			}
			else{
				// And should not be empty lists
				assertNotEquals(atomsOne.get(l).getBonds().size(), 0);
			}
		}		
	}

	/**
	 * Function to ensure that all bonds that can be made are being made.
	 * @param structure
	 */
	public void checkIfIntraGroupBondsAreCreated(Group inputGroup) {

		List<Group> allGroups = new ArrayList<>(inputGroup.getAltLocs());
		allGroups.add(inputGroup);

		for (Group group : allGroups) {
			// Get the CCD 
			ChemComp inputChemComp = group.getChemComp();
			// Get the bonds
			for (ChemCompBond inputBond : inputChemComp.getBonds()) {
				String atomNameOne = inputBond.getAtom_id_1();
				String atomNameTwo = inputBond.getAtom_id_2();
				Atom atomOne = group.getAtom(atomNameOne);
				Atom atomTwo = group.getAtom(atomNameTwo);
				if (atomOne != null && atomTwo != null) {
					// Ensure that if both atoms exist - the bond exists.
					checkBonded(atomOne, atomTwo, inputBond.getNumericalBondOrder(), true);
				}
			}
		}

	}		

/**
 * Check that two atoms are bonded
 * @param atomOne
 * @param atomTwo
 */
private void checkBonded(Atom atomOne, Atom atomTwo, Integer bondOrder, boolean isBonded) {
	List<Atom> otherAtoms = new ArrayList<>();
	List<Integer> bondInds = new ArrayList<>();
	for (Bond groupBond  : atomOne.getBonds()) {
		otherAtoms.add(groupBond.getOther(atomOne));
		bondInds.add(groupBond.getBondOrder());
	}
	// Assert that it's bonded to the other
	assertTrue(otherAtoms.contains(atomTwo)==isBonded);
	// And that's it not bonded to itself
	assertTrue(!otherAtoms.contains(atomOne));
	if (isBonded) {
		// Assert that it's only bonded once to the other
		if(otherAtoms.indexOf(atomTwo)!=otherAtoms.lastIndexOf(atomTwo)){
			System.out.println("Bonded more than once: "+atomOne.getGroup().getChain().getStructure().getPDBCode());
			System.out.println(atomOne);
			System.out.println(atomTwo);
		}
		assertEquals(otherAtoms.indexOf(atomTwo), otherAtoms.lastIndexOf(atomTwo));
		// Assert that the bond order is correct
		if(bondOrder != bondInds.get(otherAtoms.indexOf(atomTwo))){
			System.out.println("Wrong bond order: "+atomOne.getGroup().getChain().getStructure().getPDBCode());
			System.out.println(atomOne);
			System.out.println(atomTwo);
		}
		assertEquals(bondOrder, bondInds.get(otherAtoms.indexOf(atomTwo)));		
	}
}
}
