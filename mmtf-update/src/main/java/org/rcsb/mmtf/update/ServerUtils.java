package org.rcsb.mmtf.update;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class ServerUtils {

	
	/**
	 * General function to a ping a generic server.
	 * @param host
	 * @param port
	 * @return
	 * @throws MalformedURLException 
	 */
	public boolean pingServer(String urlInput) throws MalformedURLException {
		URL thisUrl = new URL(urlInput);
		String host = thisUrl.getHost();
		int port = thisUrl.getPort();
		System.out.println("Pinging: "+host+" on port: "+port);
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 100);
			return true;
		} catch (IOException e) {
			return false; // Either timeout or unreachable or failed DNS lookup.
		}

	}
	
	/**
	 * Build the extension to the url for accessing data.
	 * @param inputCode
	 * @return
	 */
	public String generateDataExtension(String inputCode) {
		return inputCode.substring(1,3)+"/"+inputCode+"/"+inputCode+".cif.gz";
	}
}
