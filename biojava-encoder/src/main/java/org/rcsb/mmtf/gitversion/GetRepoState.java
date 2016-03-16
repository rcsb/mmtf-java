package org.rcsb.mmtf.gitversion;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

public class GetRepoState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997275420826350281L;

	/**
	 * Function to read the git repo information and return a class containing that data
	 * @return
	 * @throws IOException
	 */
	public GitRepositoryState getGitRepositoryState() throws IOException
	{
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
		GitRepositoryState gitRepositoryState = new GitRepositoryState(properties);
		return gitRepositoryState;
	}

	/**
	 * Function to get the curretn git version
	 * @return
	 * @throws IOException
	 */
	public String getCurrentVersion(){
		GetRepoState grs = new GetRepoState();
		try{
			GitRepositoryState repoState = grs.getGitRepositoryState();
			return repoState.getCommitId();
		}
		catch(IOException e){
			return "NA";
		}
		catch(NullPointerException e){
			return "NA";
		}
	}
}
