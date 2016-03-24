package org.rcsb.mmtf.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.Partition;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;

import scala.Tuple2;

public class ProcessHadoopFile implements Serializable {

	private static final String prefix = "/home/anthony/src/codec-devel/data/";	

	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {


		// Helper classes for writing files
		WriteHashMap sparkHadoopHashMapWriter = new WriteHashMap();
		// The path of the hadoop file
		String uri =  prefix+"Total.hadoop.update.bzip2";
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(ProcessHadoopFile.class.getSimpleName());
		conf.set("spark.driver.maxResultSize", "14g");
		// Set the config
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Read in with spark
		JavaPairRDD<String, byte[]> totalDataset = sc
				.sequenceFile(uri, Text.class, BytesWritable.class, 24 * 3)
				.mapToPair(new ByteWriteToByteArr());
		// GET THE TOTAL MAP
		JavaPairRDD<String, byte[]> mainMap = totalDataset.filter(t -> t._1.endsWith("_total"))
				.mapToPair(new RemoveSuffixAndGzip());

		// NOW GET THE HEADER MAP
		JavaPairRDD<String, byte[]> headerMap = totalDataset.filter(new Function<Tuple2<String,byte[]>, Boolean>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7574230201179927345L;

			@Override
			public Boolean call(Tuple2<String, byte[]> v1) throws Exception {
				if(v1._1.endsWith("_header")==true){
					return true;
				}
				return false;
			}
		}).mapToPair(new RemoveSuffixAndGzip());

		/// NOW GET THE CALPHA MAP
		JavaPairRDD<String, byte[]> calphaMap = totalDataset.filter(new Function<Tuple2<String,byte[]>, Boolean>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -8312184119385524L;

			@Override
			public Boolean call(Tuple2<String, byte[]> v1) throws Exception {
				if(v1._1.endsWith("_calpha")==true){
					return true;
				}
				return false;
			}
		}).mapToPair(new RemoveSuffixAndGzip());

		// Now write the hadoop sequence file as the whole pdb
		// Now collect these as maps

		sparkHadoopHashMapWriter.writeHashMapToFile(headerMap.collectAsMap(), prefix+"headerMap.map");
		sparkHadoopHashMapWriter.writeHashMapToFile(calphaMap.collectAsMap(), prefix+"calphaMap.map");
		// Now do the main map
		//		 Now write this out as a hash map
		sparkHadoopHashMapWriter.writeHashMapToFile(mainMap.collectAsMap(), prefix+"mainMap.map");

		List<Partition> parted = mainMap.partitions();
		for(int i=0; i<parted.size();i++){
			int[] thisArr = new int[1];
			thisArr[0] = i;
			List<Tuple2<String, byte[]>> ans = mainMap.collectPartitions(thisArr)[0];
			// Now 
			writeToFile(ans);
		}



		// Now get the total dataset - without gzip and write to a hadoop sequence file
		JavaPairRDD<String, byte[]> mainMapNoGzip = totalDataset.filter(new Function<Tuple2<String,byte[]>, Boolean>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -7172364344277495432L;

			@Override
			public Boolean call(Tuple2<String, byte[]> v1) throws Exception {
				if(v1._1.endsWith("_total")==true){
					return true;
				}
				return false;
			}
		}).mapToPair(new RemoveSuffix());

		//THIS SHOULD BE IT'S OWN FUNCTION ->>>> NONE-GZIP COMPRESSED DATA
		JavaPairRDD<Text, BytesWritable> mainDataset = mainMapNoGzip.mapToPair(new StringByteToTextByteWriter());
		String outURI = prefix+"Total.hadoop.maindata.bzip2";		
		mainDataset.saveAsHadoopFile(outURI, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		sc.close();
	}

	private static void writeToFile(List<Tuple2<String, byte[]>> ans) throws IOException {

		String basePath = prefix+"structures";
		for(Tuple2<String, byte[]> v1: ans){
			// Get the key value pairs
			String pdbCode = v1._1.toLowerCase();
			byte[] byteArr = v1._2;
			// Make the new dir 
			File theDir = new File(basePath+"/"+pdbCode.substring(1, 3));
			if(theDir.exists() == false){
				theDir.mkdirs();
			}
			// Write the file	
			FileOutputStream fos = null;
			// Try and except
			try{
				fos = new FileOutputStream(basePath+"/"+pdbCode.substring(1, 3)+"/"+pdbCode);
				fos.write(byteArr);
			}
			finally{
				fos.close();
			}
		}
	}

}


