package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSummaryReport;

/**
 * The table cell renderer for summary report table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererSummaryReport extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
		
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Get the table model
		TableModelSummaryReport tableModel = (TableModelSummaryReport) table.getModel();
		
		// Get the table entry
		TableEntrySummaryReport tableEntry =  tableModel.getTableEntry(row);
				
		// Set the background color of the row
		if (!isSelected) {
			cellComponent.setBackground(tableEntry.getColor());
			cellComponent.setForeground(Color.BLACK);
		} else {
			cellComponent.setBackground(Color.BLACK);
			cellComponent.setForeground(tableEntry.getColor());
		}
		
		// Set the border between conflicting differences and non conflicting differences
		if ((row != tableModel.getRowCount() - 1) && (row > 0)) {
			if (!tableModel.getValueAt(row, 5).equals(tableModel.getValueAt(row - 1, 5))) {
				// Create top border
				cellComponent.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			}
		}
		
		// Get the triple
		Triple triple = tableEntry.getDifference().getTriple();
		
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
			} else {
				setIcon(new ImageIcon("images/icons/table/NoConflict.png"));
				setText("");
			}
		} else if ((column == 6) || (column == 7)) {
			String text = (String) value;
			if (text.startsWith(SDDTripleStateEnum.ADDED.toString())) {
				setIcon(new ImageIcon("images/icons/table/Added.png"));
				setText("");
			} else if (text.startsWith(SDDTripleStateEnum.DELETED.toString())) {
				setIcon(new ImageIcon("images/icons/table/Deleted.png"));
				setText("");
			}
		} else if (column == 8) {
			if (((String) value).equals("TRUE")) {
				setIcon(new ImageIcon("images/icons/table/Approved.png"));
				setText("");
			} else {
				setIcon(new ImageIcon("images/icons/table/NotApproved.png"));
				setText("");
			}
		} else {
			setIcon(null);
		}
		setHorizontalAlignment(JLabel.CENTER);
		
		// Border definitions
		int top = 0;
		int left = 0;
		int bottom = 0;
		int right = 0;
		
		// Set the border between conflicting differences and non conflicting differences
		if ((row != tableModel.getRowCount()) && (row > 0)) {
			if (!tableModel.getValueAt(row, 5).equals(tableModel.getValueAt(row - 1, 5))) {
				// Create top border
				top = 2;
			}
		}
		
		// Set border right to object column
		if (column == 2) {
			right = 2;
		}
		
		// Set border right to conflicting column
		if (column == 7) {
			right = 2;
		}
		
		// Set border
		if (!isSelected) {
			cellComponent.setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
		} else {
			cellComponent.setBorder(new MatteBorder(top, left, bottom, right, Color.WHITE));
		}
		
		return cellComponent;
	}

}
