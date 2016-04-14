package org.rcsb.mmtf.decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.deserializers.MessagePackDeserializer;

public class ReaderUtils {

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
	public static MmtfBean getDataFromUrl(String inputCode) throws IOException {	
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
		}
		finally {
			if (is != null) { is.close(); }
		}
		byte[] b = baos.toByteArray();
		// Now return the gzip deflated and deserialized byte array
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		return messagePackDeserializer.deserialize(deflateGzip(b));
	}
	
	/**
	 * Deflate a gzip byte array.
	 * @param inputBytes -> gzip compressed byte
	 * array
	 * @return A deflated byte array
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] deflateGzip(byte[] inputBytes){
		// Start the byte input stream
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputBytes);
		GZIPInputStream gzipInputStream;
		try {
			gzipInputStream = new GZIPInputStream(byteInputStream);
		} catch (IOException e) {
			System.err.println("Error in opening byte array.");
			e.printStackTrace();
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// Make a buffer
		byte[] buffer = new byte[BYTE_BUFFER_CHUNK_SIZE];
		try {
			while (gzipInputStream.available() == 1) {
				int size = gzipInputStream.read(buffer);
				if(size==-1){
					break;
				}
				byteArrayOutputStream.write(buffer, 0, size);
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} 
		finally {
			try {
				if (byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		return  byteArrayOutputStream.toByteArray();
	}

	/**
	 * A function to get MMTF data from a file path
	 * @param filePath
	 * @return the deserialized mmtfBean
	 * @throws IOException 
	 */
	public static MmtfBean getDataFromFile(String filePath) throws IOException {
		// Now return the gzip deflated and deserialized byte array
		MessagePackDeserializer messagePackDeserializer = new MessagePackDeserializer();
		return messagePackDeserializer.deserialize(readFile(filePath));
	}

	/**
	 * Read a byte array from a file
	 * @param filePath the input file path
	 * @return the returned byte array
	 * @throws IOException
	 */
	private static byte[] readFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		byte[] data = Files.readAllBytes(path);
		return data;
	}
}
