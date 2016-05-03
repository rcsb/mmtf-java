package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.api.StructureAdapterInterface;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.BioAssemblyTransformation;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.Group;
import org.rcsb.mmtf.utils.CodecUtils;

/**
 * A class to move data from the {@link StructureAdapterInterface}
 * to the {@link StructureDataInterface}.
 * @author Anthony Bradley
 *
 */
public class AdapterToStructureData implements StructureDataInterface, StructureAdapterInterface {


	/** The X coordinates */
	private float[] cartnX;

	/** The Y coordinates */
	private float[] cartnY;

	/** The Z coordinates */
	private float[] cartnZ;

	/** The B factors */
	private float[] bFactor;

	/** The occupancy */
	private float[] occupancy;

	/** The atom id. */
	private int[] atomId;

	/** The alt id. */
	private char[] altId;

	/** The ins code. */
	private char[] insertionCodeList;

	/** The group num. */
	private int[] groupNum;

	/** The group map. */
	private List<Group> groupMap;

	/** The group list. */
	private int[] groupList;

	/** The sequence ids of the groups */
	private int[] seqResGroupList;

	/** The public facing chain ids*/
	private String[] publicChainIds;

	/** The number of chains per model*/
	private int[] chainsPerModel;

	/** The number of groups per (internal) chain*/
	private int[] groupsPerChain;

	/** The space group of the structure*/
	private String spaceGroup;

	/** The unit cell of the structure*/
	private float[] unitCell;

	/** The bioassembly information for the structure*/
	private List<BioAssemblyData> bioAssembly;

	/** The bond indices for bonds between groups*/
	private List<Integer> interGroupBondIndices;

	/** The bond orders for bonds between groups*/
	private List<Integer> interGroupBondOrders;

	/** The chosen list of chain ids */
	private String[] chainList;

	/** The mmtf version */
	private String mmtfVersion = "UNKNOWN";

	/** The mmtf prodcuer */
	private String mmtfProducer = "UNKNOWN";

	/** The list of entities in this structure. */
	private Entity[] entityList;

	/** The PDB id	 */
	private String pdbId;

	/** The reported resolution of the dataset. */
	private Float resolution;

	/** The reported R Free of the model. */
	private Float rFree;

	/** The reported R Work of the model. */
	private Float rWork;

	/** The title of the model. */
	private String title;

	/** The list of experimental methods. */
	private String[] experimentalMethods;

	/** The deposition date of hte structure */
	private String depositionDate;
	
	/** The release date of the structure */
	private String releaseDate;

	/** The total number of models */
	private int numModels;
	
	/** The secondary structure information */
	private int[] secStructInfo;

	/** The atom counter */
	private int atomIndex = 0;
	/** The atom counter within a group*/
	private int groupAtomIndex = 0;
	/** The current group bond */
	private int groupBondIndex = 0;
	/** The group counter */
	private int groupIndex = 0;
	/** The chain counter */
	private int chainIndex = 0;
	/** The model counter */
	private int modelIndex = 0;
	/** Add the atom information for the current group */
	private Group pdbGroup;
	/** A List for Entities as the number of entities is not defined*/
	private List<Entity> entities;
	/** The total number of bonds*/
	private int totalNumBonds;
	/** The list of {@link Group} objects */
	private List<Group> pdbGroupList;
	/** The map of chain indices to the entity */
	private Map<Integer, Integer> chainToEntityIndexMap;

	@Override
	public float[] getxCoords() {
		return cartnX;
	}

	@Override
	public float[] getyCoords() {
		return cartnY;

	}

	@Override
	public float[] getzCoords() {
		return cartnZ;
	}

	@Override
	public float[] getbFactors() {
		return bFactor;
	}

	@Override
	public float[] getOccupancies() {
		return occupancy;
	}

	@Override
	public int[] getAtomIds() {
		return atomId;
	}

	@Override
	public char[] getAltLocIds() {
		return altId;
	}

	@Override
	public char[] getInsCodes() {
		return insertionCodeList;
	}

	@Override
	public int[] getGroupIds() {
		return groupNum;
	}

	@Override
	public String getGroupName(int groupInd) {
		return getGroup(groupInd).getGroupName();
	}

