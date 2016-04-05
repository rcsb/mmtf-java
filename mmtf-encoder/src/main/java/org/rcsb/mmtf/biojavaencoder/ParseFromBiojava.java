package org.rcsb.mmtf.biojavaencoder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Matrix4d;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Bond;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Element;
import org.biojava.nbio.structure.EntityInfo;
import org.biojava.nbio.structure.ExperimentalTechnique;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.JournalArticle;
import org.biojava.nbio.structure.PDBCrystallographicInfo;
import org.biojava.nbio.structure.PDBHeader;
import org.biojava.nbio.structure.ResidueNumber;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.quaternary.BioAssemblyInfo;
import org.biojava.nbio.structure.quaternary.BiologicalAssemblyTransformation;
import org.biojava.nbio.structure.secstruc.DSSPParser;
import org.biojava.nbio.structure.secstruc.SecStrucCalc;
import org.biojava.nbio.structure.secstruc.SecStrucState;
import org.biojava.nbio.structure.xtal.CrystalCell;
import org.biojava.nbio.structure.xtal.SpaceGroup;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioDataStruct;
import org.rcsb.mmtf.dataholders.BioAssemblyTrans;
import org.rcsb.mmtf.dataholders.CalphaBean;
import org.rcsb.mmtf.dataholders.CodeHolders;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.HeaderBean;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;


/**
 * A class to use biojava to parse MMCIF data and produce a data structure that can be fed into the MMTF.
 */
public class ParseFromBiojava {

	/**
	 * The multiplication factor for coordinate information
	 */
	private static final int COORD_MULT = 1000;

	/** The bio struct. */
	private BioDataStruct bioStruct = new BioDataStruct();

	/** The calpha struct. */
	private CalphaBean calphaStruct = new CalphaBean();

	/** The header struct. */
	private HeaderBean headerStruct = new HeaderBean();

	/** The code holder. */
	private CodeHolders codeHolder = new CodeHolders();

	/** The bonds for the structure. Used to keep track of which bonds have already been considered */
	private  List<Bond> totBonds = new ArrayList<Bond>();

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

	/** The Constant MY_MAP. Relates the group name to the type of atom id */
	private static final Map<String, String> MY_MAP;
	static {
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put("hetatm", "HETATM");
		aMap.put("amino", "ATOM");
		aMap.put("nucleotide", "ATOM");
		MY_MAP = Collections.unmodifiableMap(aMap);
	}

	/**
	 * Gets the bio struct.
	 *
	 * @return the bio struct
	 */
	public BioDataStruct getBioStruct() {
		return bioStruct;
	}


	/**
	 * Sets the bio struct.
	 *
	 * @param bioStruct the new bio struct
	 */
	public void setBioStruct(BioDataStruct bioStruct) {
		this.bioStruct = bioStruct;
	}


	/**
	 * Gets the calpha struct.
	 *
	 * @return the calpha struct
	 */
	public CalphaBean getCalphaStruct() {
		return calphaStruct;
	}


	/**
	 * Sets the calpha struct.
	 *
	 * @param calphaStruct the new calpha struct
	 */
	public void setCalphaStruct(CalphaBean calphaStruct) {
		this.calphaStruct = calphaStruct;
	}


	/**
	 * Gets the header struct.
	 *
	 * @return the header struct
	 */
	public HeaderBean getHeaderStruct() {
		return headerStruct;
	}


	/**
	 * Sets the header struct.
	 *
	 * @param headerStruct the new header struct
	 */
	public void setHeaderStruct(HeaderBean headerStruct) {
		this.headerStruct = headerStruct;
	}



	/**
	 * Helper function to generate a main, calpha and header data form a PDB id.
	 *
	 * @param pdbId the pdb id
	 * @param bioStructMap the bio struct map
	 */
	public void createFromJavaStruct(String pdbId, Map<Integer, PDBGroup> bioStructMap) {		
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
		generateDataStructuresFromBioJavaStructure(bioJavaStruct, bioStructMap);
	}


