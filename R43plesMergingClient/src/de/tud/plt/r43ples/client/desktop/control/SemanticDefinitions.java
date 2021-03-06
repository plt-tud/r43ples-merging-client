package de.tud.plt.r43ples.client.desktop.control;

import java.util.ArrayList;
import java.util.HashMap;

import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
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

	/** The definition map for individual descriptions. **/
	private static HashMap<String, String> definitionMapIndividualDescriptions = new HashMap<String, String>();
	/** The definition map for individual resolution options. **/
	private static HashMap<String,  ArrayList<String>> definitionMapIndividualResolutionOptions = new HashMap<String, ArrayList<String>>();
	/** The definition map for property descriptions. **/
	private static HashMap<String, String> definitionMapPropertyDescriptions = new HashMap<String, String>();
	/** The definition map for property resolution options. **/
	private static HashMap<String,  ArrayList<String>> definitionMapPropertyResolutionOptions = new HashMap<String, ArrayList<String>>();
	
	
	/**
	 * The constructor.
	 */
	public SemanticDefinitions() {
		setIndividualDescriptionDefinitions();
		setIndividualResolutionOptions();
		setPropertyDescriptionDefinitions();
		setPropertyResolutionOptions();
	}
	
	
	/**
	 * Set the individual description definitions.
	 */
	private static void setIndividualDescriptionDefinitions() {
		// Remove all entries
		definitionMapIndividualDescriptions.clear();
		// Put entries
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, "Individual remains deleted.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, "Explicit added individual will be removed.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ORIGINAL, "Original individual will be removed.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
		
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.DELETED, "Explicit removed individual will be added.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ADDED, "Individual remains added.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ORIGINAL, "Individual remains original.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.NOTINCLUDED, "Not included individual will be added.");
		
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.DELETED, "Explicit removed individual will be added.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ADDED, "Individual remains added.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ORIGINAL, "Individual remains original.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
		
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.DELETED, "ERROR - Combination is not allowed.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ADDED, "Individual will be removed.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ORIGINAL, "ERROR - Combination is not allowed.");
		definitionMapIndividualDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
	}
	
	
	/**
	 * Get the individual description by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the individual description
	 */
	public static String getIndividualDescription(String identifier) {
		return definitionMapIndividualDescriptions.get(identifier);
	}
	
	
	/**
	 * Set the individual resolution options.
	 */
	private static void setIndividualResolutionOptions() {
		// Remove all entries
		definitionMapIndividualResolutionOptions.clear();
		// Put entries
		ArrayList<String> delDel = new ArrayList<String>();
		delDel.add("Approve deletion");
		delDel.add("Add individual to result");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, delDel);
		ArrayList<String> delAdd = new ArrayList<String>();
		delAdd.add("Approve deletion");
		delAdd.add("Do not remove added individual");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, delAdd);
		ArrayList<String> delOrig = new ArrayList<String>();
		delOrig.add("Approve deletion");
		delOrig.add("Do not remove original individual");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ORIGINAL, delOrig);
		ArrayList<String> delNotIncl = new ArrayList<String>();
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.NOTINCLUDED, delNotIncl);
		
		ArrayList<String> addDel = new ArrayList<String>();
		addDel.add("Do not add removed individual");
		addDel.add("Approve addition");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.DELETED, addDel);
		ArrayList<String> addAdd = new ArrayList<String>();
		addAdd.add("Remove individual from result");
		addAdd.add("Approve addition");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ADDED, addAdd);
		ArrayList<String> addOrig = new ArrayList<String>();
		addOrig.add("Remove individual from result");
		addOrig.add("Approve no change of original individual");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ORIGINAL, addOrig);
		ArrayList<String> addNotIncl = new ArrayList<String>();
		addNotIncl.add("Do not add not included individual");
		addNotIncl.add("Approve addition");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.NOTINCLUDED, addNotIncl);
		
		ArrayList<String> origDel = new ArrayList<String>();
		origDel.add("Do not add removed individual");
		origDel.add("Approve addition");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.DELETED, origDel);
		ArrayList<String> origAdd = new ArrayList<String>();
		origAdd.add("Remove individual from result");
		origAdd.add("Approve addition");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ADDED, origAdd);
		ArrayList<String> origOrig = new ArrayList<String>();
		origOrig.add("Remove individual from result");
		origOrig.add("Approve no change of original individual");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ORIGINAL, origOrig);
		ArrayList<String> origNotIncl = new ArrayList<String>();
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.NOTINCLUDED, origNotIncl);
		
		ArrayList<String> notInclDel = new ArrayList<String>();
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.DELETED, notInclDel);
		ArrayList<String> notInclAdd = new ArrayList<String>();
		notInclAdd.add("Approve deletion");
		notInclAdd.add("Do not remove added individual");
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ADDED, notInclAdd);
		ArrayList<String> notInclOrig = new ArrayList<String>();
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ORIGINAL, notInclOrig);
		ArrayList<String> notInclNotIncl = new ArrayList<String>();
		definitionMapIndividualResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.NOTINCLUDED, notInclNotIncl);
	}
	
	
	/**
	 * Get the property resolution options by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the property resolution options
	 */
	public static ArrayList<String> getIndividualResolutionOptions(String identifier) {
		return definitionMapIndividualResolutionOptions.get(identifier);
	}
	
	
	/**
	 * Set the property description definitions.
	 */
	private static void setPropertyDescriptionDefinitions() {
		// Remove all entries
		definitionMapPropertyDescriptions.clear();
		// Put entries
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, "Property remains deleted.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, "Explicit added property will be removed.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ORIGINAL, "Original property will be removed.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
		
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.DELETED, "Explicit removed property will be added.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ADDED, "Property remains added.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ORIGINAL, "Property remains original.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.NOTINCLUDED, "Not included property will be added.");
		
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.DELETED, "Explicit removed property will be added.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ADDED, "Property remains added.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ORIGINAL, "Property remains original.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
		
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.DELETED, "ERROR - Combination is not allowed.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ADDED, "Property will be removed.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ORIGINAL, "ERROR - Combination is not allowed.");
		definitionMapPropertyDescriptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.NOTINCLUDED, "ERROR - Combination is not allowed.");
	}
	
	
	/**
	 * Get the property description by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the property description
	 */
	public static String getPropertyDescription(String identifier) {
		return definitionMapPropertyDescriptions.get(identifier);
	}
	
	
	/**
	 * Set the property resolution options.
	 */
	private static void setPropertyResolutionOptions() {
		// Remove all entries
		definitionMapPropertyResolutionOptions.clear();
		// Put entries
		ArrayList<String> delDel = new ArrayList<String>();
		delDel.add("Approve deletion");
		delDel.add("Add property to result");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.DELETED, delDel);
		ArrayList<String> delAdd = new ArrayList<String>();
		delAdd.add("Approve deletion");
		delAdd.add("Do not remove added property");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ADDED, delAdd);
		ArrayList<String> delOrig = new ArrayList<String>();
		delOrig.add("Approve deletion");
		delOrig.add("Do not remove original property");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.ORIGINAL, delOrig);
		ArrayList<String> delNotIncl = new ArrayList<String>();
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.DELETED + "-" + SDDTripleStateEnum.NOTINCLUDED, delNotIncl);
		
		ArrayList<String> addDel = new ArrayList<String>();
		addDel.add("Do not add removed property");
		addDel.add("Approve addition");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.DELETED, addDel);
		ArrayList<String> addAdd = new ArrayList<String>();
		addAdd.add("Remove property from result");
		addAdd.add("Approve addition");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ADDED, addAdd);
		ArrayList<String> addOrig = new ArrayList<String>();
		addOrig.add("Remove property from result");
		addOrig.add("Approve no change of original property");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.ORIGINAL, addOrig);
		ArrayList<String> addNotIncl = new ArrayList<String>();
		addNotIncl.add("Do not add not included property");
		addNotIncl.add("Approve addition");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ADDED + "-" + SDDTripleStateEnum.NOTINCLUDED, addNotIncl);
		
		ArrayList<String> origDel = new ArrayList<String>();
		origDel.add("Do not add removed property");
		origDel.add("Approve addition");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.DELETED, origDel);
		ArrayList<String> origAdd = new ArrayList<String>();
		origAdd.add("Remove property from result");
		origAdd.add("Approve addition");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ADDED, origAdd);
		ArrayList<String> origOrig = new ArrayList<String>();
		origOrig.add("Remove property from result");
		origOrig.add("Approve no change of original property");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.ORIGINAL, origOrig);
		ArrayList<String> origNotIncl = new ArrayList<String>();
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.ORIGINAL + "-" + SDDTripleStateEnum.NOTINCLUDED, origNotIncl);
		
		ArrayList<String> notInclDel = new ArrayList<String>();
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.DELETED, notInclDel);
		ArrayList<String> notInclAdd = new ArrayList<String>();
		notInclAdd.add("Approve deletion");
		notInclAdd.add("Do not remove added property");
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ADDED, notInclAdd);
		ArrayList<String> notInclOrig = new ArrayList<String>();
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.ORIGINAL, notInclOrig);
		ArrayList<String> notInclNotIncl = new ArrayList<String>();
		definitionMapPropertyResolutionOptions.put(SDDTripleStateEnum.NOTINCLUDED + "-" + SDDTripleStateEnum.NOTINCLUDED, notInclNotIncl);
	}
	
	
	/**
	 * Get the property resolution options by identifier.
	 * 
	 * @param identifier the identifier (STATE-STATE)
	 * @return the property resolution options
	 */
	public static ArrayList<String> getPropertyResolutionOptions(String identifier) {
		return definitionMapPropertyResolutionOptions.get(identifier);
	}
	
	
	/**
	 * Get the selected resolution option by difference group.
	 * 
	 * @param difference the difference
	 * @param differenceGroup the difference group
	 * @return the selected resolution option
	 */
	public static int getSelectedResolutionOption(Difference difference, DifferenceGroup differenceGroup) {
		// Generate the selected index
		if (difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
			// Difference was already approved - use the approved state
			if (difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED)) {
				return 1;
			} else {
				return 0;
			}
		} else {
			// Difference was not already approved - use the default resolution state characterized by automatic resolution state
			if (differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) {
				return 1;
			} else {
				return 0;
			}
		}
	}


	/**
	 * Check the identifier existence in all individual maps.
	 * 
	 * @param identifier the identifier (STATE-STATE) to check
	 * @return true when identifier is contained in all maps
	 */
	public static boolean checkIdentifierExistenceInIndividualMaps(String identifier) {
		return definitionMapIndividualDescriptions.containsKey(identifier) && definitionMapIndividualResolutionOptions.containsKey(identifier);
	}
	
	
	/**
	 * Check the identifier existence in all property maps.
	 * 
	 * @param identifier the identifier (STATE-STATE) to check
	 * @return true when identifier is contained in all maps
	 */
	public static boolean checkIdentifierExistenceInPropertyMaps(String identifier) {
		return definitionMapPropertyDescriptions.containsKey(identifier) && definitionMapPropertyResolutionOptions.containsKey(identifier);
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
		
		// Check if triple is an individual triple
		if (Management.getPredicate(difference.getTriple()).equals("a")) {
			if (checkIdentifierExistenceInIndividualMaps(identifier)) {
				return new SemanticDefinitionResult(getIndividualDescription(identifier), getIndividualResolutionOptions(identifier), getSelectedResolutionOption(difference, differenceGroup));
			}
		} else {
			if (checkIdentifierExistenceInPropertyMaps(identifier)) {
				return new SemanticDefinitionResult(getPropertyDescription(identifier), getPropertyResolutionOptions(identifier), getSelectedResolutionOption(difference, differenceGroup));
			}
		}
		
		// Return empty semantic definition result - ERROR
		ArrayList<String> emptyList = new ArrayList<String>();
		return new SemanticDefinitionResult("ERROR", emptyList, -1);
	}
	
}
