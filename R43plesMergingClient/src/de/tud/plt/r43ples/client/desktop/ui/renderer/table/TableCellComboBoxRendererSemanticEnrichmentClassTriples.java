package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentClassTriples;

/**
 * The table combo box renderer of semantic enrichment class triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellComboBoxRendererSemanticEnrichmentClassTriples extends JComboBox<String> implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			//super.setBackground(table.getSelectionBackground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		// Get current table entry
		TableEntrySemanticEnrichmentClassTriples tableEntry = ((TableModelSemanticEnrichmentClassTriples) table.getModel()).getTableEntry(row);
		ArrayList<String> options = tableEntry.getSemanticResolutionOptions();
		if (!options.isEmpty()) {
			table.setValueAt(options.get(tableEntry.getDefaultSemanticResolutionOption()), row, column);
		}
		
		return this;
    }

}

