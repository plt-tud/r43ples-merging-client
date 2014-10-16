package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
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
	
	
	/**
	 * The constructor.
	 */
	public TableCellComboBoxRendererSemanticEnrichmentClassTriples() {
		super();
		setOpaque(true);
	}
	

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
		
		// Get the combo box
		JComboBox<String> comboBox = (JComboBox<String>) this;
		
		// Get the table entry
		TableEntrySemanticEnrichmentClassTriples tableEntry = ((TableModelSemanticEnrichmentClassTriples) table.getModel()).getTableEntry(row);
		
		// Get the combo box model
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
		
		// Remove all entries
		model.removeAllElements();		
		// Get current table entry
		tableEntry = ((TableModelSemanticEnrichmentClassTriples) table.getModel()).getTableEntry(row);
		// Add options to model
		ArrayList<String> options = tableEntry.getSemanticResolutionOptions();
		if (!options.isEmpty()) {
			Iterator<String> iteOptions = options.iterator();
			while (iteOptions.hasNext()) {
				String currentOptions = iteOptions.next();
				model.addElement(currentOptions);
			}
			comboBox.setSelectedIndex(tableEntry.getSelectedSemanticResolutionOption());
			//table.setValueAt(options.get(tableEntry.getDefaultSemanticResolutionOption()), row, column);
		}

		return comboBox;
    }

}

