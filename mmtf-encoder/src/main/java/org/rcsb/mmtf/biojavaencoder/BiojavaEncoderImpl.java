package org.rcsb.mmtf.biojavaencoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Bond;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Element;
import org.biojava.nbio.structure.EntityInfo;
import org.biojava.nbio.structure.ExperimentalTechnique;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.ResidueNumber;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.secstruc.SecStrucState;
import org.rcsb.mmtf.dataholders.BioDataStruct;
import org.rcsb.mmtf.dataholders.CalphaBean;
import org.rcsb.mmtf.dataholders.DsspType;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.HeaderBean;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;
import org.rcsb.mmtf.encoder.EncoderInterface;
import org.rcsb.mmtf.encoder.EncoderUtils;


/** A class to use biojava to parse MMCIF data and produce a data structure that can be fed into the MMTF. */
public class BiojavaEncoderImpl implements EncoderInterface {

	/** The bio struct. */
	private BioDataStruct bioStruct = new BioDataStruct();

	/** The calpha struct. */
	private CalphaBean calphaStruct = new CalphaBean();

	/** The header struct. */
	private HeaderBean headerStruct = new HeaderBean();

	/** The bonds for the structure. Used to keep track of which bonds have already been considered */
	private List<Bond> totBonds = new ArrayList<Bond>();

	/** The number of groups per calpha chain. */
	private int[] calphaGroupsPerChain;

	/** The hash to calpha res. */
	private Map<Integer, Integer> hashToCalphaRes;

	/** The a map relating hash codes to groups. For calphas */
	private Map<Integer, PDBGroup> calphaHashCodeToGroupMap;

	/** The chain counter. */
	private int chainCounter;

	/** The calpha group / residue counter. */
	private int calphaResCounter;	

	/** The Biojava group currently being parsed. */
	private Group currentGroup;
	

	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#getBioStruct()
	 */
	@Override
	public BioDataStruct getBioStruct() {
		return bioStruct;
	}


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#setBioStruct(org.rcsb.mmtf.dataholders.BioDataStruct)
	 */
	@Override
	public void setBioStruct(BioDataStruct bioStruct) {
		this.bioStruct = bioStruct;
	}


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#getCalphaStruct()
	 */
	@Override
	public CalphaBean getCalphaStruct() {
		return calphaStruct;
	}


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#setCalphaStruct(org.rcsb.mmtf.dataholders.CalphaBean)
	 */
	@Override
	public void setCalphaStruct(CalphaBean calphaStruct) {
		this.calphaStruct = calphaStruct;
	}


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#getHeaderStruct()
	 */
	@Override
	public HeaderBean getHeaderStruct() {
		return headerStruct;
	}


	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#setHeaderStruct(org.rcsb.mmtf.dataholders.HeaderBean)
	 */
	@Override
	public void setHeaderStruct(HeaderBean headerStruct) {
		this.headerStruct = headerStruct;
	}



	/* (non-Javadoc)
	 * @see org.rcsb.mmtf.biojavaencoder.EncoderInterface#generateDataStructuresFromPdbId(java.lang.String, java.util.Map)
	 */
	@Override
	public final void generateDataStructuresFromPdbId(String pdbId, Map<Integer, PDBGroup> bioStructMap) {		
		// Get the structure from here
		Structure bioJavaStruct;
		try {
			bioJavaStruct = StructureIO.getStructure(pdbId);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not find/open data file for input: "+pdbId);
			throw new RuntimeException(e);
		} catch (StructureException e) {
			e.printStackTrace();
			System.err.println("Error in parsing structure for input: "+pdbId);
			throw new RuntimeException(e);
		}
		generateDataStructuresFromPdbId(bioJavaStruct, bioStructMap);
	}


