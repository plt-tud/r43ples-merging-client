package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.ColorDefinitions;
import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSummaryReport;

/**
 * The table cell renderer for summary report table.
 * Only columns 3 and 4 - will return a panel.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellPanelRendererSummaryReport implements TableCellRenderer {

	/** The table cell panel. **/
	private static JPanel panel;
	/** The space panel. **/
	private static JPanel panelSpace;
	/** The content panel. **/
	private static JPanel panelContent;
	/** The icon label. **/
	private static JLabel lblIcon;
	/** The text label. **/
	private static JLabel lblText;
	
	
	/**
	 * The constructor.
	 */
	public TableCellPanelRendererSummaryReport() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));

		panelSpace = new JPanel();
		panel.add(panelSpace, BorderLayout.WEST);
		
		panelContent = new JPanel();
		panelContent.setLayout(new BorderLayout(0, 0));
		panel.add(panelContent, BorderLayout.CENTER);
		
		lblIcon = new JLabel();
		lblIcon.setHorizontalAlignment(SwingConstants.LEFT);
		panelContent.add(lblIcon, BorderLayout.WEST);
		
		lblText = new JLabel();
		lblText.setHorizontalAlignment(SwingConstants.CENTER);
		panelContent.add(lblText, BorderLayout.CENTER);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		// Get the table model
		TableModelSummaryReport tableModel = (TableModelSummaryReport) table.getModel();
		
		// Get the table entry
		TableEntrySummaryReport tableEntry =  tableModel.getTableEntry(row);
				
		// Set the background color of the row
		if (!isSelected) {
			panel.setBackground(tableEntry.getColor());
			panelSpace.setBackground(tableEntry.getColor());
			panelContent.setBackground(tableEntry.getColor());
			lblText.setForeground(Color.BLACK);
		} else {
			panel.setBackground(ColorDefinitions.backgroundColorSelectedRow);
			panelSpace.setBackground(ColorDefinitions.backgroundColorSelectedRow);
			panelContent.setBackground(ColorDefinitions.backgroundColorSelectedRow);
			lblText.setForeground(tableEntry.getColor());
		}
		
		// Get the triple
		Triple triple = tableEntry.getDifference().getTriple();
		
		// Replace URI by prefix if available
		if (column == 0) {
			// Subject
			lblText.setText(Controller.convertTripleStringToPrefixTripleString(Controller.getSubject(triple)));
		} else if (column == 1) {
			// Predicate
			lblText.setText(Controller.convertTripleStringToPrefixTripleString(Controller.getPredicate(triple)));
		} else if (column == 2) {
			// Object
			lblText.setText(Controller.convertTripleStringToPrefixTripleString(Controller.getObject(triple)));
		}
		
		// Set the icon
		if ((column == 3) || (column == 4)) {
			String text = (String) value;
			if (text.startsWith(SDDTripleStateEnum.ADDED.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Added.png"));
				lblText.setText(text.substring(text.indexOf("("), text.length()));
			} else if (text.startsWith(SDDTripleStateEnum.DELETED.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Deleted.png"));
				lblText.setText(text.substring(text.indexOf("("), text.length()));
			} else if (text.startsWith(SDDTripleStateEnum.NOTINCLUDED.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/NotIncluded.png"));
				lblText.setText("");
			} else if (text.startsWith(SDDTripleStateEnum.ORIGINAL.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Original.png"));
				lblText.setText(text.substring(text.indexOf("("), text.length()));
			}
		} else if (column == 5) {
			if (((String) value).equals("TRUE")) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Conflict.png"));
				lblText.setText("");
			} else {
				lblIcon.setIcon(new ImageIcon("images/icons/table/NoConflict.png"));
				lblText.setText("");
			}
		} else if ((column == 6) || (column == 7)) {
			String text = (String) value;
			if (text.startsWith(SDDTripleStateEnum.ADDED.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Added.png"));
				lblText.setText("");
			} else if (text.startsWith(SDDTripleStateEnum.DELETED.toString())) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Deleted.png"));
				lblText.setText("");
			}
		} else if (column == 8) {
			if (((String) value).equals("TRUE")) {
				lblIcon.setIcon(new ImageIcon("images/icons/table/Approved.png"));
				lblText.setText("");
			} else {
				lblIcon.setIcon(new ImageIcon("images/icons/table/NotApproved.png"));
				lblText.setText("");
			}
		} else {
			lblIcon.setIcon(null);
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
		
		// Set border right to resolution state column
		if (column == 7) {
			right = 1;
		}
		if (column == 8) {
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
		panel.setBorder(border);

		return panel;
	}

}
