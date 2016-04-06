package org.rcsb.mmtf.biojavaencoder;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Matrix4d;

import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioAssemblyTrans;
import org.rcsb.mmtf.dataholders.HeaderBean;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.JournalArticle;
import org.biojava.nbio.structure.PDBCrystallographicInfo;
import org.biojava.nbio.structure.PDBHeader;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;
import org.biojava.nbio.structure.quaternary.BiologicalAssemblyTransformation;
import org.biojava.nbio.structure.secstruc.DSSPParser;
import org.biojava.nbio.structure.secstruc.SecStrucCalc;
import org.biojava.nbio.structure.xtal.CrystalCell;
import org.biojava.nbio.structure.xtal.SpaceGroup;

/**
 * A utils class of functions needed for Biojava.
 * @author Anthony Bradley
 *
 */
public class BiojavaUtils {
	/**
	 * Set up the configuration parameters for BioJava.
	 */
	public AtomCache setUpBioJava() {
		// Set up the atom cache etc
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		params.setUseInternalChainId(true);
		CustomChemCompProvider cc = new CustomChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		return cache;
	}

	/**
	 * Set up the configuration parameters for BioJava. - with an extra URL
	 */
	public AtomCache setUpBioJava(String extraUrl) {
		// Set up the atom cache etc
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		params.setUseInternalChainId(true);
		CustomChemCompProvider cc = new CustomChemCompProvider(extraUrl);
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		return cache;
	}


	/**
	 * This sets all microheterogeneous groups (previously alternate location groups) as separate groups.
	 * @param bioJavaStruct
	 */
	public final void fixMicroheterogenity(Structure bioJavaStruct) {
		// Loop through the models
		for (int i=0; i<bioJavaStruct.nrModels(); i++){
			// Then the chains
			List<Chain> chains = bioJavaStruct.getModel(i);
			for (Chain c : chains) {
				// Build a new list of groups
				List<Group> outGroups = new ArrayList<>();
				for (Group g : c.getAtomGroups()) {
					List<Group> removeList = new ArrayList<>();
					for (Group altLoc : g.getAltLocs()) {	  
						// Check if they are not equal -> microheterogenity
						if(! altLoc.getPDBName().equals(g.getPDBName())) {
							// Now add this group to the main list
							removeList.add(altLoc);
						}
					}
					// Add this group
					outGroups.add(g);
					// Remove any microhet alt locs
					g.getAltLocs().removeAll(removeList);
					// Add these microhet alt locs
					outGroups.addAll(removeList);
				}
				c.setAtomGroups(outGroups);
			}
		}
	}

	/**
	 * Function to get all the atoms in the strucutre as a list.
	 *
	 * @param bioJavaStruct the bio java struct
	 * @return the all atoms
	 */
	public final List<Atom> getAllAtoms(Structure bioJavaStruct) {
		// Get all the atoms
		List<Atom> theseAtoms = new ArrayList<Atom>();
		for (int i=0; i<bioJavaStruct.nrModels(); i++){
			List<Chain> chains = bioJavaStruct.getModel(i);
			for (Chain c : chains) {
				for (Group g : c.getAtomGroups()) {
					for(Atom a: getAtomsForGroup(g)){
						theseAtoms.add(a);					
					}
				}
			}
		}
		return theseAtoms;
	}

	/**
	 * Function to get a list of atoms for a group.
	 *
	 * @param inputGroup the Biojava Group to consider
	 * @return the atoms for the input Biojava Group
	 */
	public final List<Atom> getAtomsForGroup(Group inputGroup) {
		Set<Atom> uniqueAtoms = new HashSet<Atom>();
		List<Atom> theseAtoms = new ArrayList<Atom>();
		for(Atom a: inputGroup.getAtoms()){
			theseAtoms.add(a);
			uniqueAtoms.add(a);
		}
		List<Group> altLocs = inputGroup.getAltLocs();
		for(Group thisG: altLocs){
			for(Atom a: thisG.getAtoms()){
				if(uniqueAtoms.contains(a)){ 
					continue;
				}
				theseAtoms.add(a);
			}
		}
		return theseAtoms;
	}
	
	
	/**
	 * Function to generate the secondary structure for a Biojava structure object.
	 * @param bioJavaStruct the Biojava structure for which it is to be calculate.
	 */
	public void calculateDsspSecondaryStructure(Structure bioJavaStruct) {
		SecStrucCalc ssp = new SecStrucCalc();
		try{
			ssp.calculate(bioJavaStruct, true);
		}
		catch(StructureException e) {
			try{
				DSSPParser.fetch(bioJavaStruct.getPDBCode(), bioJavaStruct, true); //download from PDB the DSSP result
			}
			catch(FileNotFoundException enew){
			}
			catch(Exception bige){
				System.out.println(bige);
			}
		}

	}
	
