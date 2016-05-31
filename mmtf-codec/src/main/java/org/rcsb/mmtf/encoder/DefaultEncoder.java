package org.rcsb.mmtf.encoder;


import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.ArrayConverters;
import org.rcsb.mmtf.codec.FloatCodecs;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * Default encoding class. Converts a {@link StructureDataInterface} into an
 * {@link MmtfStructure}.
 * @author Anthony Bradley
 *
 */
public class DefaultEncoder implements EncoderInterface {

	private MmtfStructure mmtfBean;

	/**
	 * The constructor for the encoder.
	 * @param structureDataInterface the interface of data to be encoded.
	 */
	public DefaultEncoder(StructureDataInterface structureDataInterface) {
		mmtfBean = new MmtfStructure();
		// Set the group types
		mmtfBean.setGroupTypeList(
				ArrayConverters.convertIntegersToFourByte(
						structureDataInterface.getGroupTypeIndices()));
		// Encode the coordinate  and B-factor arrays.
		mmtfBean.setxCoordList(FloatCodecs.INT_DELTA_RECURSIVE.encode(structureDataInterface.getxCoords(),1000));
		mmtfBean.setyCoordList(FloatCodecs.INT_DELTA_RECURSIVE.encode(structureDataInterface.getyCoords(),1000));
		mmtfBean.setzCoordList(FloatCodecs.INT_DELTA_RECURSIVE.encode(structureDataInterface.getzCoords(),1000));
		mmtfBean.setbFactorList(FloatCodecs.INT_DELTA_RECURSIVE.encode(structureDataInterface.getbFactors(),100));
		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertFloatsToInts(
								structureDataInterface.getOccupancies(),
								MmtfStructure.OCCUPANCY_BFACTOR_DIVIDER))));
		// Run length and delta
		mmtfBean.setAtomIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(structureDataInterface.getAtomIds()))));
		// Run length encoded
		mmtfBean.setAltLocList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertCharToIntegers(
								structureDataInterface.getAltLocIds()))));
		mmtfBean.setInsCodeList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertCharToIntegers(
								structureDataInterface.getInsCodes()))));

		// Set the groupNumber
		mmtfBean.setGroupIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(
								structureDataInterface.getGroupIds()))));

		// Set the group map (all the unique groups in the structure).
		mmtfBean.setGroupList(EncoderUtils.generateGroupMap(structureDataInterface));
		// Set the indices for the groups mapping to the sequence
		mmtfBean.setSequenceIndexList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(
								structureDataInterface.getGroupSequenceIndices()))));
		// Set the number of chains per model
		mmtfBean.setChainsPerModel(structureDataInterface.getChainsPerModel());
		mmtfBean.setGroupsPerChain(structureDataInterface.getGroupsPerChain());
		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(ArrayConverters.encodeChainList(structureDataInterface.getChainNames(),MmtfStructure.CHAIN_LENGTH));
		mmtfBean.setChainIdList(ArrayConverters.encodeChainList(structureDataInterface.getChainIds(),MmtfStructure.CHAIN_LENGTH));
		// Set the space group information
		mmtfBean.setSpaceGroup(structureDataInterface.getSpaceGroup());
		mmtfBean.setUnitCell(structureDataInterface.getUnitCell());
		// Set the bioassembly and entity information
		mmtfBean.setBioAssemblyList(
				EncoderUtils.generateBioassemblies(structureDataInterface));
		mmtfBean.setEntityList(
				EncoderUtils.generateEntityList(structureDataInterface)
				);
		// Set the bond orders and indcices
		mmtfBean.setBondOrderList(ArrayConverters.convertIntegersToBytes(
				structureDataInterface.getInterGroupBondOrders()));
		mmtfBean.setBondAtomList(ArrayConverters.convertIntegersToFourByte(
				structureDataInterface.getInterGroupBondIndices()));
		// Set the version and producer information
		mmtfBean.setMmtfProducer(structureDataInterface.getMmtfProducer());
		mmtfBean.setStructureId(structureDataInterface.getStructureId());
		// Set some header data
		mmtfBean.setNumAtoms(structureDataInterface.getNumAtoms());
		mmtfBean.setNumBonds(structureDataInterface.getNumBonds());
		mmtfBean.setNumChains(structureDataInterface.getNumChains());
		mmtfBean.setNumGroups(structureDataInterface.getNumGroups());
		mmtfBean.setNumModels(structureDataInterface.getNumModels());
		mmtfBean.setrFree(structureDataInterface.getRfree());
		mmtfBean.setrWork(structureDataInterface.getRwork());
		mmtfBean.setResolution(structureDataInterface.getResolution());
		mmtfBean.setTitle(structureDataInterface.getTitle());
		mmtfBean.setExperimentalMethods(structureDataInterface.getExperimentalMethods());
		mmtfBean.setDepositionDate(structureDataInterface.getDepositionDate());
		mmtfBean.setReleaseDate(structureDataInterface.getReleaseDate());
		mmtfBean.setSecStructList(ArrayConverters.convertIntegersToBytes(structureDataInterface.getSecStructList()));
	}

	@Override
	public MmtfStructure getMmtfEncodedStructure() {
		return mmtfBean;
	}


	


}