	/**
	 * Function to generate a main, calpha and header data form a biojava structure.
	 *
	 * @param inputBiojavaStruct the Biojava structure
	 * @param bioStructMap the map relating hash codes to PDB groups.
	 * input so that a consistent map can be held across several structures
	 */
	public final void generateDataStructuresFromPdbId(Structure inputBiojavaStruct, Map<Integer, PDBGroup> bioStructMap) {
		EncoderUtils encoderUtils = new EncoderUtils();
		BiojavaUtils biojavaUtils = new BiojavaUtils();
		// Reset structure to consider altloc groups with the same residue number but different group names as seperate groups
		biojavaUtils.fixMicroheterogenity(inputBiojavaStruct);
		// Generate the secondary structure
		biojavaUtils.calculateDsspSecondaryStructure(inputBiojavaStruct);
		// Set the header information
		biojavaUtils.setHeaderInfo(inputBiojavaStruct, headerStruct);
		// Get the number of models
		Integer numModels = inputBiojavaStruct.nrModels();
		bioStruct.setNumModels(numModels);
		// Set these maps and lists
		List<List<Integer>> bioStructList= new ArrayList<List<Integer>>();
		Map<Integer,Integer> hashToRes = new HashMap<Integer,Integer>();
		hashToCalphaRes = new HashMap<Integer,Integer>();
		// Set these counters
		int atomCounter = 0;
		chainCounter = 0;
		int resCounter = 0;
		int totAsymChains = 0;
		// Get the total number of chains
		for (int i=0; i<numModels; i++){		
			totAsymChains += inputBiojavaStruct.getChains(i).size();
		}
		// Generate the group map for Calphas
		calphaHashCodeToGroupMap = new HashMap<Integer, PDBGroup>();
		// Get these lists to keep track of everthing - and to give  a datastrcutrue at the end
		// List of chains per model
		int[] chainsPerModel = new int[numModels];
		int[] internalChainsPerModel = new int[numModels];
		// Set this list
		headerStruct.setChainsPerModel(chainsPerModel);
		headerStruct.setAsymChainsPerModel(internalChainsPerModel);
		byte[] charChainList = new byte[totAsymChains*4];
		byte[] charInternalChainList = new byte[totAsymChains*4];
		headerStruct.setChainList(charChainList);
		headerStruct.setAsymChainList(charInternalChainList);
		int[] groupsPerChain = new int[totAsymChains];
		int[] groupsPerInternalChain = new int[totAsymChains];
		headerStruct.setAsymGroupsPerChain(groupsPerInternalChain);
		headerStruct.setGroupsPerChain(groupsPerChain);
		headerStruct.setSequence(new ArrayList<String>());
		// Find the experimental techniques
		Set<ExperimentalTechnique> techniqueSet = inputBiojavaStruct.getPDBHeader().getExperimentalTechniques();
		headerStruct.setExperimentalMethods(new ArrayList<String>());
		for (ExperimentalTechnique currentTechnique : techniqueSet){
			headerStruct.getExperimentalMethods().add(currentTechnique.toString());
		}		
		headerStruct.setSeqResGroupIds(new ArrayList<Integer>());
		int bondCounter = 0;

		calphaGroupsPerChain = new int[totAsymChains];
		for(int i=0; i<totAsymChains; i++){
			calphaGroupsPerChain[i] = 0;
		}
		calphaStruct.setGroupsPerChain(calphaGroupsPerChain);
		// Now let's find the entity infomration
		findEntityInfo(inputBiojavaStruct);
		// Get all the atoms
		List<Atom> totAtoms = biojavaUtils.getAllAtoms(inputBiojavaStruct);
		for (int i=0; i<numModels; i++){
			// Now let's loop over all the atom site record
			List<Chain> chains = inputBiojavaStruct.getModel(i);
			// Set the PDB Code
			bioStruct.setPdbCode(inputBiojavaStruct.getPDBCode());
			ArrayList<String> chainList = new ArrayList<String>();
			// Set the number of chains in this model
			internalChainsPerModel[i] = chains.size();
			// Get the number of unique ones
			Set<String> chainIdSet = new HashSet<String>();
			for(Chain c : chains){
				String intChainId = c.getInternalChainID();
				chainIdSet.add(intChainId);
			}
			chainsPerModel[i] = chainIdSet.size();
			// Take the atomic information and place in a Hashmap
			for (Chain biojavaChain: chains) {	
				// Get the seq res groups for this chain
				List<Group> seqResGroups = biojavaChain.getSeqResGroups();
				// Set the sequence  - if it's the first model...
				if(i==0){
					headerStruct.getSequence().add(biojavaChain.getSeqResSequence());
				}
				// Set the auth chain id
				encoderUtils.setChainId(biojavaChain.getInternalChainID(), charChainList, chainCounter);
				// Set the asym chain id	
				encoderUtils.setChainId(biojavaChain.getChainID(), charInternalChainList, chainCounter);
				// Set the number of groups per chain
				groupsPerChain[chainCounter] += biojavaChain.getAtomGroups().size();
				// Set the number of groups per internal chain
				groupsPerInternalChain[chainCounter] = biojavaChain.getAtomGroups().size();				
				// Add this chain to the list
				chainList.add(biojavaChain.getChainID());
				// Reset the number of bonds counter for this chain
				int numBonds = 0;
				for (Group loopGroup : biojavaChain.getAtomGroups()) {
					currentGroup = loopGroup;
					// Set the seq res group id 
					if(i==0){
						headerStruct.getSeqResGroupIds().add(seqResGroups.indexOf(currentGroup));
					} 
					// Get the atoms for this group
					List<Atom> atomsInThisGroup = biojavaUtils.getAtomsForGroup(currentGroup);
					// Get any bonds between groups
					getInterGroupBond(atomsInThisGroup, totAtoms);
					// Count the number of bonds
					// Now loop through and get the coords
					// Generate the group level data
					// Get the data that defines a group
					List<String> groupInfo = getGroupData(atomsInThisGroup);
					// Get the atomic info required - bioStruct is the unique identifier of the group 
					int hashCode = getHashFromStringList(groupInfo);
					// If we need bioStruct new information 
					if (hashToRes.containsKey(hashCode)==false){
						// Make a new group
						PDBGroup outGroup = new PDBGroup();
						// Set the one letter code
						outGroup.setSingleLetterCode(currentGroup.getChemComp().getOne_letter_code().charAt(0));
						// Set the group type
						outGroup.setChemCompType(currentGroup.getChemComp().getType());
						outGroup.setGroupName(groupInfo.remove(0));
						outGroup.setAtomNameList(groupInfo);
						// Now get the bond list (lengths, orders and indices)
						createBondList(atomsInThisGroup, outGroup); 
						getCharges(atomsInThisGroup, outGroup);
						// Put the residue information into this bio structure map
						bioStructMap.put(resCounter, outGroup);
						hashToRes.put(hashCode, resCounter);
						bioStruct.getResOrder().add(resCounter);
						resCounter+=1;
						numBonds = outGroup.getBondOrderList().size();
					}
					else{
						// Add this to the residue order
						bioStruct.getResOrder().add(hashToRes.get(hashCode));	
						numBonds = bioStructMap.get(hashToRes.get(hashCode)).getBondOrderList().size();
					}
					// Add the number of bonds 
					bondCounter+=numBonds;

					ResidueNumber residueNum = currentGroup.getResidueNumber();

					// bioStruct data item corresponds to the PDB insertion code.
					Character insertionCode = residueNum.getInsCode();
					if (insertionCode==null){
						bioStruct.get_atom_site_pdbx_PDB_ins_code().add(null);
					}
					else{
						bioStruct.get_atom_site_pdbx_PDB_ins_code().add(insertionCode.toString());
					}

					SecStrucState props = (SecStrucState) currentGroup.getProperty("secstruc");
					// Only assign secondary structure for the first model
					if(i==0){
						if(props==null){
							bioStruct.getSecStruct().add(DsspType.dsspTypeFromString("NA").getDsspIndex());
						}
						else{
							bioStruct.getSecStruct().add(DsspType.dsspTypeFromString(props.getType().name).getDsspIndex());
						}
					}
					// Now add the residue sequnece number
					bioStruct.get_atom_site_auth_seq_id().add(residueNum.getSeqNum());
					// Set whether or not this is a calpha
					List<Atom> cAlphaGroup = new ArrayList<Atom>();
					for (Atom currentAtom : atomsInThisGroup) {
						// Update the structure
						addAtomInfo(currentAtom, loopGroup, biojavaChain);
						// Update the calpha
						updateCalpha(cAlphaGroup, currentAtom);
						// Increment the atom counter
						atomCounter+=1;
					}
					// Now add this group - if there is something to consider
					addCalphaGroup(cAlphaGroup, props, residueNum);
				}
				// Increment again by one
				chainCounter+=1;
			}
		}
		// Set this  final information in the total datastruct
		bioStruct.setGroupList(bioStructList);
		bioStruct.setGroupMap(bioStructMap);	
		calphaStruct.setGroupMap(calphaHashCodeToGroupMap);
		// Now set this header info
		headerStruct.setNumBonds(bondCounter+bioStruct.getInterGroupBondInds().size());
		headerStruct.setNumAtoms(atomCounter);
		headerStruct.setNumChains(chainCounter);
		headerStruct.setPdbCode(inputBiojavaStruct.getPDBCode());
	}


