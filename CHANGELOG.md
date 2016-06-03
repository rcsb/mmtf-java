# Change Log
All notable changes to this project will be documented in this file, following the suggestions of [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to [Semantic Versioning](http://semver.org/).

## v0.1.0 - 2016-04-22
### Added
- Initial release

## v0.1.1 - 2016-06-02
### Changed
- Find max in empty int array now returns -1. Added a test to do so.
- Refactored some of the code to remove repitition
- Added private to several class level variables

### Added
- Added a new reduced encoder to produce the reduced format of the data.

## v0.2.0 - 2016-06-03
### Added
- mmtf-codec module - consolidating mmtf-encoder and mmtf-decoder
- org.rcsb.mmtf.codec package - with enums for encoding and decoding generic data
- NCS Operator information
- R-work and release date information
- Number models, number chains and number groups in the MMTF data
- Reduced format encoder
- Enums to define the different encoding strategy
- common-lang dependency
- OptionParser class to parse type, length and parameter from 12 byte header

### Changed
- Codecs defined in 12 bytes at start of byte arrays. https://github.com/rcsb/mmtf/blob/master/spec.md#codecs
- Split lists are now recursive index encoded as single lists
- atomChargeList to formalChargeList
- xCoords -> xCoordList (and y,z and B-factors)

### Removed
- mmtf-encoder and mmtf-decoder modules


