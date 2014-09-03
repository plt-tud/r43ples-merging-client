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
	
	/** The R43ples server port. **/
	public static int r43plesServerPort;
	/** The R43ples server URI. **/
	public static String r43plesServerUri;
	
	/**
	 * Read the configuration information from local file
	 * 
	 * @param configFilePath path to configuration file
	 * @throws ConfigurationException
	 */
	public static void readConfig(final String configFilePath) throws ConfigurationException{
		
		PropertiesConfiguration config = new PropertiesConfiguration(configFilePath);
		r43plesServerPort = config.getInt("r43ples.server.port");
		r43plesServerUri = config.getString("r43ples.server.uri");
		
	}

}