	/**
	 * Find and store the entity information in the header structure.
	 * @param bioJavaStruct
	 */
	private final void findEntityInfo(Structure bioJavaStruct) {
		List<EntityInfo> entities = bioJavaStruct.getEntityInfos();
		// Get the list of chains for all the models
		List<Chain> structChains = new ArrayList<>();
		for (int i=0; i < bioJavaStruct.nrModels(); i++) {
			structChains.addAll(bioJavaStruct.getChains(i));
		}
		Entity[] entityList = new Entity[entities.size()];
		int entityCounter = 0;
		for(EntityInfo entityInfo : entities) { 
			Entity newEntity = new Entity();
			// Get the indices for the chains in this guy
			List<Chain> entChains = entityInfo.getChains();
			int[] indexList = new int[entChains.size()];
			int counter = 0;
			for(Chain entChain : entChains) {
				int indexChain = structChains.indexOf(entChain);
				indexList[counter] = indexChain;
				counter++;
			}
			newEntity.setChainIndexList(indexList);
			newEntity.setDescription(entityInfo.getDescription());
			newEntity.setType(entityInfo.getType().toString());
			if (entityInfo.getChains().size()==0){
				newEntity.setSequence("");
			}
			else {
				newEntity.setSequence(entityInfo.getChains().get(0).getSeqResSequence());
			}
			entityList[entityCounter] = newEntity;
			entityCounter++;
		}	
		headerStruct.setEntityList(entityList);
	}


