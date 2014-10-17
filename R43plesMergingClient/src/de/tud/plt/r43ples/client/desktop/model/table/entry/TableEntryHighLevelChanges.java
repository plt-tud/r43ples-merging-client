package de.tud.plt.r43ples.client.desktop.model.table.entry;

import de.tud.plt.r43ples.client.desktop.model.structure.HighLevelChangeRenaming;

/**
 * Table entry of resolution high level changes table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntryHighLevelChanges {

	/** The high level change (renaming). **/
	private HighLevelChangeRenaming highLevelChangeRenaming;
	/** The row data. **/
	private Object[] rowData;
	
		
	/**
	 * The constructor.
	 * 
	 * @param difference the difference
	 * @param nodeObject the node object
	 * @param treePath the tree path
	 * @param rowData the row data
	 */
	public TableEntryHighLevelChanges(HighLevelChangeRenaming highLevelChangeRenaming, Object[] rowData) {
		this.setHighLevelChangeRenaming(highLevelChangeRenaming);
		this.setRowData(rowData);
	}


	/**
	 * Get the high level change (renaming).
	 * 
	 * @return the high level change (renaming)
	 */
	public HighLevelChangeRenaming getHighLevelChangeRenaming() {
		return highLevelChangeRenaming;
	}


	/**
	 * Set the high level change (renaming).
	 * 
	 * @param highLevelChangeRenaming the high level change (renaming) to set
	 */
	public void setHighLevelChangeRenaming(HighLevelChangeRenaming highLevelChangeRenaming) {
		this.highLevelChangeRenaming = highLevelChangeRenaming;
	}
	

	/**
	 * Get the row data.
	 * 
	 * @return the row data
	 */
	public Object[] getRowData() {
		return rowData;
	}


	/**
	 * Set the row data.
	 * 
	 * @param rowData the row data
	 */
	public void setRowData(Object[] rowData) {
		this.rowData = rowData;
	}
	
}
