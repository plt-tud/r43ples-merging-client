package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.ColorDefinitions;
import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentAllIndividuals;

/**
 * The table cell renderer of semantic enrichment all individuals table.
 *  
 * @author Stephan Hensel
 *
 */
public class TableCellRendererSemanticEnrichmentAllIndividuals extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
		
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// Get the table model
		TableModelSemanticEnrichmentAllIndividuals tableModel = (TableModelSemanticEnrichmentAllIndividuals) table.getModel();
		
		// Get the table entry
		TableEntrySemanticEnrichmentAllIndividuals tableEntry =  tableModel.getTableEntry(row);
		
		// Get the resolution state
		ResolutionState resolutionState = Controller.getResolutionStateOfTableEntrySemanticEnrichmentAllIndividuals(tableEntry.getIndividualStructureA(), tableEntry.getIndividualStructureB());
		
		// Set the background color of the row
		Color color = Color.BLACK;
		if (resolutionState.equals(ResolutionState.RESOLVED)) {
			color = ColorDefinitions.approvedRowColor;
		} else if (resolutionState.equals(ResolutionState.DIFFERENCE)) {
			color = ColorDefinitions.nonConflictingRowColor;
		} else if (resolutionState.equals(ResolutionState.CONFLICT)) {
			color = ColorDefinitions.conflictingRowColor;
		} else {
			color = table.getBackground();
		}
		
		if (!isSelected) {
			cellComponent.setBackground(color);
			cellComponent.setForeground(Color.BLACK);
		} else {
			cellComponent.setBackground(ColorDefinitions.backgroundColorSelectedRow);
			cellComponent.setForeground(color);
		}

		// Replace URI by prefix if available
		String text = (String) value;
		if (!text.equals("")) {
			setValue(Controller.convertTripleStringToPrefixTripleString("<" + (String) value + ">"));
		}
		
		// Horizontal alignment
		setHorizontalAlignment(JLabel.CENTER);
		
		return cellComponent;
	}

}
