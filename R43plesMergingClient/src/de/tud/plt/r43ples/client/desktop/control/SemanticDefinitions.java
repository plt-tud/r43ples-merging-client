package de.tud.plt.r43ples.client.desktop.control;

import java.util.ArrayList;
import java.util.HashMap;

import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.SemanticDefinitionResult;

/**
 * Stores all semantic definitions.
 * 
 * @author Stephan Hensel
 *
 */
public class SemanticDefinitions {

	/** The definition map for class descriptions. **/
	private static HashMap<String, String> definitionMapClassDescriptions = new HashMap<String, String>();
	/** The definition map for class resolution options. **/
	private static HashMap<String,  ArrayList<String>> definitionMapClassResolutionOptions = new HashMap<String, ArrayList<String>>();
	/** The definition map for class default resolution option. **/
	private static HashMap<String, Integer> definitionMapClassDefaultResolutionOptions = new HashMap<String, Integer>();
	
	
	/**
	 * The constructor.
	 */
	public SemanticDefinitions() {
		setClassDescriptionDefinitions();
		setClassResolutionOptions();
		setClassDefaultResolutionOptions();
	}
	
	
	/**
	 * Set the class description definitions.
	 */
	private static void setClassDescriptionDefinitions() {
		// Remove all entries
		definitionMapClassDescriptions.clear();
		// Put entries
		definitionMapClassDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, "Class remains deleted.");
		
		definitionMapClassDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, "Explicit added class will be removed.");
		// TODO Add all definitions
	}
	
	
	/**
	 * Get the class description by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the class description
	 */
	public static String getClassDescription(String identifier) {
		return definitionMapClassDescriptions.get(identifier);
	}
	
	
	/**
	 * Set the class resolution options.
	 */
	private static void setClassResolutionOptions() {
		// Remove all entries
		definitionMapClassResolutionOptions.clear();
		// Put entries
		ArrayList<String> delDel = new ArrayList<String>();
		delDel.add("Approve deletion");
		delDel.add("Add the class to result.");
		definitionMapClassResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, delDel);
		
		ArrayList<String> delAdd = new ArrayList<String>();
		delAdd.add("Approve deletion");
		delAdd.add("Do not change the ADD state.");
		definitionMapClassResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, delAdd);
		// TODO Add all definitions
	}
	
	
	/**
	 * Get the class resolution options by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the class resolution options
	 */
	public static ArrayList<String> getClassResolutionOptions(String identifier) {
		return definitionMapClassResolutionOptions.get(identifier);
	}
	
	
	/**
	 * Set the class default resolution options.
	 */
	public static void setClassDefaultResolutionOptions() {
		// Remove all entries
		definitionMapClassDefaultResolutionOptions.clear();
		// Put entries
		definitionMapClassDefaultResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, 0);
		
		definitionMapClassDefaultResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, 0);
		// TODO Add all definitions
		
		// TODO Set default by using the SDD automatic resolution state
	}
	
	
	/**
	 * Get the class default resolution option by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the class default resolution option
	 */
	public static int getClassDefaultResolutionOptions(String identifier) {
		return definitionMapClassDefaultResolutionOptions.get(identifier);
	}


	/**
	 * Get the semantic definition result.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @return the semantic definition result
	 */
	public static SemanticDefinitionResult getSemanticDefinitionResult(Difference difference, DifferenceGroup differenceGroup) {
		// Create the identifier (STATE-STATE)
		String identifier = differenceGroup.getTripleStateA() + "-" + differenceGroup.getTripleStateB();
		
		// Check if triple is a class triple
		if (Management.getPredicate(difference.getTriple()).equals("a")) {
			return new SemanticDefinitionResult(getClassDescription(identifier), getClassResolutionOptions(identifier), getClassDefaultResolutionOptions(identifier));
		}
		
		// Return empty semantic definition result - ERROR
		ArrayList<String> emptyList = new ArrayList<String>();
		return new SemanticDefinitionResult("ERROR", emptyList, 0);
	}
	
}
