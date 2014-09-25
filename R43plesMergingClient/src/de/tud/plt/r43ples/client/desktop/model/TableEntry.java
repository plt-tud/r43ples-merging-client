package de.tud.plt.r43ples.client.desktop.model;

import javax.swing.tree.TreePath;

/**
 * Table entry of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntry {

	/** The difference. **/
	private Difference difference;
	/** The tree node object. **/
	private TreeNodeObject nodeObject;
	/** The tree path. **/
	private TreePath treePath;
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
	public TableEntry(Difference difference, TreeNodeObject nodeObject, TreePath treePath , Object[] rowData) {
		this.setDifference(difference);
		this.setNodeObject(nodeObject);
		this.setTreePath(treePath);
		this.setRowData(rowData);
	}


	/**
	 * Get the difference.
	 * 
	 * @return the difference
	 */
	public Difference getDifference() {
		return difference;
	}


	/**
	 * Set the difference.
	 * 
	 * @param difference the difference to set
	 */
	public void setDifference(Difference difference) {
		this.difference = difference;
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
	 * Get the tree path.
	 * 
	 * @return the treePath
	 */
	public TreePath getTreePath() {
		return treePath;
	}


	/**
	 * Set the tree path.
	 * 
	 * @param treePath the treePath to set
	 */
	public void setTreePath(TreePath treePath) {
		this.treePath = treePath;
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
