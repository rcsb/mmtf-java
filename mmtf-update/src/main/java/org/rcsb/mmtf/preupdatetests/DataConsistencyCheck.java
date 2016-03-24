package org.rcsb.mmtf.preupdatetests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.StructureIO;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.update.TestingUtils;
import org.rcsb.mmtf.update.WeeklyUpdateUtils;

/**
 * Is the data to be added on the FTP site available, parseable and consistent when roundtripped.
 * @author Anthony Bradley
 *
 */
public class DataConsistencyCheck {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, IOException, StructureException {


		// Set up the atom cache etc
	  	EncoderUtils encoderUtils = new EncoderUtils();
	  	AtomCache cache = encoderUtils.setUpBioJava();
	  	FileParsingParameters params = cache.getFileParsingParams();

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