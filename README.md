[![Build Status](https://travis-ci.org/rcsb/mmtf-java.svg?branch=master)](https://travis-ci.org/rcsb/mmtf-java)
[![Coverage Status](https://coveralls.io/repos/github/rcsb/mmtf-java/badge.svg?branch=master)](https://coveralls.io/github/rcsb/mmtf-java?branch=master)
[![Version](http://img.shields.io/badge/version-1.0.9-blue.svg?style=flat)](https://github.com/rcsb/mmtf-java/) 
[![Changelog](https://img.shields.io/badge/changelog--lightgrey.svg?style=flat)](https://github.com/rcsb/mmtf-java/blob/master/CHANGELOG.md)

# MMTF Java API and encoder/decoder

The **m**acro**m**olecular **t**ransmission **f**ormat (MMTF) is a binary encoding of biological structures.

This repository holds the Java API, encoding and decoding libraries. Along with a description of the data in the MMTF using Java data types.


Releases are available on Maven central.

```xml
	<dependency>
	    <groupId>org.rcsb</groupId>
	    <artifactId>mmtf-codec</artifactId>
	    <version>1.0.9</version>
	</dependency>
        <dependency>
            <groupId>org.rcsb</groupId>
            <artifactId>mmtf-api</artifactId>
            <version>1.0.9</version>
        </dependency>
```

## Quick getting started.

1) Get the data for a PDB structure and print the number of chains:
```java
StructureDataInterface dataInterface = new GenericDecoder(ReaderUtils.getDataFromUrl("4CUP"));
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

## For developers: release process

Prepare the release, setting new version numbers when prompted
```
mvn release:prepare
```

Perform the release, uploading artifacts to sonatype/maven central

```
mvn release:perform
```

To be able to upload to sonatype (to publish to maven central) you need to have an
[oss sonatype login, obtainable by signing up to their JIRA tracking system](https://central.sonatype.org/pages/releasing-the-deployment.html). 
Once you have a username and password add this to your `~/.m2/settings.xml` maven file (`<servers>` section):

```xml
<server>
      <id>ossrh</id>
      <username>my_username</username>
      <password>my_password</password>
</server>
``` 

Finally edit the release tag in github and update the CHANGELOG.md file.
