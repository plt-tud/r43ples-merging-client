package de.tud.plt.r43ples.client.desktop.model.tree;

import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;

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
	private boolean selected = false;
	
	/**
	 * The constructor.
	 * 
	 * @param text the text
	 * @param resolutionState the resolution state
	 * @param object the object
	 */
	public TreeNodeObject(String text, ResolutionState resolutionState, Object object) {
		this.setText(text);
		this.setObject(object);
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
		if (object != null) {
			if (object.getClass().equals(Difference.class)) {
				return ((Difference) object).getResolutionState();
			}
		}
		return resolutionState;
	}


	/**
	 * Set the resolution state.
	 * 
	 * @param resolutionState the resolution state
	 */
	public void setResolutionState(ResolutionState resolutionState) {
		if (object != null) {
			if (object.getClass().equals(Difference.class)) {
				((Difference) object).setResolutionState(resolutionState);
			}
		}
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


	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