	/**
	 * Adds the calpha group.
	 * @param cAlphaGroup the c alpha group
	 * @param props the props
	 * @param residueNum the residue number Biojava objext
	 * @param singleLetterCode the single letter code
	 */
	private void addCalphaGroup(List<Atom> cAlphaGroup,SecStrucState props, ResidueNumber residueNum) {
		// Generate a variable of the residue number
		int thisResNum;
		if(cAlphaGroup.size()>0){
			calphaGroupsPerChain[chainCounter] = calphaGroupsPerChain[chainCounter]+1;
			List<String> calphaAtomInfo = getGroupData(cAlphaGroup);
			/// Now consider the C-Alpha, phosophate and ligand cases
			int calphaHashCode = getHashFromStringList(calphaAtomInfo);
			// If we need bioStruct new information 
			if (hashToCalphaRes.containsKey(calphaHashCode)==false){
				// Make a new group
				PDBGroup outGroup = new PDBGroup();
				outGroup.setSingleLetterCode(currentGroup.getChemComp().getOne_letter_code().charAt(0));
				// Set the chemical component type.
				outGroup.setChemCompType(currentGroup.getChemComp().getType());
				outGroup.setGroupName(calphaAtomInfo.remove(0));
				outGroup.setAtomNameList(calphaAtomInfo);
				// Now get the bond list (lengths, orders and indices) and atom charges
				List<Integer> bondIndices = new ArrayList<Integer>();
				List<Integer> bondOrders = new ArrayList<Integer>();
				List<Integer> atomCharges = new ArrayList<Integer>();
				for(Atom a : cAlphaGroup){
					atomCharges.add((int) a.getCharge());
					List<Bond> thisAtomBonds = a.getBonds();
					if(thisAtomBonds!=null){
						for (Bond b: thisAtomBonds) {
							// Get the index
							int thisInd = cAlphaGroup.indexOf(a);
							int otherInd = cAlphaGroup.indexOf(b.getOther(a));
							if(otherInd!=-1){
								if(thisInd<otherInd){
									bondIndices.add(thisInd);
									bondIndices.add(otherInd);
									bondOrders.add(b.getBondOrder());
								}

							}
						}
					}
				}
				// Now set them
				outGroup.setBondAtomList(bondIndices);
				outGroup.setBondOrderList(bondOrders);
				outGroup.setAtomChargeList(atomCharges);
				// 
				calphaHashCodeToGroupMap.put(calphaResCounter, outGroup);
				hashToCalphaRes.put(calphaHashCode, calphaResCounter);
				thisResNum = calphaResCounter;
				calphaResCounter+=1;
			}
			else{
				// Add this to the residue order
				thisResNum = hashToCalphaRes.get(calphaHashCode);						
			}						
			// Now set this as the answer
			calphaStruct.getResOrder().add(thisResNum);
			// Now add all these atoms to the calpha
			for(Atom a: cAlphaGroup){
				addCalpha(a, props, residueNum);
			}
		}

	}


