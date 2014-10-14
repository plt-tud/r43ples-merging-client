package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionTriples;

/**
 * The table cell renderer for resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererResolutionTriples extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
		
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// Get the table model
		TableModelResolutionTriples tableModel = (TableModelResolutionTriples) table.getModel();
						
		// Set the border between conflicting differences and non conflicting differences
		if ((row != tableModel.getRowCount() - 1) && (row > 0)) {
			if (!tableModel.getValueAt(row, 0).equals(tableModel.getValueAt(row - 1, 0))) {
				// Create top border
				cellComponent.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			}
		}
		
		// Replace URI by prefix if available
		if ((column >= 3) && (column <= 5)) {
			// Column 3 -> subject
			// Column 4 -> predicate
			// Column 5 -> object
			setValue(Controller.convertTripleStringToPrefixTripleString((String) value)); 
		}
		
		return cellComponent;
	}

}
