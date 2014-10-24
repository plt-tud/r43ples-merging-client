package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.ColorDefinitions;
import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentIndividualTriples;

/**
 * The table combo box renderer of semantic enrichment individual triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellComboBoxRendererSemanticEnrichmentIndividualTriples extends JComboBox<String> implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * The constructor.
	 */
	public TableCellComboBoxRendererSemanticEnrichmentIndividualTriples() {
		super();
		setOpaque(true);
	}
	

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// Get the table model
		TableModelSemanticEnrichmentIndividualTriples tableModel = (TableModelSemanticEnrichmentIndividualTriples) table.getModel();
		
		// Get the table entry
		TableEntrySemanticEnrichmentIndividualTriples tableEntry =  tableModel.getTableEntry(row);
		
		if (!isSelected) {
			setBackground(table.getBackground());
			setForeground(table.getForeground());
		} else {
			setBackground(ColorDefinitions.backgroundColorSelectedRow);
			setForeground(Color.WHITE);
		}
		
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
					color = ColorDefinitions.approvedRowColor;
				} else {
					// Entry resolution changed old is approved
					color = ColorDefinitions.approvedButChangedRowColor;
				}
			} else {
				DifferenceGroup differenceGroup = Controller.getDifferenceGroupOfDifference(difference);
				if (((selectedOption == 1) && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
						(!(selectedOption == 1) && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.DELETED))) {
					// Entry is not changed and not approved
					if (differenceGroup.isConflicting()) {
						color = ColorDefinitions.conflictingRowColor;
					} else {
						color = ColorDefinitions.nonConflictingRowColor;
					}
				} else {
					// Entry is changed but not approved
					color = ColorDefinitions.notApprovedButChangedRowColor;
				}
			}
			
			if (!isSelected) {
				setBackground(color);
				setForeground(Color.BLACK);
			} else {
				setBackground(ColorDefinitions.backgroundColorSelectedRow);
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
		
		// Border definitions
		int top = 0;
		int left = 0;
		int bottom = 0;
		int right = 0;
		
		// Set the border between conflicting differences and non conflicting differences
		if (row > 0) {
			if (!tableModel.getValueAt(row, 5).equals(tableModel.getValueAt(row - 1, 5))) {
				// Create top border
				top = 1;
			}
		}
		if (row != tableModel.getRowCount() - 1) {
			if (!tableModel.getValueAt(row, 5).equals(tableModel.getValueAt(row + 1, 5))) {
				// Create bottom border
				bottom = 1;
			}
		}
		
		// Set border right to object column
		if (column == 2) {
			right = 1;
		}
		if (column == 3) {
			left = 1;
		}
		
		// Set border right to conflicting column
		if (column == 6) {
			right = 1;
		}
		if (column == 7) {
			left = 1;
		}
		
		Border border = BorderFactory.createCompoundBorder();

        border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
        
		// Set border
		if (!isSelected) {
			border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
		} else {
			border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(top, left, bottom, right, Color.WHITE));
			if (column == 0) {
				left = 1;
			} else { 
				left = 0;
			}
			if (column == tableModel.getColumnCount() - 1) {
				right = 1;
			} else {
				right = 0;
			}
			
			top = 1;
			bottom = 1;			
			border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(top, left, bottom, right, ColorDefinitions.borderColorSelectedRow));
		}
		setBorder(border);

		return comboBox;
    }

}

