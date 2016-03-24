package org.rcsb.mmtf.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

public class FileWriters {


	/**
	 * Function to write out flat files
	 * to the file system
	 * @param collectAsMap
	 * @throws IOException 
	 */
	public void writeOutFlatFiles(Map<String, byte[]> dataMap, String basePath) throws IOException {
		for(Entry<String, byte[]> entry:dataMap.entrySet()){
			// Get the key value pairs
			String pdbCode = entry.getKey();
			byte[] byteArr = entry.getValue();
			// Make the new dir 
			File theDir = new File(basePath+"/"+pdbCode.substring(1, 3));
			if(theDir.exists() == false){
				theDir.mkdirs();
			}
			// Write the file	
			FileOutputStream fos = null;
			// Try and except
			try{
				fos = new FileOutputStream(basePath+"/"+pdbCode.substring(1, 3)+"/"+pdbCode);
				fos.write(byteArr);
			}
			finally{
				fos.close();
			}
		}
	}
	
	public void writeOutFlatFilesToPath(String basePath, Map<String, byte[]> dataMap) throws IOException {
		for(Entry<String, byte[]> entry:dataMap.entrySet()){
			// Get the key value pairs
			String pdbCode = entry.getKey();
			byte[] byteArr = entry.getValue();
			// Make the new dir 
			File theDir = new File(basePath+"/"+pdbCode.substring(1, 3));
			if(theDir.exists() == false){
				theDir.mkdirs();
			}
			// Write the file	
			FileOutputStream fos = null;
			// Try and except
			try{
				fos = new FileOutputStream(basePath+"/"+pdbCode.substring(1, 3)+"/"+pdbCode);
				fos.write(byteArr);
			}
			finally{
				fos.close();
			}
		}
	}
}
