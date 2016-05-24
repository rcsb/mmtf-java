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

	/**
	 * The constructor for the encoder.
	 * @param structureDataInterface the interface of data to be encoded.
	 */
	public GenericEncoder(StructureDataInterface structureDataInterface) {
		mmtfBean = new MmtfStructure();
		// Delta split three and two
		mmtfBean.setxCoords(EncoderUtils.encodeByteArr(FloatCodecs.DELTA_SPLIT_3,structureDataInterface.getxCoords()));
		mmtfBean.setyCoords(EncoderUtils.encodeByteArr(FloatCodecs.DELTA_SPLIT_3,structureDataInterface.getyCoords()));
		mmtfBean.setzCoords(EncoderUtils.encodeByteArr(FloatCodecs.DELTA_SPLIT_3,structureDataInterface.getzCoords()));
		mmtfBean.setbFactors(EncoderUtils.encodeByteArr(FloatCodecs.DELTA_SPLIT_2,structureDataInterface.getbFactors()));
		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(EncoderUtils.encodeByteArr(FloatCodecs.RUN_LENGTH_2,structureDataInterface.getOccupancies()));
		// Run length and delta
		mmtfBean.setAtomIdList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getAtomIds()));
		// Run length encoded
		mmtfBean.setAltLocList(EncoderUtils.encodeByteArr(CharCodecs.RUN_LENGTH,structureDataInterface.getAltLocIds()));
		mmtfBean.setInsCodeList(EncoderUtils.encodeByteArr(CharCodecs.RUN_LENGTH,structureDataInterface.getInsCodes()));
		// Set the groupNumber
		mmtfBean.setGroupIdList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getGroupIds()));
		mmtfBean.setSequenceIndexList(EncoderUtils.encodeByteArr(IntCodecs.RUN_LENGTH_DELTA,structureDataInterface.getGroupSequenceIndices()));
		// Set the indices for the groups mapping to the sequence
		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(EncoderUtils.encodeByteArr(StringCodecs.ENCOODE_CHAINS,structureDataInterface.getChainNames()));
		mmtfBean.setChainIdList(EncoderUtils.encodeByteArr(StringCodecs.ENCOODE_CHAINS,structureDataInterface.getChainIds()));
		// Four bytes
		mmtfBean.setBondAtomList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_4_BYTE,structureDataInterface.getInterGroupBondIndices()));
		// Set the group types
		mmtfBean.setGroupTypeList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_4_BYTE,structureDataInterface.getGroupTypeIndices()));
		// Single bytes
		mmtfBean.setSecStructList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_BYTE,structureDataInterface.getSecStructList()));
		mmtfBean.setBondOrderList(EncoderUtils.encodeByteArr(IntCodecs.CONVERT_BYTE,structureDataInterface.getInterGroupBondOrders()));

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
		mmtfBean.setrFree(structureDataInterface.getRfree());
		mmtfBean.setrWork(structureDataInterface.getRwork());
		mmtfBean.setResolution(structureDataInterface.getResolution());
		mmtfBean.setTitle(structureDataInterface.getTitle());
		mmtfBean.setExperimentalMethods(structureDataInterface.getExperimentalMethods());
		mmtfBean.setDepositionDate(structureDataInterface.getDepositionDate());
		mmtfBean.setReleaseDate(structureDataInterface.getReleaseDate());
	}

	@Override
	public MmtfStructure getMmtfEncodedStructure() {
		return mmtfBean;
	}



}
