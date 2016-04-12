package org.rcsb.mmtf.encoder;

import java.io.IOException;
import java.util.List;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;
import org.rcsb.mmtf.gitversion.GetRepoState;

/**
 * Class to take a encode the DecodedDataInterface into the MmtfBean.
 * @author Anthony Bradley
 *
 */
public class DataApiToBean {

	private MmtfBean mmtfBean;

	public DataApiToBean(MmtfDecodedDataInterface mmtfDecodedDataInterface) throws IOException {
		mmtfBean = new MmtfBean();
		// Set the group types
		mmtfBean.setGroupTypeList(
				ArrayConverters.convertIntegersToFourByte(
						mmtfDecodedDataInterface.getGroupTypeIndices()));
		// Encode the coordinate  and B-factor arrays.
		List<int[]> xCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								mmtfDecodedDataInterface.getxCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setxCoordBig(ArrayConverters.convertIntegersToFourByte(xCoords.get(0)));
		mmtfBean.setxCoordSmall(ArrayConverters.convertIntegersToTwoBytes(xCoords.get(1)));

		List<int[]> yCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								mmtfDecodedDataInterface.getyCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setyCoordBig(ArrayConverters.convertIntegersToFourByte(yCoords.get(0)));
		mmtfBean.setyCoordSmall(ArrayConverters.convertIntegersToTwoBytes(yCoords.get(1)));

		List<int[]> zCoords = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								mmtfDecodedDataInterface.getzCoords(),
								MmtfBean.COORD_DIVIDER)));
		mmtfBean.setzCoordBig(ArrayConverters.convertIntegersToFourByte(zCoords.get(0)));
		mmtfBean.setzCoordSmall(ArrayConverters.convertIntegersToTwoBytes(zCoords.get(1)));


		List<int[]> bFactor = ArrayConverters.splitIntegers(
				ArrayEncoders.deltaEncode(
						ArrayConverters.convertFloatsToInts(
								mmtfDecodedDataInterface.getbFactors(),
								MmtfBean.OCCUPANCY_BFACTOR_DIVIDER)));
		mmtfBean.setbFactorBig(ArrayConverters.convertIntegersToFourByte(bFactor.get(0)));
		mmtfBean.setbFactorSmall(ArrayConverters.convertIntegersToTwoBytes(bFactor.get(1)));


		// Run length encode the occupancy array
		mmtfBean.setOccupancyList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayConverters.convertFloatsToInts(
								mmtfDecodedDataInterface.getOccupancies(),
								MmtfBean.OCCUPANCY_BFACTOR_DIVIDER))));

		// Run length and delta
		mmtfBean.setAtomIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(mmtfDecodedDataInterface.getAtomIds()))));
		// Run length encoded
		mmtfBean.setAltLocList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(
						mmtfDecodedDataInterface.getAltLocIds()))));
		mmtfBean.setInsCodeList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(
						mmtfDecodedDataInterface.getInsCodes()))));

		// Set the groupNumber
		mmtfBean.setGroupIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.runlengthEncode(
						ArrayEncoders.deltaEncode(
								mmtfDecodedDataInterface.getGroupIds()))));

		// Set the group map (all the unique groups in the structure).
		mmtfBean.setGroupList(EncoderUtils.generateGroupMap(mmtfDecodedDataInterface));
		// Set the indices for the groups mapping to the sequence
		mmtfBean.setSequenceIndexList(ArrayConverters.convertIntegersToFourByte(
				mmtfDecodedDataInterface.getGroupSequenceIndices()));
		// Set the number of chains per model
		mmtfBean.setChainsPerModel(mmtfDecodedDataInterface.getChainsPerModel());
		mmtfBean.setGroupsPerChain(mmtfDecodedDataInterface.getGroupsPerChain());
		// Set the internal and public facing chain ids
		mmtfBean.setChainNameList(ArrayConverters.encodeChainList(mmtfDecodedDataInterface.getChainNames()));
		mmtfBean.setChainIdList(ArrayConverters.encodeChainList(mmtfDecodedDataInterface.getChainIds()));
		// Set the space group information
		mmtfBean.setSpaceGroup(mmtfDecodedDataInterface.getSpaceGroup());
		mmtfBean.setUnitCell(mmtfDecodedDataInterface.getUnitCell());
		// Set the bioassembly and entity information
		mmtfBean.setBioAssemblyList(
				EncoderUtils.generateBioassemblies(mmtfDecodedDataInterface));
		mmtfBean.setEntityList(
				EncoderUtils.generateEntityList(mmtfDecodedDataInterface)
				);
		// Set the bond orders and indcices
		mmtfBean.setBondOrderList(ArrayConverters.convertIntegersToBytes(
				mmtfDecodedDataInterface.getInterGroupBondOrders()));
		mmtfBean.setBondAtomList(ArrayConverters.convertIntegersToFourByte(
				mmtfDecodedDataInterface.getInterGroupBondIndices()));
		// Set the version and producer information
		mmtfBean.setMmtfProducer("RCSB-PDB Generator---version: "+GetRepoState.getCurrentVersion());
		mmtfBean.setStructureId(mmtfDecodedDataInterface.getStructureId());
		// Set some header data
		mmtfBean.setNumAtoms(mmtfDecodedDataInterface.getNumAtoms());
		mmtfBean.setNumBonds(mmtfDecodedDataInterface.getNumBonds());
		mmtfBean.setrFree(mmtfDecodedDataInterface.getRfree());
		mmtfBean.setrWork(mmtfDecodedDataInterface.getRwork());
		mmtfBean.setResolution(mmtfDecodedDataInterface.getResolution());
		mmtfBean.setTitle(mmtfDecodedDataInterface.getTitle());
		mmtfBean.setExperimentalMethods(mmtfDecodedDataInterface.getExperimentalMethods());
		mmtfBean.setDepositionDate(mmtfDecodedDataInterface.getDepositionDate());
		mmtfBean.setReleaseDate(mmtfDecodedDataInterface.getReleaseDate());
		mmtfBean.setSecStructList(ArrayConverters.convertIntegersToFourByte(mmtfDecodedDataInterface.getSecStructList()));
	}

	public MmtfBean getMmtfBean() {
		return mmtfBean;
	}

}
