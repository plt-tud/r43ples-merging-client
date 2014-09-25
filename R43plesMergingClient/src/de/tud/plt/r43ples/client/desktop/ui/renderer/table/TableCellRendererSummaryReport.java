package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.model.table.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.TableModelSummaryReport;

/**
 * The table cell renderer for summary report table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererSummaryReport extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
		
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Get the table model
		TableModelSummaryReport tableModel = (TableModelSummaryReport) table.getModel();
		
		// Get the table entry
		TableEntrySummaryReport tableEntry =  tableModel.getTableEntry(row);
				
		// Set the background color of the row
		if (!isSelected) {
			cellComponent.setBackground(tableEntry.getColor());
			cellComponent.setForeground(Color.BLACK);
		} else {
			cellComponent.setBackground(Color.BLACK);
			cellComponent.setForeground(tableEntry.getColor());
		}
		
		// Set the border between conflicting differences and non conflicting differences
		if ((row != tableModel.getRowCount() - 1) && (row > 0)) {
			if (!tableModel.getValueAt(row, 5).equals(tableModel.getValueAt(row - 1, 5))) {
				// Create top border
				cellComponent.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			}
		}		
		
		return cellComponent;
	}

}
