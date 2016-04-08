package org.rcsb.mmtf.encoder;

import java.io.IOException;
import java.util.List;

import org.rcsb.mmtf.api.MmtfDecodedDataInterface;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.encoder.ArrayConverters;
import org.rcsb.mmtf.encoder.ArrayEncoders;

/**
 * Class to take a encode the DecodedDataInterface into the MmtfBean.
 * @author Anthony Bradley
 *
 */
public class GetToBean {

	private MmtfBean mmtfBean;

	public GetToBean(MmtfDecodedDataInterface mmtfDecodedDataInterface) throws IOException {
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


		// Run length decode the occupancy array
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
		mmtfBean.setAltLocList(ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(mmtfDecodedDataInterface.getAltLocIds())));
		mmtfBean.setInsCodeList(ArrayEncoders.runlengthEncode(
				ArrayConverters.convertCharToIntegers(mmtfDecodedDataInterface.getInsCodes())));

		// Get the groupNumber
		mmtfBean.setGroupIdList(ArrayConverters.convertIntegersToFourByte(
				mmtfDecodedDataInterface.getGroupTypeIndices()));

		// Get the group map (all the unique groups in the structure).
		mmtfBean.setGroupList(EncoderUtils.generateGroupMap(mmtfDecodedDataInterface));
		// Get the seqRes groups
		mmtfBean.setGroupIdList(ArrayConverters.convertIntegersToFourByte(
				ArrayEncoders.deltaEncode(
						ArrayEncoders.runlengthEncode(
								mmtfDecodedDataInterface.getGroupIds()))));
		// Get the number of chains per model
		mmtfBean.setChainsPerModel(mmtfDecodedDataInterface.getChainsPerModel());
		mmtfBean.setGroupsPerChain(mmtfDecodedDataInterface.getGroupsPerChain());
		// Get the internal and public facing chain ids
		mmtfBean.setChainNameList(ArrayConverters.encodeChainList(mmtfDecodedDataInterface.getChainNames()));
		mmtfBean.setChainIdList(ArrayConverters.encodeChainList(mmtfDecodedDataInterface.getChainIds()));

		mmtfBean.setSpaceGroup(mmtfDecodedDataInterface.getSpaceGroup());
		mmtfBean.setUnitCell(mmtfDecodedDataInterface.getUnitCell());
		
		mmtfBean.setBioAssemblyList(
				EncoderUtils.generateBioassemblies(mmtfDecodedDataInterface));
		mmtfBean.setEntityList(
				EncoderUtils.generateEntityList(mmtfDecodedDataInterface)
				);
		
		mmtfBean.setBondOrderList(ArrayConverters.convertIntegersToFourByte(
				mmtfDecodedDataInterface.getInterGroupBondOrders()));
		mmtfBean.setBondAtomList(ArrayConverters.convertIntegersToFourByte(
				mmtfDecodedDataInterface.getInterGroupBondIndices()));
		
		mmtfBean.setMmtfVersion(mmtfDecodedDataInterface.getMmtfVersion());
		mmtfBean.setMmtfProducer(mmtfDecodedDataInterface.getMmtfProducer());
		mmtfBean.setStructureId(mmtfDecodedDataInterface.getStructureId());
		// Now get the header data
		mmtfBean.setrFree(mmtfDecodedDataInterface.getRfree());
		// Optional fields
		mmtfBean.setrWork(mmtfDecodedDataInterface.getRwork());
		mmtfBean.setResolution(mmtfDecodedDataInterface.getResolution());
		mmtfBean.setTitle(mmtfDecodedDataInterface.getTitle());
		mmtfBean.setExperimentalMethods(mmtfDecodedDataInterface.getExperimentalMethods());
		// Now get the relase information
		mmtfBean.setDepositionDate(mmtfDecodedDataInterface.getDepositionDate());
	}

	public MmtfBean getMmtfBean() {
		return mmtfBean;
	}

}
