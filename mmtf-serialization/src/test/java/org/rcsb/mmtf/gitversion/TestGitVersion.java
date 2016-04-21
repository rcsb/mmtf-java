package org.rcsb.mmtf.gitversion;



import static org.junit.Assert.assertNotEquals;

import org.junit.Test;


/**
 * Class to test the git version can be found.
 * Would break if not in a git repo.
 * @author Anthony Bradley
 *
 */
public class TestGitVersion {

	/**
	 * Check that the git repo state is not NA or null.
	 */
	@Test
	public void doesGitVersionExist(){
		String repoState = GetRepoState.getCurrentVersion();
		assertNotEquals("NA", repoState);
		assertNotEquals(null, repoState);

	}
	

}
