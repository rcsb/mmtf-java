package org.rcsb.mmtf.dataholders;


import static org.junit.Assert.assertNotNull;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * A class to test the dataholders.
 * @author Anthony Bradley
 *
 */
public class TestDataHolders {
	
	/**
	 * Test the available beans
	 * @throws IllegalAccessException related to Jackson conversion
	 * @throws IllegalArgumentException related to Jackson conversion
	 * @throws InvocationTargetException related to Jackson conversion
	 * @throws IntrospectionException related to Jackson conversion
	 */
	@Test
	public void testBeans() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		PodamFactory factory = new PodamFactoryImpl();
		// Tests if setters are set appropriately
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(MmtfStructure.class));
		testData(MmtfStructure.class, factory.manufacturePojo(MmtfStructure.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyData.class));
		testData(BioAssemblyData.class, factory.manufacturePojo(BioAssemblyData.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyTransformation.class));
		testData(BioAssemblyTransformation.class, factory.manufacturePojo(BioAssemblyTransformation.class));

		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(Entity.class));
		testData(Entity.class, factory.manufacturePojo(Entity.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(Group.class));
		testData(Group.class, factory.manufacturePojo(Group.class));

	}
	
	
	private void testData(@SuppressWarnings("rawtypes") Class beanClass, Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		for(PropertyDescriptor propertyDescriptor : 
		    Introspector.getBeanInfo(beanClass).getPropertyDescriptors()){
			assertNotNull(propertyDescriptor.getReadMethod().invoke(object));
		}
	}


}
