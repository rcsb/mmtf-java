package org.rcsb.mmtf.postupdatetests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.mappers.ByteArrayToBioJavaStructMapper;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;


public class CheckHadoopFile  implements Serializable {    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3037567648753603114L;
	//	private static final Logger logger = LoggerFactory.getLogger(SparkRead.class);
	public static void main(String[] args ) throws IOException
	{
		String inPath = "/home/ubuntu/data/Total.hadoop.maindata.bzip2";
		String outPath = "/home/ubuntu/data/Total.hadoop.maindata.tested.bzip2";
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(CheckHadoopFile.class.getSimpleName());
		// Set the config for the spark context
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaPairRDD<String, Structure> jprdd = sc
				.sequenceFile(inPath, Text.class, BytesWritable.class, 24)
				.mapToPair(new ByteWriteToByteArr())
				// Now get the structure
				.mapToPair(new ByteArrayToBioJavaStructMapper());
		JavaRDD<String> values = jprdd.keys();
		List<String> outValues = values.collect();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("/home/ubuntu/data/outputs.txt")));
		writer.write(outValues.size());
		writer.close();
		// Now move the folder 
		Files.move(Paths.get(inPath), Paths.get(outPath));	
		sc.close();
	}
}