package org.rcsb.mmtf.postupdatetests;

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
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.biojava.nbio.structure.Structure;
import org.rcsb.mmtf.mappers.ByteArrayToBioJavaStructMapper;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;


public class CheckHadoopFile  implements Serializable {    

	
	private static final long serialVersionUID = 3037567648753603114L;
	
	/**
	 * A function to read a hadoop sequence file to Biojava structures.
	 * 1) The input path of the available data
	 * 2) The output path indicating the number of PDB ids and a list of the ids.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args ) throws IOException
	{
		// The input path for the data is the fist 
		String inPath = args[0];
		String pdbIdList = args[1];
	
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
		// Write the PDB files to a file
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pdbIdList)));
		writer.write(outValues.size()+"\n");
		for (String pdbId : outValues) {
			writer.write(pdbId+",");
		}
		writer.close();
		// Now move the folder 
		sc.close();
	}
}