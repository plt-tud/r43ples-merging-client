package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.ColorDefinitions;
import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionTriples;

/**
 * The table cell check box renderer of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellCheckBoxRendererResolutionTriples extends JCheckBox implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public TableCellCheckBoxRendererResolutionTriples() {
		setHorizontalAlignment(JLabel.CENTER);
		setBorderPainted(true);
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setSelected((value != null && ((Boolean) value).booleanValue()));
		
		// Get the table model
		TableModelResolutionTriples tableModel = (TableModelResolutionTriples) table.getModel();
		
		// Get the table entry
		TableEntry tableEntry =  tableModel.getTableEntry(row);
				
		// Get the difference
		Difference difference = tableEntry.getDifference();
		
		// Get the current check box state
		Boolean checkBoxStateBool = (Boolean) tableModel.getTableEntry(row).getRowData()[6];
		
		// Set the background color of the row
		Color color = Color.BLACK;
		if (difference.getResolutionState().equals(ResolutionState.RESOLVED)) {
			if ((checkBoxStateBool.booleanValue() && difference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
					(!checkBoxStateBool.booleanValue() && difference.getTripleResolutionState().equals(SDDTripleStateEnum.DELETED))) {
				// Entry is resolved and approved
				color = ColorDefinitions.approvedRowColor;
			} else {
				// Entry resolution changed old is approved
				color = ColorDefinitions.approvedButChangedRowColor;
			}
		} else {
			DifferenceGroup differenceGroup = Controller.getDifferenceGroupOfDifference(difference);
			if ((checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
					(!checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.DELETED))) {
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
		if (column == 5) {
			right = 1;
		}
		if (column == 6) {
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
		
		return this;
    }
}
