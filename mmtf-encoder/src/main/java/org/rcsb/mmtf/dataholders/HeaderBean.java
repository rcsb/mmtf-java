package org.rcsb.mmtf.dataholders;

import java.util.Date;
import java.util.List;

import org.rcsb.mmtf.dataholders.BioAssemblyData;

/**
 * Class to store header information in a bean that can be converted to a messagepack.
 *
 * @author abradley
 */
public class HeaderBean {

	/** The number of chains. */
	// The number of chains
	private int numChains;

	/** The number of atoms. */
	// The number of atoms
	private int numAtoms;

	/** The number of bonds. */
	// The number of bonds
	private int numBonds;

	/** The id code. */
	// The PDBCode
	private String idCode;

	/** The title. */
	// The title of the structure
	private String title;

	/** The description. */
	// PDB HEADER INFORMATION
	private String description;

	/** The classification. */
	private String classification;	

	/** The depositon date. */
	private Date depDate;

	/** The modified date. */
	private Date modDate;

	/** The resolution.-1.0 if not available */
	private float resolution;

	/** The r free. -1.0 if not available*/
	private float rFree;

	/** The r work. -1.0 if not available */
	private float rWork;

	/** The doi. */
	private String doi;

	/** The authors. */
	private String authors;

	/** The SEQRES sequence, per asym chain. */
	private List<String> sequence;

	/** The SeqRes group ids. */
	private List<Integer> seqResGroupIds;

	/** The experimental method(s). */
	private List<String> experimentalMethods;

	/** The chains per model. */
	private int chainsPerModel;

	/** The asym chains per model. Assumes model homogenity.*/
	private int asymChainsPerModel;

	/** The chain list. */
	// List to store the chainids
	private byte[] chainList;

	/** The asym chain list. */
	// List to store the chainids
	private byte[] asymChainList;

	/** The groups per chain. */
	// List to store the number of groups per chain
	private int[] groupsPerChain;

	/** The asym groups per chain. */
	private int[] asymGroupsPerChain;

	/** The space group. */
	// LOTS OF OTHER STUFF HERE -> TBD
	private String spaceGroup;

	/** The list of entities in this structure. */
	private Entity[] entityList;

	/** The pdbx_description of a given chain (entity) */
	private String[] chainDescriptionList;

	/** The type of a given chain (entity) */
	private String[] chainTypeList;

	/** The unit cell. */
	private float[] unitCell; 

	/** The bio assembly. */
	private List<BioAssemblyData> bioAssembly; 

	public HeaderBean() {
		resolution = (float) -1.0;

		rFree = (float) -1.0;

		rWork = (float) -1.0;
	}

	// Getters and setters
	/**
	 * Gets the number of chains.
	 *
	 * @return the number of chains
	 */
	public int getNumChains() {
		return numChains;
	}

	/**
	 * Sets the number of chains.
	 *
	 * @param numChains the new number of chains
	 */
	public void setNumChains(int numChains) {
		this.numChains = numChains;
	}

	/**
	 * Gets the number of atoms.
	 *
	 * @return the number of atoms
	 */
	public int getNumAtoms() {
		return numAtoms;
	}

	/**
	 * Sets the number of atoms.
	 *
	 * @param numAtoms the new number of atoms
	 */
	public void setNumAtoms(int numAtoms) {
		this.numAtoms = numAtoms;
	}

	/**
	 * Gets the pdb code.
	 *
	 * @return the pdb code
	 */
	public String getPdbCode() {
		return idCode;
	}

