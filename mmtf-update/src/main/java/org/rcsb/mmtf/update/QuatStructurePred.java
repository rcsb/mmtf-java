package org.rcsb.mmtf.update;


import java.io.Serializable;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.rcsb.mmtf.mappers.ByteArrayToBioJavaStructMapper;
import org.rcsb.mmtf.mappers.ByteWriteToByteArr;
import org.rcsb.mmtf.mappers.StructureToBioAssemblies;
import org.rcsb.mmtf.proccessors.PredictQuatStructures;

/**
 * Worker to generate quaternary structures from a hadoop sequence file of the PDB
 * @author  Anthony Bradley
 */
public class QuatStructurePred implements Serializable {    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3037567648753603114L;
	//	private static final Logger logger = LoggerFactory.getLogger(SparkRead.class);
	public static void main(String[] args ) 
	{
		
		////  DOESN'T WORK
		//// 1) NEEDS ASYM ID
		//// 2) NEEDS FIX IN THE CODE TO GET ChemComp
			
		String path = "/Users/anthony/codec-devel/Total.hadoop.maindata.bzip2";
		
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		DownloadChemCompProvider cc = new DownloadChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(QuatStructurePred.class.getSimpleName());
		// Set the config
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Time the proccess
		long start = System.nanoTime();
		JavaPairRDD<String, String> jprdd = sc
				.sequenceFile(path, Text.class, BytesWritable.class,12)
				.sample(true,0.0001)
				// Now get the structure
				.mapToPair(new ByteWriteToByteArr())
				.mapToPair(new ByteArrayToBioJavaStructMapper())
				.flatMapToPair(new StructureToBioAssemblies())
				.mapToPair(new PredictQuatStructures());
		// Now collect the results
		jprdd.saveAsTextFile("outputData.txt");
		sc.stop();
		sc.close();
		System.out.println("Time: " + (System.nanoTime() - start)/1E9 + " sec.");
	}
}

