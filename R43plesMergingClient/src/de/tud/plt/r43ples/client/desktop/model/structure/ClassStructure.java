package de.tud.plt.r43ples.client.desktop.model.structure;

import java.util.HashMap;

/**
 * Stores all information of a class.
 * 
 * @author Stephan Hensel
 *
 */
public class ClassStructure {

	/** The class URI. **/
	private String classUri;
	/** The hash map which contains all corresponding triples of class. **/
	private HashMap<String, TripleClassStructure> triples;
	
	
	/**
	 * The constructor.
	 * 
	 * @param classUri the class URI
	 */
	public ClassStructure(String classUri) {
		this.setClassUri(classUri);
		triples = new HashMap<String, TripleClassStructure>();
	}


	/**
	 * Get the class URI.
	 * 
	 * @return the class URI
	 */
	public String getClassUri() {
		return classUri;
	}


	/**
	 * Set the class URI.
	 * 
	 * @param classUri the class URI to set
	 */
	public void setClassUri(String classUri) {
		this.classUri = classUri;
	}


	/**
	 * Get the corresponding triples of class.
	 * 
	 * @return the triples
	 */
	public HashMap<String, TripleClassStructure> getTriples() {
		return triples;
	}


	/**
	 * Set the corresponding triples of class.
	 * 
	 * @param triples the triples to set
	 */
	public void setTriples(HashMap<String, TripleClassStructure> triples) {
		this.triples = triples;
	}
	
	
	/**
	 * Add a triple. If the triple identifier already exists the old triple will be overwritten.
	 * 
	 * @param identifier the identifier (triple to string)
	 * @param triple the triple
	 */
	public void addTriple(String identifier, TripleClassStructure triple) {
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
