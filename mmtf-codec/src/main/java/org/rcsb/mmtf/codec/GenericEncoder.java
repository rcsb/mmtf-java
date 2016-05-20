package org.rcsb.mmtf.codec;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.encoder.EncoderUtils;

/**
 * An encoder for encoding with the generic strategy.
 * @author Anthony Bradley
 *
 */
public class GenericEncoder {
	
	
	private MmtfStructure mmtfBean;

	/**
	 * The constructor for the encoder.
	 * @param structureDataInterface the interface of data to be encoded.
	 */
	public GenericEncoder(StructureDataInterface structureDataInterface) {
		mmtfBean = new MmtfStructure();
		// Delta split three and two
		mmtfBean.setxCoords(FloatCodecs.DELTA_SPLIT_3.encode(structureDataInterface.getxCoords()));
		mmtfBean.setyCoords(FloatCodecs.DELTA_SPLIT_3.encode(structureDataInterface.getyCoords()));
		mmtfBean.setzCoords(FloatCodecs.DELTA_SPLIT_3.encode(structureDataInterface.getzCoords()));
		mmtfBean.setbFactors(FloatCodecs.DELTA_SPLIT_2.encode(structureDataInterface.getbFactors()));
		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(FloatCodecs.RUN_LENGTH_2.encode(structureDataInterface.getOccupancies()));
		// Run length and delta
		mmtfBean.setAtomIdList(IntCodecs.RUN_LENGTH_DELTA.encode(structureDataInterface.getAtomIds()));
		// Run length encoded
		mmtfBean.setAltLocList(CharCodecs.RUN_LENGTH.encode(structureDataInterface.getAltLocIds()));
		mmtfBean.setInsCodeList(CharCodecs.RUN_LENGTH.encode(structureDataInterface.getInsCodes()));
		// Set the groupNumber
		mmtfBean.setGroupIdList(IntCodecs.RUN_LENGTH_DELTA.encode(structureDataInterface.getGroupIds()));
		mmtfBean.setSequenceIndexList(IntCodecs.RUN_LENGTH_DELTA.encode(structureDataInterface.getGroupSequenceIndices()));
		

		// Set the indices for the groups mapping to the sequence

		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(StringCodecs.ENCOODE_CHAINS.encode(structureDataInterface.getChainNames()));
		mmtfBean.setChainIdList(StringCodecs.ENCOODE_CHAINS.encode(structureDataInterface.getChainIds()));
		
		// Four bytes
		mmtfBean.setBondAtomList(IntCodecs.CONVERT_4_BYTE.encode(structureDataInterface.getInterGroupBondIndices()));
		// Set the group types
		mmtfBean.setGroupTypeList(IntCodecs.CONVERT_4_BYTE.encode(structureDataInterface.getGroupTypeIndices()));
		
		// Single bytes
		mmtfBean.setSecStructList(IntCodecs.CONVERT_BYTE.encode(structureDataInterface.getSecStructList()));
		mmtfBean.setBondOrderList(IntCodecs.CONVERT_BYTE.encode(structureDataInterface.getInterGroupBondOrders()));

		
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



	/**
	 * Get the MmtfBean of encoded data.
	 * @return the encoded data as an MmtfBean
	 */
	public MmtfStructure getMmtfEncodedStructure() {
		return mmtfBean;
	}
}
