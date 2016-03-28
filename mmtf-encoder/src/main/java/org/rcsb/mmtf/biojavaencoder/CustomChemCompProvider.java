package org.rcsb.mmtf.biojavaencoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import org.biojava.nbio.core.util.InputStreamProvider;
import org.biojava.nbio.structure.align.util.HTTPConnectionTools;
import org.biojava.nbio.structure.align.util.UserConfiguration;
import org.biojava.nbio.structure.io.mmcif.AllChemCompProvider;
import org.biojava.nbio.structure.io.mmcif.ChemCompConsumer;
import org.biojava.nbio.structure.io.mmcif.ChemCompProvider;
import org.biojava.nbio.structure.io.mmcif.ChemicalComponentDictionary;
import org.biojava.nbio.structure.io.mmcif.DownloadChemCompProvider;
import org.biojava.nbio.structure.io.mmcif.MMcifParser;
import org.biojava.nbio.structure.io.mmcif.ReducedChemCompProvider;
import org.biojava.nbio.structure.io.mmcif.SimpleMMcifParser;
import org.biojava.nbio.structure.io.mmcif.model.ChemComp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/** This is a custom provider of chem comp information - to provide data from custom urls (specified at run time).
 *
 *
 * @author Anthony Bradley
 * @author Andreas Prlic
 *
 */
public class CustomChemCompProvider implements ChemCompProvider {

	private static final Logger logger = LoggerFactory.getLogger(DownloadChemCompProvider.class);

	public static final String CHEM_COMP_CACHE_DIRECTORY = "chemcomp";

	public static final String SERVER_LOCATION = "http://www.rcsb.org/pdb/files/ligand/";


	private static String extraServerLocation;
	private static File path;
	//private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String NEWLINE = System.getProperty("line.separator");


	// flags to make sure there is only one thread running that is loading the dictionary
	static AtomicBoolean loading = new AtomicBoolean(false);

	static final List<String> protectedIDs = new ArrayList<String> ();
	static {
		protectedIDs.add("CON");
		protectedIDs.add("PRN");
		protectedIDs.add("AUX");
		protectedIDs.add("NUL");
	}

	/** by default we will download only some of the files. User has to request that all files should be downloaded...
	 *
	 */
	boolean downloadAll = false;
	public CustomChemCompProvider(){
		logger.debug("Initialising DownloadChemCompProvider");
		extraServerLocation = null;

		// note that path is static, so this is just to make sure that all non-static methods will have path initialised
		initPath();
	}
	public CustomChemCompProvider(String extraUrl){
		logger.debug("Initialising DownloadChemCompProvider");
		extraServerLocation = extraUrl;

		// note that path is static, so this is just to make sure that all non-static methods will have path initialised
		initPath();
	}

	public CustomChemCompProvider(String cacheFilePath, String extraUrl){
		logger.debug("Initialising DownloadChemCompProvider");

		// note that path is static, so this is just to make sure that all non-static methods will have path initialised
		path = new File(cacheFilePath);
		extraServerLocation = extraUrl;
	}

	private static void initPath(){

		if (path==null) {
			UserConfiguration config = new UserConfiguration();
			path = new File(config.getCacheFilePath());
		}
	}

	/**
	 * Checks if the chemical components already have been installed into the PDB directory.
	 * If not, will download the chemical components definitions file and split it up into small
	 * subfiles.
	 */
	public void checkDoFirstInstall(){

		if ( ! downloadAll ) {
			return;
		}


		// this makes sure there is a file separator between every component,
		// if path has a trailing file separator or not, it will work for both cases
		File dir = new File(path, CHEM_COMP_CACHE_DIRECTORY);
		File f = new File(dir, "components.cif.gz");

		if ( ! f.exists()) {

			downloadAllDefinitions();

		} else {
			// file exists.. did it get extracted?

			FilenameFilter filter =new FilenameFilter() {

				@Override
				public boolean accept(File dir, String file) {
					return file.endsWith(".cif.gz");
				}
			};
			String[] files = dir.list(filter);
			if ( files.length < 500) {
				// not all did get unpacked
				try {
					split();
				} catch (IOException e) {
					logger.error("Could not split file {} into individual chemical component files. Error: {}",
							f.toString(), e.getMessage());
				}
			}
		}
	}

