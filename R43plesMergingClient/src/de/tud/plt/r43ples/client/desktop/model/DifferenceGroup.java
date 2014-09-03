package de.tud.plt.r43ples.client.desktop.model;

import java.util.HashMap;

/**
 * Stores all information of a difference group.
 * 
 * @author Stephan Hensel
 *
 */
public class DifferenceGroup {

	/** The array list which contains all differences. **/
	private HashMap<String, Difference> differences;
	
	
	/**
	 * The constructor.
	 */
	public DifferenceGroup() {
		setDifferences(new HashMap<String, Difference>());
	}


	/**
	 * Get the differences.
	 * 
	 * @return the hash map with all differences
	 */
	public HashMap<String, Difference> getDifferences() {
		return differences;
	}


	/**
	 * Set the differences.
	 * 
	 * @param differences the differences to set
	 */
	public void setDifferences(HashMap<String, Difference> differences) {
		this.differences = differences;
	}
	
	
	/**
	 * Add a difference. If the difference identifier already exists the old difference will be overwritten.
	 * 
	 * @param identifier the identifier
	 * @param differences the difference
	 */
	public void addDifference(String identifier, Difference difference) {
		this.differences.put(identifier, difference);
	}
	
	
	/**
	 * Remove entry from difference.
	 * 
	 * @param identifier the identifier of the difference to remove
	 */
	public void removeDifference(String identifier) {
		this.differences.remove(identifier);
	}
	
}
