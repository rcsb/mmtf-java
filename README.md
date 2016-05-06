[![Build Status](https://travis-ci.org/rcsb/mmtf-java.svg?branch=master)](https://travis-ci.org/rcsb/mmtf-java)
[![Coverage Status](https://coveralls.io/repos/github/rcsb/mmtf-java/badge.svg?branch=master)](https://coveralls.io/github/rcsb/mmtf-java?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/56feb8e5fcd19a0039f1553c/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56feb8e5fcd19a0039f1553c)
[![Version](http://img.shields.io/badge/version-0.1.0-blue.svg?style=flat)] [![License](http://img.shields.io/badge/license-Apache 2.0-blue.svg?style=flat)](https://github.com/rcsb/mmtf-java/blob/master/LICENSE.txt)



The **m**acro**m**olecular **t**ransmission **f**ormat (MMTF) is a binary encoding of biological structures.

This repository holds the Java API, encoding and decoding libraries. Along with a description of the data in the MMTF using Java data types.


The alpha release is available on Maven central.

```xml
		<dependency>
			<groupId>org.rcsb</groupId>
			<artifactId>mmtf-decoder</artifactId>
			<version>0.1.0</version>
		</dependency>
        <dependency>
            <groupId>org.rcsb</groupId>
            <artifactId>mmtf-api</artifactId>
            <version>0.1.0</version>
        </dependency>
```

Or you can clone this repo and install yourself.


Quick getting started.

1) Get the data for a PDB structure and print the number of chains:
```java
StructureDataInterface dataInterface = new DefaultDecoder(ReaderUtils.getDataFromUrl("4cup"));
System.out.println("PDB Code: "+dataInterface.getStructureId()+" has "+dataInterface.getNumChains()+" chains");
```

2) Show the charge information for the first group:
```java
System.out.println("Group name: "+dataInterface.getGroupName(0)+" has the following atomic charges: "+dataInterface.getGroupAtomCharges(0));
```

3) Show how many bioassemblies it has:
```java
System.out.println("PDB Code: "+dataInterface.getStructureId()+" has "+dataInterface.getNumBioassemblies()+" bioassemblies");
```
