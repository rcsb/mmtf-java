package org.rcsb.mmtf.preupdatetests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.rcsb.mmtf.biojavaencoder.EncoderUtils;
import org.rcsb.mmtf.update.ServerUtils;
import org.rcsb.mmtf.update.TestingUtils;
import org.rcsb.mmtf.update.WeeklyUpdateUtils;

/**
 * Is the data to be added on the FTP site available, parseable and consistent when roundtripped.
 * @author Anthony Bradley
 *
 */
public class DataConsistencyCheck {

	/**
	 * 1) Argument one is the FTP server for the update lists
	 * 2) Argumnet two is the server for the mmcif.gz files
	 * 3) Argument three is the file to write at the end
	 * 4) The URL for the CCD data
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 * @throws StructureException
	 */
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, IOException, StructureException {


		// Set up the atom cache etc
	  	EncoderUtils encoderUtils = new EncoderUtils();
	  	ServerUtils serverUtils = new ServerUtils();
	  	AtomCache cache = encoderUtils.setUpBioJava(args[4]);
	  	FileParsingParameters params = cache.getFileParsingParams();	  	
	  	
		// Get the data
		WeeklyUpdateUtils weeklyUpdate = new WeeklyUpdateUtils();
		weeklyUpdate.getDataFromFtpSite(args[0]);
		List<String> listToAdd = weeklyUpdate.getAddedList();
		String[] urlList = new String[listToAdd.size()];
		for (int i =0; i< listToAdd.size(); i++) {
			urlList[i] = args[1] + serverUtils.generateDataExtension(listToAdd.get(i));
		}
		String outPutFile = args[2];
		TestingUtils testingUtils = new TestingUtils();
		testingUtils.testAll(urlList, params, cache);
		File f = new File(outPutFile);
		f.getParentFile().mkdirs(); 
		f.createNewFile();
	}


}