	/**
	 * Add the calpha/phosphate/ligand backbone data to a group. If it is a calpha/phosphate or ligand.
	 * @param inputAtomData the input list of atoms associated to this group
	 * @param inputAtom the atom to be added
	 */
	private void updateCalpha(List<Atom> inputAtomData, Atom inputAtom) {
		// Check if it is a calpha
		if (inputAtom.getName().equals("CA") && inputAtom.getElement().toString().equals("C")){
			// Now add the calpha
			inputAtomData.add(inputAtom);
		}
		// Check if it is a phosphate
		else if(inputAtom.getName().equals("P")){	
			inputAtomData.add(inputAtom);
		}
		// Check if it is a ligand
		else if(currentGroup.isWater()==false && currentGroup.getType().name().equals("HETATM")){
			inputAtomData.add(inputAtom);
		}
	}


	/**
	 * Add a calpha atom to the C-alpha structure.
	 * @param inputAtom the input atom data.
	 * @param secondaryStructureInfo the secondary structure information (DSSP) for the group this atom is related to.
	 * @param residueNumber the residue number of the group this atom is part of.
	 */
	private void addCalpha(Atom inputAtom, SecStrucState secondaryStructureInfo, ResidueNumber residueNumber) {
		// Set the number of atoms in the group
		calphaStruct.setNumAtoms(calphaStruct.getNumAtoms()+1);
		// Covert the cooridnates for this group
		calphaStruct.getCartn_x().add((int) Math.round(inputAtom.getX()*MmtfBean.COORD_DIVIDER));
		calphaStruct.getCartn_y().add((int) Math.round(inputAtom.getY()*MmtfBean.COORD_DIVIDER));
		calphaStruct.getCartn_z().add((int) Math.round(inputAtom.getZ()*MmtfBean.COORD_DIVIDER));
		// Get the residue name
		calphaStruct.get_atom_site_auth_seq_id().add(residueNumber.getSeqNum());
		calphaStruct.get_atom_site_label_entity_poly_seq_num().add(residueNumber.getSeqNum());
		// Now set the sec structure
		if(secondaryStructureInfo==null){
			calphaStruct.getSecStruct().add(DsspType.dsspTypeFromString("NA").getDsspIndex());
		}
		else{
			calphaStruct.getSecStruct().add(DsspType.dsspTypeFromString(secondaryStructureInfo.getType().name).getDsspIndex());
		}
	}


	/**
	 * Find and store the atomic charge information.
	 * @param inputAtoms the atoms being coisdered.
	 * @param pdbGroup the PDBGroup to be modified.
	 * @return the atomic charges
	 */
	private void getCharges(List<Atom> inputAtoms, PDBGroup pdbGroup) {
		for(Atom a: inputAtoms){
			pdbGroup.getAtomChargeList().add((int) a.getCharge());
		}

	}


