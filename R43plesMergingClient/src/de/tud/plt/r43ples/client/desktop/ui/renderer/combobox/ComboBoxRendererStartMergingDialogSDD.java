package de.tud.plt.r43ples.client.desktop.ui.renderer.combobox;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;

/**
 * The combo box renderer of start merging dialog SDD combo box.
 * 
 * @author Stephan Hensel
 *
 */
public class ComboBoxRendererStartMergingDialogSDD extends JLabel implements ListCellRenderer<Object> {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public ComboBoxRendererStartMergingDialogSDD() {
		setOpaque(true);
	}
	
	
	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setText(Controller.convertTripleStringToPrefixTripleString("<" + value + ">"));
		setToolTipText(Controller.convertTripleStringToPrefixTripleString("<" + value + ">"));
		
		return this;
	}

}