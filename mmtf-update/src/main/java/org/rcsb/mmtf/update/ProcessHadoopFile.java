package org.rcsb.mmtf.update;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;

import scala.Tuple2;

public class ProcessHadoopFile implements Serializable {

	
	private static final long serialVersionUID = 1L;

	/**
	 * First argument is the base path in the file system
	 * Second argument is the version of MMTF being used
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{

		String inputUri = args[0];
		String basePath = args[1];
		String outPutFile = args[2];
		

		// Helper classes for writing files
		WriteHashMap sparkHadoopHashMapWriter = new WriteHashMap();
		// The path of the hadoop file
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(ProcessHadoopFile.class.getSimpleName());
		conf.set("spark.driver.maxResultSize", "14g");
		// Set the config
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Read in with spark
		JavaPairRDD<String, byte[]> totalDataset = sc
				.sequenceFile(inputUri, Text.class, BytesWritable.class, 24 * 3)
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

		sparkHadoopHashMapWriter.writeHashMapToFile(headerMap.collectAsMap(), basePath+"headerMap.map");
		sparkHadoopHashMapWriter.writeHashMapToFile(calphaMap.collectAsMap(), basePath+"calphaMap.map");
		// Now do the main map
		//		 Now write this out as a hash map
		sparkHadoopHashMapWriter.writeHashMapToFile(mainMap.collectAsMap(), basePath+"mainMap.map");

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

		JavaPairRDD<Text, BytesWritable> mainDataset = mainMapNoGzip.mapToPair(new StringByteToTextByteWriter());
		String outURI = basePath+"hadoopFullData";		
		mainDataset.saveAsHadoopFile(outURI, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		sc.close();
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
		
	}


}


