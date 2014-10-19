package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
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
		
		// Get the deletion difference
		Difference deletionDifference = tableEntry.getHighLevelChangeRenaming().getDeletionDifference();
		
		// Get the addition difference
		Difference additionDifference = tableEntry.getHighLevelChangeRenaming().getAdditionDifference();		
		
		// Get the triple
		Triple triple = tableEntry.getHighLevelChangeRenaming().getDeletionDifference().getTriple();
		
		// Get the current check box state
		Boolean checkBoxStateBool = (Boolean) tableModel.getTableEntry(row).getRowData()[4];
		
		// Set the background color of the row
		Color color = Color.BLACK;		
		if (deletionDifference.getResolutionState().equals(ResolutionState.RESOLVED) && additionDifference.getResolutionState().equals(ResolutionState.RESOLVED)) {
			if ((checkBoxStateBool.booleanValue() && deletionDifference.getTripleResolutionState().equals(SDDTripleStateEnum.DELETED) && additionDifference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
					(!checkBoxStateBool.booleanValue() && deletionDifference.getTripleResolutionState().equals(SDDTripleStateEnum.ADDED) && additionDifference.getTripleResolutionState().equals(SDDTripleStateEnum.DELETED))) {
				// Entry is resolved and approved
				color = Color.GREEN;
			} else {
				// Entry resolution changed old is approved
				color = Color.ORANGE;
			}
		} else {
			DifferenceGroup differenceGroup = Controller.getDifferenceGroupOfDifference(additionDifference);
			if ((checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
					(!checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.DELETED))) {
				// Entry is not changed and not approved
				color = table.getBackground();
			} else {
				// Entry is changed but not approved
				color = Color.LIGHT_GRAY;
			}
		}
		
		if (!isSelected) {
			cellComponent.setBackground(color);
			cellComponent.setForeground(Color.BLACK);
		} else {
			cellComponent.setBackground(Color.BLACK);
			cellComponent.setForeground(color);
		}
		
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
