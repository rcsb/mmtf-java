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
import org.rcsb.mmtf.serialization.MessagePackSerialization;
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
		byte[] byteArr = getByteArrayFromUrl(pdbCode);
		// Now return the gzip deflated and deserialized byte array
		MessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(new ByteArrayInputStream(deflateGzip(byteArr)));
	}
	
	/**
	 * Get the GZIP compressed and messagepack serialized data from the MMTF servers
	 * @param pdbCode the PDB code for the data required
	 * @return the byte array (GZIP compressed) of the data from the URL
	 * @throws IOException an error reading the URL
	 */
	public static byte[] getByteArrayFromUrl(String pdbCode)  throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream inputStream = null;
		URL url = new URL(getUrl(pdbCode));
		try {
			inputStream = url.openStream();
			byte[] byteChunk = new byte[BYTE_BUFFER_CHUNK_SIZE]; // Or whatever size you want to read in at a time.
			int n;
			while ( (n = inputStream.read(byteChunk)) > 0 ) {
				baos.write(byteChunk, 0, n);
			}
		} finally {
			if (inputStream != null) { inputStream.close(); }
		}
		return baos.toByteArray();
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

		return getDataFromInputStream(Files.newInputStream(filePath));
	}

	/**
	 * Read an input stream to an {@link MmtfStructure} object.
	 * @param inStream the {@link InputStream} to read.
	 * @return the {@link MmtfStructure} to be returned 
	 */
	public static MmtfStructure getDataFromInputStream(InputStream inStream) {
		MessagePackSerialization mmtfBeanSeDeMessagePackImpl = new MessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(inStream);
		
	}

	/**
	 * Get the URL to return a given PDB id
	 * @param pdbId the input PDB id
	 * @return the URL {@link String} to get data from
	 */
	public static String getUrl(String pdbId) {
		return CodecUtils.BASE_URL + pdbId;
	}
}
