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

public class TestDataHolders {
	
	@Test
	public void testBeans() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		PodamFactory factory = new PodamFactoryImpl();
		// Tests if setters are set appropriately
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(MmtfBean.class));
		testData(MmtfBean.class, factory.manufacturePojo(MmtfBean.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyData.class));
		testData(BioAssemblyData.class, factory.manufacturePojo(BioAssemblyData.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyTrans.class));
		testData(BioAssemblyTrans.class, factory.manufacturePojo(BioAssemblyTrans.class));

		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(Entity.class));
		testData(Entity.class, factory.manufacturePojo(Entity.class));
		
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(PDBGroup.class));
		testData(PDBGroup.class, factory.manufacturePojo(PDBGroup.class));

	}
	
	
	private void testData(@SuppressWarnings("rawtypes") Class beanClass, Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
		for(PropertyDescriptor propertyDescriptor : 
		    Introspector.getBeanInfo(beanClass).getPropertyDescriptors()){
			assertNotNull(propertyDescriptor.getReadMethod().invoke(object));
		}
	}


}
