package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableCellRenderer;

import de.tud.plt.r43ples.client.desktop.model.table.TableModelResolutionTriples;

/**
 * The table check box renderer of resolution triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCheckBoxRendererResolutionTriples extends JCheckBox implements TableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public TableCheckBoxRendererResolutionTriples() {
		setHorizontalAlignment(JLabel.CENTER);
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
		TableModelResolutionTriples tableModel = (TableModelResolutionTriples) table.getModel();
		
		// Set the border between conflicting differences and non conflicting differences
		if ((row != tableModel.getRowCount() - 1) && (row > 0)) {
			if (!tableModel.getValueAt(row, 0).equals(tableModel.getValueAt(row - 1, 0))) {
				// Create top border
				setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			} else {
				setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
			}
		} else {
			setBorder(new MatteBorder(0, 0, 0, 0, Color.BLACK));
		}
		
		return this;
    }
}
