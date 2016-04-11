package org.rcsb.mmtf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class DownloadUtils {

	/** The base url. */
	public static final String BASE_URL = "http://mmtf.rcsb.org/full/";
	/** The size of a chunk for a byte buffer. */
	private static final int BYTE_BUFFER_CHUNK_SIZE = 4096;
	
	/**
	 * Find the message pack byte array from the web using the input code and a base url.
	 * Caches the file if possible.
	 * @param inputCode
	 * @return the byte array
	 * @throws IOException
	 */
	public static byte[] getDataFromUrl(String inputCode) throws IOException {	
		// Get these as an inputstream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		URL url = new URL(BASE_URL + inputCode);
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[BYTE_BUFFER_CHUNK_SIZE]; // Or whatever size you want to read in at a time.
			int n;

			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		}
		catch (IOException e) {
			System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
			e.printStackTrace ();
			// Perform any other exception handling that's appropriate.
		}
		finally {
			if (is != null) { is.close(); }
		}
		byte[] b = baos.toByteArray();
		// Now return the gzip deflated byte array
		return deflateGzip(b);
	}
	
	/**
	 * Deflate a gzip byte array.
	 * @param inputBytes -> gzip compressed byte
	 * array
	 * @return A deflated byte array
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static byte[] deflateGzip(final byte[] inputBytes){
		// Start the byte input stream
		ByteArrayInputStream bis = new ByteArrayInputStream(inputBytes);
		GZIPInputStream gis;
		try {
			gis = new GZIPInputStream(bis);
		} catch (IOException e) {
			System.err.println("Error in opening byte array.");
			e.printStackTrace();
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// Make a buffer
		byte[] tmp = new byte[BYTE_BUFFER_CHUNK_SIZE];
		try {
			while (gis.available() == 1) {
				int size = gis.read(tmp);
				if(size==-1){
					break;
				}
				baos.write(tmp, 0, size);
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} 
		finally {
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		// Get the bytes
		byte[] outArr = baos.toByteArray();
		return outArr;
	}
}
