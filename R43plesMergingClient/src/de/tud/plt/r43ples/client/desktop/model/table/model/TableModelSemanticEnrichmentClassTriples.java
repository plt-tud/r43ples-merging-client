package de.tud.plt.r43ples.client.desktop.model.table.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentClassTriples;

/**
 * Table model for semantic enrichment table which contains all triples of selected class.
 * Column 7 will contain combo boxes.
 * 
 * @author Stephan Hensel
 *
 */
public class TableModelSemanticEnrichmentClassTriples extends AbstractTableModel {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** The table header. **/
	private static final String[] header = {"Subject", "Predicate", "Object", "State A (Revision)", "State B (Revision)", "Conflicting", "Semantic description", "Semantic resolution"};
	/** The row data list. **/
	private List<TableEntrySemanticEnrichmentClassTriples> entries;

	
	/**
	 * The constructor.
	 * 
	 * @param entries the initial entries
	 */
	public TableModelSemanticEnrichmentClassTriples(List<TableEntrySemanticEnrichmentClassTriples> entries) {
		this.entries = entries;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
    	return header[column];
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		return super.getColumnClass(column);		
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return header.length;
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return entries.size();
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TableEntrySemanticEnrichmentClassTriples entry = entries.get(rowIndex);
		
		if (columnIndex == 7) {
			if (!entry.getSemanticResolutionOptions().isEmpty()) {
				return entry.getSemanticResolutionOptions().get(entry.getSelectedSemanticResolutionOption());
			}
		}

		return entry.getRowData()[columnIndex];
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        entries.get(rowIndex).getRowData()[columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);// notify listeners
    }
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 7) 
			return true;
		
		return false;
	}
	
	
	/**
	 * Add a row to the end of the model.
	 * 
	 * @param entry the table entry to add
	 */
	public void addRow(TableEntrySemanticEnrichmentClassTriples entry) {
		entries.add(entry);
	}
	
	
	/**
	 * Remove a row of the model.
	 * 
	 * @param row the row to remove
	 */
	public void removeRow(int row) {
		entries.remove(row);
	}


	/**
	 * Remove all elements.
	 */
	public void removeAllElements() {
		entries.clear();		
	}
	
	
	/**
	 * Get table entry.
	 * 
	 * @param rowIndex the row index of the entry
	 * @return the table entry
	 */
	public TableEntrySemanticEnrichmentClassTriples getTableEntry(int rowIndex) {
		return entries.get(rowIndex);
	}

}