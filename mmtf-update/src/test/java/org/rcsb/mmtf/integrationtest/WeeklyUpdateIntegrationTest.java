package org.rcsb.mmtf.integrationtest;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.api.java.JavaPairRDD;

/**
 * Tests to see if the weekly update process will work.
 * i.e. if we can take a file, update it with actual data and it provides the expected output.
 * Includes tests for Byte consistency - to ensure no changes in the Messagepack have occured.
 * @author Anthony Bradley
 *
 */
public class WeeklyUpdateIntegrationTest {
	
	/**
	 * Test (using some internal testing) that the data derived from the FTP site is correct
	 */
	private void testFtpListsCorrect() {
		// Fail because we haven't checked anything yet
		assertNotEquals(true, true);
	}
	
	
	/**
	 * Test to see that the data produced in the RDD is consistent week on week.
	 */
	private void testDataConsistency() {
		// GET THE DATA JavaPairRDD<Text, BytesWritable> distData = mapperUtils.generateRDD(sparkContext, weeklyUpdate.getAddedList());
		// CHECK IT'S THE SAME (byte for byte) Obviously will need updating from release to release.
	}
	
	/**
	 * 
	 */
	private void testDataUpdating() {
		// CHECK THAT READING IN, UPDATING ON SOME SPECIFIED ELEMENTS AND WRITING OUT GIVES THE SAME LOGIC
//		JavaPairRDD<Text, BytesWritable> totalDataset = weeklyUpdate.filterElements(sparkContext, uri);
//		JavaPairRDD<Text, BytesWritable> distData = mapperUtils.generateRDD(sparkContext, weeklyUpdate.getAddedList());
//		// Now join them
//		weeklyUpdate.joinDataSet(joinedUri, totalDataset, distData);
//		sparkContext.close();
	}
}
