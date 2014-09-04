package de.tud.plt.r43ples.client.desktop.model;

/**
 * Table entry of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntry {

	/** The tree node object. **/
	private TreeNodeObject nodeObject;
	/** The row data. **/
	private Object[] rowData;
	
		
	/**
	 * The constructor.
	 * 
	 * @param nodeObject the node object
	 * @param rowData the row data
	 */
	public TableEntry(TreeNodeObject nodeObject, Object[] rowData) {
		this.setNodeObject(nodeObject);
		this.setRowData(rowData);
	}


	/**
	 * Get the node object.
	 * 
	 * @return the node object
	 */
	public TreeNodeObject getNodeObject() {
		return nodeObject;
	}


	/**
	 * Set the node object.
	 * 
	 * @param nodeObject the node object
	 */
	public void setNodeObject(TreeNodeObject nodeObject) {
		this.nodeObject = nodeObject;
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
