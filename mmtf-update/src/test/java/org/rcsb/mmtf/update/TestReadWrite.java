package org.rcsb.mmtf.update;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.junit.Test;
import org.rcsb.mmtf.integrationtest.IntegrationTestUtils;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;
import org.rcsb.mmtf.sparkexamples.SparkSDSCHashMapWriter;

import scala.Tuple2;
/**
 * Functions to test the weekly update read in and read out.
 * From very basic can we update the data.
 * Some integration testing on reading in and out structures.
 * Some consistency testing between updates.
 * To be run before a run to check we're not going to mess stuff up.
 * @author Anthony Bradley
 *
 */
public class TestReadWrite {
	
	// Set some data to remove
	private static final String[] OBSOLETE = new String[] {"1abc"};
	// Set some data to update
	private static final String[] MODIFIED = new String[] {"2abc"};
	private static final String[] RELOADED = new String[] {"3abc"};
	// Set some data to add
	private static final String[] ADDED = new String[] {"4abc"};
	
	
    IntegrationTestUtils integrationTestUtils;
	// App name
	private static final String APP_NAME = "thisAppName";
	
	public TestReadWrite() {
		integrationTestUtils = new IntegrationTestUtils();
	}
    
	@Test
	/**
	 * Can we write out then read back in a Hadoop sequence file of data.
	 * @throws IOException 
	 */
	public void writeReadFlatFile() throws IOException { 
		FileWriters fileWriters = new FileWriters();
		Map<String, byte[]> dataMap = getDummyData("abc");
		// First write this out
	    Path tmpDir = integrationTestUtils.returnTempDir();
	    String tmpPathString = tmpDir.toAbsolutePath().toString();
	    // Write out these flat files
		fileWriters.writeOutFlatFiles(dataMap, tmpPathString);
		// Check that they all exist
		for ( Entry<String, byte[]> currentSet : dataMap.entrySet()) { 
			// Now read in the byte array
			FileInputStream fileInput = new FileInputStream(tmpPathString+"/ab/"+currentSet.getKey());
			byte[] inputArr = IOUtils.toByteArray(fileInput);
			assertNotNull(inputArr);
			assertArrayEquals(currentSet.getValue(), inputArr);
		}
	}
	
