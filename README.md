[![Build Status](https://travis-ci.org/rcsb/mmtf-java.svg?branch=master)](https://travis-ci.org/rcsb/mmtf-java)
[![Dependency Status](https://www.versioneye.com/user/projects/56feb8e5fcd19a0039f1553c/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56feb8e5fcd19a0039f1553c)
[![Version](http://img.shields.io/badge/version-0.0.1alpha3-blue.svg?style=flat)](http://biojava.org/wiki/BioJava:Download) [![License](http://img.shields.io/badge/license-Apache 2.0-blue.svg?style=flat)](https://github.com/rcsb/mmtf-java/blob/master/LICENSE.txt)


The **m**acro**m**olecular **t**ransmission **f**ormat (MMTF) is a binary encoding of biological structures.

This repository holds the Java API, encoding and decoding libraries. Along with a description of the data in the MMTF using Java data types.


The alpha release is available on Maven central.

```xml
<groupId>org.rcsb</groupId>
<artifactId>mmtf-decoder</artifactId>
<packaging>pom</packaging>
<version>0.0.1-alpha3</version>
```

```xml
<groupId>org.rcsb</groupId>
<artifactId>mmtf-api</artifactId>
<packaging>pom</packaging>
<version>0.0.1-alpha3</version>
```

Or you can clone this repo and install yourself.


Quick getting started.

1) Get the data for a PDB structure and print the number of chains:
```java
HandleIO handleIO = new HandleIO();
DataApiInterface dataApi = handleIO.getDataApiFromUrlOrFile("4cup");
System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getNumChains()+" chains");
```

2) Show the charge information for the first group:
```java
PDBGroup pdbGroup = dataApi.getGroupMap().get(0);
System.out.println("HET group "+pdbGroup.getGroupName()+" has the following atomic charges: "+pdbGroup.getAtomCharges());
```

3) Show how many bioassemblies it has:
```java
System.out.println("PDB Code: "+dataApi.getPdbId()+" has "+dataApi.getBioAssembly().size()+" bioassemblies");
```
