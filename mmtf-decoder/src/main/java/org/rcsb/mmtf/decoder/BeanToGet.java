package org.rcsb.mmtf.decoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * Takes an input MMTF bean and converts to a data API.
 * @author Anthony Bradley
 *
 */
public class BeanToGet implements MmtfDecodedDataInterface {

	public BeanToGet(MmtfBean inputData) throws IOException {
		groupList = ArrayConverters.convertFourByteToIntegers(inputData.getGroupTypeList());
		// Decode the coordinate  and B-factor arrays.
		cartnX = ArrayConverters.convertIntsToFloats(
				ArrayDecoders.deltaDecode(
						ArrayConverters.combineIntegers(
								ArrayConverters.convertTwoByteToIntegers(inputData.getxCoordSmall()),
								ArrayConverters.convertFourByteToIntegers(inputData.getxCoordBig()))),
				MmtfBean.COORD_DIVIDER);
		cartnY = ArrayConverters.convertIntsToFloats(
				ArrayDecoders.deltaDecode(
						ArrayConverters.combineIntegers(
								ArrayConverters.convertTwoByteToIntegers(inputData.getyCoordSmall()),
								ArrayConverters.convertFourByteToIntegers(inputData.getyCoordBig()))),
				MmtfBean.COORD_DIVIDER);
		cartnZ = ArrayConverters.convertIntsToFloats(
				ArrayDecoders.deltaDecode(
						ArrayConverters.combineIntegers(
								ArrayConverters.convertTwoByteToIntegers(inputData.getzCoordSmall()),
								ArrayConverters.convertFourByteToIntegers(inputData.getzCoordBig()))),
				MmtfBean.COORD_DIVIDER);
		bFactor = ArrayConverters.convertIntsToFloats(
				ArrayDecoders.deltaDecode(
						ArrayConverters.combineIntegers(
								ArrayConverters.convertTwoByteToIntegers(inputData.getbFactorSmall()),
								ArrayConverters.convertFourByteToIntegers(inputData.getbFactorBig()))),
				MmtfBean.OCCUPANCY_BFACTOR_DIVIDER);
		// Run length decode the occupancy array
		occupancy = ArrayConverters.convertIntsToFloats(
				ArrayDecoders.runlengthDecode(
						ArrayConverters.convertFourByteToIntegers(inputData.getOccupancyList())), 
				MmtfBean.OCCUPANCY_BFACTOR_DIVIDER);
		// Run length and delta 
		atomId = ArrayDecoders.deltaDecode(
				ArrayDecoders.runlengthDecode(
						ArrayConverters.convertFourByteToIntegers(inputData.getAtomIdList())));
		// Run length encoded
		altId = ArrayConverters.convertIntegerToChar(
				ArrayDecoders.runlengthDecode(inputData.getAltLocList()));
		insertionCodeList = ArrayConverters.convertIntegerToChar(
				ArrayDecoders.runlengthDecode(inputData.getInsCodeList()));

		// Get the groupNumber
		groupNum = ArrayConverters.convertByteToIntegers(
				inputData.getGroupIdList());
		// Get the group map (all the unique groups in the structure).
		groupMap = inputData.getGroupList();
		// Get the seqRes groups
		seqResGroupList = ArrayDecoders.deltaDecode(
						ArrayConverters.convertFourByteToIntegers(
								inputData.getSequenceIdList()));
		// Get the number of chains per model
		chainsPerModel = inputData.getChainsPerModel();
		groupsPerChain = inputData.getGroupsPerChain();
		// Get the internal and public facing chain ids
		publicChainIds = ArrayConverters.decodeChainList(inputData.getChainNameList());
		chainList = ArrayConverters.decodeChainList(inputData.getChainIdList());
		spaceGroup = inputData.getSpaceGroup();
		unitCell = inputData.getUnitCell();
		bioAssembly  = inputData.getBioAssemblyList();
		interGroupBondIndices = ArrayConverters.convertFourByteToIntegers(inputData.getBondAtomList());
		interGroupBondOrders = ArrayConverters.convertByteToIntegers(inputData.getBondOrderList());
		mmtfVersion = inputData.getMmtfVersion();
		mmtfProducer = inputData.getMmtfProducer();
		entityList = inputData.getEntityList();
		pdbId = inputData.getStructureId();
		// Now get the header data
		rFree = inputData.getrFree();
		// Optional fields
		rWork = inputData.getrWork();
		resolution = inputData.getResolution();
		title = inputData.getTitle();
		experimentalMethods = inputData.getExperimentalMethods();
		// Now get the relase information
		depositionDate = inputData.getDepositionDate();
	}

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

