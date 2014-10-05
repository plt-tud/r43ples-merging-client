package de.tud.plt.r43ples.client.desktop.ui.editor.table;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

import de.tud.plt.r43ples.client.desktop.model.table.TableEntrySemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.table.TableModelSemanticEnrichmentClassTriples;

/**
 * The custom combo box editor. 
 * 
 * @author Stephan Hensel
 *
 */
public class CustomComboBoxEditor extends DefaultCellEditor {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	/** The combo box model. **/
	private DefaultComboBoxModel<String> model;
	
	
	/**
	 * The constructor.
	 */
	@SuppressWarnings("unchecked")
	public CustomComboBoxEditor() {
		super(new JComboBox<String>());
		this.model = (DefaultComboBoxModel<String>)((JComboBox<String>)getComponent()).getModel();
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// Get current table entry
		TableEntrySemanticEnrichmentClassTriples tableEntry = ((TableModelSemanticEnrichmentClassTriples) table.getModel()).getTableEntry(row);
		// Add options to model
		ArrayList<String> options = tableEntry.getSemanticResolutionOptions();
		Iterator<String> iteOptions = options.iterator();
		while (iteOptions.hasNext()) {
			String currentOptions = iteOptions.next();
			model.addElement(currentOptions);
		}
		model.setSelectedItem(options.get(tableEntry.getDefaultSemanticResolutionOption()));

		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	} 

}
