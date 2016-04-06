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
	
	private GroupType(String groupName, String groupType) {
		this.groupName = groupName;
		this.groupType = groupType;
	}

	/**
	 * @return the groupType
	 */
	public String getGroupType() {
		return groupType;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
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
