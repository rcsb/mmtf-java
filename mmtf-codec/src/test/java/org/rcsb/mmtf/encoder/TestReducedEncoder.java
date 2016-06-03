package org.rcsb.mmtf.encoder;

import static org.junit.Assert.assertNotNull;

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
		ClassLoader classLoader = getClass().getClassLoader();
		Path inFile = Paths.get(classLoader.getResource("mmtf/4cup.mmtf").getFile());
		return new GenericDecoder(ReaderUtils.getDataFromFile(inFile));
	}

}
