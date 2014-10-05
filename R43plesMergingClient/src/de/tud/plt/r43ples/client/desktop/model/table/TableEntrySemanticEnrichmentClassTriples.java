package de.tud.plt.r43ples.client.desktop.model.table;

import java.util.ArrayList;

import de.tud.plt.r43ples.client.desktop.model.structure.Difference;

/**
 * Table entry of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntrySemanticEnrichmentClassTriples {

	/** The difference. **/
	private Difference difference;
	/** The semantic description. **/
	private String semanticDescription;
	/** The semantic resolution options. **/
	private ArrayList<String> semanticResolutionOptions;
	/** The default semantic resolution option. **/
	private int defaultSemanticResolutionOption;
	/** The row data. **/
	private Object[] rowData;
	
		
	/**
	 * The constructor.
	 * 
	 * @param difference the difference
	 * @param rowData the row data
	 */
	public TableEntrySemanticEnrichmentClassTriples(Difference difference, String semanticDescription, ArrayList<String> semanticResolutionOptions, int defaultSemanticResolutionOption, Object[] rowData) {
		this.setDifference(difference);
		this.setSemanticDescription(semanticDescription);
		this.setSemanticResolutionOptions(semanticResolutionOptions);
		this.setDefaultSemanticResolutionOption(defaultSemanticResolutionOption);
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
	 * Get the semantic description.
	 * 
	 * @return the semantic description
	 */
	public String getSemanticDescription() {
		return semanticDescription;
	}


	/**
	 * Set the semantic description.
	 * 
	 * @param semanticDescription the semantic description to set
	 */
	public void setSemanticDescription(String semanticDescription) {
		this.semanticDescription = semanticDescription;
	}


	/**
	 * Get the semantic resolution options.
	 * 
	 * @return the semantic resolution options
	 */
	public ArrayList<String> getSemanticResolutionOptions() {
		return semanticResolutionOptions;
	}


	/**
	 * Set the semantic resolution options.
	 * 
	 * @param semanticResolutionOptions the semantic resolution options to set
	 */
	public void setSemanticResolutionOptions(ArrayList<String> semanticResolutionOptions) {
		this.semanticResolutionOptions = semanticResolutionOptions;
	}


	/**
	 * Get the default semantic resolution option.
	 * 
	 * @return the default semantic resolution option
	 */
	public int getDefaultSemanticResolutionOption() {
		return defaultSemanticResolutionOption;
	}


	/**
	 * Set the default semantic resolution option.
	 * 
	 * @param defaultSemanticResolutionOption the default semantic resolution option to set
	 */
	public void setDefaultSemanticResolutionOption(int defaultSemanticResolutionOption) {
		this.defaultSemanticResolutionOption = defaultSemanticResolutionOption;
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
