package org.rcsb.mmtf.update;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rcsb.mmtf.mappers.MapperUtils;

public class WeeklyUpdateRun {

	/**
	 * First argument is the path in the file system
	 * Second argument is the URL to the FTP site
	 * Third argument is the URL for the MMCifdata
	 * @param args
	 */
	public static void main(String args[]) {
		
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		MapperUtils mapperUtils = new MapperUtils();
		
		String fileName = args[0];
		String ftpSiteUrl = args[1];
		String mmcifDataUrl = args[2];
		
		// Call this rsync function
		// Get the ones that need updating - first argument is the url to look at.
		weeklyUpdate.getDataFromFtpSite(ftpSiteUrl);
		// TODO Check if there is actually any work to do.
		// Go through the current list
		// The path of the hadoop file
		// Specify the path of the input file
		String inputUri = fileName;
		String joinedUri = fileName+"updated";
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(WeeklyUpdateRun.class.getSimpleName());
		// Set the config
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		// Read in and filter - these three things need unit tests
		JavaPairRDD<Text, BytesWritable> totalDataset = weeklyUpdate.filterElements(sparkContext, inputUri);
		List<String> urlPdbList = new ArrayList<>();
		for (String pdbId : weeklyUpdate.getAddedList()) {
			urlPdbList.add(mmcifDataUrl+"/"+pdbId+".cif.gz");
		}
		JavaPairRDD<Text, BytesWritable> distData = mapperUtils.generateRDD(sparkContext, urlPdbList);
		// Now join them
		weeklyUpdate.joinDataSet(joinedUri, totalDataset, distData);
		sparkContext.close();
	}

}
