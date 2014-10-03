package de.tud.plt.r43ples.client.desktop.model.structure;

import java.util.HashMap;

/**
 * Stores all information of a class.
 * 
 * @author Stephan Hensel
 *
 */
public class ClassStructure {

	/** The hash map which contains all corresponding triples of class. **/
	private HashMap<String, Triple> triples;
	
	
	/**
	 * The constructor.
	 */
	public ClassStructure() {
		triples = new HashMap<String, Triple>();
	}


	/**
	 * Get the corresponding triples of class.
	 * 
	 * @return the triples
	 */
	public HashMap<String, Triple> getTriples() {
		return triples;
	}


	/**
	 * Set the corresponding triples of class.
	 * 
	 * @param triples the triples to set
	 */
	public void setTriples(HashMap<String, Triple> triples) {
		this.triples = triples;
	}
	
	
	/**
	 * Add a triple. If the triple identifier already exists the old triple will be overwritten.
	 * 
	 * @param identifier the identifier (triple to string)
	 * @param triple the triple
	 */
	public void addTriple(String identifier, Triple triple) {
		triples.put(identifier, triple);
	}
		
	
	/**
	 * Remove entry from triples.
	 * 
	 * @param identifier the identifier (triple to string) of the triple to remove
	 */
	public void removeTriple(String identifier) {
		this.triples.remove(identifier);
	}
	
}
