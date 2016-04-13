package org.rcsb.mmtf.encoder;

import java.io.IOException;
import java.util.List;

import org.rcsb.mmtf.api.DecodedDataInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;
import org.rcsb.mmtf.gitversion.GetRepoState;

/**
 * Default encoding class from a DecodedDataInterface.
 * @author Anthony Bradley
 *
 */
public class DefaultEncoder {

	private MmtfBean mmtfBean;

	public DefaultEncoder(DecodedDataInterface decodedDataInterface) throws IOException {
		mmtfBean = new MmtfBean();
		// Set the group types
		mmtfBean.setGroupTypeList(
				ArrayConverters.convertIntegersToFourByte(
						decodedDataInterface.getGroupTypeIndices()));
		// Encode the coordinate  and B-factor arrays.
		List<int[]> xCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								decodedDataInterface.getxCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setxCoordBig(ArrayConverters.convertIntegersToFourByte(xCoords.get(0)));
		mmtfBean.setxCoordSmall(ArrayConverters.convertIntegersToTwoBytes(xCoords.get(1)));

		List<int[]> yCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								decodedDataInterface.getyCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setyCoordBig(ArrayConverters.convertIntegersToFourByte(yCoords.get(0)));
		mmtfBean.setyCoordSmall(ArrayConverters.convertIntegersToTwoBytes(yCoords.get(1)));

		List<int[]> zCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								decodedDataInterface.getzCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setzCoordBig(ArrayConverters.convertIntegersToFourByte(zCoords.get(0)));
		mmtfBean.setzCoordSmall(ArrayConverters.convertIntegersToTwoBytes(zCoords.get(1)));


		List<int[]> bFactor = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								decodedDataInterface.getbFactors(),
								MmtfBean.OCCUPANCY_BFACTOR_DIVIDER)));
		mmtfBean.setbFactorBig(ArrayConverters.convertIntegersToFourByte(bFactor.get(0)));
		mmtfBean.setbFactorSmall(ArrayConverters.convertIntegersToTwoBytes(bFactor.get(1)));


		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertFloatsToInts(
								decodedDataInterface.getOccupancies(),
								MmtfBean.OCCUPANCY_BFACTOR_DIVIDER))));

		// Run length and delta
		mmtfBean.setAtomIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(decodedDataInterface.getAtomIds()))));
		// Run length encoded
		mmtfBean.setAltLocList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(
						decodedDataInterface.getAltLocIds()))));
		mmtfBean.setInsCodeList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(
						decodedDataInterface.getInsCodes()))));

		// Set the groupNumber
		mmtfBean.setGroupIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(
								decodedDataInterface.getGroupIds()))));

		// Set the group map (all the unique groups in the structure).
		mmtfBean.setGroupList(EncoderUtils.generateGroupMap(decodedDataInterface));
		// Set the indices for the groups mapping to the sequence
		mmtfBean.setSequenceIndexList(ArrayConverters.convertIntegersToFourByte(
				decodedDataInterface.getGroupSequenceIndices()));
		// Set the number of chains per model
		mmtfBean.setChainsPerModel(decodedDataInterface.getChainsPerModel());
		mmtfBean.setGroupsPerChain(decodedDataInterface.getGroupsPerChain());
		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(ArrayConverters.encodeChainList(decodedDataInterface.getChainNames()));
		mmtfBean.setChainIdList(ArrayConverters.encodeChainList(decodedDataInterface.getChainIds()));
		// Set the space group information
		mmtfBean.setSpaceGroup(decodedDataInterface.getSpaceGroup());
		mmtfBean.setUnitCell(decodedDataInterface.getUnitCell());
		// Set the bioassembly and entity information
		mmtfBean.setBioAssemblyList(
				EncoderUtils.generateBioassemblies(decodedDataInterface));
		mmtfBean.setEntityList(
				EncoderUtils.generateEntityList(decodedDataInterface)
				);
		// Set the bond orders and indcices
		mmtfBean.setBondOrderList(ArrayConverters.convertIntegersToBytes(
				decodedDataInterface.getInterGroupBondOrders()));
		mmtfBean.setBondAtomList(ArrayConverters.convertIntegersToFourByte(
				decodedDataInterface.getInterGroupBondIndices()));
		// Set the version and producer information
		mmtfBean.setMmtfProducer("RCSB-PDB Generator---version: "+GetRepoState.getCurrentVersion());
		mmtfBean.setStructureId(decodedDataInterface.getStructureId());
		// Set some header data
		mmtfBean.setNumAtoms(decodedDataInterface.getNumAtoms());
		mmtfBean.setNumBonds(decodedDataInterface.getNumBonds());
		mmtfBean.setrFree(decodedDataInterface.getRfree());
		mmtfBean.setrWork(decodedDataInterface.getRwork());
		mmtfBean.setResolution(decodedDataInterface.getResolution());
		mmtfBean.setTitle(decodedDataInterface.getTitle());
		mmtfBean.setExperimentalMethods(decodedDataInterface.getExperimentalMethods());
		mmtfBean.setDepositionDate(decodedDataInterface.getDepositionDate());
		mmtfBean.setReleaseDate(decodedDataInterface.getReleaseDate());
		mmtfBean.setSecStructList(ArrayConverters.convertIntegersToFourByte(decodedDataInterface.getSecStructList()));
	}

	public MmtfBean getMmtfBean() {
		return mmtfBean;
	}

}
