package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.BioAssemblyInfoNew;
import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * A bean to store C-Alpha / DNA backbone and ligand information - in a format that can 
 * then be efficiently sent as messagepack.
 *
 * @author abradley
 */
public class CalphaDistBean {
	
	/** The mmtf version. */
	// The version of the format
	private String mmtfVersion = "0.1";
	
	/** The mmtf producer. */
	// The producer
	private String mmtfProducer;
	
	/** The num bonds. */
	// The number of bonds
	private int numBonds;
	
	/** The pdb id. */
	// The PDBCode
	private String pdbId;
	
	/** The title. */
	// The title of the structure
	private String title;
	
	/** The space group. */
	// String for the space group
	private String spaceGroup;
	
	/** The unit cell. */
	// The unit cell information
	private List<Float> unitCell = new ArrayList<Float>(); 
	
	/** The bio assembly. */
	// A map of Bioassembly -> new class so serializable
	private Map<Integer, BioAssemblyInfoNew> bioAssembly = new HashMap<Integer, BioAssemblyInfoNew>(); 
	
	/** The group map. */
	// The list of sequence information
	private  Map<Integer, PDBGroup> groupMap = new HashMap<Integer, PDBGroup>();
	
	/** The group num list. */
	// Delta and run length
	private byte[] groupNumList;
	
	/** The group type list. */
	private byte[] groupTypeList;
	
	/** The sec struct list. */
	private byte[] secStructList;
	
	/** The x coord big. */
	// For the big arrays split into two -> one of 32 bit ints, one of 16
	private byte[] xCoordBig;
	
	/** The y coord big. */
	private byte[] yCoordBig;
	
	/** The z coord big. */
	private byte[] zCoordBig;
	
	/** The x coord small. */
	// Now for the small ints -> 16 bit
	private byte[] xCoordSmall;
	
	/** The y coord small. */
	private byte[] yCoordSmall;
	
	/** The z coord small. */
	private byte[] zCoordSmall;
	// Add this header info
	/** The num atoms. */
	// Total data for memory allocation
	private int numAtoms;
	
	/** The chains per model. */
	// Add this to store the model information
	private int[] chainsPerModel;
	
	/** The chain list. */
	// List to store the chainids
	private byte[] chainList;
	
	/** The groups per chain. */
	// List to store the number of groups per chain
	private int[] groupsPerChain;
	
	/** The one letter amin seq. */
	// Store the one letter amino acid sequence of the protein
	private char[] oneLetterAminSeq;
	
	/**
	 * Gets the pdb id.
	 *
	 * @return the pdb id
	 */
	public String getPdbId() {
		return pdbId;
	}
	
	/**
	 * Sets the pdb id.
	 *
	 * @param pdbId the new pdb id
	 */
	public void setPdbId(String pdbId) {
		this.pdbId = pdbId;
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
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the space group.
	 *
	 * @return the space group
	 */
	public String getSpaceGroup() {
		return spaceGroup;
	}
	
	/**
	 * Sets the space group.
	 *
	 * @param spaceGroup the new space group
	 */
	public void setSpaceGroup(String spaceGroup) {
		this.spaceGroup = spaceGroup;
	}
	
	/**
	 * Gets the unit cell.
	 *
	 * @return the unit cell
	 */
	public List<Float> getUnitCell() {
		return unitCell;
	}
	
	/**
	 * Sets the unit cell.
	 *
	 * @param unitCell the new unit cell
	 */
	public void setUnitCell(List<Float> unitCell) {
		this.unitCell = unitCell;
	}
	
	/**
	 * Gets the bio assembly.
	 *
	 * @return the bio assembly
	 */
	public Map<Integer, BioAssemblyInfoNew> getBioAssembly() {
		return bioAssembly;
	}
	
	/**
	 * Sets the bio assembly.
	 *
	 * @param bioAssembly the bio assembly
	 */
	public void setBioAssembly(Map<Integer, BioAssemblyInfoNew> bioAssembly) {
		this.bioAssembly = bioAssembly;
	}
	
	/**
	 * Gets the num atoms.
	 *
	 * @return the num atoms
	 */
	public int getNumAtoms() {
		return numAtoms;
	}
	
	/**
	 * Sets the num atoms.
	 *
	 * @param numAtoms the new num atoms
	 */
	public void setNumAtoms(int numAtoms) {
		this.numAtoms = numAtoms;
	}
	
	/**
	 * Gets the chains per model.
	 *
	 * @return the chains per model
	 */
	public int[] getChainsPerModel() {
		return chainsPerModel;
	}
	
	/**
	 * Sets the chains per model.
	 *
	 * @param chainsPerModel the new chains per model
	 */
	public void setChainsPerModel(int[] chainsPerModel) {
		this.chainsPerModel = chainsPerModel;
	}
	
	/**
	 * Gets the chain list.
	 *
	 * @return the chain list
	 */
	public byte[] getChainList() {
		return chainList;
	}
	
	/**
	 * Sets the chain list.
	 *
	 * @param chainList the new chain list
	 */
	public void setChainList(byte[] chainList) {
		this.chainList = chainList;
	}
	
	/**
	 * Gets the groups per chain.
	 *
	 * @return the groups per chain
	 */
	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}
	
