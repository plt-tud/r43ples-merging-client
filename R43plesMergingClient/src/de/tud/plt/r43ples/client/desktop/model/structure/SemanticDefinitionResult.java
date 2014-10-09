package de.tud.plt.r43ples.client.desktop.model.structure;

import java.util.ArrayList;

/**
 * Stores the semantic definition result.
 * 
 * @author Stephan Hensel
 *
 */
public class SemanticDefinitionResult {

	/** The semantic description. **/
	private String semanticDescription;
	/** The semantic resolution options. **/
	private ArrayList<String> semanticResolutionOptions;
	/** The default semantic resolution option. **/
	private int defaultSemanticResolutionOption;
	
	
	/**
	 * The constructor.
	 * 
	 * @param semanticDescription the semantic description
	 * @param semanticResolutionOptions the semantic resolution options
	 * @param defaultSemanticResolutionOption the default semantic resolution option
	 */
	public SemanticDefinitionResult(String semanticDescription, ArrayList<String> semanticResolutionOptions, int defaultSemanticResolutionOption) {
		this.semanticDescription = semanticDescription;
		this.semanticResolutionOptions = semanticResolutionOptions;
		this.defaultSemanticResolutionOption = defaultSemanticResolutionOption;
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
	public void setSemanticResolutionOptions(
			ArrayList<String> semanticResolutionOptions) {
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
	
}
