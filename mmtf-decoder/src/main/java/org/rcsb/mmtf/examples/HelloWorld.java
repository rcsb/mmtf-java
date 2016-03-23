package org.rcsb.mmtf.examples;

import org.rcsb.mmtf.api.DataApiInterface;
import org.rcsb.mmtf.dataholders.PDBGroup;

public class HelloWorld {

	public static void main(String[] args) {
		HandleIO handleIO = new HandleIO();
		DataApiInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
		System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getNumChains()+" chains");
		PDBGroup pdbGroup = dataApi.getGroupMap().get(1);
		System.out.println("HET group "+pdbGroup.getGroupName()+" has the following atomic charges: "+pdbGroup.getAtomCharges());
		System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getBioAssembly().size()+" bioassemblies");
	}

}
