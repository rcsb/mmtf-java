package org.rcsb.mmtf.biojavaencoder;

/**
 * An enum to specify the group type of a PDBGroup.
 * @author Anthony Bradley
 */
public enum GroupType {
	
	HETATM("hetatm","HEATM"),
	AMINO("amino","ATOM"),
	NUCLEOTIDE("nucleotide","ATOM");
	
	private String groupType;
	private String groupName;
	
	
	private GroupType(String inputGroupName, String inputGroupType) {
		this.setGroupName(inputGroupName);
		this.setGroupType(inputGroupType);
	}


	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}


	/**
	 * @param groupType the groupType to set
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}


	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}


	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public static GroupType groupTypeFromString(String groupType)
	{

		if ( groupType == null)
			return null;

		for(GroupType et : GroupType.values())
		{
			if(groupType.equals(et.groupName))
			{
				return et;
			}
		}
		// Return a null entry.
		return null;
	}

}
