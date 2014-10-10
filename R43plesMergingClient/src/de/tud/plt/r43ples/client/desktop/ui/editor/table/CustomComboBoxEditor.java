package de.tud.plt.r43ples.client.desktop.ui.editor.table;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
	/** The combo box. **/
	private JComboBox<String> comboBox;
	/** The table entry. **/
	private TableEntrySemanticEnrichmentClassTriples tableEntry;
	
	
	/**
	 * The constructor.
	 */
	@SuppressWarnings("unchecked")
	public CustomComboBoxEditor() {
		super(new JComboBox<String>());
		this.model = (DefaultComboBoxModel<String>)((JComboBox<String>)getComponent()).getModel();
		this.comboBox = (JComboBox<String>)getComponent();
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// Remove all entries
		model.removeAllElements();		
		// Get current table entry
		tableEntry = ((TableModelSemanticEnrichmentClassTriples) table.getModel()).getTableEntry(row);
		// Add options to model
		ArrayList<String> options = tableEntry.getSemanticResolutionOptions();
		if (!options.isEmpty()) {
			Iterator<String> iteOptions = options.iterator();
			while (iteOptions.hasNext()) {
				String currentOptions = iteOptions.next();
				model.addElement(currentOptions);
			}
			comboBox.setSelectedIndex(tableEntry.getSelectedSemanticResolutionOption());
			table.setValueAt(options.get(tableEntry.getDefaultSemanticResolutionOption()), row, column);
		}
		
		// Add listener
		comboBox.addItemListener(new ItemListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
			 */
			@Override
			public void itemStateChanged(ItemEvent e) {
				int selectedIndex = comboBox.getSelectedIndex();
				if (selectedIndex != -1) {
					tableEntry.setSelectedSemanticResolutionOption(comboBox.getSelectedIndex());
				}
			}
		});
		
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}

}
