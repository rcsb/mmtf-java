package org.rcsb.mmtf.update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;

public class WeeklyUpdateUtils implements Serializable {

	private static final long serialVersionUID = 4909752674491325131L;
	private List<String> removeList;
	private List<String> addedList;

	/**
	 * Function to filter out elements from a hadoop seqeunce file
	 * @param sparkContext The spark context for reading the file
	 * @param fileUri The full path for the hadoop seqeunce file
	 * @return The RDD of the data after filtering
	 */
	public JavaPairRDD<Text, BytesWritable> filterElements(JavaSparkContext sparkContext, String fileUri) {
		return sparkContext.sequenceFile(fileUri, Text.class, BytesWritable.class, 12)
		.mapToPair(new ByteWriteToByteArr())
		.filter(t -> removeList.contains(t._1)==false)
		.mapToPair(new StringByteToTextByteWriter());
	}

	/**
	 * Join to RDDs together into one larger one
	 * @param outUri
	 * @param datasetOne
	 * @param datasetTwo
	 */
	public void joinDataSet(String outUri, JavaPairRDD<Text, BytesWritable> datasetOne, JavaPairRDD<Text, BytesWritable> datasetTwo) {
		// Now join them together
		datasetOne.join(datasetTwo);
		// Now save this as a Hadoop sequence file
		datasetOne.saveAsHadoopFile(outUri, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);		
	}

	/**
	 * Get the update lists from the FTP server. Join them into remove lists and added lists.
	 * Updated items are in both lists.
	 */
	public void getUpdateLists(String[] obsolete, String[] modified, String[] reloaded, String[] added) {
		// Now make the added and removed list
		removeList = joinLists(reloaded,modified,obsolete);
		setAddedList(joinLists(added,reloaded,modified));		
	}
	
	
	/**
	 * Retrieve the data from the FTP site and populate the added and remove lists.
	 */
	public void getDataFromFtpSite(String inputUrl){
		// Get the class of functions here
		PullFtpData pullFtpData = new PullFtpData(inputUrl);
		// **** DO THIS
		String[] obsolete = pullFtpData.getObsoleteUpdate();
		String[] modified = pullFtpData.getModifiedUpdate();
		String[] reloaded = pullFtpData.getReloadedUpdate();
		String[] added = pullFtpData.getAdded();
		// Now get these lists from the other lists
		getUpdateLists(obsolete, modified, reloaded, added);
	}

	/**
	 * Joins a series of three lists into a single lists.
	 * @param listOne
	 * @param listTwo
	 * @param listThree
	 * @return
	 */
	public List<String> joinLists(String[] listOne, String[] listTwo, String[] listThree) {
		List<String> strings = new ArrayList<String>(Arrays.asList(listOne));
		for(String item: listTwo){
			strings.add(item);
		}
		for(String item: listThree){
			strings.add(item);
		}
		return strings;
	}

	/**
	 * The getters and setters
	 */
	
	
	public List<String> getAddedList() {
		return addedList;
	}

	public void setAddedList(List<String> addedList) {
		this.addedList = addedList;
	}


	public List<String> getRemoveList() {
		return removeList;
	}
}
