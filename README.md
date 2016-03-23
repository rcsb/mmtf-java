The **m**acro**m**olecular **t**ransmission **f**ormat (MMTF) is a binary encoding of biological structures.

This repository holds the Java API, encoding and decoding libraries. Along with a description of the data in the MMTF using Java data types.


The alpha release is available on Maven central.

```<groupId>org.rcsb</groupId>
<artifactId>mmtf</artifactId>
<packaging>pom</packaging>
<version>0.0.1-alpha1</version>
```


Or you can clone this repo and install yourself.


Quick getting started.

1) Get the data for a PDB structure and print the number of chains:
```
HandleIO handleIO = new HandleIO();
DataApiInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getNumChains()+" chains");
```

2) Show the charge information for the first group:
```
PDBGroup pdbGroup = dataApi.getGroupMap().get(0);
System.out.println("HET group "+pdbGroup.getGroupName()+" has the following atomic charges: "+pdbGroup.getAtomCharges());
```

3) Show how many bioassemblies it has:
```
System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getBioAssembly().size()+" bioassemblies");
```
