package de.tud.plt.r43ples.client.desktop.ui;

import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 * Non editable cell editor for JTable column.
 * 
 * @author Stephan Hensel
 *
 */
public class NonEditableCellEditor extends DefaultCellEditor {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;


	/**
	 * The constructor.
	 * 
	 * @param textField the check box
	 */
	public NonEditableCellEditor(JTextField textField) {
		super(textField);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.DefaultCellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return false;
	}

}