	/**
	 * Function to generate a main, calpha and header data form a biojava structure.
	 *
	 * @param bioJavaStruct the Biojava structure
	 * @param bioStructMap the map relating hash codes to PDB groups.
	 * input so that a consistent map can be held across several structures
	 */
	public void generateDataStructuresFromBioJavaStructure(Structure bioJavaStruct, Map<Integer, PDBGroup> bioStructMap) {
		EncoderUtils encoderUtils = new EncoderUtils();
		// Reset structure to consider altloc groups with the same residue number but different group names as seperate groups
		encoderUtils.fixMicroheterogenity(bioJavaStruct);
		// Generate the secondary structure
		genSecStruct(bioJavaStruct);
		// Set the header information
		setHeaderInfo(bioJavaStruct);
		// Get the number of models
		Integer numModels = bioJavaStruct.nrModels();
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
			totAsymChains += bioJavaStruct.getChains(i).size();
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
		Set<ExperimentalTechnique> techniqueSet = bioJavaStruct.getPDBHeader().getExperimentalTechniques();
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
		findEntityInfo(bioJavaStruct);
		// Get all the atoms
		List<Atom> totAtoms = encoderUtils.getAllAtoms(bioJavaStruct);
		for (int i=0; i<numModels; i++){
			// Now let's loop over all the atom site record
			List<Chain> chains = bioJavaStruct.getModel(i);
			// Set the PDB Code
			bioStruct.setPdbCode(bioJavaStruct.getPDBCode());
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
				setChainId(biojavaChain.getInternalChainID(), charChainList, chainCounter);
				// Set the asym chain id	
				setChainId(biojavaChain.getChainID(), charInternalChainList, chainCounter);
				// Set the number of groups per chain
				groupsPerChain[chainCounter] += biojavaChain.getAtomGroups().size();
				// Set the number of groups per internal chain
				groupsPerInternalChain[chainCounter] = biojavaChain.getAtomGroups().size();				
				// Add this chain to the list
				chainList.add(biojavaChain.getChainID());
				// Get the groups
				String currentChainId = biojavaChain.getChainID();
				int numBonds = 0;
				for (Group loopGroup : biojavaChain.getAtomGroups()) {
					currentGroup = loopGroup;
					// Set the seq res group id 
					if(i==0){
						headerStruct.getSeqResGroupIds().add(seqResGroups.indexOf(currentGroup));
					}
					// Get the pdb id
					String res_id = currentGroup.getPDBName();
					// Get the atoms for this group
					List<Atom> atomsInThisGroup = encoderUtils.getAtomsForGroup(currentGroup);
					// Get any bonds between groups
					getInterGroupBond(atomsInThisGroup, totAtoms, atomCounter);
					// Count the number of bonds
					// Now loop through and get the coords

					// Generate the group level data
					// Get the 
					List<String> atomInfo = getAtomInfo(atomsInThisGroup);
					// Get the atomic info required - bioStruct is the unique identifier of the group 
					int hashCode = getHashFromStringList(atomInfo);
					// If we need bioStruct new information 
					if (hashToRes.containsKey(hashCode)==false){
						// Make a new group
						PDBGroup outGroup = new PDBGroup();
						// Set the one letter code
						outGroup.setSingleLetterCode(currentGroup.getChemComp().getOne_letter_code());
						// Set the group type
						outGroup.setChemCompType(currentGroup.getChemComp().getType());
						outGroup.setGroupName(atomInfo.remove(0));
						outGroup.setAtomInfo(atomInfo);
						// Now get the bond list (lengths, orders and indices)
						createBondList(atomsInThisGroup, outGroup); 
						getCharges(atomsInThisGroup, outGroup);
						// 
						bioStructMap.put(resCounter, outGroup);
						hashToRes.put(hashCode, resCounter);
						bioStruct.getResOrder().add(resCounter);
						resCounter+=1;
						numBonds = outGroup.getBondOrders().size();
					}
					else{
						// Add this to the residue order
						bioStruct.getResOrder().add(hashToRes.get(hashCode));	
						numBonds = bioStructMap.get(hashToRes.get(hashCode)).getBondOrders().size();
					}
					// Add the number of bonds 
					bondCounter+=numBonds;

					ResidueNumber residueNum = currentGroup.getResidueNumber();

					// bioStruct data item corresponds to the PDB insertion code.
					Character insertionCode = residueNum.getInsCode();
					if (insertionCode==null){
						bioStruct.get_atom_site_pdbx_PDB_ins_code().add(MmtfBean.UNAVAILABLE_STRING_VALUE);
					}
					else{
						bioStruct.get_atom_site_pdbx_PDB_ins_code().add(insertionCode.toString());
					}

					SecStrucState props = (SecStrucState) currentGroup.getProperty("secstruc");
					// Only assign secondary structure for the first model
					if(i==0){
						if(props==null){
							bioStruct.getSecStruct().add(codeHolder.getDsspMap().get("NA"));
						}
						else{
							bioStruct.getSecStruct().add(codeHolder.getDsspMap().get(props.getType().name));
						}
					}
					// Now add the residue sequnece number
					bioStruct.get_atom_site_auth_seq_id().add(residueNum.getSeqNum());
					// Set whether or not this is a calpha
					List<Atom> cAlphaGroup = new ArrayList<Atom>();
					for (Atom currentAtom : atomsInThisGroup) {
						// Update the structure
						addAtomInfo(currentAtom, currentChainId, res_id, residueNum, biojavaChain);
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
		headerStruct.setPdbCode(bioJavaStruct.getPDBCode());
	}


	/**
	 * Find and store the entity information in the header structure.
	 * @param bioJavaStruct
	 */
	private void findEntityInfo(Structure bioJavaStruct) {
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
	 * Sets the header info.
	 *
	 * @param bioJavaStruct the new header info
	 */
	private void setHeaderInfo(Structure bioJavaStruct) {
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
	 *  
	 * Function to generate the secondary structuee for a biojava structure object.
	 *
	 * @param bioJavaStruct the bio java struct
	 */
	private void genSecStruct(Structure bioJavaStruct) {
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
	 * Adds the calpha group.
	 *
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
			List<String> calphaAtomInfo = getAtomInfo(cAlphaGroup);
			/// Now consider the C-Alpha, phosophate and ligand cases
			int calphaHashCode = getHashFromStringList(calphaAtomInfo);
			// If we need bioStruct new information 
			if (hashToCalphaRes.containsKey(calphaHashCode)==false){
				// Make a new group
				PDBGroup outGroup = new PDBGroup();
				outGroup.setSingleLetterCode(currentGroup.getChemComp().getOne_letter_code());
				// 
				outGroup.setChemCompType(currentGroup.getChemComp().getType());
				outGroup.setGroupName(calphaAtomInfo.remove(0));
				outGroup.setAtomInfo(calphaAtomInfo);
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
				outGroup.setBondIndices(bondIndices);
				outGroup.setBondOrders(bondOrders);
				outGroup.setAtomCharges(atomCharges);
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
	 * Update calpha.
	 *
	 * @param totG the tot g
	 * @param cAlphaGroup the c alpha group
	 * @param a the a
	 */
	private void updateCalpha(List<Atom> cAlphaGroup, Atom a) {
		// NOW THE CALPHA / PHOSPHATE / LIGAND STUFF
		// GET THE CALPHA
		if (a.getName().equals("CA") && a.getElement().toString().equals("C")){
			// Now add the calpha
			cAlphaGroup.add(a);
		}
		// GET THE PHOSPHATE
		if(a.getName().equals("P")){	
			cAlphaGroup.add(a);
		}
		// GET THE LIGANDS
		if(currentGroup.isWater()==false && currentGroup.getType().name().equals("HETATM")){
			cAlphaGroup.add(a);
		}
	}


	/**
	 * Functon to set the chain id.
	 *
	 * @param chainId the chain id
	 * @param charChainList the char chain list
	 * @param chainCounter the chain counter
	 */
	private void setChainId(String chainId, byte[] charChainList, int chainCounter) {
		// A char array to store the chars
		char[] outChar = new char[4];
		// The lenght of this chain id
		int chainIdLen =  chainId.length();
		chainId.getChars(0, chainIdLen, outChar, 0);
		// Set the bytrarray - chain ids can be up to 4 chars - pad with empty bytes
		charChainList[chainCounter*4+0] = (byte) outChar[0];
		if(chainIdLen>1){
			charChainList[chainCounter*4+1] = (byte) outChar[1];
		}
		else{
			charChainList[chainCounter*4+1] = (byte) 0;
		}
		if(chainIdLen>2){
			charChainList[chainCounter*4+2] = (byte) outChar[2];
		}				
		else{
			charChainList[chainCounter*4+2] = (byte) 0;
		}
		if(chainIdLen>3){
			charChainList[chainCounter*4+3] = (byte) outChar[3];
		}				
		else{
			charChainList[chainCounter*4+3] =  (byte) 0;
		}		
	}


	/**
	 * Add a new calpha / phosophate / ligand atom.
	 *
	 * @param a the a
	 * @param props the props
	 * @param residueNumber the residue number (Biojava group)
	 */
	private void addCalpha(Atom a, SecStrucState props, ResidueNumber residueNumber) {
		calphaStruct.setNumAtoms(calphaStruct.getNumAtoms()+1); 
		calphaStruct.getCartn_x().add((int) Math.round(a.getX()*COORD_MULT));
		calphaStruct.getCartn_y().add((int) Math.round(a.getY()*COORD_MULT));
		calphaStruct.getCartn_z().add((int) Math.round(a.getZ()*COORD_MULT));
		// Get the residue name
		calphaStruct.get_atom_site_auth_seq_id().add(residueNumber.getSeqNum());
		calphaStruct.get_atom_site_label_entity_poly_seq_num().add(residueNumber.getSeqNum());
		// Now set the sec structure
		//
		if(props==null){
			calphaStruct.getSecStruct().add(codeHolder.getDsspMap().get("NA"));

		}
		else{
			calphaStruct.getSecStruct().add(codeHolder.getDsspMap().get(props.getType().name));
		}

	}


	/**
	 * Find the atomic charge information.
	 *
	 * @param inputAtoms the atoms
	 * @param pdbGroup the PDBGroup being considered
	 * @return the atomic charges
	 */
	private void getCharges(List<Atom> inputAtoms, PDBGroup pdbGroup) {
		for(Atom a: inputAtoms){
			pdbGroup.getAtomCharges().add((int) a.getCharge());
		}

	}


	/**
	 * Generate a serializable biotransformation for storing
	 * in the messagepack.
	 *
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
	 * Get the index of chain given a particular Asym id. Assumes there are more asym ids than auth ids...
	 * @param thisChain
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


	/**
	 * Function to find a hash code from a list of strings.
	 *
	 * @param strings the strings
	 * @return the hash from string list
	 */
	private int getHashFromStringList(List<String> strings){
		int prime = 31;
		int result = 1;
		for( String s : strings )
		{
			result = result * prime + s.hashCode();
		}
		return result;
	}




	/**
	 * Get the atomic information from a list of Atoms.
	 *
	 * @param atomList the atom list
	 * @return the atom info
	 */
	private List<String> getAtomInfo(List<Atom> atomList){
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
	 * Find bonds between groups.
	 *
	 * @param atoms the atoms
	 * @param totAtoms the tot atoms
	 * @param atomCounter the atom counter
	 * @return the inter group bond
	 */
	private void getInterGroupBond(List<Atom> inputAtomsInThisGroup, List<Atom> totAtoms, int atomCounter){
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
	 * Generate lists for the bonds in the group.
	 *
	 * @param atoms the atoms
	 * @param outGroup the out group
	 */
	private void createBondList(List<Atom> atoms, PDBGroup outGroup) {
		int n = atoms.size();
		if (n == 0) {
			System.out.println("creating empty bond list");
		}

		// Lists to hold bond indices and orders
		List<Integer> bondList = new ArrayList<Integer>();
		List<Integer> bondOrder = new ArrayList<Integer>();

		List<List<Integer>> totalBondList = new ArrayList<List<Integer>>();

		for (int i = 0; i < n; i++) {
			// Get the  atom
			Atom a = atoms.get(i);
			List<Bond> thisAtomBonds = a.getBonds();
			if(thisAtomBonds!=null){
				for (Bond b: thisAtomBonds) {
					Atom other = b.getOther(a);
					int index = atoms.indexOf(other);
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
		outGroup.setBondOrders(bondOrder);
		outGroup.setBondIndices(bondList);
	}


	/**
	 * Adds the atom info.
	 *
	 * @param inputAtom the input atom
	 * @param inputChainId the input chain id
	 * @param inputResidueId the input residue id
	 * @param residueNumber the residue number
	 * @param biojavaChain the input chain
	 */
	private void addAtomInfo(Atom inputAtom, String inputChainId, String inputResidueId,
			ResidueNumber residueNumber, Chain biojavaChain) {

		bioStruct.get_atom_site_id().add(inputAtom.getPDBserial());
		// Atom symbol
		Element ele = inputAtom.getElement();
		bioStruct.get_atom_site_symbol().add(ele.toString());
		bioStruct.get_atom_site_asym_id().add(inputChainId);
		// identify coordinate records (e.g. ATOM or HETATM).
		bioStruct.get_atom_site_group_PDB().add(MY_MAP.get(inputAtom.getGroup().getType().toString()));
		// bioStruct item is a uniquely identifies for each alternative site for
		// bioStruct atom position.
		if (inputAtom.getAltLoc()==" ".charAt(0)){
			bioStruct.get_atom_site_label_alt_id().add(MmtfBean.UNAVAILABLE_STRING_VALUE);
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
		bioStruct.get_atom_site_label_asym_id().add(biojavaChain.getInternalChainID().toString());
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
		bioStruct.get_atom_site_label_entity_id().add(MY_MAP.get(inputAtom.getGroup().getType().toString()));
		// bioStruct data item is a reference to _entity_poly_seq.num defined in
		// the ENTITY_POLY_SEQ category. bioStruct item is used to maintain the
		// correspondence between the chemical sequence of a polymeric
		// entity and the sequence information in the coordinate list and in
		// may other structural categories. bioStruct identifier has no meaning
		// for non-polymer entities.
		bioStruct.get_atom_site_label_entity_poly_seq_num().add(residueNumber.getSeqNum());
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
