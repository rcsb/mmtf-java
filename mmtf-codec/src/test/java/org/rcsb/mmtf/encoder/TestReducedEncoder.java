package org.rcsb.mmtf.encoder;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.codec.Utils;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;

/**
 * Class of tests for the Reduced encoder.
 * @author Anthony Bradley
 *
 */
public class TestReducedEncoder {
	
	
	/**
	 * Test that data can be reduced and read back in correctly.
	 * @throws IOException error reading the file from the resource
	 */
	@Test
	public void testReducedRoundTrip() throws IOException {
		StructureDataInterface structureDataInterface = ReducedEncoder.getReduced(getDefaultFullData());
		testStructDataInterface(structureDataInterface);
		Utils.compare(structureDataInterface);
		}

	/**
	 * Test that data can be converted to reduced and certain core data stays the same.
	 * @throws IOException error reading the file from the resource
	 */
	@Test
	public void testReducedData() throws IOException {
		StructureDataInterface full = getDefaultFullData();
		StructureDataInterface reduced = ReducedEncoder.getReduced(full);
		// Check that the chain names are the same
		assertArrayEquals(full.getChainNames(), reduced.getChainNames());
		assertArrayEquals(full.getChainIds(), reduced.getChainIds());
		// Check that all non water groups are included
		assertEquals(removeWaters(full), reduced.getGroupTypeIndices().length);
	}
	
	
	private int removeWaters(StructureDataInterface structureDataInterface) {
		int outCounter = 0;
		for (int groupType : structureDataInterface.getGroupTypeIndices()){
			if(structureDataInterface.getGroupName(groupType).equals("HOH")){
				continue;
			}
			outCounter++;
		}
		return outCounter;
	}

	/**
	 * Check that the data read in is not null.
	 * @param structDataInterface
	 */
	private void testStructDataInterface(StructureDataInterface structDataInterface) {
		try {
			for(PropertyDescriptor propertyDescriptor :
				Introspector.getBeanInfo(StructureDataInterface.class).getPropertyDescriptors()){
				if(propertyDescriptor.getReadMethod()!=null){
					if(propertyDescriptor.getReadMethod().invoke(structDataInterface).getClass().isArray()){
						
					}
					else{
						assertNotNull(propertyDescriptor.getReadMethod().invoke(structDataInterface));
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			throw new RuntimeException();
		}
		// Now check the arrays
		assertNotNull(structDataInterface.getAtomIds());
		assertNotNull(structDataInterface.getxCoords());
		assertNotNull(structDataInterface.getyCoords());
		assertNotNull(structDataInterface.getzCoords());
		assertNotNull(structDataInterface.getbFactors());
		assertNotNull(structDataInterface.getOccupancies());
		assertNotNull(structDataInterface.getAltLocIds());	
		assertNotNull(structDataInterface.getChainIds());
		assertNotNull(structDataInterface.getSecStructList());
		assertNotNull(structDataInterface.getChainNames());
		assertNotNull(structDataInterface.getExperimentalMethods());
		assertNotNull(structDataInterface.getGroupIds());
		assertNotNull(structDataInterface.getGroupSequenceIndices());
		assertNotNull(structDataInterface.getGroupsPerChain());
		assertNotNull(structDataInterface.getGroupTypeIndices());
		assertNotNull(structDataInterface.getInsCodes());
		assertNotNull(structDataInterface.getInterGroupBondIndices());
		assertNotNull(structDataInterface.getInterGroupBondOrders());
	}


	/**
	 * Get the default data for the full format.
	 * @return a {@link StructureDataInterface} for the full data.
	 * @throws IOException
	 */
	private StructureDataInterface getDefaultFullData() throws IOException {
		Path p = Utils.getResource("/mmtf/4cup.mmtf");
		return new GenericDecoder(ReaderUtils.getDataFromFile(p));
	}
}
