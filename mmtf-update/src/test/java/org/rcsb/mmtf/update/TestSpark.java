package org.rcsb.mmtf.update;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests to see if Sparks can be launched succesfully.
 * @author Anthony Bradley
 *
 */
public class TestSpark {

	JavaSparkContext sc;
	// App name
	private static final String APP_NAME = "thisAppName";
	
	public TestSpark() {
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(APP_NAME);
		// Set the config for the spark context
		sc = new JavaSparkContext(conf);
	}
	
	@Test
	/**
	 * Basic test that we can even get spark running
	 */
	public void testSparkContextSetup() {
		assertEquals(sc.appName(), APP_NAME);
		sc.close();
		sc.stop();
	}
}
