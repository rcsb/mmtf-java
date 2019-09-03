package org.rcsb.mmtf.decoder;

import java.io.BufferedInputStream;
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
 * This class provides methods to retrieve and decode MMTF data from the MMTF server and MMTF files. 
 *
 * @author Anthony Bradley
 *
 */
public class ReaderUtils {

	/**
	 * The size of a chunk for a byte buffer.
	 */
	private static final int BYTE_BUFFER_CHUNK_SIZE = 4096;

	
	/**
	 * Returns a full (atom atom) MmmtfStructure given a PDB Id from the MMTF web server using HTTP.
	 *
	 * @param pdbId the pdb code for the desired structure.
	 * @return MmtfStructure containing the decoded structure
	 * @throws IOException if the data cannot be read from the URL
	 */
	public static MmtfStructure getDataFromUrl(String pdbId) throws IOException {
	    return getDataFromUrl(pdbId, false, false);
	}

	/**
	 * Returns an MmmtfStructure given a PDB Id from the MMTF web server. 
	 * It requests, gets, decompresses, deserializes and decodes the message pack byte array from the MMTF web server.
	 * This methods support http and https protocols and two MMTF representations: full and reduced.
	 * reduced: C-alpha atoms for polypeptides, P for polynucleotides, and all atom for all other groups (residues) at 0.1 A coordinate precision;
	 * full: all atoms at 0.001 A coordinate precision
	 *
	 * @param pdbId the pdb code for the desired structure.
	 * @return decoded MmmtfStructure
	 * @throws IOException if the data cannot be read from the URL
	 */
	public static MmtfStructure getDataFromUrl(String pdbId, boolean https, boolean reduced)
		throws IOException {
		// Get these as an inputstream
		byte[] bytes = getByteArrayFromUrl(pdbId, https, reduced);
		// Now return the gzip deflated and deserialized byte array
		MessagePackSerialization mmtfBeanSeDeMessagePackImpl
			= new MessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(new ByteArrayInputStream(
			deflateGzip(bytes)));
	}
	


	/**
	 * Gets the GZIP compressed and messagepack serialized data from the MMTF servers
	 *
	 * @param pdbId the PDB code for the data required
	 * @return the byte array (GZIP compressed) of the data from the URL
	 * @throws IOException an error reading the URL
	 */
	public static byte[] getByteArrayFromUrl(String pdbId) throws IOException {
		return getByteArrayFromUrl(pdbId, false, false);
	}
	
	/**
	 * Gets the GZIP compressed and messagepack serialized data from the MMTF servers.
	 * This methods support http and https protocols and two MMTF representations: full and reduced.
	 * reduced: C-alpha atoms for polypeptides, P for polynucleotides, and all atom for all other groups (residues) at 0.1 A coordinate precision;
	 * full: all atoms at 0.001 A coordinate precision
	 *
	 * @param pdbId the PDB Id to retrieve
	 * @return the byte array (GZIP compressed) of the data from the URL
	 * @throws IOException an error reading the URL
	 */
	public static byte[] getByteArrayFromUrl(String pdbId, boolean https, boolean reduced)
		throws IOException {
		URL url = new URL(CodecUtils.getMmtfEntryUrl(pdbId, https, reduced));
		try (InputStream inputStream = url.openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			byte[] byteChunk = new byte[BYTE_BUFFER_CHUNK_SIZE];
			// Or whatever size you want to read in at a time.
			int n;
			while ((n = inputStream.read(byteChunk)) > 0) {
				baos.write(byteChunk, 0, n);
			}
			return baos.toByteArray();
		}
	}

	/**
	 * Deflate a gzip byte array.
	 *
	 * @param inputBytes a gzip compressed byte array
	 * @return a deflated byte array
	 * @throws IOException error in gzip input stream
	 */
	public static byte[] deflateGzip(byte[] inputBytes) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream
			= new ByteArrayOutputStream();
		try (GZIPInputStream gzipInputStream = new GZIPInputStream(
			new ByteArrayInputStream(inputBytes))) {
			byte[] buffer = new byte[BYTE_BUFFER_CHUNK_SIZE];
			while (gzipInputStream.available() == 1) {
				int size = gzipInputStream.read(buffer);
				if (size == -1) {
					break;
				}
				byteArrayOutputStream.write(buffer, 0, size);
			}
			return byteArrayOutputStream.toByteArray();
		}
	}

	/**
	 * Reads and deserializes an uncompressed MMTF file.
	 *
	 * @param filePath the full path of the file to be read
	 * @return the deserialized {@link MmtfStructure}
	 * @throws IOException an error reading the file
	 */
	public static MmtfStructure getDataFromFile(Path filePath)
		throws IOException {
		// Now return the gzip deflated and deserialized byte array
		try (InputStream is = new ByteArrayInputStream(readFile(filePath))) {
			return getDataFromInputStream(is);
		}
	}

	/**
	 * Read a byte array from a file
	 *
	 * @param path the input file path
	 * @return the returned byte array
	 * @throws IOException an error reading the file
	 */
	private static byte[] readFile(Path path) throws IOException {
		byte[] data = Files.readAllBytes(path);
		return data;
	}

	/**
	 * Read an input stream to an {@link MmtfStructure} object.
	 *
	 * @param inStream the {@link InputStream} to read.
	 * @return the {@link MmtfStructure} to be returned
	 * @throws java.io.IOException if the inStream cannot be read
	 */
	public static MmtfStructure getDataFromInputStream(InputStream inStream)
		throws IOException {
		// ensure that InputStream is buffered if needed  (i.e. not externally buffered e.g. via GZIPInputStream)
		if (!(inStream instanceof BufferedInputStream)) {
			inStream = new BufferedInputStream(inStream, 65536);
		}

		MessagePackSerialization mmtfBeanSeDeMessagePackImpl
			= new MessagePackSerialization();
		return mmtfBeanSeDeMessagePackImpl.deserialize(inStream);
	}

}
