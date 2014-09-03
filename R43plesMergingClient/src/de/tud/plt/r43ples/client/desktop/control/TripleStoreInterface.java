package de.tud.plt.r43ples.client.desktop.control;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/** 
 * Provides a interface to the triple store with URI and port specified in the configuration file.
 *
 * @author Stephan Hensel
 *
 */
public class TripleStoreInterface {

	/** The logger. */
	private static Logger logger = Logger.getLogger(TripleStoreInterface.class);
	/** The endpoint to use. **/
	private static String endpoint = Config.r43ples_sparql_endpoint;

	
	/**
	 * Executes a SPARQL-query against the triple store without authorization.
	 * 
	 * @param query the SPARQL query
	 * @param format the format of the result (e.g. HTML, xml/rdf, JSON, ...)
	 * @return the result of the query
	 * @throws IOException 
	 */
	public static String executeQueryWithoutAuthorization(String query, String format) throws IOException {
		URL url = null;
		
		url = new URL(endpoint + "/r43ples/sparql?query=" + URLEncoder.encode(query, "UTF-8") + "&format=" + URLEncoder.encode(format, "UTF-8") + "&timeout=0");
		logger.debug(url.toString());

		URLConnection con = null;
		InputStream in = null;
		con = url.openConnection();
		in = con.getInputStream();
	
		String encoding = con.getContentEncoding();
		encoding = (encoding == null) ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		return body;
		
	}
	
	
	/**
	 * Executes a HTTP-GET.
	 * 
	 * @param path the path
	 * @return the result of the query
	 * @throws IOException 
	 */
	public static String executeHttpGet(String path) throws IOException {
		URL url = null;
		
		url = new URL(endpoint + path);
		logger.debug(url.toString());

		URLConnection con = null;
		InputStream in = null;
		con = url.openConnection();
		in = con.getInputStream();
	
		String encoding = con.getContentEncoding();
		encoding = (encoding == null) ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		return body;
		
	}
	
}
