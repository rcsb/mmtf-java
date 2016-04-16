package org.rcsb.mmtf.decoder;

import org.rcsb.mmtf.api.DecodedDataInterface;
import org.rcsb.mmtf.api.DataTransferInterface;

/**
 * A class of static functions to be used to aid decoding from MMTFBean to DecodedDataInterface.
 * @author Anthony Bradley
 *
 */
public class DecoderUtils {

	/**
	 * Parses the bioassembly data and inputs it to the structure inflator
	 * @param dataApi the interface to the decoded data
	 * @param structInflator the interface to put the data into the client object
	 */
	public static void generateBioAssembly(DecodedDataInterface dataApi, DataTransferInterface structInflator) {
		for (int i=0; i<dataApi.getNumBioassemblies(); i++) {
			for(int j=0; j<dataApi.getNumTransInBioassembly(i); j++) {
				structInflator.setBioAssemblyTrans(i+1, dataApi.getChainIndexListForTransform(i, j), dataApi.getMatrixForTransform(i,j));    
			}
		}
	}

	/**
	 * Generate inter group bonds.
	 * Bond indices are specified within the whole structure and start at 0.
	 * @param dataApi the interface to the decoded data
	 * @param structInflator the interface to put the data into the client object
	 */
	public static void addInterGroupBonds(DecodedDataInterface dataApi, DataTransferInterface structInflator) {
		for (int i = 0; i < dataApi.getInterGroupBondOrders().length; i++) {
			structInflator.setInterGroupBond(dataApi.getInterGroupBondIndices()[i * 2],
					dataApi.getInterGroupBondIndices()[i * 2 + 1], dataApi.getInterGroupBondOrders()[i]);
		} 		
	}

	/**
	 * Add ancilliary header information to the structure.
	 * @param dataApi the interface to the decoded data
	 * @param structInflator the interface to put the data into the client object
	 */
	public static void addHeaderInfo(DecodedDataInterface dataApi, DataTransferInterface structInflator) {
		structInflator.setHeaderInfo(dataApi.getRfree(),dataApi.getRwork(), dataApi.getResolution(), 
				dataApi.getTitle(), dataApi.getDepositionDate(), dataApi.getReleaseDate(), dataApi.getExperimentalMethods());		
	}

	
	/**
	 * Add the crystallographic data to the structure.
	 * @param dataApi the interface to the decoded data
	 * @param structInflator the interface to put the data into the client object
	 */
	public static void addXtalographicInfo(DecodedDataInterface dataApi, DataTransferInterface structInflator) {
		if(dataApi.getUnitCell()!=null){
			structInflator.setXtalInfo(dataApi.getSpaceGroup(), dataApi.getUnitCell());    
		}		
	}

	/**
	 * Add the entity info to the structure.
	 * @param dataApi the interface to the decoded data
	 * @param structInflator the interface to put the data into the client object
	 */
	public static void addEntityInfo(DecodedDataInterface dataApi, DataTransferInterface structInflator) {
		for (int i=0; i<dataApi.getNumEntities(); i++) {
			String[] chainIdList = new String[dataApi.getEntityChainIndexList(i).length];
			int counter = 0;
			for (int chainInd : dataApi.getEntityChainIndexList(i)) {
				chainIdList[counter] = dataApi.getChainIds()[chainInd];
				counter++;
			}
			structInflator.setEntityInfo(dataApi.getEntityChainIndexList(i), dataApi.getEntitySequence(i), dataApi.getEntityDescription(i), dataApi.getEntityType(i));
		}			
	}

}

