package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.ColorDefinitions;
import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentIndividualTriples;

/**
 * The table renderer of semantic enrichment individual triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererSemanticEnrichmentIndividualTriples extends DefaultTableCellRenderer  {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Get the table model
		TableModelSemanticEnrichmentIndividualTriples tableModel = (TableModelSemanticEnrichmentIndividualTriples) table.getModel();
		
		// Get the table entry
		TableEntrySemanticEnrichmentIndividualTriples tableEntry =  tableModel.getTableEntry(row);
		
		if (!isSelected) {
			cellComponent.setBackground(table.getBackground());
			cellComponent.setForeground(table.getForeground());
		} else {
			cellComponent.setBackground(ColorDefinitions.backgroundColorSelectedRow);
			cellComponent.setForeground(Color.WHITE);
		}
		
		// Check if difference exists
		if (tableEntry.getDifference() != null) {
			// Get the difference
			Difference difference = tableEntry.getDifference();
			
			// Get the triple			
			Triple triple = difference.getTriple();
			
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
				cellComponent.setBackground(color);
				cellComponent.setForeground(Color.BLACK);
			} else {
				cellComponent.setBackground(ColorDefinitions.backgroundColorSelectedRow);
				cellComponent.setForeground(color);
			}
			
			// Replace URI by prefix if available
			if (column == 0) {
				// Subject
				setValue(Controller.convertTripleStringToPrefixTripleString(Controller.getSubject(triple)));
			} else if (column == 1) {
				// Predicate
				setValue(Controller.convertTripleStringToPrefixTripleString(Controller.getPredicate(triple)));
			} else if (column == 2) {
				// Object
				setValue(Controller.convertTripleStringToPrefixTripleString(Controller.getObject(triple)));
			}
		}
		
		// Set the icon
		if ((column == 3) || (column == 4)) {
			String text = (String) value;
			if (text.startsWith(SDDTripleStateEnum.ADDED.toString())) {
				setIcon(new ImageIcon("images/icons/table/Added.png"));
				setText(text.substring(text.indexOf("("), text.length()));
			} else if (text.startsWith(SDDTripleStateEnum.DELETED.toString())) {
				setIcon(new ImageIcon("images/icons/table/Deleted.png"));
				setText(text.substring(text.indexOf("("), text.length()));
			} else if (text.startsWith(SDDTripleStateEnum.NOTINCLUDED.toString())) {
				setIcon(new ImageIcon("images/icons/table/NotIncluded.png"));
				setText("");
			} else if (text.startsWith(SDDTripleStateEnum.ORIGINAL.toString())) {
				setIcon(new ImageIcon("images/icons/table/Original.png"));
				setText(text.substring(text.indexOf("("), text.length()));
			}
		} else if (column == 5) {
			if (((String) value).equals("TRUE")) {
				setIcon(new ImageIcon("images/icons/table/Conflict.png"));
				setText("");
			} else if (((String) value).equals("FALSE")) {
				setIcon(new ImageIcon("images/icons/table/NoConflict.png"));
				setText("");
			} else {
				setIcon(null);
			}
		} else {
			setIcon(null);
		}
		
		// Horizontal alignment
		setHorizontalAlignment(JLabel.CENTER);
		
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
		cellComponent.setBorder(border);
		
		return cellComponent;
    }

}

