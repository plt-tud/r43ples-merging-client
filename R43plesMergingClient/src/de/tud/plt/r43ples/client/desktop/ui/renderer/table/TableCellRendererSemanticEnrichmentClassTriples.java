package de.tud.plt.r43ples.client.desktop.ui.renderer.table;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentClassTriples;

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
		
		// Get the table model
		TableModelSemanticEnrichmentClassTriples tableModel = (TableModelSemanticEnrichmentClassTriples) table.getModel();
		
		// Get the table entry
		TableEntrySemanticEnrichmentClassTriples tableEntry =  tableModel.getTableEntry(row);
				
		// Get the triple
		if (tableEntry.getDifference() != null) {
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
		}
		
		return cellComponent;
    }

}

