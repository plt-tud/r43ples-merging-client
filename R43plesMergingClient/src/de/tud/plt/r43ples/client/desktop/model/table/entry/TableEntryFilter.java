package de.tud.plt.r43ples.client.desktop.model.table.entry;

/**
 * Table entry of filter table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntryFilter {

	/** The text to display. **/
	private String text;
	/** The check state. **/
	private boolean checked;
	
		
	/**
	 * The constructor.
	 * 
	 * @param text the text to display
	 * @param checked the check state
	 */
	public TableEntryFilter(String text, boolean checked) {
		this.text = text;
		this.checked = checked;
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
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}


	/**
	 * Get the check state.
	 * 
	 * @return the check state
	 */
	public boolean isChecked() {
		return checked;
	}


	/**
	 * Set the check state.
	 * 
	 * @param checked the check state to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
