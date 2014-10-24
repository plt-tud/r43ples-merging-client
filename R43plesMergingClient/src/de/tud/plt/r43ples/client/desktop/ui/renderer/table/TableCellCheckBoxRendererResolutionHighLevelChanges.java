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
				color = ColorDefinitions.approvedRowColor;
			} else {
				// Entry resolution changed old is approved
				color = ColorDefinitions.approvedButChangedRowColor;
			}
		} else {
			DifferenceGroup differenceGroup = Controller.getDifferenceGroupOfDifference(additionDifference);
			if ((checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.ADDED)) || 
					(!checkBoxStateBool.booleanValue() && differenceGroup.getAutomaticResolutionState().equals(SDDTripleStateEnum.DELETED))) {
				// Entry is not changed and not approved
				color = table.getBackground();
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
		
		// Set border right to predicate column
		if (column == 1) {
			right = 1;
		}
		if (column == 2) {
			left = 1;
		}
		
		// Set border right to renaming column
		if (column == 3) {
			right = 1;
		}
		if (column == 4) {
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