	private void split() throws IOException {

		logger.info("Installing individual chem comp files ...");

		File dir = new File(path, CHEM_COMP_CACHE_DIRECTORY);
		File f = new File(dir, "components.cif.gz");


		int counter = 0;
		InputStreamProvider prov = new InputStreamProvider();

		try( BufferedReader buf = new BufferedReader (new InputStreamReader (prov.getInputStream(f)));
				) {
			String line = null;
			line = buf.readLine ();
			StringWriter writer = new StringWriter();

			String currentID = null;
			while (line != null){

				if ( line.startsWith("data_")) {
					// a new record found!

					if ( currentID != null) {
						writeID(writer.toString(), currentID);
						counter++;
					}

					currentID = line.substring(5);
					writer = new StringWriter();
				}

				writer.append(line);
				writer.append(NEWLINE);

				line = buf.readLine ();
			}

			// write the last record...
			writeID(writer.toString(),currentID);
			counter++;

		}

		logger.info("Created " + counter + " chemical component files.");
	}

	/**
	 * Output chemical contents to a file
	 * @param contents File contents
	 * @param currentID Chemical ID, used to determine the filename
	 * @throws IOException
	 */
	private void writeID(String contents, String currentID) throws IOException{

		String localName = DownloadChemCompProvider.getLocalFileName(currentID);

		try ( PrintWriter pw = new PrintWriter(new GZIPOutputStream(new FileOutputStream(localName))) ) {

			pw.print(contents.toString());
			pw.flush();
		}
	}

	/**
	 * Loads the definitions for this {@link ChemComp} from a local file and instantiates a new object.
	 *
	 * @param recordName the ID of the {@link ChemComp}
	 * @return a new {@link ChemComp} definition.
	 */
	@Override
	public  ChemComp getChemComp(String recordName) {

		// make sure we work with upper case records
		recordName = recordName.toUpperCase().trim();

		boolean haveFile = true;
		if ( recordName.equals("?")){
			return null;
		}

		if ( ! fileExists(recordName)) {
			// check if we should install all components
			checkDoFirstInstall();
		}
		if ( ! fileExists(recordName)) {
			// we previously have installed already the definitions,
			// just do an incrememntal update
			haveFile = downloadChemCompRecord(recordName);  
		}

		// Added check that download was successful and chemical component is available.
		if (haveFile) {
			String filename = getLocalFileName(recordName);
			InputStream inStream = null;
			try {

				InputStreamProvider isp = new InputStreamProvider();

				inStream = isp.getInputStream(filename);

				MMcifParser parser = new SimpleMMcifParser();

				ChemCompConsumer consumer = new ChemCompConsumer();

				// The Consumer builds up the BioJava - structure object.
				// you could also hook in your own and build up you own data model.
				parser.addMMcifConsumer(consumer);

				parser.parse(new BufferedReader(new InputStreamReader(inStream)));

				ChemicalComponentDictionary dict = consumer.getDictionary();

				ChemComp chemComp = dict.getChemComp(recordName);

				return chemComp;

			} catch (IOException e) {

				logger.error("Could not parse chemical component file {}. Error: {}. "
						+ "There will be no chemical component info available for {}", filename, e.getMessage(), recordName);

			}
			finally{
				// Now close it
				if(inStream!=null){
					try {
						inStream.close();
					} catch (IOException e) {
						// This would be weird...
						logger.error("Could not close chemical component file {}. A resource leak could occur!!", filename);
					}
				}

			}
		}

		// see https://github.com/biojava/biojava/issues/315
		// probably a network error happened. Try to use the ReducedChemCOmpProvider
		ReducedChemCompProvider reduced = new ReducedChemCompProvider();

		return reduced.getChemComp(recordName);

	}

	/** Returns the file name that contains the definition for this {@link ChemComp}
	 *
	 * @param recordName the ID of the {@link ChemComp}
	 * @return full path to the file
	 */
	public static String getLocalFileName(String recordName){

		if ( protectedIDs.contains(recordName)){
			recordName = "_" + recordName;
		}

		initPath();

		File f = new File(path, CHEM_COMP_CACHE_DIRECTORY);
		if (! f.exists()){
			logger.info("Creating directory " + f);

			boolean success = f.mkdir();
			// we've checked in initPath that path is writable, so there's no need to check if it succeeds
			// in the unlikely case that in the meantime it isn't writable at least we log an error
			if (!success) logger.error("Directory {} could not be created",f);

		}

		File theFile = new File(f,recordName + ".cif.gz");

		return theFile.toString();
	}

