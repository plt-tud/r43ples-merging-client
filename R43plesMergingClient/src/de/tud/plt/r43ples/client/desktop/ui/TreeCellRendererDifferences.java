package de.tud.plt.r43ples.client.desktop.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.model.TreeNodeObject;

public class TreeCellRendererDifferences extends DefaultTreeCellRenderer {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;

	
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		// Get the tree node object
		TreeNodeObject treeNodeObject = (TreeNodeObject) ((DefaultMutableTreeNode) value).getUserObject();
		
		if (treeNodeObject.getResolutionState() == ResolutionState.RESOLVED) {
			// RESOLVED state
			setIcon(new ImageIcon("images/icons/Resolved.png"));
		} else if (treeNodeObject.getResolutionState() == ResolutionState.DIFFERENCE) {
			// DIFFERENCE state
			setIcon(new ImageIcon("images/icons/Difference.png"));
		} else {
			// CONFLICT state
			setIcon(new ImageIcon("images/icons/Conflict.png"));
		}
		
		setText(treeNodeObject.getText());
		

//		if (selected) {
//			setBackground(Color.BLUE);
//		}

		return this;
	}
	
}
