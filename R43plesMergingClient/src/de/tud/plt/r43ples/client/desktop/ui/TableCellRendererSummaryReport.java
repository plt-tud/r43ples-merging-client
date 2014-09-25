package de.tud.plt.r43ples.client.desktop.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.tud.plt.r43ples.client.desktop.model.TableModelSummaryReport;

public class TableCellRendererSummaryReport extends DefaultTableCellRenderer {

	/** The default serial version. **/
	private static final long serialVersionUID = 1L;
	
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		cellComponent.setBackground(((TableModelSummaryReport) table.getModel()).getTableEntry(row).getColor());
		
		return cellComponent;
	}

}
