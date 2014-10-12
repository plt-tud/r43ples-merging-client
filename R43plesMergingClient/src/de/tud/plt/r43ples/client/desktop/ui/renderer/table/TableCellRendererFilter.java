package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Management;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryFilter;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelFilter;

/**
 * The table check box renderer of filter table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererFilter extends JCheckBox implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public TableCellRendererFilter() {
		setHorizontalAlignment(JLabel.LEFT);
		setBorderPainted(true);
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			//super.setBackground(table.getSelectionBackground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelected((value != null && ((Boolean) value).booleanValue()));
		
		// Get the table model
		TableModelFilter tableModel = (TableModelFilter) table.getModel();
		// Get the entry
		TableEntryFilter entry = tableModel.getTableEntry(row);
		
		setText(Management.convertTripleStringToPrefixTripleString("<" + entry.getText() + ">"));
		
		return this;
    }
	
}
