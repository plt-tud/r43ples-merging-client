package de.tud.plt.r43ples.client.desktop.model.table.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;

/**
 * Table model for resolution high level changes table.
 * Column 6 will contain checkboxes.
 * 
 * @author Stephan Hensel
 *
 */
public class TableModelResolutionHighLevelChanges extends AbstractTableModel {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** The table header. **/
	private static final String[] header = {"Subject", "Predicate", "Object", "Object will be renamed to", "Rename yes or no"};
	/** The row data list. **/
	private List<TableEntryHighLevelChanges> entries;

	
	/**
	 * The constructor.
	 * 
	 * @param entries the initial entries
	 */
	public TableModelResolutionHighLevelChanges(List<TableEntryHighLevelChanges> entries) {
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
		if (column == 4) {
			return Boolean.class;
		}
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
		TableEntryHighLevelChanges entry = entries.get(rowIndex);
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
		if (column == 4) 
			return true;
		
		return false;
	}
	
	
	/**
	 * Add a row to the end of the model.
	 * 
	 * @param entry the table entry to add
	 */
	public void addRow(TableEntryHighLevelChanges entry) {
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
	public TableEntryHighLevelChanges getTableEntry(int rowIndex) {
		return entries.get(rowIndex);
	}

}
