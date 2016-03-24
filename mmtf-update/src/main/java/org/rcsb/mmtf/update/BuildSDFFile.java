package org.rcsb.mmtf.update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.rcsb.mmtf.filters.IsLigand;
import org.rcsb.mmtf.mappers.ByteArrayToBioJavaStructMapper;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.GroupToSDF;
import org.rcsb.mmtf.mappers.StructToPDBGroup;
import org.rcsb.mmtf.sparkexamples.SparkServerRead;

public class BuildSDFFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3141464711627304724L;

	//	private static final Logger logger = LoggerFactory.getLogger(SparkRead.class);
	public static void main(String[] args ) throws IOException
	{
		String path = "/home/ubuntu/data/Total.hadoop.maindata.tested.bzip2";
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(SparkServerRead.class.getSimpleName());
		// Set the config
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Time the proccess
		long start = System.nanoTime();
		JavaPairRDD<String,String> jprdd = sc
				// Read the file
				.sequenceFile(path, Text.class, BytesWritable.class, 12)
				// Now get the structure
				.mapToPair(new ByteWriteToByteArr())
				.mapToPair(new ByteArrayToBioJavaStructMapper())
				.flatMapToPair(new StructToPDBGroup())
				.filter(new IsLigand())
				.mapToPair(new GroupToSDF());
		
		File sdfFile = new File("/home/ubuntu/data/Total.sdf");
        BufferedWriter writer = new BufferedWriter(new FileWriter(sdfFile));
        
		List<String> output = jprdd.values().collect();
		for(String sdFile: output){
			writer.write(sdFile);
			writer.write("$$$$\n");
			writer.flush();
		}
		writer.close();
		sc.stop();
		sc.close();
		System.out.println("Time: " + (System.nanoTime() - start)/1E9 + " sec.");
	}
}
