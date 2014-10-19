package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionHighLevelChanges;

/**
 * The table cell check box renderer of resolution high level changes table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellCheckBoxRendererResolutionHighLevelChanges extends JCheckBox implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public TableCellCheckBoxRendererResolutionHighLevelChanges() {
		setHorizontalAlignment(JLabel.CENTER);
		setBorderPainted(true);
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setSelected((value != null && ((Boolean) value).booleanValue()));
		
		// Get the table model
		TableModelResolutionHighLevelChanges tableModel = (TableModelResolutionHighLevelChanges) table.getModel();
		
		// Get the table entry
		TableEntryHighLevelChanges tableEntry =  tableModel.getTableEntry(row);
		
		// Get the deletion difference
		Difference deletionDifference = tableEntry.getHighLevelChangeRenaming().getDeletionDifference();
		
		// Get the addition difference
		Difference additionDifference = tableEntry.getHighLevelChangeRenaming().getAdditionDifference();		
		
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
			setBackground(color);
			setForeground(Color.BLACK);
		} else {
			setBackground(Color.BLACK);
			setForeground(color);
		}
		
		// Border definitions
		int top = 0;
		int left = 0;
		int bottom = 0;
		int right = 0;
		
		// Set border
		if (!isSelected) {
			setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
		} else {
			setBorder(new MatteBorder(top, left, bottom, right, Color.WHITE));
		}
		
		return this;
    }
}