	@Override
	public int getNumAtomsInGroup(int groupInd) {
		return getGroup(groupInd).getAtomChargeList().length;
	}

	@Override
	public String[] getGroupAtomNames(int groupInd) {
		return getGroup(groupInd).getAtomNameList();
	}

	@Override
	public String[] getGroupElementNames(int groupInd) {
		return getGroup(groupInd).getElementList();

	}

	@Override
	public int[] getGroupBondOrders(int groupInd) {
		return getGroup(groupInd).getBondOrderList();

	}

	@Override
	public int[] getGroupBondIndices(int groupInd) {
		return getGroup(groupInd).getBondAtomList();
	}

	@Override
	public int[] getGroupAtomCharges(int groupInd) {
		return getGroup(groupInd).getAtomChargeList();
	}

	@Override
	public char getGroupSingleLetterCode(int groupInd) {
		return getGroup(groupInd).getSingleLetterCode();
	}

	@Override
	public String getGroupChemCompType(int groupInd) {
		return getGroup(groupInd).getChemCompType();
	}


	@Override
	public int[] getGroupTypeIndices() {
		return groupList;
	}

	@Override
	public int[] getGroupSequenceIndices() {
		return seqResGroupList;
	}

	@Override
	public String[] getChainIds() {
		return chainList;
	}

	@Override
	public String[] getChainNames() {
		return publicChainIds;
	}

	@Override
	public int[] getChainsPerModel() {
		return chainsPerModel;
	}

