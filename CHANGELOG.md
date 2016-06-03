# Change Log
All notable changes to this project will be documented in this file, following the suggestions of [Keep a CHANGELOG](http://keepachangelog.com/). This project adheres to [Semantic Versioning](http://semver.org/).

## v0.1.0 - 2016-04-22
### Added
- Initial release

## v0.2.0 - 2016-06-03
### Added
- mmtf-codec module - consolidating mmtf-encoder and mmtf-decoder
- org.rcsb.mmtf.codec package - with enums for encoding and decoding generic data
- NCS Operator information
- R-work and release date information
- Number models, number chains and number groups
- Reduced format encoder

### Changed
- Codecs defined in 12 bytes at start of byte arrays. https://github.com/rcsb/mmtf/blob/master/spec.md#codecs


### Removed
- mmtf-encoder and mmtf-decoder modules