	/**
	 * Sets the pdb code.
	 *
	 * @param pdbCode the new pdb code
	 */
	public void setPdbCode(String pdbCode) {
		this.idCode = pdbCode;
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
	public float[] getUnitCell() {
		return unitCell;
	}

	/**
	 * Sets the unit cell.
	 *
	 * @param unitCell the new unit cell
	 */
	public void setUnitCell(float[] inputUnitCell) {
		this.unitCell = inputUnitCell;
	}

	/**
	 * Gets the chains per model.
	 *
	 * @return the chains per model
	 */
	public int getChainsPerModel() {
		return chainsPerModel;
	}

	/**
	 * Sets the chains per model.
	 *
	 * @param chainsPerModel the new chains per model
	 */
	public void setChainsPerModel(int chainsPerModel) {
		this.chainsPerModel = chainsPerModel;
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
	 * Gets the id code.
	 *
	 * @return the id code
	 */
	public String getIdCode() {
		return idCode;
	}

	/**
	 * Sets the id code.
	 *
	 * @param idCode the new id code
	 */
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the classification.
	 *
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Sets the classification.
	 *
	 * @param classification the new classification
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * Gets the dep date.
	 *
	 * @return the dep date
	 */
	public Date getDepDate() {
		return depDate;
	}

	/**
	 * Sets the dep date.
	 *
	 * @param depDate the new dep date
	 */
	public void setDepDate(Date depDate) {
		this.depDate = depDate;
	}

	/**
	 * Gets the mod date.
	 *
	 * @return the mod date
	 */
	public Date getModDate() {
		return modDate;
	}

	/**
	 * Sets the mod date.
	 *
	 * @param modDate the new mod date
	 */
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	/**
	 * Gets the resolution.
	 *
	 * @return the resolution
	 */
	public float getResolution() {
		return resolution;
	}

	/**
	 * Sets the resolution.
	 *
	 * @param resolution the new resolution
	 */
	public void setResolution(float resolution) {
		this.resolution = resolution;
	}

	/**
	 * Gets the r free.
	 *
	 * @return the r free
	 */
	public float getrFree() {
		return rFree;
	}

	/**
	 * Sets the r free.
	 *
	 * @param rFree the new r free
	 */
	public void setrFree(float rFree) {
		this.rFree = rFree;
	}

	/**
	 * Gets the doi.
	 *
	 * @return the doi
	 */
	public String getDoi() {
		return doi;
	}

	/**
	 * Sets the doi.
	 *
	 * @param doi the new doi
	 */
	public void setDoi(String doi) {
		this.doi = doi;
	}

	/**
	 * Gets the authors.
	 *
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * Sets the authors.
	 *
	 * @param authors the new authors
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * Gets the bio assembly.
	 *
	 * @return the bio assembly
	 */
	public List<BioAssemblyData> getBioAssembly() {
		return bioAssembly;
	}

	/**
	 * Sets the bio assembly.
	 *
	 * @param bioAssembly the bio assembly
	 */
	public void setBioAssembly(List<BioAssemblyData> inputBioAssembly) {
		this.bioAssembly = inputBioAssembly;
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
	 * Gets the asym chain list.
	 *
	 * @return the asym chain list
	 */
	public byte[] getAsymChainList() {
		return asymChainList;
	}

	/**
	 * Sets the asym chain list.
	 *
	 * @param asymChainList the new asym chain list
	 */
	public void setAsymChainList(byte[] asymChainList) {
		this.asymChainList = asymChainList;
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
	 * Gets the sequence.
	 *
	 * @return the sequence
	 */
	public List<String> getSequence() {
		return sequence;
	}

	/**
	 * Sets the sequence.
	 *
	 * @param sequence the new sequence
	 */
	public void setSequence(List<String> sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets the number of bonds.
	 *
	 * @return the number of bonds
	 */
	public int getNumBonds() {
		return numBonds;
	}

	/**
	 * Sets the number of bonds.
	 *
	 * @param numBonds the new number of bonds
	 */
	public void setNumBonds(int numBonds) {
		this.numBonds = numBonds;
	}

	/**
	 * Gets the asym chains per model.
	 *
	 * @return the asym chains per model
	 */
	public int getAsymChainsPerModel() {
		return asymChainsPerModel;
	}

	/**
	 * Sets the asym chains per model.
	 *
	 * @param asymChainsPerModel the new asym chains per model
	 */
	public void setAsymChainsPerModel(int asymChainsPerModel) {
		this.asymChainsPerModel = asymChainsPerModel;
	}

	/**
	 * Gets the asym groups per chain.
	 *
	 * @return the asym groups per chain
	 */
	public int[] getAsymGroupsPerChain() {
		return asymGroupsPerChain;
	}

	/**
	 * Sets the asym groups per chain.
	 *
	 * @param asymGroupsPerChain the new asym groups per chain
	 */
	public void setAsymGroupsPerChain(int[] asymGroupsPerChain) {
		this.asymGroupsPerChain = asymGroupsPerChain;
	}

	/**
	 * @return the seqResGroupIds
	 */
	public List<Integer> getSeqResGroupIds() {
		return seqResGroupIds;
	}

	/**
	 * @param seqResGroupIds the seqResGroupIds to set
	 */
	public void setSeqResGroupIds(List<Integer> seqResGroupIds) {
		this.seqResGroupIds = seqResGroupIds;
	}

	/**
	 * @return the experimental methods
	 */
	public List<String> getExperimentalMethods() {
		return experimentalMethods;
	}

	/**
	 * @param experimentalMethods the experimental methods to set
	 */
	public void setExperimentalMethods(List<String> experimentalMethods) {
		this.experimentalMethods = experimentalMethods;
	}

	/**
	 * @return the rWork
	 */
	public float getrWork() {
		return rWork;
	}

	/**
	 * @param rWork the rWork to set
	 */
	public void setrWork(float rWork) {
		this.rWork = rWork;
	}

	/**
	 * @return the chainDescriptionList
	 */
	public String[] getChainDescriptionList() {
		return chainDescriptionList;
	}

	/**
	 * @param chainDescriptionList the chainDescriptionList to set
	 */
	public void setChainDescriptionList(String[] chainDescriptionList) {
		this.chainDescriptionList = chainDescriptionList;
	}

	/**
	 * @return the chainTypeList
	 */
	public String[] getChainTypeList() {
		return chainTypeList;
	}

	/**
	 * @param chainTypeList the chainTypeList to set
	 */
	public void setChainTypeList(String[] chainTypeList) {
		this.chainTypeList = chainTypeList;
	}

	public Entity[] getEntityList() {
		return entityList;
	}

	public void setEntityList(Entity[] entityList) {
		this.entityList = entityList;
	}	
}