	/**
	 * Find a unique hash code from a list of strings.
	 * Multiplies the hashcode of each string in the list by a prime number.
	 * Finds the product of this.
	 * @param inputStrings the input strings
	 * @return the integer hashcode from this string list
	 */
	private int getHashFromStringList(List<String> inputStrings){
		// A prime number need for multiplication.
		int prime = 31;
		// The starting number
		int result = 1;
		for( String s : inputStrings )
		{
			result = result * prime + s.hashCode();
		}
		return result;
	}




	/**
	 * Get the atomic information from a list of Atoms.
	 * @param atomList the input atom list.
	 * @return a string list of atomic info comprising the element and name (in pairs) of 
	 * each atom in the input list.
	 */
	private List<String> getGroupData(List<Atom> atomList){
		int numAtoms = atomList.size();
		int arraySize = numAtoms*2+2;
		List<String> outString = new ArrayList<String>(arraySize);
		// Get the group name
		outString.add(atomList.get(0).getGroup().getPDBName());
		for (Atom a: atomList){
			outString.add(a.getElement().toString());
			outString.add(a.getName());
		}
		return outString;
	}


	/**
	 * Finds the atoms between groups in a given input list of atoms
	 * @param inputAtomsInThisGroup the atoms in the group being considered
	 * @param totAtoms the total list of atoms across the whole structure.
	 */
	private void getInterGroupBond(List<Atom> inputAtomsInThisGroup, List<Atom> totAtoms){
		// Get the atoms
		for (Atom currentAtom : inputAtomsInThisGroup) {
			// Get the  atom
			List<Bond> currentAtomBonds = currentAtom.getBonds();
			if(currentAtomBonds!=null){
				for (Bond currentBond: currentAtomBonds) {
					Atom other = currentBond.getOther(currentAtom);
					int index = inputAtomsInThisGroup.indexOf(other);
					int order = currentBond.getBondOrder();
					if (index<0 || index >= totAtoms.size()){
						// Get the index of hte atom ins the total list
						int newInd = totAtoms.indexOf(other);
						if(newInd > -1){
							// Check if it exists in tot bonds
							if(totBonds.indexOf(currentBond)!=-1){
								continue;
							}
							// Otherwise add it to the list
							totBonds.add(currentBond);
							// Then add this inter group bond
							bioStruct.getInterGroupBondInds().add(newInd);
							bioStruct.getInterGroupBondInds().add(totAtoms.indexOf(currentAtom));
							bioStruct.getInterGroupBondOrders().add(order);
						}
					}
				}
			}

		}
	}

	/**
	 * Creates the bond list for a given PDBGroup object.
	 * @param inputAtoms a list of atoms in the group
	 * @param pdbGroup a PDBGroup object to update the information to
	 */
	private void createBondList(List<Atom> inputAtoms, PDBGroup pdbGroup) {
		int n = inputAtoms.size();
		if (n == 0) {
			System.out.println("creating empty bond list");
		}

		// Lists to hold bond indices and orders
		List<Integer> bondList = new ArrayList<Integer>();
		List<Integer> bondOrder = new ArrayList<Integer>();

		List<List<Integer>> totalBondList = new ArrayList<List<Integer>>();

		for (int i = 0; i < n; i++) {
			// Get the  atom
			Atom a = inputAtoms.get(i);
			List<Bond> thisAtomBonds = a.getBonds();
			if(thisAtomBonds!=null){
				for (Bond b: thisAtomBonds) {
					Atom other = b.getOther(a);
					int index = inputAtoms.indexOf(other);
					int order = b.getBondOrder();
					// Now build this to check if the indices 
					List<Integer> thisArr = new ArrayList<Integer>();
					thisArr.add(index);
					thisArr.add(i);
					Collections.sort(thisArr);
					// Now check if we've done it
					if(totalBondList.contains(thisArr)){
						continue;
					}
					if (index != -1) {
						// Add the information
						bondList.add(index);
						bondList.add(i);
						bondOrder.add(order);
					}
					totalBondList.add(thisArr);
				}
			}
		}
		pdbGroup.setBondOrderList(bondOrder);
		pdbGroup.setBondAtomList(bondList);
	}


