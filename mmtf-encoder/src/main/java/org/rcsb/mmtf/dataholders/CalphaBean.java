package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * A bean to store C-Alpha / DNA backbone and ligand information.
 *
 * @author Anthony Bradley
 */
public class CalphaBean {
	
	/** The number of bonds. */
	// Two integers to store the number of bonds and numeb
	private int numBonds = 0;
	
	/** The number of atoms. */
	private int numAtoms = 0;
	
	/** The map of residue codes to groups. */
	// Convert this information
	private  Map<Integer, PDBGroup> groupMap = new HashMap<Integer, PDBGroup>();
	
	/** The _atom_site_auth_seq_id. */
	// Delta and run length
	private List<Integer>  _atom_site_auth_seq_id = new ArrayList<Integer>();
	
	/** The _atom_site_label_entity_poly_seq_num. */
	// Delta and run length encoded
	private List<Integer>  _atom_site_label_entity_poly_seq_num = new ArrayList<Integer>();
	
	/** The residue order. */
	private List<Integer>  resOrder = new ArrayList<Integer>();
	
	/** The X coords. */
	// The list of c-alpha coords
	private List<Integer> cartn_x = new ArrayList<Integer>();
	
	/** The Y coords. */
	private List<Integer> cartn_y = new ArrayList<Integer>();
	
	/** The Z coords.  */
	private List<Integer> cartn_z = new ArrayList<Integer>();
	
	/** The secondary structure list. */
	//secondary structure (on a per reisude basis
	private List<Integer> secStruct = new ArrayList<Integer>();
	
	/** The number of groups per chain. */
	// A list of integers indicating the number of groups in a chain
	private int[] groupsPerChain;	
	
	/**
	 * Gets the cartn_x.
	 *
	 * @return the cartn_x
	 */
	// Now the getters and setters
	public List<Integer> getCartn_x() {
		return cartn_x;
	}
	
	/**
	 * Sets the cartn_x.
	 *
	 * @param cartn_x the new cartn_x
	 */
	public void setCartn_x(List<Integer> cartn_x) {
		this.cartn_x = cartn_x;
	}
	
	/**
	 * Gets the cartn_y.
	 *
	 * @return the cartn_y
	 */
	public List<Integer> getCartn_y() {
		return cartn_y;
	}
	
	/**
	 * Sets the cartn_y.
	 *
	 * @param cartn_y the new cartn_y
	 */
	public void setCartn_y(List<Integer> cartn_y) {
		this.cartn_y = cartn_y;
	}
	
	/**
	 * Gets the cartn_z.
	 *
	 * @return the cartn_z
	 */
	public List<Integer> getCartn_z() {
		return cartn_z;
	}
	
	/**
	 * Sets the cartn_z.
	 *
	 * @param cartn_z the new cartn_z
	 */
	public void setCartn_z(List<Integer> cartn_z) {
		this.cartn_z = cartn_z;
	}
	
	/**
	 * Gets the sec struct.
	 *
	 * @return the sec struct
	 */
	public List<Integer> getSecStruct() {
		return secStruct;
	}
	
	/**
	 * Sets the sec struct.
	 *
	 * @param secStruct the new sec struct
	 */
	public void setSecStruct(List<Integer> secStruct) {
		this.secStruct = secStruct;
	}
	
	/**
	 * Gets the _atom_site_auth_seq_id.
	 *
	 * @return the _atom_site_auth_seq_id
	 */
	public List<Integer> get_atom_site_auth_seq_id() {
		return _atom_site_auth_seq_id;
	}
	
	/**
	 * Sets the _atom_site_auth_seq_id.
	 *
	 * @param _atom_site_auth_seq_id the new _atom_site_auth_seq_id
	 */
	public void set_atom_site_auth_seq_id(List<Integer> _atom_site_auth_seq_id) {
		this._atom_site_auth_seq_id = _atom_site_auth_seq_id;
	}
	
	/**
	 * Gets the _atom_site_label_entity_poly_seq_num.
	 *
	 * @return the _atom_site_label_entity_poly_seq_num
	 */
	public List<Integer> get_atom_site_label_entity_poly_seq_num() {
		return _atom_site_label_entity_poly_seq_num;
	}
	
	/**
	 * Sets the _atom_site_label_entity_poly_seq_num.
	 *
	 * @param _atom_site_label_entity_poly_seq_num the new _atom_site_label_entity_poly_seq_num
	 */
	public void set_atom_site_label_entity_poly_seq_num(List<Integer> _atom_site_label_entity_poly_seq_num) {
		this._atom_site_label_entity_poly_seq_num = _atom_site_label_entity_poly_seq_num;
	}
	
	/**
	 * Gets the res order.
	 *
	 * @return the res order
	 */
	public List<Integer> getResOrder() {
		return resOrder;
	}
	
	/**
	 * Sets the res order.
	 *
	 * @param resOrder the new res order
	 */
	public void setResOrder(List<Integer> resOrder) {
		this.resOrder = resOrder;
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
}
