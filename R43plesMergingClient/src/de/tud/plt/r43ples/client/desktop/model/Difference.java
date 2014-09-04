package de.tud.plt.r43ples.client.desktop.model;

import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;

/**
 * Stores all information of a difference.
 * 
 * @author Stephan Hensel
 *
 */
public class Difference {

	/** The triple. **/
	private Triple triple;
	/** The referenced revision URI in A. Characterizes in which revision the difference . **/
	private String referencedRevisionA;
	/** The referenced revision URI in B. Characterizes in which revision the difference . **/
	private String referencedRevisionB;
	/** The resolution state. **/
	private SDDTripleStateEnum resolutionState;
	
	
	/**
	 * The constructor.
	 * 
	 * @param triple the triple
	 * @param referencedRevisionA the referenced revision in A
	 * @param referencedRevisionB the referenced revision in B
	 * @param resolutionState the resolution state
	 */
	public Difference(Triple triple, String referencedRevisionA, String referencedRevisionB, SDDTripleStateEnum resolutionState) {
		this.triple = triple;
		this.referencedRevisionA = referencedRevisionA;
		this.referencedRevisionB = referencedRevisionB;
		this.setResolutionState(resolutionState);
	}


	/**
	 * Get the triple
	 * 
	 * @return the triple
	 */
	public Triple getTriple() {
		return triple;
	}


	/**
	 * Set the triple
	 * 
	 * @param triple the triple
	 */
	public void setTriple(Triple triple) {
		this.triple = triple;
	}


	/**
	 * Get the referenced revision in A.
	 * 
	 * @return the referenced revision in A
	 */
	public String getReferencedRevisionA() {
		return referencedRevisionA;
	}


	/**
	 * Set the referenced revision in A.
	 * 
	 * @param referencedRevisionA the referenced revision in A
	 */
	public void setReferencedRevisionA(String referencedRevisionA) {
		this.referencedRevisionA = referencedRevisionA;
	}


	/**
	 * Get the referenced revision in B.
	 * 
	 * @return the referenced revision in B
	 */
	public String getReferencedRevisionB() {
		return referencedRevisionB;
	}


	/**
	 * Set the referenced revision in B.
	 * 
	 * @param referencedRevisionA the referenced revision in B
	 */
	public void setReferencedRevisionB(String referencedRevisionB) {
		this.referencedRevisionB = referencedRevisionB;
	}


	/**
	 * Get the resolution state.
	 * 
	 * @return the resolution state
	 */
	public SDDTripleStateEnum getResolutionState() {
		return resolutionState;
	}


	/**
	 * Set the resolution state.
	 * 
	 * @param resolutionState the resolution state
	 */
	public void setResolutionState(SDDTripleStateEnum resolutionState) {
		this.resolutionState = resolutionState;
	}
	
}
