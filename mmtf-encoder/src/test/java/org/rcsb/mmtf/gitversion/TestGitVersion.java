package org.rcsb.mmtf.gitversion;



import static org.junit.Assert.assertNotEquals;

import org.junit.Test;


public class TestGitVersion {
  
  @Test
  public void doesGitVersionExist(){
    GetRepoState getCurrentVersion = new GetRepoState();
    assertNotEquals("NA", getCurrentVersion.getCurrentVersion());
  }

}
