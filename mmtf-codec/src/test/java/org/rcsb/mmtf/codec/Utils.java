package org.rcsb.mmtf.codec;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rcsb.mmtf.api.StructureDataInterface;
import org.rcsb.mmtf.decoder.GenericDecoder;
import org.rcsb.mmtf.decoder.ReaderUtils;
import org.rcsb.mmtf.encoder.GenericEncoder;

/**
 * Utils for perfoming the tests.
 * @author Anthony Bradley
 *
 */
public class Utils {


	/**
	 * Compare the data after roundtripping {@link StructureDataInterface}s.
	 * @param structureDataInterface the {@link StructureDataInterface}
	 */
	public static void compare(StructureDataInterface structureDataInterface) {
		compareStructDataInfs(structureDataInterface, new GenericDecoder(new GenericEncoder(structureDataInterface).getMmtfEncodedStructure()));		
	}
	
	/**
	 * Compare the data in two {@link StructureDataInterface}s.
	 * @param interfaceOne the first {@link StructureDataInterface}
	 * @param interfaceTwo the second {@link StructureDataInterface}
	 */
	public static void compareStructDataInfs(StructureDataInterface interfaceOne, StructureDataInterface interfaceTwo) {

		// Check the non-array values
		try {
			for(PropertyDescriptor propertyDescriptor :
				Introspector.getBeanInfo(StructureDataInterface.class).getPropertyDescriptors()){
				if(propertyDescriptor.getReadMethod()!=null){
					if(propertyDescriptor.getReadMethod().invoke(interfaceOne).getClass().isArray()){
						
					}
					else{
						assertEquals(propertyDescriptor.getReadMethod().invoke(interfaceOne),propertyDescriptor.getReadMethod().invoke(interfaceTwo));
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			throw new RuntimeException();
		}
		// Now check the arrays
		assertArrayEquals(interfaceOne.getAtomIds(), interfaceTwo.getAtomIds());
		assertArrayEquals(interfaceOne.getxCoords(), interfaceTwo.getxCoords(),0.0009f);
		assertArrayEquals(interfaceOne.getyCoords(), interfaceTwo.getyCoords(),0.0009f);
		assertArrayEquals(interfaceOne.getzCoords(), interfaceTwo.getzCoords(),0.0009f);
		assertArrayEquals(interfaceOne.getbFactors(), interfaceTwo.getbFactors(), 0.009f);
		assertArrayEquals(interfaceOne.getOccupancies(), interfaceTwo.getOccupancies(), 0.009f);		
		assertArrayEquals(interfaceOne.getAltLocIds(), interfaceTwo.getAltLocIds());		
		assertArrayEquals(interfaceOne.getChainIds(), interfaceTwo.getChainIds());
		assertArrayEquals(interfaceOne.getSecStructList(), interfaceTwo.getSecStructList());
		assertArrayEquals(interfaceOne.getChainNames(), interfaceTwo.getChainNames());
		assertArrayEquals(interfaceOne.getExperimentalMethods(), interfaceTwo.getExperimentalMethods());
		assertArrayEquals(interfaceOne.getGroupIds(), interfaceTwo.getGroupIds());
		assertArrayEquals(interfaceOne.getGroupSequenceIndices(), interfaceTwo.getGroupSequenceIndices());
		assertArrayEquals(interfaceOne.getGroupsPerChain(), interfaceTwo.getGroupsPerChain());
		assertArrayEquals(interfaceOne.getGroupTypeIndices(), interfaceTwo.getGroupTypeIndices());
		assertArrayEquals(interfaceOne.getInsCodes(), interfaceTwo.getInsCodes());	
		assertArrayEquals(interfaceOne.getInterGroupBondIndices(), interfaceTwo.getInterGroupBondIndices());
		assertArrayEquals(interfaceOne.getInterGroupBondOrders(), interfaceTwo.getInterGroupBondOrders());
	}
	
	/**
	 * path = "/mmtf/4CUP.mmtf"
	 */
	public static Path getResource(String p) throws IOException {
		// in java 11+ we need to use a class within the module to locate the resource properly. See https://stackoverflow.com/questions/54021754/why-does-the-getresource-method-return-null-in-jdk-11
		File f = new File(Utils.class.getResource(p).getFile());
		return Paths.get(f.getAbsolutePath());
	}
}
