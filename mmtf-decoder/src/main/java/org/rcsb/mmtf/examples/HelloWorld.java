package org.rcsb.mmtf.examples;

import org.rcsb.mmtf.api.DataApiInterface;

public class HelloWorld {

	public static void main(String[] args) {
		HandleIO handleIO = new HandleIO();
		DataApiInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
		System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getNumChains()+" chains");
		System.out.println("HET group "+dataApi.getGroupName(0)+" has the following atomic charges: "+dataApi.getGroupAtomCharges(0));
		System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getNumBioassemblies()+" bioassemblies");
	}

}
