package de.tud.plt.r43ples.client.desktop.model.table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for summary report table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableModelSummaryReport extends AbstractTableModel {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** The table header. **/
	private static final String[] header = {"Subject", "Predicate", "Object", "State A (Revision)", "State B (Revision)", "Conflicting", "Automatic resolution state", "Resolution state", "Approved"};
	/** The row data list. **/
	private List<TableEntrySummaryReport> entries;

	
	/**
	 * The constructor.
	 * 
	 * @param entries the initial entries
	 */
	public TableModelSummaryReport(List<TableEntrySummaryReport> entries) {
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
		TableEntrySummaryReport entry = entries.get(rowIndex);
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
		if (column == 5) 
			return true;
		
		return true;
	}
	
	
	/**
	 * Add a row to the end of the model.
	 * 
	 * @param entry the table entry to add
	 */
	public void addRow(TableEntrySummaryReport entry) {
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
	public TableEntrySummaryReport getTableEntry(int rowIndex) {
		return entries.get(rowIndex);
	}

}
