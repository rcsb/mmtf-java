	package org.rcsb.mmtf.update;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rcsb.mmtf.mappers.MapperUtils;

public class WeeklyUpdateRun {


	public static void main(String args[]) {
		
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		MapperUtils mapperUtils = new MapperUtils();
		// Call this rsync function
		// Get the ones that need updating
		weeklyUpdate.getDataFromFtpSite();
		// TODO Check if there is actually any work to do.
		
		// Go through the current list
		// The path of the hadoop file
		// Specify the path of the input file
		String uri = args[0];
		// The basic URI - to which we add  a timestamp
		String timeStamp = new SimpleDateFormat("yyyyMMddhhmm'.txt'").format(new Date());
		String joinedUri = args[1]+timeStamp;
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(WeeklyUpdateRun.class.getSimpleName());
		// Set the config
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		// Read in and filter - these three things need unit tests
		JavaPairRDD<Text, BytesWritable> totalDataset = weeklyUpdate.filterElements(sparkContext, uri);
		JavaPairRDD<Text, BytesWritable> distData = mapperUtils.generateRDD(sparkContext, weeklyUpdate.getAddedList());
		// Now join them
		weeklyUpdate.joinDataSet(joinedUri, totalDataset, distData);
		sparkContext.close();
	}

}
