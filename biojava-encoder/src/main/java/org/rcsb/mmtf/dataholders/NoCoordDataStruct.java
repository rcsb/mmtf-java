package org.rcsb.mmtf.dataholders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rcsb.mmtf.dataholders.PDBGroup;

/**
 * A bean to store information about non coordinate information - fields are related strongly
 * to the mmCif field they come from.
 *
 * @author anthony
 */
public class NoCoordDataStruct extends CoreDataStruct {

	/** The _atom_site_symbol. */
	// The symbol of thae atom
	private List<String> _atom_site_symbol = new ArrayList<String>();
	// This data item is an author defined alternative to the value of
	// _atom_site_label_asym_id_ This item holds the PDB chain
	/** The _atom_site_asym_id. */
	// identifier_
	private List<String> _atom_site_asym_id = new ArrayList<String>();
	// This data item is an author defined alternative to the value of
	// _atom_site_label_atom_id_ This item holds the PDB atom name_
//	private List<String> _atom_site_auth_atom_id = new ArrayList<String>();
	// This data item is an author defined alternative to the value of
	// _atom_site_label_comp_id_ This item holds the PDB 3-letter-code
	// residue names
//	private List<String> _atom_site_auth_comp_id = new ArrayList<String>();
	// This data item is an author defined alternative to the value of
	/** The _atom_site_auth_seq_id. */
	// _atom_site_label_seq_id_ This item holds the PDB residue number_
	private List<Integer> _atom_site_auth_seq_id = new ArrayList<Integer>();
	
	/** The _atom_site_label_entity_poly_seq_num. */
	private List<Integer> _atom_site_label_entity_poly_seq_num= new ArrayList<Integer>();
	
	/** The _atom_site_pdbx_ pd b_ins_code. */
	// This data item corresponds to the PDB insertion code_
	private List<String> _atom_site_pdbx_PDB_ins_code = new ArrayList<String>();
	// This data item identifies the model number in an ensemble of
	// coordinate data_
//	private List<Integer> _atom_site_pdbx_PDB_model_num = new ArrayList<Integer>();
	// This data item is a place holder for the tags used by the PDB to
	/** The _atom_site_group_ pdb. */
	// identify coordinate records (e_g_ ATOM or HETATM)_
	private List<String> _atom_site_group_PDB = new ArrayList<String>();
	// This item is a uniquely identifies for each alternative site for
	/** The _atom_site_label_alt_id. */
	// this atom position_
	private List<String> _atom_site_label_alt_id= new ArrayList<String>();
	
	/** The _atom_site_label_asym_id. */
	// This data item is reference to item _struct_asym_id defined in
	private List<String> _atom_site_label_asym_id= new ArrayList<String>();
	// This data item is a reference to item _chem_comp_atom_atom_id
	// defined in category CHEM_COMP_ATOM which is stored in the
	// Chemical Component Dictionary_ This atom identifier uniquely
	/** The _atom_site_label_atom_id. */
	// identifies each atom within each chemical component_
	private List<String> _atom_site_label_atom_id= new ArrayList<String>();
	// This data item is a reference to item _chem_comp_id defined in
	// category CHEM_COMP_ This item is the primary identifier for
	// chemical components which may either be mononers in a polymeric
	/** The _atom_site_label_comp_id. */
	// entity or complete non-polymer entities_
	private List<String> _atom_site_label_comp_id= new ArrayList<String>();
	// This data item is a reference to _entity_id defined in the ENTITY
	// category_ This item is used to identify chemically distinct
	// portions of the molecular structure (e_g_ polymer chains,
	// ligands, solvent)_
	// This data item is a reference to _entity_poly_seq_num defined in
	// the ENTITY_POLY_SEQ category_ This item is used to maintain the
	// correspondence between the chemical sequence of a polymeric
	// entity and the sequence information in the coordinate list and in
	// may other structural categories_ This identifier has no meaning
	/** The _atom_site_label_entity_id. */
	// for non-polymer entities_
	private List<String> _atom_site_label_entity_id= new ArrayList<String>();
	
	/** The inter group bond inds. */
	// The indices and orders of bonds between groups
	private List<Integer> interGroupBondInds = new ArrayList<Integer>();
	
	/** The inter group bond orders. */
	private List<Integer> interGroupBondOrders = new ArrayList<Integer>();
	
	/** The sec struct. */
	private List<Integer> secStruct = new ArrayList<Integer>();
	
	/** The res order. */
	private List<Integer> resOrder = new ArrayList<Integer>();
	
	/** The group list. */
	private List<List<Integer>> groupList = new ArrayList<List<Integer>>();
	
	/**
	 * Gets the _atom_site_symbol.
	 *
	 * @return the _atom_site_symbol
	 */
	public List<String> get_atom_site_symbol() {
		return _atom_site_symbol;
	}
	
	/**
	 * Sets the _atom_site_symbol.
	 *
	 * @param _atom_site_symbol the new _atom_site_symbol
	 */
	public void set_atom_site_symbol(List<String> _atom_site_symbol) {
		this._atom_site_symbol = _atom_site_symbol;
	}
	
	/**
	 * Gets the _atom_site_asym_id.
	 *
	 * @return the _atom_site_asym_id
	 */
	public List<String> get_atom_site_asym_id() {
		return _atom_site_asym_id;
	}
	
