package de.tud.plt.r43ples.client.desktop.model.table;

import java.awt.Color;

import de.tud.plt.r43ples.client.desktop.model.structure.Difference;

/**
 * Table entry of summary report table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntrySummaryReport {

	/** The difference. **/
	private Difference difference;
	/** The color **/
	private Color color;
	/** The row data. **/
	private Object[] rowData;
	
		
	/**
	 * The constructor.
	 * 
	 * @param difference the difference
	 * @param color the color
	 * @param rowData the row data
	 */
	public TableEntrySummaryReport(Difference difference, Color color, Object[] rowData) {
		this.difference = difference;
		this.color = color;
		this.rowData = rowData;
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
	 * Get the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}


	/**
	 * Set the color.
	 * 
	 * @param color the color
	 */
	public void setColor(Color color) {
		this.color = color;
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