	/**
	 * Sets the groups per chain.
	 *
	 * @param groupsPerChain the new groups per chain
	 */
	public void setGroupsPerChain(int[] groupsPerChain) {
		this.groupsPerChain = groupsPerChain;
	}
	
	/**
	 * Gets the group map.
	 *
	 * @return the group map
	 */
	public Map<Integer, PDBGroup> getGroupMap() {
		return groupMap;
	}
	
	/**
	 * Sets the group map.
	 *
	 * @param groupMap the group map
	 */
	public void setGroupMap(Map<Integer, PDBGroup> groupMap) {
		this.groupMap = groupMap;
	}
	
	/**
	 * Gets the group num list.
	 *
	 * @return the group num list
	 */
	public byte[] getGroupNumList() {
		return groupNumList;
	}
	
	/**
	 * Sets the group num list.
	 *
	 * @param _atom_site_auth_seq_id the new group num list
	 */
	public void setGroupNumList(byte[] _atom_site_auth_seq_id) {
		this.groupNumList = _atom_site_auth_seq_id;
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
	 * Gets the x coord big.
	 *
	 * @return the x coord big
	 */
	public byte[] getxCoordBig() {
		return xCoordBig;
	}
	
	/**
	 * Sets the x coord big.
	 *
	 * @param cartn_x_big the new x coord big
	 */
	public void setxCoordBig(byte[] cartn_x_big) {
		this.xCoordBig = cartn_x_big;
	}
	
	/**
	 * Gets the y coord big.
	 *
	 * @return the y coord big
	 */
	public byte[] getyCoordBig() {
		return yCoordBig;
	}
	
	/**
	 * Sets the y coord big.
	 *
	 * @param cartn_y_big the new y coord big
	 */
	public void setyCoordBig(byte[] cartn_y_big) {
		this.yCoordBig = cartn_y_big;
	}
	
	/**
	 * Gets the z coord big.
	 *
	 * @return the z coord big
	 */
	public byte[] getzCoordBig() {
		return zCoordBig;
	}
	
	/**
	 * Sets the z coord big.
	 *
	 * @param cartn_z_big the new z coord big
	 */
	public void setzCoordBig(byte[] cartn_z_big) {
		this.zCoordBig = cartn_z_big;
	}
	
	/**
	 * Gets the x coord small.
	 *
	 * @return the x coord small
	 */
	public byte[] getxCoordSmall() {
		return xCoordSmall;
	}
	
	/**
	 * Sets the x coord small.
	 *
	 * @param cartn_x_small the new x coord small
	 */
	public void setxCoordSmall(byte[] cartn_x_small) {
		this.xCoordSmall = cartn_x_small;
	}
	
	/**
	 * Gets the y coord small.
	 *
	 * @return the y coord small
	 */
	public byte[] getyCoordSmall() {
		return yCoordSmall;
	}
	
	/**
	 * Sets the y coord small.
	 *
	 * @param cartn_y_small the new y coord small
	 */
	public void setyCoordSmall(byte[] cartn_y_small) {
		this.yCoordSmall = cartn_y_small;
	}
	
	/**
	 * Gets the z coord small.
	 *
	 * @return the z coord small
	 */
	public byte[] getzCoordSmall() {
		return zCoordSmall;
	}
	
	/**
	 * Sets the z coord small.
	 *
	 * @param cartn_z_small the new z coord small
	 */
	public void setzCoordSmall(byte[] cartn_z_small) {
		this.zCoordSmall = cartn_z_small;
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
	 * @param mmtfVersion the new mmtf version
	 */
	public void setMmtfVersion(String mmtfVersion) {
		this.mmtfVersion = mmtfVersion;
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
	 * @param mmtfProducer the new mmtf producer
	 */
	public void setMmtfProducer(String mmtfProducer) {
		this.mmtfProducer = mmtfProducer;
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
	 * Sets the num bonds.
	 *
	 * @param numBonds the new num bonds
	 */
	public void setNumBonds(int numBonds) {
		this.numBonds = numBonds;
	}
	
	/**
	 * Gets the one letter amin seq.
	 *
	 * @return the one letter amin seq
	 */
	public char[] getOneLetterAminSeq() {
		return oneLetterAminSeq;
	}
	
	/**
	 * Sets the one letter amin seq.
	 *
	 * @param oneLetterAminSeq the new one letter amin seq
	 */
	public void setOneLetterAminSeq(char[] oneLetterAminSeq) {
		this.oneLetterAminSeq = oneLetterAminSeq;
	}

}
