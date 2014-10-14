package de.tud.plt.r43ples.client.desktop.ui.renderer.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.tree.TreeNodeObject;

/**
 * The tree cell renderer for difference tree (division).
 * 
 * @author Stephan Hensel
 *
 */
public class TreeCellRendererDifferences extends DefaultTreeCellRenderer {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;

	
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus); 
		
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
		
		if ((treeNodeObject.getObject() != null) && (treeNodeObject.getObject().getClass().equals(Difference.class))) {
			Difference difference = (Difference) treeNodeObject.getObject();
			String subject = Controller.convertTripleStringToPrefixTripleString("<" + difference.getTriple().getSubject() + ">");
			String predicate = Controller.convertTripleStringToPrefixTripleString(Controller.getPredicate(difference.getTriple()));
			String object = Controller.convertTripleStringToPrefixTripleString("<" + difference.getTriple().getObject() + ">");
			setText(subject + " " + predicate + " " + object + " .");		
		} else {
			setText(treeNodeObject.getText());
		}
		setBackground(Color.black);

		return this;
	}

}
