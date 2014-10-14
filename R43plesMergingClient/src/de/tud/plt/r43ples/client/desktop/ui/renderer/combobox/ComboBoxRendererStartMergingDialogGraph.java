package de.tud.plt.r43ples.client.desktop.ui.renderer.combobox;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The combo box renderer of start merging dialog graph combo box.
 * 
 * @author Stephan Hensel
 *
 */
public class ComboBoxRendererStartMergingDialogGraph extends JLabel implements ListCellRenderer<Object> {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;

	
	/**
	 * The constructor.
	 */
	public ComboBoxRendererStartMergingDialogGraph() {
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
		setText((String) value);
		setToolTipText((String) value );
		
		return this;
	}

}