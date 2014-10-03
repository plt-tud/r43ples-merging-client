package de.tud.plt.r43ples.client.desktop.model.structure;

import java.util.HashMap;

/**
 * The class model provides the structure for storing class information of a revision.
 * 
 * @author Stephan Hensel
 *
 */
public class ClassModel {

	/** The hash map which contains all classes. Key is the class URI. **/
	private HashMap<String, ClassStructure> classStructures;
	
	
	/**
	 * The constructor.
	 */
	public ClassModel() {
		setClassStructures(new HashMap<String, ClassStructure>());
	}


	/**
	 * Get the class structures.
	 * 
	 * @return the class structures
	 */
	public HashMap<String, ClassStructure> getClassStructures() {
		return classStructures;
	}


	/**
	 * Set the class structures
	 * 
	 * @param classStructures the class structures to set
	 */
	public void setClassStructures(HashMap<String, ClassStructure> classStructures) {
		this.classStructures = classStructures;
	}

	
	/**
	 * Add a class structure. If the class structure identifier already exists the old class structure will be overwritten.
	 * 
	 * @param identifier the identifier
	 * @param classStructure the class structure
	 */
	public void addClassStructure(String identifier, ClassStructure classStructure) {
		this.classStructures.put(identifier, classStructure);
	}
	
	
	/**
	 * Remove entry from class structures.
	 * 
	 * @param identifier the identifier of the class structure to remove
	 */
	public void removeClassStructure(String identifier) {
		this.classStructures.remove(identifier);
	}
	
	
	/**
	 * Clear the class model.
	 */
	public void clear() {
		classStructures.clear();
	}

}
