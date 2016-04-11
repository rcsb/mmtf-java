package org.rcsb.mmtf.examples;

import java.io.IOException;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;

public class HelloWorld {

	public static void main(String[] args) throws IOException {
		HandleIO handleIO = new HandleIO();
		MmtfDecodedDataInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
		System.out.println("PDB Code: "+dataApi.getStructureId()+" has "+dataApi.getNumChains()+" chains");
		System.out.println("HET group "+dataApi.getGroupName(0)+" has the following atomic charges: "+dataApi.getGroupAtomCharges(0));
		System.out.println("PDB Code: "+dataApi.getStructureId()+" has "+dataApi.getNumBioassemblies()+" bioassemblies");
	}

}
