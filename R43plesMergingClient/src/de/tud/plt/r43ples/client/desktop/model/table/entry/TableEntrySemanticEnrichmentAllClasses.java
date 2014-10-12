package de.tud.plt.r43ples.client.desktop.model.table.entry;

import de.tud.plt.r43ples.client.desktop.model.structure.ClassStructure;

/**
 * Table entry of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableEntrySemanticEnrichmentAllClasses {

	/** The class structure A. **/
	private ClassStructure classStructureA;
	/** The class structure B. **/
	private ClassStructure classStructureB;
	/** The row data. **/
	private Object[] rowData;
	
		
	/**
	 * The constructor.
	 * 
	 * @param classStructureA the class structure A
	 * @param classStructureB the class structure B
	 * @param rowData the row data
	 */
	public TableEntrySemanticEnrichmentAllClasses(ClassStructure classStructureA, ClassStructure classStructureB, Object[] rowData) {
		this.setClassStructureA(classStructureA);
		this.setClassStructureB(classStructureB);
		this.setRowData(rowData);
	}


	/**
	 * Get the class structure A.
	 * 
	 * @return the class structure A
	 */
	public ClassStructure getClassStructureA() {
		return classStructureA;
	}


	/**
	 * Set the class structure A.
	 * 
	 * @param classStructure the class structure A to set
	 */
	public void setClassStructureA(ClassStructure classStructure) {
		this.classStructureA = classStructure;
	}
	
	
	/**
	 * Get the class structure B.
	 * 
	 * @return the class structure B
	 */
	public ClassStructure getClassStructureB() {
		return classStructureB;
	}


	/**
	 * Set the class structure B.
	 * 
	 * @param classStructure the class structure B to set
	 */
	public void setClassStructureB(ClassStructure classStructure) {
		this.classStructureB = classStructure;
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
