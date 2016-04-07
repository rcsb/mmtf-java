package org.rcsb.mmtf.encoder;

import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.api.ByteArrayToObjectConverterInterface;
import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.api.MmtfDecoderInterface;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * A class for the encoding data api.
 * @author Anthony Bradley
 *
 */
public class InflatorInterfaceToGetApi implements MmtfDecodedDataInterface, MmtfDecoderInterface {

	
	/** The X coordinates */
	private float[] cartnX;

	/** The Y coordinates */
	private float[] cartnY;

	/** The Z coordinates */
	private float[] cartnZ;

	/** The X coordinates */
	private float[] bFactor;

	/** The Y coordinates */
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
	private PDBGroup[] groupMap;

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
	private int[] interGroupBondIndices;

	/** The bond orders for bonds between groups*/
	private int[] interGroupBondOrders;

	/** The chosen list of chain ids */
	private String[] chainList;

	/** The mmtf version */
	private String mmtfVersion;

	/** The mmtf prodcuer */
	private String mmtfProducer;

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
	
	
	
	/** The atom counter */
	int atomCounter = 0;
	
	
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
		return groupMap[groupList[groupInd]].getGroupName();
	}

	@Override
	public int getNumAtomsInGroup(int groupInd) {
		return groupMap[groupList[groupInd]].getAtomChargeList().size();
	}

	@Override
	public String[] getGroupAtomNames(int groupInd) {
		groupMap[groupList[groupInd]].getAtomNames();
	}

	@Override
	public String[] getGroupElementNames(int groupInd) {
		groupMap[groupList[groupInd]].getElementNames();

	}

	@Override
	public int[] getGroupBondOrders(int groupInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupBondIndices(int groupInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupAtomCharges(int groupInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getGroupSingleLetterCode(int groupInd) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getGroupChemCompType(int groupInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupTypeIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupSequenceIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getChainIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getChainNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getChainsPerModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupsPerChain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSpaceGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float[] getUnitCell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumBioassemblies() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumTransInBioassembly(int bioassemblyIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getChainIndexListForTransform(int bioassemblyIndex, int transformationIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getMatrixForTransform(int bioassemblyIndex, int transformationIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getInterGroupBondIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getInterGroupBondOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMmtfVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMmtfProducer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumEntities() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getEntityDescription(int entityInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityType(int entityInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getEntityChainIndexList(int entityInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntitySequence(int entityInd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStructureId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumModels() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumChains() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumGroups() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumAtoms() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRfree() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getRwork() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getResolution() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExperimentalMethods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDepositionDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInputData(byte[] byteArray, ByteArrayToObjectConverterInterface byteArrayConverter) {
		// TODO Auto-generated method stub
		
	}
	
	
	// Now provide the capability to fill this data.
	@Override
	public void initStructure(int totalNumAtoms, int totalNumGroups, int totalNumChains, int totalNumModels,
			String structureId) {
		cartnX = new float[totalNumAtoms];
		cartnY= new float[totalNumAtoms];
		cartnZ = new float[totalNumAtoms];
		occupancy = new float[totalNumAtoms];
		bFactor = new float[totalNumAtoms];
		atomId = new int[totalNumAtoms];
		altId = new char[totalNumAtoms];
		insertionCodeList = new char[totalNumAtoms];
		
		
	}

	@Override
	public void finalizeStructure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModelCount(int modelCount) {
		// TODO Auto-generated method stub
		this.modelCount=modelCount;
	}

	@Override
	public void setModelInfo(int modelId, int chainCount) {
		// TODO Auto-generated method stub
		
		modelIndex++;
	}

	@Override
	public void setChainInfo(String chainId, String chainName, int groupCount) {
		// TODO Auto-generated method stub
		
		chainIndex++;
	}

	@Override
	public void setEntityInfo(String[] chainIds, String sequence, String description, String title) {
		// TODO Auto-generated method stub
		
		entityIndex++;
	}

	@Override
	public void setGroupInfo(String groupName, int groupNumber, char insertionCode, String polymerType, int atomCount) {
		// TODO Auto-generated method stub
		
		groupIndex++;
	}

	@Override
	public void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, float x, float y, float z,
			float occupancy, float temperatureFactor, String element, int charge) {

		
		atomIndex++;
		
	}

	@Override
	public void setBioAssemblyTrans(int bioAssemblyIndex, int[] inputChainIndices, double[] inputTransform) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setXtalInfo(String spaceGroup, float[] unitCell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupBond(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInterGroupBond(int thisBondIndOne, int thisBondIndTwo, int thisBondOrder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeaderInfo(float rFree, float rWork, float resolution, String title, String depositionDate,
			String[] experimnetalMethods) {
		this.rFree = rFree;
		this.rWork = rWork;
		this.resolution = resolution;
		this.title = title;
		this.depositionDate = depositionDate;
		this.experimentalMethods = experimnetalMethods;
		
	}

}
