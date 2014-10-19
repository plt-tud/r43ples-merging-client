package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
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
		// Get the table model
		TableModelSemanticEnrichmentClassTriples tableModel = (TableModelSemanticEnrichmentClassTriples) table.getModel();
		
		// Get the table entry
		TableEntrySemanticEnrichmentClassTriples tableEntry =  tableModel.getTableEntry(row);
		
		// Check if difference exists
		if (tableEntry.getDifference() != null) {
			// Get the difference
			Difference difference = tableEntry.getDifference();
			
			// Set the background color of the row
			Color color = Color.BLACK;
			// Get the selected option
			int selectedOption = tableEntry.getSelectedSemanticResolutionOption();
			if (difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
				if (((selectedOption == 1) && difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
						(!(selectedOption == 1) && difference.getTripleResolutionState().equals(SDDTripleStateEnum.DELETED))) {
					// Entry is resolved and approved
					color = Color.GREEN;
				} else {
					// Entry resolution changed old is approved
					color = Color.ORANGE;
				}
			} else {
				DifferenceGroup differenceGroup = Controller.getDifferenceGroupOfDifference(difference);
				if (((selectedOption == 1) && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
						(!(selectedOption == 1) && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.DELETED))) {
					// Entry is not changed and not approved
					color = table.getBackground();
				} else {
					// Entry is changed but not approved
					color = Color.LIGHT_GRAY;
				}
			}
			
			if (!isSelected) {
				setBackground(color);
				setForeground(Color.BLACK);
			} else {
				setBackground(Color.BLACK);
				setForeground(color);
			}
		}

		// Get the combo box
		JComboBox<String> comboBox = (JComboBox<String>) this;
		
		// Get the combo box model
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
		
		// Remove all entries
		model.removeAllElements();
		// Add options to model
		ArrayList<String> options = tableEntry.getSemanticResolutionOptions();
		if (!options.isEmpty()) {
			Iterator<String> iteOptions = options.iterator();
			while (iteOptions.hasNext()) {
				String currentOptions = iteOptions.next();
				model.addElement(currentOptions);
			}
			comboBox.setSelectedIndex(tableEntry.getSelectedSemanticResolutionOption());
		}

		return comboBox;
    }

}