	@Test
	/**
	 * Can we write out then read back in a Hadoop sequence file of data.
	 */
	public void writeReadHadoopFile() { 
		
		Map<String, byte[]> inputData = getDummyData("abc");
		// Get the spark context
		JavaSparkContext sparkContext = getSparkContext();
		JavaPairRDD<Text, BytesWritable> mainDataset = JavaPairRDD.fromJavaRDD(sparkContext.parallelize(convertToTuple(inputData))).
				mapToPair(new StringByteToTextByteWriter());
		Path tmpDir = integrationTestUtils.returnTempDir();
	    String tmpPathString = tmpDir.toAbsolutePath().toString();
		String outURI = tmpPathString+"/outData.bzip2";		
		mainDataset.saveAsHadoopFile(outURI, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		// Now read it in
		List<Tuple2<String, byte[]>> outPutData = sparkContext.sequenceFile(outURI, Text.class, BytesWritable.class, 24 * 3)
		.mapToPair(new ByteWriteToByteArr()).collect();
		// Now check we're the same
		for (Tuple2<String, byte[]> thisEntry : outPutData) {
			// Check the byte arrs are the same
			assertArrayEquals(inputData.get(thisEntry._1), thisEntry._2);
		}
		sparkContext.close();
	}
	

	
	@Test
	/**
	 * Can we update the Hadoop file reasonably
	 */
	public void updateHadoopFile() { 
		// Should add three files to the output, read it back in and have the set number
		JavaSparkContext sparkContext = getSparkContext();
		// 
		Map<String, byte[]> totalDataMap = new HashMap<>();
		
		Map<String, byte[]> inputData = getDummyData("abc");
		Path tmpDir = integrationTestUtils.returnTempDir();
	    String tmpPathString = tmpDir.toAbsolutePath().toString();
		String outURI = tmpPathString+"/outData.bzip2";		
		JavaPairRDD<Text, BytesWritable> mainDataset = JavaPairRDD.fromJavaRDD(sparkContext.parallelize(convertToTuple(inputData))).
				mapToPair(new StringByteToTextByteWriter());
		mainDataset.saveAsHadoopFile(outURI, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		// Now read this hadoop file and filter an element out
		List<String> dataToRemove = new ArrayList<>();
		dataToRemove.addAll(inputData.keySet());
		Collections.shuffle(dataToRemove);
		dataToRemove = dataToRemove.subList(0, 4);
		final List<String> thisDataToRemove = new ArrayList<>(dataToRemove);
		JavaPairRDD<Text, BytesWritable> filteredDataset = sparkContext
				.sequenceFile(outURI, Text.class, BytesWritable.class, 4 * 3)
				.mapToPair(new ByteWriteToByteArr())
				.filter(t ->  thisDataToRemove.contains(t._1)==false)
				.mapToPair(new StringByteToTextByteWriter());
		
		Map<String, byte[]> updateData = getDummyData("def");
		// Now generate the update data and convert
		JavaPairRDD<Text, BytesWritable> updateDataset = JavaPairRDD.fromJavaRDD(sparkContext.parallelize(convertToTuple(updateData))).
				mapToPair(new StringByteToTextByteWriter());
		// Now join them together
		filteredDataset.join(updateDataset);
		String updateURI = tmpPathString+"/updateData.bzip2";		
		filteredDataset.saveAsHadoopFile(updateURI, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		
		// Read it back in again
		List<Tuple2<String, byte[]>> totalData = sparkContext
				.sequenceFile(updateURI, Text.class, BytesWritable.class, 4 * 3)
				.mapToPair(new ByteWriteToByteArr()).collect();
		// GENEREATE THE TEST DATA
		for (Entry<String, byte[]> thisEntry : inputData.entrySet()) {
			if (thisDataToRemove.contains(thisEntry.getKey()) ){
				System.out.println(thisEntry.getKey());
				continue;
			}
			totalDataMap.put(thisEntry.getKey(),thisEntry.getValue());
		}
		for (Entry<String, byte[]> thisEntry : updateData.entrySet()) {
			if (thisDataToRemove.contains(thisEntry.getKey()) ){
				continue;
			}
			totalDataMap.put(thisEntry.getKey(),thisEntry.getValue());
		}
		// 
		for ( Tuple2<String, byte[]> dataEntry : totalData) {
			System.out.println(dataEntry._1);
			assertArrayEquals(totalDataMap.get(dataEntry._1), dataEntry._2);
			
		}
		
		
		
		// Close spark down
		sparkContext.close();

	}


	@Test
	/**
	 * Test that the function to join lists actually works
	 */
	public void testJoinLists() {
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		List<String> testedList = weeklyUpdate.joinLists(new String[] {"A"}, new String[] {"B"}, new String[] {"C"});
		List<String> testList = new ArrayList<String>();
		testList.add("A");
		testList.add("B");
		testList.add("C");
		assertEquals(testList.size(), testedList.size());
		assertArrayEquals(testedList.toArray(new String[3]), testList.toArray(new String[3]));
	}
	
	@Test
	/**
	 * Test the generation of added and removed lists in the weekly update.
	 * Possibly add some integration tests to check
	 */
	public void testGenLists() {
		WeeklyUpdateUtils weeklyUpdate = getLists();
		// Now get these and check they're the same as the should be
		List<String> addedList = weeklyUpdate.getAddedList();
		Collections.sort(addedList);
		assertArrayEquals(addedList.toArray( new String[addedList.size()]), new String[] {"2abc", "3abc", "4abc"});
		List<String> removedList = weeklyUpdate.getRemoveList();
		Collections.sort(removedList);
		assertArrayEquals(removedList.toArray( new String[removedList.size()]), new String[] {"1abc","2abc", "3abc"});
	}

	@Test
	/**
	 * Can we write and read the hashmaps
	 * @throws IOException 
	 */
	public void writeReadHashMaps() throws IOException {
		Map<String, byte[]> dataMap = getDummyData("abc");
	    Path tmpDir = integrationTestUtils.returnTempDir();
	    String tmpPathString = tmpDir.toAbsolutePath().toString();
		SparkSDSCHashMapWriter sparkSDSCHashMapWriter = new SparkSDSCHashMapWriter();
		// Write it out
		sparkSDSCHashMapWriter.writeHashMapToFile(dataMap, tmpPathString+"mainMap.map");
	}
	
	/**
	 * Find the dummy data of the PDB - as would be produced from Encoding
	 * @return
	 */
	private Map<String, byte[]> getDummyData(String suffix) {
		
		Map<String, byte[]> dataMap = new HashMap<>();
		// First build the datamap -> random strings and random bytes
		for (int i=0; i < 10; i++) {
			byte[] b = new byte[ThreadLocalRandom.current().nextInt(1024, 4048)];
			new Random().nextBytes(b);
			String dummyPdbCode = i+suffix;
			dataMap.put(dummyPdbCode, b);
		}
		return dataMap;
		
	}
	
	/**
	 * Convert a dictionary to a tuple for spark.
	 * Input is a string/bytearr and output is text byteswriteable
	 * @param dummyData The input map of dummy string value pairs
	 * @return The output list of Tuples
	 */
	private List<Tuple2<String, byte []>> convertToTuple(Map<String, byte[]> dummyData) {
		// Set the out list
		List<Tuple2<String, byte[]>> outList = new ArrayList<>();
		for ( Entry<String, byte[]> thisEntry : dummyData.entrySet() ) {
			outList.add(new Tuple2<String, byte []>(thisEntry.getKey(), thisEntry.getValue()));
		}
		return outList;
	}
	
	private JavaSparkContext getSparkContext() {
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(APP_NAME);
		// Set the config for the spark context
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		return sparkContext;
	}
	
	/**
	 * Get the lists indicate which items should be removed or added 
	 * for the weekly update.
	 * @return 
	 */
	private WeeklyUpdateUtils getLists() {
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		// Now get these lists
		weeklyUpdate.getUpdateLists(OBSOLETE, MODIFIED, RELOADED, ADDED);
		return weeklyUpdate;
	}

}
