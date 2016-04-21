package org.rcsb.mmtf.gitversion;

import java.util.Properties;

/**
 * Class to store the current git information
 * @author abradley
 *
 */
public class GitRepositoryState {

	/**
	 * Initialise the class with a properties object.
	 * @param properties the properties object to bused to get the git commit.
	 */
	public GitRepositoryState(Properties properties)
	{


		this.commitId = String.valueOf(properties.get("git.commit.id")); // OR properties.get("git.commit.id") depending on your configuration

	}

	private String commitId;

	
	/**
	 * @return the current commit id SHA key.
	 */
	public String getCommitId() {
		return commitId;
	}




}
