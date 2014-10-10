package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Management;

/**
 * The table renderer of semantic enrichment class triples table.
 * 
 * @author Stephan Hensel
 *
 */
public class TableCellRendererSemanticEnrichmentClassTriples extends DefaultTableCellRenderer  {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JComponent cellComponent = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Replace URI by prefix if available
		if ((column >= 3) && (column <= 5)) {
			// Column 3 -> subject
			// Column 4 -> predicate
			// Column 5 -> object
			setValue(Management.convertTripleStringToPrefixTripleString((String) value)); 
		}
		
		return cellComponent;
    }

}

