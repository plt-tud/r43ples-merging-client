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
	/** The object which stores all information. **/
	private Object object;
	
	
	/**
	 * The constructor.
	 * 
	 * @param text the text
	 * @param resolutionState the resolution state
	 * @param object the object
	 */
	public TreeNodeObject(String text, ResolutionState resolutionState, Object object) {
		this.setText(text);
		this.setResolutionState(resolutionState);
		this.object = object;
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


	/**
	 * Get the object.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}


	/**
	 * Set the object.
	 * 
	 * @param object the object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
}
