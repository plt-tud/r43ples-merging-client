package de.tud.plt.r43ples.client.desktop.control;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Reads and stores the configuration parameters.
 * 
 * @author Stephan Hensel
 *
 */
public class Config {
	
	/** The R43ples SPARQL endpoint. **/
	public static String r43ples_sparql_endpoint;
	/** The R43ples revision graph. **/
	public static String r43ples_revision_graph;
	
	/**
	 * Read the configuration information from local file
	 * 
	 * @param configFilePath path to configuration file
	 * @throws ConfigurationException
	 */
	public static void readConfig(final String configFilePath) throws ConfigurationException{
		
		PropertiesConfiguration config = new PropertiesConfiguration(configFilePath);
		r43ples_sparql_endpoint = config.getString("r43ples.sparql.endpoint");
		r43ples_revision_graph = config.getString("r43ples.revision.graph");
		
	}

}