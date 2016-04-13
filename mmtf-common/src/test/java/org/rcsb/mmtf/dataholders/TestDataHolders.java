package org.rcsb.mmtf.dataholders;


import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestDataHolders {
	
	@Test
	public void testBeans(){
		PodamFactory factory = new PodamFactoryImpl();
		// Tests if setters are set appropriately
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(MmtfBean.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyData.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(BioAssemblyTrans.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(Entity.class));
		ReflectionAssert.assertPropertiesNotNull("Some properties null.", 
				factory.manufacturePojo(PDBGroup.class));
	}
}
