package org.rcsb.mmtf.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;

import org.apache.hadoop.io.BytesWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.LocalPDBDirectory.FetchBehavior;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.biojava.nbio.structure.rcsb.GetRepresentatives;
import org.rcsb.mmtf.mappers.DataStructToByteArrs;
import org.rcsb.mmtf.mappers.PdbIdToDataStruct;
import org.rcsb.mmtf.mappers.StringByteToTextByteWriter;

public class BuildFirstDataSet {

	public static void main(String[] args )
	{

		// The arguments indicate where the PDB cache is AND what the version is.
		if (args.length<2) {
			System.err.println("NOT ENOUGH ARGUMENTS PROVIDED");
			throw new RuntimeException();
		}
		
		// This is the default 2 line structure for Spark applications
		SparkConf conf = new SparkConf().setMaster("local[*]")
				.setAppName(BuildFirstDataSet.class.getSimpleName());
		// Set the config
		JavaSparkContext sc = new JavaSparkContext(conf);

		// A hack to make sure we're not downloading the whole pdb
		Properties sysProps = System.getProperties();
		sysProps.setProperty("PDB_CACHE_DIR", args[0]);
		sysProps.setProperty("PDB_DIR", args[0]);
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		cache.setFetchBehavior(FetchBehavior.FETCH_FILES);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		DownloadChemCompProvider dcc = new DownloadChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(dcc);
		dcc.checkDoFirstInstall();
		dcc.setDownloadAll(true);
		params.setUseInternalChainId(true);
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);
		// Get all the PDB IDs
		SortedSet<String> thisSet = GetRepresentatives.getAll();
		List<String> pdbCodeList = new ArrayList<String>(thisSet);
		// Now read this list in
		JavaPairRDD<Text, BytesWritable> distData =
				sc.parallelize(pdbCodeList)
				.mapToPair(new PdbIdToDataStruct())
				.flatMapToPair(new DataStructToByteArrs())
				.mapToPair(new StringByteToTextByteWriter());
		// Now save this as a Hadoop sequence file
		String uri = args[1];		
		distData.saveAsHadoopFile(uri, Text.class, BytesWritable.class, SequenceFileOutputFormat.class, org.apache.hadoop.io.compress.BZip2Codec.class);
		sc.close();
	}
}
