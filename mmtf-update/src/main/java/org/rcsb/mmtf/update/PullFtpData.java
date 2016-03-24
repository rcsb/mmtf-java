package org.rcsb.mmtf.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Current status
 * all current entries and models
 * all obsolete entries and models

	Weekly deltas
 * added entries (and models = 0?)
 * modified entries (and models=0?)
 * reloaded entries (and models=0?)
 * obsolete entries (and models=0?)
 *
 */

public class PullFtpData {

	private static final String PROTOCOL = "http";
	private static final String HOST = "sandboxwest.rcsb.org";
	private static final int PORT = 10601;
	private static final String BASE_URL = PROTOCOL+"://"+HOST+":"+PORT+"/ftp-v3-support/update-lists/";


	public boolean pingServer() {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(HOST, PORT), 100);
			return true;
		} catch (IOException e) {
			return false; // Either timeout or unreachable or failed DNS lookup.
		}

	}
	/**
	 * 
	 * @return A list of all the current PDB ids of PDB and NMR models (before the update)
	 */
	public String[] getAllCurrentEntries(){
		/**
	all-pdb-list	2016-Jan-29 12:09:35	565.2K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB 
		String[] pdbAll = readFile("all-pdb-list");
		totList.add(pdbAll);

		return joinLists(totList);
	}

	/**
	 * 
	 * @return A list of all the current PDB ids of computational models (before the update)
	 */
	public String[] getAllCurrentModels(){
		/**
	all-model-list	2016-Jan-29 12:09:35	6.7K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();

		// Get all the models
		String[] allModels = readFile("all-model-list");
		totList.add(allModels);

		return joinLists(totList);
	}

	/**
	 * 
	 * @return A list of all obsolete computational models
	 */
	public String[] getAllObsoleteModels(){
		/**
	obsolete-model-list	2016-Jan-29 12:09:35	0.1K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB  models that have ever been obsoleted
		String[] modelObsolete = readFile("obsolete-model-list");
		totList.add(modelObsolete);

		return joinLists(totList);
	}

	/**
	 * 
	 * @return A list of all obsolete PDB and NMR models
	 */
	public String[] getAllObsoleteEntries(){
		/**
	obsolete-pdb-list	2016-Jan-29 12:09:35	16.5K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB  models that have ever been obsoleted
		String[] pdbObsolete = readFile("obsolete-pdb-list");
		totList.add(pdbObsolete);

		return joinLists(totList);


	}


	/**
	 * 
	 * @return A list of all PDB and NMR models to be added this weekly update
	 */
	public String[] getAdded(){
		/**
	added-entries	2016-Jan-29 12:09:35	1.0K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();
		// Get the NMR and the PDB 
		String[] pdbAdded = readFile("added-entries");
		totList.add(pdbAdded);
		return joinLists(totList);

	}


	/**
	 * 
	 * @return A list of all PDB and NMR models to be modified this weekly update
	 */
	public String[] getModifiedUpdate(){
		/**
	modified-entries	2016-Jan-29 12:09:35	0.9K	application/octet-stream
		 */

		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB 
		String[] pdbModified = readFile("modified-entries");
		totList.add(pdbModified);

		return joinLists(totList);
	}

	/**
	 * 
	 * @return A list of all PDB and NMR models to be reloaded this weekly update
	 */
	public String[] getReloadedUpdate(){
		/**
	reload-entries	2016-Jan-29 12:09:35	0.0K	application/octet-stream
		 */
		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB 
		String[] reloadEntries = readFile("reload-entries");
		totList.add(reloadEntries);
		return joinLists(totList);
	}


	/**
	 * 
	 * @return A list of all PDB and NMR models to be obsoloted this weekly update
	 */
	public String[] getObsoleteUpdate(){
		/**
	obsolete-entries	2016-Jan-29 12:09:35	0.1K	application/octet-stream
		 */

		List<String[]> totList = new ArrayList<String[]>();

		// Get the NMR and the PDB 
		String[] pdbObsolete = readFile("obsolete-entries");
		totList.add(pdbObsolete);
		return joinLists(totList);

	}


	/**
	 * Now a series of helper functions
	 */


	/**
	 * Takes a list of string arrays and joins them into one.
	 * @param totList
	 * @return
	 */
	private String[] joinLists(List<String[]> totList) {
		// Now join the lists together
		// If it's empty then jusr return an empty list
		if(totList.size()==0){
			return new String[0];
		}
		// 
		List<String> strings = new ArrayList<String>(Arrays.asList(totList.get(0)));
		if(totList.size()>1){
			for(int i=1;i<totList.size();i++){
				String[] thisList = totList.get(i);
				for(String item: thisList){
					strings.add(item);
				}
			}
		}
		return strings.toArray(new String[0]);
	}
	/**
	 * Reads a file and returns each line as an entry in a string list
	 * @param fileIn The input file from the FTP site
	 * @return The list of strings.
	 */
	private String[] readFile(String fileIn){
		List<String> outList = new ArrayList<String>();
		String urlIn = BASE_URL+fileIn;
		try
		{
			// create a url object
			URL url = new URL(urlIn);

			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();

			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null)
			{
				outList.add(line);
			}
			bufferedReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return outList.toArray(new String[0]);
	}

}
