package org.rcsb.mmtf.dataholders;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class to store the data sent in an MMTF data source.
 *
 * @author Anthony Bradley
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MmtfBean implements Serializable {


	/** The number to divide coordinate int values by. */
	public static final float COORD_DIVIDER = 1000.0f;
	/** The number to divide occupancy and bfactor int values by. */
	public static final float OCCUPANCY_BFACTOR_DIVIDER = 100.0f;
	/** The default value for Rfree and Rwork */
	public static final float UNAVAILABLE_R_VALUE = 1.0f;
	/** The default value for resolution when not available or not applicable  */
	public static final float UNAVAILABLE_RESOLUTION_VALUE = 99.0f;
	/** The default value for a missing or null string field */
	public static final char UNAVAILABLE_CHAR_VALUE = '\0';


	/** Serial id for this version of the format. */
	private static final long serialVersionUID = 384559979573830324L;

	/** The mmtf version. */
	private String mmtfVersion;

	/** The mmtf producer. */
	private String mmtfProducer;

	/** The number of bonds. */
	private int numBonds;

	/** The structure Id. Most commonly this will be the four character PDB id. */
	private String structureId;

	/** The title. */
	private String title;

	/** The number of atoms. */
	private int numAtoms;

	/** The number of chains per model. */
	private int[] chainsPerModel;

	/** The internal groups per chain. */
	private int[] groupsPerChain;

	/** The names of the chains. Each chain is allocated four bytes. Chain names can be up to four characters long. 0 bytes indicate the end of the chain name. These are taken from the auth id. */
	private byte[] chainNameList;

	/** The names of the chains. Each chain is allocated four bytes. Chain names can be up to four characters long. 0 bytes indicate the end of the chain name. These are taken from the asym id. */
	private byte[] chainIdList;

	/** The space group. */
	private String spaceGroup;

	/** The unit cell. */
	private float[] unitCell;

	/** The bio assembly. */
	private List<BioAssemblyData> bioAssemblyList;

	/** The bond atom (indices) list. */
	private byte[] bondAtomList;

	/** The bond order list. */
	private byte[] bondOrderList;

	/** The list of different PDBGroups in the structure. */
	private PDBGroup[] groupList;

	/** The x coord big. 4 byte integers in pairs. */
	private byte[] xCoordBig;

	/** The y coord big. 4 byte integers in pairs. */
	private byte[] yCoordBig;

	/** The z coord big. 4 byte integers in pairs. */
	private byte[] zCoordBig;

	/** The b factor big. 4 byte integers in pairs. */
	private byte[] bFactorBig;

	/** The x coord small. 2 byte integers. */
	private byte[] xCoordSmall;

	/** The y coord small. 2 byte integers.*/
	private byte[] yCoordSmall;

	/** The z coord small. 2 byte integers.*/
	private byte[] zCoordSmall;

	/** The b factor small. 2 byte integers.*/
	private byte[] bFactorSmall;

	/** The secondary structure list. Stored as 1 byte ints. */
	private byte[] secStructList;

	/** The occupancy list. */
	private byte[] occupancyList;

	/** The list of alternate location ids. */
	private byte[] altLocList;

	/** The insertion code list. */
	private byte[] insCodeList;

	/** The group type list. */
	private byte[] groupTypeList;

	/** The group id list. Identifies each group along the chain. */
	private byte[]  groupIdList;

	/** The atom id list. */
	private byte[] atomIdList;

	/** The SeqRes group ids. */
	private byte[] sequenceIndexList;

	/** The experimental method(s). */
	private String[] experimentalMethods;

	/** The resolution in Angstrom. Null if not applicable*/
	private float resolution;

	/** The rfree. Null if not applicable */
	private float rFree;

	/** The r-work. Null if not applicable */
	private float rWork;

	/** The list of entities in this structure. */
	private Entity[] entityList;

	/** The deposition date of the structure in ISO time standard format. https://www.cl.cam.ac.uk/~mgk25/iso-time.html */
	private String depositionDate;

	/** The release data of the structure in ISO time standard format. https://www.cl.cam.ac.uk/~mgk25/iso-time.html */
	private String releaseDate;

	/** Constructor to set the default values for floats */
	public MmtfBean() {

		/** The mmtf version. Set here. */
		mmtfVersion = "0.1";

		/** The mmtf producer. NA is default and means error. */
		mmtfProducer = "NA";

		/** The resolution in Angstrom. -1.0 if not applicable*/
		resolution = UNAVAILABLE_RESOLUTION_VALUE;

		/** The rfree. 1.0 if not applicable */
		rFree = UNAVAILABLE_R_VALUE;

		rWork = UNAVAILABLE_R_VALUE;

		/** The number of atoms. Default is -1 indicates error */
		numAtoms = -1;

		/** The number of bonds. Default of -1 indicates error*/
		numBonds = -1;

	}

	/**
	 * @return the resolution
	 */
	public Float getResolution() {
		if (resolution==UNAVAILABLE_RESOLUTION_VALUE) {
			return null;
		}
		return resolution;
	}

	/**
	 * @param resolution the resolution to set
	 */
	public void setResolution(float resolution) {
		if (resolution==0.0) {
			this.resolution = UNAVAILABLE_RESOLUTION_VALUE;
		}
		else{
			this.resolution = resolution;
		}
	}

	/**
	 * @return the rFree
	 */
	public Float getrFree() {
		if (rFree==UNAVAILABLE_R_VALUE) {
			return null;
		}
		return rFree;
	}

	/**
	 * @param rFree the rFree to set
	 */
	public void setrFree(float rFree) {
		if (rFree==0.0){
			this.rFree = UNAVAILABLE_R_VALUE;
		}
		else{
			this.rFree = rFree;
		}
	}

	/**
	 * @return the rWork
	 */
	public Float getrWork() {
		if (rWork==UNAVAILABLE_R_VALUE) {
			return null;
		}
		return rWork;
	}

	/**
	 * @param rWork the rWork to set
	 */
	public void setrWork(float rWork) {
		if (rWork==0.0){
			this.rWork = UNAVAILABLE_R_VALUE;
		}
		else{
			this.rWork = rWork;
		}
	}


	/**
	 * Gets the space group.
	 *
	 * @return the space group
	 */
	public final String getSpaceGroup() {
		return spaceGroup;
	}

	/**
	 * Sets the space group.
	 *
	 * @param inputSpaceGroup the new space group
	 */
	public final void setSpaceGroup(final String inputSpaceGroup) {
		this.spaceGroup = inputSpaceGroup;
	}

	/**
	 * Gets the unit cell.
	 *
	 * @return the unit cell
	 */
	public final float[] getUnitCell() {
		return unitCell;
	}

	/**
	 * Sets the unit cell.
	 *
	 * @param inputUnitCell the new unit cell
	 */
	public final void setUnitCell(final float[] inputUnitCell) {
		this.unitCell = inputUnitCell;
	}

	/**
	 * Gets the group num list.
	 *
	 * @return the group num list
	 */
	public final byte[] getGroupIdList() {
		return groupIdList;
	}

	/**
	 * Sets the group num list.
	 *
	 * @param inputGroupNumList the new group num list
	 */
	public final void setGroupIdList(final byte[] inputGroupNumList) {
		this.groupIdList = inputGroupNumList;
	}

	/**
	 * Gets the x coordinate big.
	 *
	 * @return the x coordinate big
	 */
	public final byte[] getxCoordBig() {
		return xCoordBig;
	}

	/**
	 * Sets the x coordinate big.
	 *
	 * @param inputXCoordBig the new 4 byte integer x coordinate array
	 */
	public final void setxCoordBig(final byte[] inputXCoordBig) {
		this.xCoordBig = inputXCoordBig;
	}

	/**
	 * Gets the y coord big.
	 *
	 * @return the y coord big
	 */
	public final byte[] getyCoordBig() {
		return yCoordBig;
	}

	/**
	 * Sets the y coordinate big.
	 *
	 * @param inputYCoordBig the new 4 byte integer y coordinate array
	 */
	public final void setyCoordBig(final byte[] inputYCoordBig) {
		this.yCoordBig = inputYCoordBig;
	}

	/**
	 * Gets the z coordinate big.
	 *
	 * @return the z coordinate big
	 */
	public final byte[] getzCoordBig() {
		return zCoordBig;
	}

	/**
	 * Sets the z coordinate big.
	 *
	 * @param inputZCoordBig the new 4 byte integer z coordinate array
	 */
	public final void setzCoordBig(final byte[] inputZCoordBig) {
		this.zCoordBig = inputZCoordBig;
	}

	/**
	 * Gets the x coordinate small.
	 *
	 * @return the x coordinate small
	 */
	public final byte[] getxCoordSmall() {
		return xCoordSmall;
	}

	/**
	 * Sets the x coordinate small.
	 *
	 * @param inputXCoordSmall the new 2 byte integer x coordinate array
	 */
	public final void setxCoordSmall(final byte[] inputXCoordSmall) {
		this.xCoordSmall = inputXCoordSmall;
	}

	/**
	 * Gets the y coordinate small.
	 *
	 * @return the y coordinate small
	 */
	public final byte[] getyCoordSmall() {
		return yCoordSmall;
	}

	/**
	 * Sets the y coordinate small.
	 *
	 * @param inputYCoordSmall the new 2 byte integer y coordinate array
	 */
	public final void setyCoordSmall(final byte[] inputYCoordSmall) {
		this.yCoordSmall = inputYCoordSmall;
	}

	/**
	 * Gets the z coordinate small.
	 *
	 * @return the z coordinate small
	 */
	public final byte[] getzCoordSmall() {
		return zCoordSmall;
	}

	/**
	 * Sets the z coordinate small.
	 *
	 * @param inputZCoordSmall the new 2 byte integer z coordinate array
	 */
	public final void setzCoordSmall(final byte[] inputZCoordSmall) {
		this.zCoordSmall = inputZCoordSmall;
	}

	/**
	 * Gets the b factor big.
	 *
	 * @return the b factor big
	 */
	public final byte[] getbFactorBig() {
		return bFactorBig;
	}

	/**
	 * Sets the b factor big.
	 *
	 * @param inputBigBFactor the new b factor big
	 */
	public final void setbFactorBig(final byte[] inputBigBFactor) {
		this.bFactorBig = inputBigBFactor;
	}

	/**
	 * Gets the b factor small.
	 *
	 * @return the b factor small
	 */
	public final byte[] getbFactorSmall() {
		return bFactorSmall;
	}

	/**
	 * Sets the b factor small.
	 *
	 * @param inputSmallBFactor the new b factor 2 byte array
	 */
	public final void setbFactorSmall(final byte[] inputSmallBFactor) {
		this.bFactorSmall = inputSmallBFactor;
	}

	/**
	 * Gets the alternate location list.
	 *
	 * @return the alternate location list
	 */
	public final byte[] getAltLocList() {
		return altLocList;
	}

	/**
	 * Sets the alt label list.
	 *
	 * @param inputAltLocList the new alternation location label list
	 */
	public final void setAltLocList(final byte[] inputAltLocList) {
		this.altLocList = inputAltLocList;
	}

	/**
	 * Gets the bio assembly.
	 *
	 * @return the bio assembly
	 */
	public final List<BioAssemblyData> getBioAssemblyList() {
		return bioAssemblyList;
	}

	/**
	 * Gets the chain names. The byte array indicating the (up to four characters) name of the chain. This is taken from the auth id.
	 *
	 * @return the chain list
	 */
	public final byte[] getChainNameList() {
		return chainNameList;
	}

	/**
	 * Sets the chain names. The byte array indicating the (up to four characters) name of the chain. This is taken from the auth id.
	 *
	 * @param inputChainList the new chain list
	 */
	public final void setChainNameList(final byte[] inputChainList) {
		this.chainNameList = inputChainList;
	}

	/**
	 * Sets the bioassembly information.
	 *
	 * @param inputBioAssembly the bio assembly
	 */
	public final void setBioAssemblyList(final List<BioAssemblyData> inputBioAssembly) {
		this.bioAssemblyList = inputBioAssembly;
	}

	/**
	 * Gets the num atoms.
	 *
	 * @return the num atoms
	 */
	public final int getNumAtoms() {
		return numAtoms;
	}

	/**
	 * Sets the num atoms.
	 *
	 * @param inputNumAtoms the new num atoms
	 */
	public final void setNumAtoms(final int inputNumAtoms) {
		this.numAtoms = inputNumAtoms;
	}

	/**
	 * Gets the occupancy list - an encoded per atom list of occupancy values.
	 *
	 * @return the occupancy list - an encoded per atom list of occupancy values.
	 */
	public final byte[] getOccupancyList() {
		return occupancyList;
	}

	/**
	 * Sets the occupancy list - an encoded per atom list of occupancy values.
	 *
	 * @param inputOccupancyList the occupancy list - an encoded per atom list of occupancy values.
	 */
	public final void setOccupancyList(final byte[] inputOccupancyList) {
		this.occupancyList = inputOccupancyList;
	}

	/**
	 * Gets the insertion code list.
	 *
	 * @return the insertion code list
	 */
	public final byte[] getInsCodeList() {
		return insCodeList;
	}

	/**
	 * Sets the ins code list.
	 *
	 * @param inputInsertionCodeList the new insertion code list
	 */
	public final void setInsCodeList(final byte[] inputInsertionCodeList) {
		this.insCodeList = inputInsertionCodeList;
	}

	/**
	 * Gets the group map.
	 *
	 * @return the group map
	 */
	public final PDBGroup[] getGroupList() {
		return groupList;
	}

	/**
	 * Sets the group map.
	 *
	 * @param inputGroupMap the group map
	 */
	public void setGroupList(PDBGroup[] inputGroupMap) {
		this.groupList = inputGroupMap;
	}

	/**
	 * Gets the sec struct list.
	 *
	 * @return the sec struct list
	 */
	public byte[] getSecStructList() {
		return secStructList;
	}

	/**
	 * Sets the sec struct list.
	 *
	 * @param secStruct the new sec struct list
	 */
	public void setSecStructList(byte[] secStruct) {
		this.secStructList = secStruct;
	}

	/**
	 * Gets the group type list.
	 *
	 * @return the group type list
	 */
	public byte[] getGroupTypeList() {
		return groupTypeList;
	}

	/**
	 * Sets the group type list.
	 *
	 * @param resOrder the new group type list
	 */
	public void setGroupTypeList(byte[] resOrder) {
		this.groupTypeList = resOrder;
	}

	/**
	 * Gets the atom id list.
	 *
	 * @return the atom id list
	 */
	public byte[] getAtomIdList() {
		return atomIdList;
	}

	/**
	 * Sets the atom id list.
	 *
	 * @param inputAtomIdList the new atom id list
	 */
	public void setAtomIdList(byte[] inputAtomIdList) {
		this.atomIdList = inputAtomIdList;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param inputTitle the new title
	 */
	public void setTitle(String inputTitle) {
		this.title = inputTitle;
	}

	/**
	 * Gets the structure id. Should be used as a unique identifier of this structure.
	 *
	 * @return the the structure id a unique String id of this structure.
	 */
	public String getStructureId() {
		return structureId;
	}

	/**
	 * Sets the structure id. Should be used as a unique identifier of this structure.
	 *
	 * @param inputId a unique String id of this structure.
	 */
	public void setStructureId(String inputId) {
		this.structureId = inputId;
	}

	/**
	 * Gets the mmtf producer.
	 *
	 * @return the mmtf producer
	 */
	public String getMmtfProducer() {
		return mmtfProducer;
	}

	/**
	 * Sets the mmtf producer.
	 *
	 * @param inputMmtfProducer the new mmtf producer
	 */
	public void setMmtfProducer(String inputMmtfProducer) {
		this.mmtfProducer = inputMmtfProducer;
	}

	/**
	 * Gets the mmtf version.
	 *
	 * @return the mmtf version
	 */
	public String getMmtfVersion() {
		return mmtfVersion;
	}

	/**
	 * Sets the mmtf version.
	 *
	 * @param inputMmtfVersion the new mmtf version
	 */
	public void setMmtfVersion(String inputMmtfVersion) {
		this.mmtfVersion = inputMmtfVersion;
	}

	/**
	 * Gets the num bonds.
	 *
	 * @return the num bonds
	 */
	public int getNumBonds() {
		return numBonds;
	}

	/**
	 * Sets the number of bonds.
	 *
	 * @param inputNumBonds the new num bonds
	 */
	public void setNumBonds(int inputNumBonds) {
		this.numBonds = inputNumBonds;
	}

	/**
	 * Gets the bond atom list.
	 *
	 * @return the bond atom list
	 */
	public byte[] getBondAtomList() {
		return bondAtomList;
	}

	/**
	 * Sets the bond atom list.
	 *
	 * @param inputBondAtomList the new bond atom list
	 */
	public void setBondAtomList(byte[] inputBondAtomList) {
		this.bondAtomList = inputBondAtomList;
	}

	/**
	 * Gets the bond order list.
	 *
	 * @return the bond order list
	 */
	public byte[] getBondOrderList() {
		return bondOrderList;
	}

	/**
	 * Sets the bond order list.
	 *
	 * @param inputBondOrderList the new bond order list
	 */
	public void setBondOrderList(byte[] inputBondOrderList) {
		this.bondOrderList = inputBondOrderList;
	}

	/**
	 * Gets the number of chains per model. Chains are currently specified by asym (internal) chain ids.
	 *
	 * @return the list of chains per model.
	 */
	public int[] getChainsPerModel() {
		return chainsPerModel;
	}

	/**
	 * Sets the number of chains per model. Currently specified by asy (internal) chain ids.
	 *
	 * @param inputInternalChainsPerModel the new list of chains per model.
	 */
	public void setChainsPerModel(int[]
			inputInternalChainsPerModel) {
		this.chainsPerModel = inputInternalChainsPerModel;
	}

	/**
	 * Gets the number of groups per chain.
	 *
	 * @return the internal groups per chain
	 */
	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}

	/**
	 * Sets the number of groups in a chain.
	 *
	 * @param inputGroupsPerChain the new internal groups per chain
	 */
	public void setGroupsPerChain(int[]
			inputGroupsPerChain) {
		this.groupsPerChain = inputGroupsPerChain;
	}

	/**
	 * Gets the internal chain list.
	 *
	 * @return the internal chain list
	 */
	public byte[] getChainIdList() {
		return chainIdList;
	}

	/**
	 * Sets the internal chain list.
	 *
	 * @param inputInternalChainList the new internal chain list
	 */
	public void setChainIdList(byte[] inputInternalChainList) {
		this.chainIdList = inputInternalChainList;
	}

	/**
	 * @return the experimental methods
	 */
	public String[] getExperimentalMethods() {
		return experimentalMethods;
	}

	/**
	 * @param experimentalMethods the experimental methods to set
	 */
	public void setExperimentalMethods(String[] experimentalMethods) {
		this.experimentalMethods = experimentalMethods;
	}

	/**
	 * @return the seqResGroupIds 
	 */
	public byte[] getSequenceIndexList() {
		return sequenceIndexList;
	}

	/**
	 * @param seqResGroupIds the seqResGroupIds to set
	 */
	public void setSequenceIndexList(byte[] seqResGroupIds) {
		this.sequenceIndexList = seqResGroupIds;
	}

	/**
	 * Get the entity list
	 * @return
	 */
	public Entity[] getEntityList() {
		return entityList;
	}

	/**
	 * Set the entity list
	 * @param entityList
	 */
	public void setEntityList(Entity[] entityList) {
		this.entityList = entityList;
	}

	/**
	 * @return the deposition date of the structure in ISO time standard.
	 */
	public String getDepositionDate() {
		return depositionDate;
	}

	/**
	 * @param depositionDate a string indicating the deposition date to set. 
	 */
	public void setDepositionDate(String depositionDate) {
		this.depositionDate = depositionDate;
	}

	/**
	 * @return the release date of the structure in ISO time standard. 
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate a string indicating the deposition date to set. 
	 */
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}



}
