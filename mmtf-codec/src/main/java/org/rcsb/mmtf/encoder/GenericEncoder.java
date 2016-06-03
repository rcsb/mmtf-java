package org.rcsb.mmtf.encoder;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.CharCodecs;
import org.rcsb.mmtf.codec.FloatCodecs;
import org.rcsb.mmtf.codec.IntCodecs;
import org.rcsb.mmtf.codec.StringCodecs;
import org.rcsb.mmtf.dataholders.MmtfStructure;

/**
 * An encoder for encoding with the generic strategy.
 * @author Anthony Bradley
 *
 */
public class GenericEncoder implements EncoderInterface {
	
	
	private MmtfStructure mmtfBean;
	private int coordDivider = MmtfStructure.COORD_DIVIDER;
	private int bfactorOccDivider = MmtfStructure.OCCUPANCY_BFACTOR_DIVIDER;

	
	/**
	 * The constructor for the encoder.
	 * @param structureDataInterface the interface of data to be encoded.
	 */
	public GenericEncoder(StructureDataInterface structureDataInterface) {
		encode(structureDataInterface);
	}
	
	/**
	 * The constructor for the encoder.
	 * @param structureDataInterface the interface of data to be encoded
	 * @param precision the precision for the storing of coordinate, B-factor and occupancy data
	 */
	public GenericEncoder(StructureDataInterface structureDataInterface, int precision) {
		coordDivider = precision;
		bfactorOccDivider = precision;
		encode(structureDataInterface);
	}

	private void encode(StructureDataInterface structureDataInterface) {
		mmtfBean = new MmtfStructure();
		// Delta split three and two
		mmtfBean.setxCoordList(EncoderUtils.encodeByteArr(FloatCodecs.INT_DELTA_RECURSIVE,structureDataInterface.getxCoords(),coordDivider));
		mmtfBean.setyCoordList(EncoderUtils.encodeByteArr(FloatCodecs.INT_DELTA_RECURSIVE,structureDataInterface.getyCoords(),coordDivider));
		mmtfBean.setzCoordList(EncoderUtils.encodeByteArr(FloatCodecs.INT_DELTA_RECURSIVE,structureDataInterface.getzCoords(),coordDivider));
		mmtfBean.setbFactorList(EncoderUtils.encodeByteArr(FloatCodecs.INT_DELTA_RECURSIVE,structureDataInterface.getbFactors(),bfactorOccDivider));
		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(EncoderUtils.encodeByteArr(FloatCodecs.INT_RUNLENGTH,structureDataInterface.getOccupancies(),bfactorOccDivider));
		// Run length and delta
		mmtfBean.setAtomIdList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getAtomIds(),EncoderUtils.NULL_PARAM));
		// Run length encoded
		mmtfBean.setAltLocList(EncoderUtils.encodeByteArr(CharCodecs.RUN_LENGTH,structureDataInterface.getAltLocIds(),EncoderUtils.NULL_PARAM));
		mmtfBean.setInsCodeList(EncoderUtils.encodeByteArr(CharCodecs.RUN_LENGTH,structureDataInterface.getInsCodes(),EncoderUtils.NULL_PARAM));
		// Set the groupNumber
		mmtfBean.setGroupIdList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getGroupIds(),EncoderUtils.NULL_PARAM));
		mmtfBean.setSequenceIndexList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getGroupSequenceIndices(),EncoderUtils.NULL_PARAM));
		// Set the indices for the groups mapping to the sequence
		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(EncoderUtils.encodeByteArr(StringCodecs.ENCOODE_CHAINS,structureDataInterface.getChainNames(),MmtfStructure.CHAIN_LENGTH));
		mmtfBean.setChainIdList(EncoderUtils.encodeByteArr(StringCodecs.ENCOODE_CHAINS,structureDataInterface.getChainIds(),MmtfStructure.CHAIN_LENGTH));
		// Four bytes
		mmtfBean.setBondAtomList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_4_BYTE,structureDataInterface.getInterGroupBondIndices(),EncoderUtils.NULL_PARAM));
		// Set the group types
		mmtfBean.setGroupTypeList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_4_BYTE,structureDataInterface.getGroupTypeIndices(),EncoderUtils.NULL_PARAM));
		// Single bytes
		mmtfBean.setSecStructList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_BYTE,structureDataInterface.getSecStructList(),EncoderUtils.NULL_PARAM));
		mmtfBean.setBondOrderList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_BYTE,structureDataInterface.getInterGroupBondOrders(),EncoderUtils.NULL_PARAM));

		// Slightly unusual thing
		// Set the group map (all the unique groups in the structure).
		mmtfBean.setGroupList(EncoderUtils.generateGroupMap(structureDataInterface));
		// Set the bioassembly and entity information
		mmtfBean.setBioAssemblyList(EncoderUtils.generateBioassemblies(structureDataInterface));
		mmtfBean.setEntityList(EncoderUtils.generateEntityList(structureDataInterface));
		
		// No need for encoding
		// Set the number of chains per model
		mmtfBean.setChainsPerModel(structureDataInterface.getChainsPerModel());
		mmtfBean.setGroupsPerChain(structureDataInterface.getGroupsPerChain());
		// Set the space group information
		mmtfBean.setSpaceGroup(structureDataInterface.getSpaceGroup());
		mmtfBean.setUnitCell(structureDataInterface.getUnitCell());
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
		mmtfBean.setNcsOperatorList(structureDataInterface.getNcsOperatorList());		
	}

	@Override
	public MmtfStructure getMmtfEncodedStructure() {
		return mmtfBean;
	}



}
