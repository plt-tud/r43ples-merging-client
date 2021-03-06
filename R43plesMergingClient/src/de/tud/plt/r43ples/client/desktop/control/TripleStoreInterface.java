package de.tud.plt.r43ples.client.desktop.control;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import de.tud.plt.r43ples.client.desktop.model.structure.HttpResponse;

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
		
		url = new URL(endpoint + "?query=" + URLEncoder.encode(query, "UTF-8") + "&format=" + URLEncoder.encode(format, "UTF-8") + "&timeout=0");
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
	 * @param urlString the URL
	 * @return the result of the query
	 * @throws IOException 
	 */
	public static String executeHttpGet(String urlString) throws IOException {
		URL url = null;
		
		url = new URL(urlString);
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
	 * Executes a SPARQL-query against the triple store without authorization using HTTP-GET.
	 * 
	 * @param query the SPARQL query
	 * @param format the format of the result (e.g. HTML, xml/rdf, JSON, ...)
	 * @return the response
	 * @throws IOException 
	 */
	public static HttpResponse executeQueryWithoutAuthorizationGetResponse(String query, String format) throws IOException {
		URL url = null;
		
		url = new URL(endpoint + "?query=" + URLEncoder.encode(query, "UTF-8") + "&format=" + URLEncoder.encode(format, "UTF-8") + "&timeout=0");
		logger.debug(url.toString());


		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		
		InputStream in;
		if (http.getResponseCode() >= 400) {
			in = http.getErrorStream();
		} else {
			in = http.getInputStream();
		}

		String encoding = http.getContentEncoding();
		encoding = (encoding == null) ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);

		// Return the response
		return new HttpResponse(http.getResponseCode(), http.getHeaderFields(), body);
	}
	
	
	/**
	 * Executes a SPARQL-query against the triple store without authorization using HTTP-POST.
	 * 
	 * @param query the SPARQL query
	 * @param format the format of the result (e.g. HTML, xml/rdf, JSON, ...)
	 * @return the response
	 * @throws IOException 
	 */
	public static HttpResponse executeQueryWithoutAuthorizationPostResponse(String query, String format) throws IOException {
		URL url = new URL(endpoint);
		Map<String,Object> params = new LinkedHashMap<>();
		params.put("query", query);
		params.put("format", format);
		params.put("timeout", 0);
				
		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String,Object> param : params.entrySet()) {
			if (postData.length() != 0) postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");
		logger.debug(postData.toString());
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);
		
		InputStream in;
		if (conn.getResponseCode() >= 400) {
			in = conn.getErrorStream();
		} else {
			in = conn.getInputStream();
		}

		String encoding = conn.getContentEncoding();
		encoding = (encoding == null) ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);

		// Return the response
		return new HttpResponse(conn.getResponseCode(), conn.getHeaderFields(), body);
	}
	
}
