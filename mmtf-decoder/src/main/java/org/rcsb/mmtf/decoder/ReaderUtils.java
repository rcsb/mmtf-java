package org.rcsb.mmtf.decoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.rcsb.mmtf.dataholders.MmtfStructure;
import org.rcsb.mmtf.serialization.MmtfStructureMessagePackSerialization;
import org.rcsb.mmtf.utils.CodecUtils;

/**
 * A class of static utility methods for reading data.
 * @author Anthony Bradley
 *
 */
public class ReaderUtils {
	
	/** The size of a chunk for a byte buffer. */
	private static final int BYTE_BUFFER_CHUNK_SIZE = 4096;
	
	/**
	 * Find the message pack byte array from the web using the input code and a base url.
	 * Caches the file if possible.
	 * @param pdbCode the pdb code for the desired structure.
	 * @return the MMTFBean of the deserialized data
	 * @throws IOException if the data cannot be read from the URL
	 */
	public static MmtfStructure getDataFromUrl(String pdbCode) throws IOException {	
		// Get these as an inputstream
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		URL url = new URL(CodecUtils.BASE_URL + pdbCode);
		try {
			is = url.openStream();
			byte[] byteChunk = new byte[BYTE_BUFFER_CHUNK_SIZE]; // Or whatever size you want to read in at a time.
			int n;
			while ( (n = is.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		} finally {
			if (is != null) { is.close(); }
		}
		byte[] b = baos.toByteArray();
		// Now return the gzip deflated and deserialized byte array
		MmtfStructureMessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MmtfStructureMessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(new ByteArrayInputStream(deflateGzip(b)));
	}
	
	/**
	 * Deflate a gzip byte array.
	 * @param inputBytes a gzip compressed byte array
	 * @return a deflated byte array
	 * @throws IOException error in gzip input stream
	 */
	public static byte[] deflateGzip(byte[] inputBytes) throws IOException {
		// Start the byte input stream
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputBytes);
		GZIPInputStream gzipInputStream;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			gzipInputStream = new GZIPInputStream(byteInputStream);

			byteArrayOutputStream = new ByteArrayOutputStream();
			// Make a buffer
			byte[] buffer = new byte[BYTE_BUFFER_CHUNK_SIZE];

			while (gzipInputStream.available() == 1) {
				int size = gzipInputStream.read(buffer);
				if(size==-1){
					break;
				}
				byteArrayOutputStream.write(buffer, 0, size);
			}

		} finally {
			if (byteArrayOutputStream != null) {
				byteArrayOutputStream.close();
			}			
		}
		return  byteArrayOutputStream.toByteArray();
	}

	/**
	 * A function to get MMTF data from a file path.
	 * @param filePath the full path of the file to be read
	 * @return the deserialized {@link MmtfStructure}
	 * @throws IOException an error reading the file 
	 */
	public static MmtfStructure getDataFromFile(Path filePath) throws IOException {
		// Now return the gzip deflated and deserialized byte array
		MmtfStructureMessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MmtfStructureMessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(new ByteArrayInputStream(readFile(filePath)));
	}

	/**
	 * Read a byte array from a file
	 * @param path the input file path
	 * @return the returned byte array
	 * @throws IOException an error reading the file 
	 */
	private static byte[] readFile(Path path) throws IOException {
		
		byte[] data = Files.readAllBytes(path);
		return data;
	}
}
