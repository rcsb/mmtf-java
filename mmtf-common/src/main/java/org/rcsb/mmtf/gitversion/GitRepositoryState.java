package org.rcsb.mmtf.gitversion;

import java.util.Properties;

/**
 * Class to store the current git information
 * @author abradley
 *
 */
public class GitRepositoryState {


	  private String commitId;                // =${git.commit.id.full} OR ${git.commit.id}


	public String getCommitId() {
		return commitId;
	}




	public GitRepositoryState(Properties properties)
	{
		

	  this.commitId = String.valueOf(properties.get("git.commit.id")); // OR properties.get("git.commit.id") depending on your configuration

	}
}
