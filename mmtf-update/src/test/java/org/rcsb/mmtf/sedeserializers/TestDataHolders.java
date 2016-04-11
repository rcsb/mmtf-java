package org.rcsb.mmtf.sedeserializers;
// TODO FIX THESE TESTS


import java.io.IOException;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.io.mmtf.MmtfActions;
import org.biojava.nbio.structure.io.mmtf.MmtfUtils;
import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.deserializers.MessagePackDeserializer;
import org.rcsb.mmtf.serializers.MessagePackSerializer;
import org.unitils.reflectionassert.ReflectionAssert;

public class TestDataHolders {


	public TestDataHolders() {

	}

	@Test
	public void testSerializable() throws IOException, StructureException {
		// Now test the data round tripping 
		testDeReSerialize();
	}

	
	/**
	 * Test round tripping data
	 * @return
	 * @throws IOException
	 * @throws StructureException
	 */
	private  boolean testDeReSerialize() throws IOException, StructureException {
		MmtfUtils.setUpBioJava();
		MmtfBean inBean = MmtfActions.getBean("4cup");
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		byte[] outArr = messagePackSerializer.serialize(inBean);
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		MmtfBean outBean = messagePackDeserializer.deserialize(outArr);
		// Make sure all fields are re-populated
		ReflectionAssert.assertPropertiesNotNull("Some properties are null in re-read object", outBean);
		// Now check they're the same
		ReflectionAssert.assertReflectionEquals(inBean, outBean); 
		return true;
	} 
}
