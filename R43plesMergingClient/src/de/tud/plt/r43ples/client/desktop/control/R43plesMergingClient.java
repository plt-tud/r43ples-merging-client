package de.tud.plt.r43ples.client.desktop.control;

import java.awt.EventQueue;

import org.apache.commons.configuration.ConfigurationException;

import de.tud.plt.r43ples.client.desktop.ui.ApplicationUI;

/**
 * The R43ples Merging Client provides an user interface for merging branches.
 * 
 * @author Stephan Hensel
 *
 */
public class R43plesMergingClient {

	
	/**
	 * Launch the application.
	 * 
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws ConfigurationException {
		
		// Read the configuration file
		Config.readConfig("client.conf");
		
		// Create UI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new ApplicationUI();
					ApplicationUI.frmRplesMergingClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
