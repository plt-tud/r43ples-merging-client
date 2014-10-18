package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionHighLevelChanges;

/**
 * The table cell renderer for resolution high level changes table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererResolutionHighLevelChanges extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
		
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// Get the table model
		TableModelResolutionHighLevelChanges tableModel = (TableModelResolutionHighLevelChanges) table.getModel();
		
		// Get the table entry
		TableEntryHighLevelChanges tableEntry =  tableModel.getTableEntry(row);
				
		// Get the triple
		Triple triple = tableEntry.getHighLevelChangeRenaming().getDeletionDifference().getTriple();
		
		// Replace URI by prefix if available
		if (column == 0) {
			// Subject
			setValue(Controller.convertTripleStringToPrefixTripleString(Controller.getSubject(triple)));
		} else if (column == 1) {
			// Predicate
			setValue(Controller.convertTripleStringToPrefixTripleString(Controller.getPredicate(triple)));
		}
		
		// Border definitions
		int top = 0;
		int left = 0;
		int bottom = 0;
		int right = 0;
		
		// Set border right to predicate column
		if (column == 1) {
			right = 2;
		}
		
		// Set border right to renaming column
		if (column == 3) {
			right = 2;
		}
		
		// Set border
		cellComponent.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
		
		return cellComponent;
	}

}