	/**
	 * Sets the _atom_site_asym_id.
	 *
	 * @param _atom_site_asym_id the new _atom_site_asym_id
	 */
	public void set_atom_site_asym_id(List<String> _atom_site_asym_id) {
		this._atom_site_asym_id = _atom_site_asym_id;
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
	 * Gets the _atom_site_pdbx_ pd b_ins_code.
	 *
	 * @return the _atom_site_pdbx_ pd b_ins_code
	 */
	public List<String> get_atom_site_pdbx_PDB_ins_code() {
		return _atom_site_pdbx_PDB_ins_code;
	}
	
	/**
	 * Sets the _atom_site_pdbx_ pd b_ins_code.
	 *
	 * @param _atom_site_pdbx_PDB_ins_code the new _atom_site_pdbx_ pd b_ins_code
	 */
	public void set_atom_site_pdbx_PDB_ins_code(List<String> _atom_site_pdbx_PDB_ins_code) {
		this._atom_site_pdbx_PDB_ins_code = _atom_site_pdbx_PDB_ins_code;
	}
	
	/**
	 * Gets the _atom_site_group_ pdb.
	 *
	 * @return the _atom_site_group_ pdb
	 */
	public List<String> get_atom_site_group_PDB() {
		return _atom_site_group_PDB;
	}
	
	/**
	 * Sets the _atom_site_group_ pdb.
	 *
	 * @param _atom_site_group_PDB the new _atom_site_group_ pdb
	 */
	public void set_atom_site_group_PDB(List<String> _atom_site_group_PDB) {
		this._atom_site_group_PDB = _atom_site_group_PDB;
	}
	
	/**
	 * Gets the _atom_site_label_alt_id.
	 *
	 * @return the _atom_site_label_alt_id
	 */
	public List<String> get_atom_site_label_alt_id() {
		return _atom_site_label_alt_id;
	}
	
	/**
	 * Sets the _atom_site_label_alt_id.
	 *
	 * @param _atom_site_label_alt_id the new _atom_site_label_alt_id
	 */
	public void set_atom_site_label_alt_id(List<String> _atom_site_label_alt_id) {
		this._atom_site_label_alt_id = _atom_site_label_alt_id;
	}
	
	/**
	 * Gets the _atom_site_label_asym_id.
	 *
	 * @return the _atom_site_label_asym_id
	 */
	public List<String> get_atom_site_label_asym_id() {
		return _atom_site_label_asym_id;
	}
	
	/**
	 * Sets the _atom_site_label_asym_id.
	 *
	 * @param _atom_site_label_asym_id the new _atom_site_label_asym_id
	 */
	public void set_atom_site_label_asym_id(List<String> _atom_site_label_asym_id) {
		this._atom_site_label_asym_id = _atom_site_label_asym_id;
	}
	
	/**
	 * Gets the _atom_site_label_atom_id.
	 *
	 * @return the _atom_site_label_atom_id
	 */
	public List<String> get_atom_site_label_atom_id() {
		return _atom_site_label_atom_id;
	}
	
	/**
	 * Sets the _atom_site_label_atom_id.
	 *
	 * @param _atom_site_label_atom_id the new _atom_site_label_atom_id
	 */
	public void set_atom_site_label_atom_id(List<String> _atom_site_label_atom_id) {
		this._atom_site_label_atom_id = _atom_site_label_atom_id;
	}
	
	/**
	 * Gets the _atom_site_label_comp_id.
	 *
	 * @return the _atom_site_label_comp_id
	 */
	public List<String> get_atom_site_label_comp_id() {
		return _atom_site_label_comp_id;
	}
	
	/**
	 * Sets the _atom_site_label_comp_id.
	 *
	 * @param _atom_site_label_comp_id the new _atom_site_label_comp_id
	 */
	public void set_atom_site_label_comp_id(List<String> _atom_site_label_comp_id) {
		this._atom_site_label_comp_id = _atom_site_label_comp_id;
	}
	
	/**
	 * Gets the _atom_site_label_entity_id.
	 *
	 * @return the _atom_site_label_entity_id
	 */
	public List<String> get_atom_site_label_entity_id() {
		return _atom_site_label_entity_id;
	}
	
	/**
	 * Sets the _atom_site_label_entity_id.
	 *
	 * @param _atom_site_label_entity_id the new _atom_site_label_entity_id
	 */
	public void set_atom_site_label_entity_id(List<String> _atom_site_label_entity_id) {
		this._atom_site_label_entity_id = _atom_site_label_entity_id;
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
	 * Gets the group list.
	 *
	 * @return the group list
	 */
	public List<List<Integer>> getGroupList() {
		return groupList;
	}
	
	/**
	 * Sets the group list.
	 *
	 * @param groupList the new group list
	 */
	public void setGroupList(List<List<Integer>> groupList) {
		this.groupList = groupList;
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
	 * Gets the inter group bond inds.
	 *
	 * @return the inter group bond inds
	 */
	public List<Integer> getInterGroupBondInds() {
		return interGroupBondInds;
	}
	
	/**
	 * Sets the inter group bond inds.
	 *
	 * @param interGroupBondInds the new inter group bond inds
	 */
	public void setInterGroupBondInds(List<Integer> interGroupBondInds) {
		this.interGroupBondInds = interGroupBondInds;
	}
	
	/**
	 * Gets the inter group bond orders.
	 *
	 * @return the inter group bond orders
	 */
	public List<Integer> getInterGroupBondOrders() {
		return interGroupBondOrders;
	}
	
	/**
	 * Sets the inter group bond orders.
	 *
	 * @param interGroupBondOrders the new inter group bond orders
	 */
	public void setInterGroupBondOrders(List<Integer> interGroupBondOrders) {
		this.interGroupBondOrders = interGroupBondOrders;
	}
	
	/** The group map. */
	protected Map<Integer, PDBGroup> groupMap = new HashMap<Integer, PDBGroup>();
}