	/**
	 * Adds the header information to a HeaderBean from a biojava structure object.
	 * @param bioJavaStruct the input Biojava structure
	 * @param headerStruct the input headerbean object
	 */
	public void setHeaderInfo(Structure bioJavaStruct, HeaderBean headerStruct) {
		headerStruct.setPdbCode(bioJavaStruct.getPDBCode());
		// Now get hte xtalographic info
		PDBCrystallographicInfo xtalInfo = bioJavaStruct.getCrystallographicInfo();
		CrystalCell xtalCell = xtalInfo.getCrystalCell();
		SpaceGroup spaceGroup = xtalInfo.getSpaceGroup();
		float[] inputUnitCell = new float[6];
		if(xtalCell==null){

		}else{
			headerStruct.setUnitCell(inputUnitCell);
			inputUnitCell[0] = (float) xtalCell.getA();
			inputUnitCell[1] = (float) xtalCell.getB();
			inputUnitCell[2] = (float) xtalCell.getC();
			inputUnitCell[3] = (float) xtalCell.getAlpha();
			inputUnitCell[4] = (float) xtalCell.getBeta();
			inputUnitCell[5] = (float) xtalCell.getGamma();
			if(spaceGroup==null){
				// This could be the I21 shown here
				headerStruct.setSpaceGroup("NA");
			}
			else{
				headerStruct.setSpaceGroup(spaceGroup.getShortSymbol());
			}
		}
		// GET THE HEADER INFORMATION
		PDBHeader header = bioJavaStruct.getPDBHeader();
		List<BioAssemblyData> outMap = generateSerializableBioAssembly(bioJavaStruct, header);
		headerStruct.setBioAssembly(outMap);
		headerStruct.setTitle(header.getTitle());
		headerStruct.setDescription(header.getDescription());
		headerStruct.setClassification(header.getClassification());
		headerStruct.setDepDate(header.getDepDate());
		headerStruct.setModDate(header.getModDate());
		headerStruct.setResolution(header.getResolution());		
		headerStruct.setrFree(header.getRfree());

		JournalArticle myJournal = header.getJournalArticle();
		if( myJournal==null){

		}
		else{
			headerStruct.setDoi(myJournal.getDoi());
		}
	}
	
	/**
	 * Generate a serializable biotransformation for storing
	 * in the messagepack.
	 * @param bioJavaStruct the Biojava structure
	 * @param header the header
	 * @return a map of the bioassembly information that is serializable
	 */
	private List<BioAssemblyData> generateSerializableBioAssembly(Structure bioJavaStruct, PDBHeader header) {
		// Get a map to reference asym ids to chains
		Map<String, Integer> chainIdToIndexMap = getChainIdToIndexMap(bioJavaStruct);
		// Here we need to iterate through and get the chain ids and the matrices
		Map<Integer, BioAssemblyInfo> inputBioAss = header.getBioAssemblies();
		List<BioAssemblyData> outMap = new ArrayList<BioAssemblyData>();
		for (Map.Entry<Integer, BioAssemblyInfo> entry : inputBioAss.entrySet()) {
			Map<Matrix4d,BioAssemblyTrans> matSet = new HashMap<Matrix4d,BioAssemblyTrans>();
			BioAssemblyInfo value = entry.getValue();
			// Make a new one of these
			BioAssemblyData newValue = new BioAssemblyData();
			outMap.add(newValue);
			// Copy across this info
			List<BioAssemblyTrans> outTrans = new ArrayList<BioAssemblyTrans>();
			for(BiologicalAssemblyTransformation transform: value.getTransforms()){
				// Get's the chain id -> this is the asym id
				String thisChain = transform.getChainId();
				// Get the current matrix 4d
				Matrix4d currentTransMat = transform.getTransformationMatrix();
				double[] outList = new double[16];
				// Iterate over the matrix
				for(int i=0; i<4; i++){
					for(int j=0; j<4; j++){
						// Now set this element
						outList[i*4+j] = currentTransMat.getElement(i,j);
					}
				}
				if(matSet.containsKey(currentTransMat)){
					// Get the trasnformation
					BioAssemblyTrans bioTransNew = matSet.get(currentTransMat);
					// Add this chain index to that list
					int[] oldList = bioTransNew.getChainIndexList();
					int oldLen = oldList.length;
					int[] newList = new int[oldLen+1];
					for (int i=0; i<oldLen; i++) {
						newList[i] = oldList[i];
					}
					// Now add the new chain id
					int newInd = 0;
					try {
						newInd = chainIdToIndexMap.get(thisChain);
					}
					// If it's not in the dictionary - because it's not a physical chain
					catch(Exception e) {
						newInd = -1;
					}					
					newList[oldLen] = newInd;
					bioTransNew.setChainIndexList(newList);
				}
				else{
					// Create a new one
					BioAssemblyTrans bioTransNew = new BioAssemblyTrans();
					bioTransNew.setTransformation(outList);
					// Create a chain index list
					int[] indexList = new int[1];
					indexList[0] = chainIdToIndexMap.get(thisChain);
					bioTransNew.setChainIndexList(indexList);
					matSet.put(currentTransMat, bioTransNew);
				}
			}
			for(BioAssemblyTrans thisTrans: matSet.values()){
				outTrans.add(thisTrans);
			}
			// Set the transform information
			newValue.setTransforms(outTrans);
		}

		return outMap;
	}
	
	/**
	 * Get the index of chain given a particular Asym id. Assumes the Biojava structure has been
	 * parsed on asym id.
	 * @param bioJavaStruct
	 * @return
	 */
	private Map<String, Integer> getChainIdToIndexMap(Structure bioJavaStruct) {
		// First build a map of asymid -> chain index
		Map<String,Integer> chainIdToIndexMapOne = new HashMap<>();
		int chainCounter = 0;
		for (int i=0; i<bioJavaStruct.nrModels(); i++) {
			for (Chain chain : bioJavaStruct.getChains(i)) {
				String idOne = chain.getChainID();
				if (!chainIdToIndexMapOne.containsKey(idOne)) { 
					chainIdToIndexMapOne.put(idOne, chainCounter);
				}
				chainCounter++;
			}
		}
		return chainIdToIndexMapOne;

	}
}
