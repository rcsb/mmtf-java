//package org.rcsb.mmtf.dataholders;
// TODO FIX THESE TESTS
//
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//import java.io.IOException;
//
//import org.junit.Test;
//import org.msgpack.jackson.dataformat.MessagePackFactory;
//import org.unitils.reflectionassert.ReflectionAssert;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import uk.co.jemos.podam.api.PodamFactory;
//
//public class TestDataHolders {
//
//	private PodamFactory factory;
//
//	public TestDataHolders() {
//
//	}
//
//	@Test
//	public void testSerializable() throws JsonParseException, JsonMappingException, IOException {
//		// MmtfBean
//		assertTrue(testClass(MmtfBean.class));
//		// This one fails - make sure it still does
//		assertFalse(testClass(BioAssemblyData.class));
//		// Now test round tripping data
//		testDataRoundTrip(MmtfBean.class);
//		// Now check that the failure bean fails this
//		// Now test round tripping data
//		assertFalse(testDataRoundTrip(FailureBean.class));
//	}
//
//	@SuppressWarnings("unchecked")
//	private boolean testClass(@SuppressWarnings("rawtypes") Class class1) throws IOException {
//
//
//		Object inBean = null;
//		try {
//			inBean = class1.newInstance();
//		} catch (InstantiationException | IllegalAccessException e2) {
//			// Weirdness
//			org.junit.Assert.fail("Weirdness in generating instance of generic class");
//		}
//		byte[] outArr = null;
//
//		outArr = encoderUtils.getMessagePack(inBean);
//
//		// 
//		Object  outBean = null;
//		try {
//			outBean = new ObjectMapper(new  MessagePackFactory()).readValue(outArr, class1);
//		} catch( JsonMappingException jsonE){
//			System.out.println("Error reading messagepack - is part of test if test doesn't fail");
//			return false;
//		}
//
//		// Now check they're the same
//		ReflectionAssert.assertReflectionEquals(inBean, outBean); 
//		return true;
//	}
//
//	/**
//	 * Test round tripping dummy data
//	 * @param class1
//	 */
//	private  boolean testDataRoundTrip(@SuppressWarnings("rawtypes") Class class1) throws JsonParseException, JsonMappingException, IOException {
//		Object inBean = factory.manufacturePojo(class1);
//		byte[] outArr = null;
//
//		outArr = encoderUtils.getMessagePack(inBean);
//
//
//		// 
//		Object  outBean = null;
//		outBean = new ObjectMapper(new  MessagePackFactory()).readValue(outArr, class1);
//
//		// Make the failure bean fail
//		try{
//			ReflectionAssert.assertPropertyReflectionEquals("fieldWithNoGetters",null, outBean);
//			ReflectionAssert.assertPropertyReflectionEquals("fieldWithRefactoredGetters",null, outBean);
//			return false;
//		}
//		catch(Exception e){
//
//		}
//		// Make sure all fields are re-populated
//		ReflectionAssert.assertPropertiesNotNull("Some properties are null in re-read object", outBean);
//
//		// Now check they're the same
//		ReflectionAssert.assertReflectionEquals(inBean, outBean); 
//		return true;
//	} 
//}
//
//
//