	@Override
	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}

	@Override
	public String getSpaceGroup() {
		return spaceGroup;
	}

	@Override
	public float[] getUnitCell() {
		return unitCell;
	}

	@Override
	public int getNumBioassemblies() {
		return bioAssembly.size();
	}

	@Override
	public int getNumTransInBioassembly(int bioassemblyIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransformList().size();
	}

	@Override
	public int[] getChainIndexListForTransform(int bioassemblyIndex, int transformationIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransformList().get(transformationIndex).getChainIndexList();
	}

	@Override
	public double[] getMatrixForTransform(int bioassemblyIndex, int transformationIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransformList().get(transformationIndex).getMatrix();
	}

	@Override
	public int[] getInterGroupBondIndices() {
		return CodecUtils.convertToIntArray(interGroupBondIndices);
	}

	@Override
	public int[] getInterGroupBondOrders() {
		return CodecUtils.convertToIntArray(interGroupBondOrders);
	}

	@Override
	public String getMmtfVersion() {
		return mmtfVersion;
	}

	@Override
	public String getMmtfProducer() {
		return mmtfProducer;
	}

	@Override
	public int getNumEntities() {
		return entityList.length;
	}

	@Override
	public String getEntityDescription(int entityInd) {
		return entityList[entityInd].getDescription();
	}

	@Override
	public String getEntityType(int entityInd) {
		return entityList[entityInd].getType();

	}

	@Override
	public int[] getEntityChainIndexList(int entityInd) {
		return entityList[entityInd].getChainIndexList();
	}

	@Override
	public String getEntitySequence(int entityInd) {
		return entityList[entityInd].getSequence();
	}

	@Override
	public String getStructureId() {
		return pdbId;
	}

	@Override
	public int getNumModels() {
		return numModels;
	}

	@Override
	public int getNumChains() {
		int sum = 0;
		for (int numChainsInModel : chainsPerModel) {
			sum+=numChainsInModel;
		}
		return sum;
	}

	@Override
	public int getNumGroups() {
		return insertionCodeList.length;
	}

	@Override
	public int getNumAtoms() {
		return cartnX.length;
	}

	@Override
	public float getRfree() {
		return rFree;
	}

	@Override
	public float getRwork() {
		return rWork;
	}

	@Override
	public float getResolution() {
		return resolution;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String[] getExperimentalMethods() {
		return experimentalMethods;
	}

	@Override
	public String getDepositionDate() {
		return depositionDate;
	}

	
	@Override
	public String getChainEntityDescription(int chainInd) {
		if(chainToEntityIndexMap==null){
			generateChanEntityIndexMap();
		}
		Integer entityInd = chainToEntityIndexMap.get(chainInd);
		if(entityInd==null){
			return null;
		}
		return getEntityDescription(entityInd);
	}

	@Override
	public String getChainEntityType(int chainInd) {
		if(chainToEntityIndexMap==null){
			generateChanEntityIndexMap();
		}
		Integer entityInd = chainToEntityIndexMap.get(chainInd);
		if(entityInd==null){
			return null;
		}
		return getEntityType(entityInd);
	}

	@Override
	public String getChainEntitySequence(int chainInd) {
		if(chainToEntityIndexMap==null){
			generateChanEntityIndexMap();
		}
		Integer entityInd = chainToEntityIndexMap.get(chainInd);
		if(entityInd==null){
			return null;
		}
		return getEntitySequence(entityInd);
	}

	/**
	 * Utility function to generate a map, mapping chain index to
	 * entity index.
	 */
	private void generateChanEntityIndexMap() {
		chainToEntityIndexMap = new HashMap<>();
		for(int i=0; i<entityList.length; i++) {
			for(int chainInd : entityList[i].getChainIndexList()){
				chainToEntityIndexMap.put(chainInd, i);
			}
		}
	}

	@Override
	public void initStructure(int totalNumBonds, int totalNumAtoms, int totalNumGroups, 
			int totalNumChains, int totalNumModels, String structureId) {
		this.totalNumBonds = totalNumBonds;
		// Intitialise the bond level info
		interGroupBondIndices = new ArrayList<>();
		interGroupBondOrders = new ArrayList<>();
		// Intitialise the atom level arrays
		cartnX = new float[totalNumAtoms];
		cartnY= new float[totalNumAtoms];
		cartnZ = new float[totalNumAtoms];
		occupancy = new float[totalNumAtoms];
		bFactor = new float[totalNumAtoms];
		atomId = new int[totalNumAtoms];	
		altId = new char[totalNumAtoms];
		// Initialise the group level data
		groupNum = new int[totalNumGroups];
		// List for storing the group level information
		pdbGroupList = new ArrayList<>();
		insertionCodeList = new char[totalNumGroups];
		seqResGroupList = new int[totalNumGroups];
		secStructInfo = new int[totalNumGroups];
		groupList = new int[totalNumGroups];
		// Intialise the chain level data 	 	
		chainList = new String[totalNumChains];
		publicChainIds = new String[totalNumChains];
		groupsPerChain = new int[totalNumChains];
		// Initialise the model level information
		numModels = totalNumModels;
		// Set the name
		pdbId = structureId;
		bioAssembly = new ArrayList<>();
		entities = new ArrayList<>();
		chainsPerModel = new int[totalNumModels];
	}

	@Override
	public void finalizeStructure() {
		// Convert the entities array to a list
		entityList = entities.toArray(new Entity[0]);
		// Cleanup the group list
		groupMap = new ArrayList<>(new HashSet<>(pdbGroupList));
		for(int i=0; i<pdbGroupList.size(); i++){		
			// Find the index of this groups information.
			groupList[i] = groupMap.indexOf(pdbGroupList.get(i));
		}
		// Now  generate this map
		generateChanEntityIndexMap();
	}

	@Override
	public void setModelInfo(int modelId, int chainCount) {
		chainsPerModel[modelIndex] = chainCount;
		modelIndex++;
	}

	@Override
	public void setChainInfo(String chainId, String chainName, int groupCount) {
		chainList[chainIndex] = chainId;
		publicChainIds[chainIndex] = chainName;
		groupsPerChain[chainIndex] = groupCount;
		chainIndex++;
	}

	@Override
	public void setEntityInfo(int[] chainIndices, String sequence, String description, String title) {	
		Entity entity = new Entity();
		entity.setChainIndexList(chainIndices);
		entity.setSequence(sequence);
		entity.setDescription(description);
		entity.setType(title);
		// Add this entity
		entities.add(entity);
	}

	@Override
	public void setGroupInfo(String groupName, int groupNumber, char insertionCode, String polymerType, 
			int atomCount, int bondCount, char singleAtomCode, int sequenceIndex, int secStructType) {
		// Make a new PDBGroup to store the repeated information
		pdbGroup = new Group();
		pdbGroupList.add(pdbGroup);
		pdbGroup.setAtomChargeList(new int[atomCount]);
		pdbGroup.setAtomNameList(new String[atomCount]);
		pdbGroup.setBondAtomList(new int[bondCount*2]);
		pdbGroup.setBondOrderList(new int[bondCount]);
		pdbGroup.setChemCompType(polymerType);
		pdbGroup.setElementList(new String[atomCount]);
		pdbGroup.setGroupName(groupName);
		pdbGroup.setSingleLetterCode(singleAtomCode);
		groupAtomIndex=0;
		groupBondIndex=0;
		// Store the group level data
		insertionCodeList[groupIndex] = insertionCode;
		groupNum[groupIndex] = groupNumber;
		seqResGroupList[groupIndex] = sequenceIndex;
		secStructInfo[groupIndex] = secStructType;
		groupIndex++;
	}

	@Override
	public void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, float x, float y, float z,
			float occupancy, float temperatureFactor, String element, int charge) {
		// Set the group level data
		pdbGroup.getAtomChargeList()[groupAtomIndex] = charge;
		pdbGroup.getAtomNameList()[groupAtomIndex] = atomName;
		pdbGroup.getElementList()[groupAtomIndex] = element;
		// Set the atom level data
		cartnX[atomIndex] = x;
		cartnY[atomIndex] = y;
		cartnZ[atomIndex] = z;
		this.occupancy[atomIndex] = occupancy;
		bFactor[atomIndex] = temperatureFactor;
		atomId[atomIndex] = serialNumber;
		altId[atomIndex] = alternativeLocationId;
		// Increment both counters
		groupAtomIndex++;
		atomIndex++;
	}

	@Override
	public void setBioAssemblyTrans(int bioAssemblyIndex, int[] chainIndices, double[] transform) {
		BioAssemblyData bioAssemblyData;
		List<BioAssemblyTransformation> bioAssemblyTranList;
		if (bioAssembly.size()>bioAssemblyIndex) {
			bioAssemblyTranList = bioAssembly.get(bioAssemblyIndex).getTransformList();
		}
		else{
			bioAssemblyData = new BioAssemblyData();
			bioAssemblyTranList = new ArrayList<>();
			bioAssemblyData.setTransformList(bioAssemblyTranList);
			bioAssembly.add(bioAssemblyData);
		}
		BioAssemblyTransformation bioAssemblyTrans = new BioAssemblyTransformation();
		bioAssemblyTrans.setChainIndexList(chainIndices);
		bioAssemblyTrans.setMatrix(transform);
		bioAssemblyTranList.add(bioAssemblyTrans);
	}

	@Override
	public void setXtalInfo(String spaceGroup, float[] unitCell) {
		this.spaceGroup = spaceGroup;
		this.unitCell = unitCell;
	}

	@Override
	public void setGroupBond(int firstAtomIndex, int secondAtomIndex, int bondOrder) {
		// Set the bond indices
		pdbGroup.getBondAtomList()[groupBondIndex*2] = firstAtomIndex;
		pdbGroup.getBondAtomList()[groupBondIndex*2+1] = secondAtomIndex;
		// Set the bond order
		pdbGroup.getBondOrderList()[groupBondIndex] = bondOrder;
		groupBondIndex++;
	}

	@Override
	public void setInterGroupBond(int firstAtomIndex, int secondAtomIndex, int bondOrder) {
		// Set the bond indices
		interGroupBondIndices.add(firstAtomIndex);
		interGroupBondIndices.add(secondAtomIndex);
		// Set the bond order
		interGroupBondOrders.add(bondOrder);
	}

	@Override
	public void setHeaderInfo(float rFree, float rWork, float resolution, String title, String depositionDate, String releaseDate,
			String[] experimnetalMethods) {
		this.rFree = rFree;
		this.rWork = rWork;
		this.resolution = resolution;
		this.title = title;
		this.depositionDate = depositionDate;
		this.releaseDate = releaseDate;
		this.experimentalMethods = experimnetalMethods;

	}

	private Group getGroup(int groupInd) {
		return groupMap.get(groupInd);
	}


	@Override
	public int getNumBonds() {
		return totalNumBonds;
	}

	@Override
	public int[] getSecStructList() {
		return secStructInfo;
	}


	@Override
	public String getReleaseDate() {
		return releaseDate;
	}

}
