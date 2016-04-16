package org.rcsb.mmtf.gitversion;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * A class to get the current git commit
 * @author Anthony Bradley
 *
 */
public class GetRepoState implements Serializable{

	/**
	 * The serial id for this object.
	 */
	private static final long serialVersionUID = -3997275420826350281L;

	/**
	 * Function to read the git repository information and return a class containing that data
	 * @return the gitrepository state class specifying the available information.
	 * @throws IOException an error reading the git files.
	 */
	public GitRepositoryState getGitRepositoryState() throws IOException
	{
		Properties properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
		GitRepositoryState gitRepositoryState = new GitRepositoryState(properties);
		return gitRepositoryState;
	}

	/**
	 * Function to get the current git commit SHA of the code.
	 * @return a string specifying the current commit SHA code.
	 */
	public static String getCurrentVersion(){
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
