package org.rcsb.mmtf.update;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;

/**
 * Is the data to be added on the FTP site available, parseable and consistent when roundtripped.
 * @author Anthony Bradley
 *
 */
public class DataConsistencyCheck {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, IOException, StructureException {


		// TODO once https://github.com/rcsb/mmtf-java/issues/2 has been resolved use the encoder utils instead
		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);
		FileParsingParameters params = cache.getFileParsingParams();
		params.setCreateAtomBonds(true);
		params.setAlignSeqRes(true);
		params.setParseBioAssembly(true);
		params.setUseInternalChainId(true);
		DownloadChemCompProvider cc = new DownloadChemCompProvider();
		ChemCompGroupFactory.setChemCompProvider(cc);
		cc.checkDoFirstInstall();
		cache.setFileParsingParams(params);
		StructureIO.setAtomCache(cache);

		// Get 
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		weeklyUpdate.getDataFromFtpSite(args[0]);
		List<String> listToAdd = weeklyUpdate.getAddedList();
		String[] urlList = new String[listToAdd.size()];
		for (int i =0; i< listToAdd.size(); i++) {
			urlList[i] = args[1] + listToAdd.get(i);
		}
		TestingUtils testingUtils = new TestingUtils();
		testingUtils.testAll(urlList, params, cache);
	}


}
