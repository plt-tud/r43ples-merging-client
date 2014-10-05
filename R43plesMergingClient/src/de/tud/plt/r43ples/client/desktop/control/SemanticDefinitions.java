package de.tud.plt.r43ples.client.desktop.control;

import java.util.ArrayList;
import java.util.HashMap;

import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;

/**
 * Stores all semantic definitions.
 * 
 * @author Stephan Hensel
 *
 */
public class SemanticDefinitions {

	/** The definition map for class descriptions. **/
	private HashMap<String, String> definitionMapClassDescriptions = new HashMap<String, String>();
	/** The definition map for class resolution options. **/
	private HashMap<String,  ArrayList<String>> definitionMapClassResolutionOptions = new HashMap<String, ArrayList<String>>();
	
	
	/**
	 * The constructor.
	 */
	public SemanticDefinitions() {
		setClassDescriptionDefinitions();
		setClassResolutionOptions();
	}
	
	
	/**
	 * Set the class description definitions.
	 */
	private void setClassDescriptionDefinitions() {
		// Remove all entries
		definitionMapClassDescriptions.clear();
		// Put entries
		definitionMapClassDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, "Class remains deleted.");
	}
	
	
	/**
	 * Get the class description by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the class description
	 */
	public String getClassDescription(String identifier) {
		return definitionMapClassDescriptions.get(identifier);
	}
	
	
	/**
	 * Set the class resolution options.
	 */
	private void setClassResolutionOptions() {
		// Remove all entries
		definitionMapClassResolutionOptions.clear();
		// Put entries
		ArrayList<String> delDel = new ArrayList<String>();
		delDel.add("Approve deletion");
		delDel.add("Add the class to result.");
		definitionMapClassResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, delDel);
	}
	
	
	/**
	 * Get the class resolution options by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the class resolution options
	 */
	public ArrayList<String> getClassResolutionOptions(String identifier) {
		return definitionMapClassResolutionOptions.get(identifier);
	}
	
	// TODO Add all definitions
	
}