	/**
	 * Add the information for a given atom to the overall structure.
	 * @param inputAtom the atom to be added
	 * @param inputGroup the group to be added
	 * @param inputChain the chain to be added
	 */
	private void addAtomInfo(Atom inputAtom, Group inputGroup, Chain inputChain) {
		bioStruct.get_atom_site_id().add(inputAtom.getPDBserial());
		// Atom symbol
		Element ele = inputAtom.getElement();
		bioStruct.get_atom_site_symbol().add(ele.toString());
		bioStruct.get_atom_site_asym_id().add(inputChain.getChainID());
		// identify coordinate records (e.g. ATOM or HETATM).
		bioStruct.get_atom_site_group_PDB().add(GroupType.groupTypeFromString(inputAtom.getGroup().getType().toString()).getGroupType());
		// bioStruct item is a uniquely identifies for each alternative site for
		// bioStruct atom position.
		if (inputAtom.getAltLoc()==" ".charAt(0)){
			bioStruct.get_atom_site_label_alt_id().add(null);
		}
		else{
			bioStruct.get_atom_site_label_alt_id().add(inputAtom.getAltLoc().toString());
		}
		// bioStruct data item is reference to item _struct_asym.id defined in
		// category STRUCT_ASYM. bioStruct item identifies an instance of
		// particular entity in the deposited coordinate set. For a
		// structure determined by crystallographic method bioStruct corresponds
		// to a unique identifier within the cyrstallographic asymmetric
		// unit.
		bioStruct.get_atom_site_label_asym_id().add(inputChain.getInternalChainID().toString());
		// bioStruct data item is a reference to item _chem_comp_atom.atom_id
		// defined in category CHEM_COMP_ATOM which is stored in the
		// Chemical Component Dictionary. bioStruct atom identifier uniquely
		// identifies each atom within each chemical component.
		bioStruct.get_atom_site_label_atom_id().add(inputAtom.getName());
		// bioStruct data item is a reference to item _chem_comp.id defined in
		// category CHEM_COMP. bioStruct item is the primary identifier for
		// chemical components which may either be mononers in a polymeric
		// entity or complete non-polymer entities.
		bioStruct.get_atom_site_label_comp_id().add(inputAtom.getGroup().getPDBName());
		// bioStruct data item is a reference to _entity.id defined in the ENTITY
		// category. bioStruct item is used to identify chemically distinct
		// portions of the molecular structure (e.g. polymer chains,
		// ligands, solvent).
		bioStruct.get_atom_site_label_entity_id().add(GroupType.groupTypeFromString(inputAtom.getGroup().getType().toString()).getGroupType());
		// bioStruct data item is a reference to _entity_poly_seq.num defined in
		// the ENTITY_POLY_SEQ category. bioStruct item is used to maintain the
		// correspondence between the chemical sequence of a polymeric
		// entity and the sequence information in the coordinate list and in
		// may other structural categories. bioStruct identifier has no meaning
		// for non-polymer entities.
		bioStruct.get_atom_site_label_entity_poly_seq_num().add(inputGroup.getResidueNumber().getSeqNum());
		// Cartesian coordinate components describing the position of bioStruct
		// atom site.
		bioStruct.get_atom_site_Cartn_x().add(inputAtom.getX());
		bioStruct.get_atom_site_Cartn_y().add(inputAtom.getY());
		bioStruct.get_atom_site_Cartn_z().add(inputAtom.getZ());
		// Isotropic atomic displacement parameter
		bioStruct.get_atom_site_B_iso_or_equiv().add(inputAtom.getTempFactor());
		// The fraction of the atom present at bioStruct atom position.
		bioStruct.get_atom_site_occupancy().add(inputAtom.getOccupancy());
		// The net integer charge assigned to bioStruct atom.
	}
}