	/** A list containing pdb group names for nucleic acids */
	List<String> nucAcidList = new ArrayList<>();

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
	public int[] getGroupTypeIndices() {
		return groupList;
	}

	@Override
	public int[] getGroupSequenceIndices() {
		return seqResGroupList;
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
	public int[] getInterGroupBondIndices() {
		return interGroupBondIndices;
	}

	@Override
	public int[] getInterGroupBondOrders() {
		return interGroupBondOrders;
	}

	@Override
	public String[] getChainIds() {
		return chainList;
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
	public String getStructureId() {
		return pdbId;
	}

	@Override
	public int getNumGroups() {
		return this.groupList.length;
	}

	@Override
	public int getNumChains() {
		return this.chainList.length;
	}

	@Override
	public int getNumModels() {	
		return this.chainsPerModel.length;
	}

	@Override
	public int getNumAtoms() {
		return this.cartnX.length;
	}

	@Override
	public float getRfree() {
		if (rFree==null) {
			return MmtfBean.UNAVAILABLE_R_VALUE;
		}
		return rFree;
	}

	@Override
	public float getResolution() {
		if (resolution==null) {
			return MmtfBean.UNAVAILABLE_RESOLUTION_VALUE;
		}
		return resolution;
	}

	@Override
	public float getRwork() {
		if (rWork==null) {
			return MmtfBean.UNAVAILABLE_R_VALUE;
		}
		return rWork;
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
	public String getGroupName(int groupInd) {
		return groupMap[groupInd].getGroupName();
	}

	public int getNumAtomsInGroup(int groupInd) {
		return groupMap[groupInd].getAtomChargeList().length;
	}

	@Override
	public String[] getGroupAtomNames(int groupInd) {
		return groupMap[groupInd].getAtomNameList();
	}

	@Override
	public String[] getGroupElementNames(int groupInd) {
		return groupMap[groupInd].getElementList();

	}

	@Override
	public int[] getGroupBondOrders(int groupInd) {
		return groupMap[groupInd].getBondOrderList();

	}

	@Override
	public int[] getGroupBondIndices(int groupInd) {
		return groupMap[groupInd].getBondAtomList();
	}

	@Override
	public int[] getGroupAtomCharges(int groupInd) {
		return groupMap[groupInd].getAtomChargeList();
	}

	@Override
	public char getGroupSingleLetterCode(int groupInd) {
		return groupMap[groupInd].getSingleLetterCode();
	}

	@Override
	public String getGroupChemCompType(int groupInd) {
		return groupMap[groupInd].getChemCompType();
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
	public int getNumEntities() {
		return entityList.length;
	}

	@Override
	public int getNumBioassemblies() {
		return bioAssembly.size();
	}

	@Override
	public int getNumTransInBioassembly(int bioassemblyIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransforms().size();
	}

	@Override
	public int[] getChainIndexListForTransform(int bioassemblyIndex, int transformationIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransforms().get(transformationIndex).getChainIndexList();
	}

	@Override
	public double[] getMatrixForTransform(int bioassemblyIndex, int transformationIndex) {
		return bioAssembly.get(bioassemblyIndex).getTransforms().get(transformationIndex).getTransformation();
	}

	@Override
	public String getDepositionDate() {
		return depositionDate;
	}

	@Override
	public int getNumBonds() {
		int numIntergroupBonds = interGroupBondOrders.length;
		for(int groupIndex : groupList) {
			numIntergroupBonds+=groupMap[groupIndex].getBondOrderList().length;
		}
		return numIntergroupBonds;
	}


}