	private static  boolean fileExists(String recordName){

		String fileName = getLocalFileName(recordName);

		File f = new File(fileName);

		return f.exists();

	}

	/**
	 * @param recordName : three-letter name
	 * @return true if successful download
	 */
	private static boolean downloadChemCompRecord(String recordName) {
		
		String localName = getLocalFileName(recordName);
		File newFile;
		try{
			newFile = File.createTempFile("chemcomp"+recordName, "cif");
		}
		catch(IOException e){
			logger.error("Could not write to temp directory {} to create the chemical component download temp file", System.getProperty("java.io.tmpdir"));
			return false;
		}
		// If there is no input server - just leave 
		if (extraServerLocation == null) {
			return false;
		}
		String inputUrl = extraServerLocation + recordName.charAt(0) + "/" + recordName + "/" + recordName + ".cif";

		logger.debug("downloading " + inputUrl);

		URL url = null;


		try {
			url = new URL(inputUrl);

			HttpURLConnection uconn = HTTPConnectionTools.openHttpURLConnection(url);

			try( PrintWriter pw = new PrintWriter(new GZIPOutputStream(new FileOutputStream(newFile)));
					BufferedReader fileBuffer = new BufferedReader(new InputStreamReader(uconn.getInputStream()));
					) {

				String line;

				while ((line = fileBuffer.readLine()) != null) {
					pw.println(line);
				}

				pw.flush();
				// Now we move this across to where it actually wants to be
				boolean couldRename = newFile.renameTo(new File(localName));

				if (!couldRename) {

					throw new IOException("Could not rename temp file "+newFile.toString()+" to file " + localName);
				}

				return true;
			}
		}  catch (IOException e){
			logger.error("Could not download "+url.toString()+" OR store locally to "+localName+" Error ="+e.getMessage());
			newFile.delete();
		}
		return false;
	}

	private void downloadAllDefinitions() {

		if ( loading.get()){
			logger.info("Waiting for other thread to install chemical components...");
		}

		while ( loading.get() ) {

			// another thread is already downloading the components definitions
			// wait for the other thread to finish...

			try {
				// wait half a second

				Thread.sleep(500);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				logger.error("Thread interrupted "+e.getMessage());
			}

			logger.info("Another thread installed the chemical components.");
			return;

		}

		loading.set(true);
		long timeS = System.currentTimeMillis();

		logger.info("Performing first installation of chemical components.");
		logger.info("Downloading components.cif.gz ...");


		try {
			AllChemCompProvider.downloadFile();
		} catch (IOException e){
			logger.error("Could not download the all chemical components file. Error: {}. "
					+ "Chemical components information won't be available", e.getMessage());
			// no point in trying to split if the file could not be downloaded
			loading.set(false);
			return;
		}
		try {
			split();
		} catch (IOException e) {
			logger.error("Could not split all chem comp file into individual chemical component files. Error: {}",
				 e.getMessage());
			// no point in reporting time
			loading.set(false);
			return;
		}
		long timeE = System.currentTimeMillis();
		logger.info("time to install chem comp dictionary: " + (timeE - timeS) / 1000 + " sec.");
		loading.set(false);

	}

	/** By default this provider will download only some of the {@link ChemComp} files.
	 * The user has to request that all files should be downloaded by setting this parameter to true.
	 *
	 *  @return flag if the all components should be downloaded and installed at startup. (default: false)
	 */
	public boolean isDownloadAll() {
		return downloadAll;
	}

	/** By default this provider will download only some of the {@link ChemComp} files.
	 * The user has to request that all files should be downloaded by setting this parameter to true.
	 *
	 * @param  flag if the all components should be downloaded and installed at startup. (default: false)
	 */
	public void setDownloadAll(boolean downloadAll) {
		this.downloadAll = downloadAll;
	}


	

}

