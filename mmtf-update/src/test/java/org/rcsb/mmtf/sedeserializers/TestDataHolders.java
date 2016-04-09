package org.rcsb.mmtf.sedeserializers;
// TODO FIX THESE TESTS

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.deserializers.MessagePackDeserializer;
import org.rcsb.mmtf.serializers.MessagePackSerializer;
import org.unitils.reflectionassert.ReflectionAssert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import uk.co.jemos.podam.api.PodamFactory;

public class TestDataHolders {

	private PodamFactory factory;

	public TestDataHolders() {

	}

	@Test
	public void testSerializable() throws JsonParseException, JsonMappingException, IOException {
		// Now test the data round tripping 
		testDataRoundTrip(MmtfBean.class);
		// Now check that the failure bean fails this
		// Now test round tripping data
		assertFalse(testDataRoundTrip(FailureBean.class));
	}

	/**
	 * Test round tripping dummy data
	 * @param class1
	 */
	private  boolean testDataRoundTrip(@SuppressWarnings("rawtypes") Class class1) throws JsonParseException, JsonMappingException, IOException {
		@SuppressWarnings("unchecked")
		Object inBean = factory.manufacturePojo(class1);
		MessagePackSerializer messagePackSerializer = new MessagePackSerializer();
		byte[] outArr = messagePackSerializer.serialize(inBean);
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		Object outBean = messagePackDeserializer.deserialize(outArr);
		// Make the failure bean fail
		try{
			ReflectionAssert.assertPropertyReflectionEquals("fieldWithNoGetters",null, outBean);
			ReflectionAssert.assertPropertyReflectionEquals("fieldWithRefactoredGetters",null, outBean);
			return false;
		}
		catch(Exception e){

		}
		// Make sure all fields are re-populated
		ReflectionAssert.assertPropertiesNotNull("Some properties are null in re-read object", outBean);

		// Now check they're the same
		ReflectionAssert.assertReflectionEquals(inBean, outBean); 
		return true;
	} 
}



