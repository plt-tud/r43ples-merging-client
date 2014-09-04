package de.tud.plt.r43ples.client.desktop.model;

import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;

/**
 * Tree node object. Stores the tree node data.
 * 
 * @author Stephan Hensel
 *
 */
public class TreeNodeObject {

	/** The text to display. **/
	private String text;
	/** The resolution state of the node. **/
	private ResolutionState resolutionState;	
	
	
	/**
	 * The constructor.
	 * 
	 * @param text the text
	 * @param resolutionState the resolution state
	 */
	public TreeNodeObject(String text, ResolutionState resolutionState) {
		this.setText(text);
		this.setResolutionState(resolutionState);
	}


	/**
	 * Get the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * Set the text.
	 * 
	 * @param text the text
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * Get the resolution state.
	 * 
	 * @return the resolution state
	 */
	public ResolutionState getResolutionState() {
		return resolutionState;
	}


	/**
	 * Set the resolution state.
	 * 
	 * @param resolutionState the resolution state
	 */
	public void setResolutionState(ResolutionState resolutionState) {
		this.resolutionState = resolutionState;
	}
	
}
