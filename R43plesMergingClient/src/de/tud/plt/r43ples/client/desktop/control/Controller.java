package de.tud.plt.r43ples.client.desktop.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.tud.plt.r43ples.client.desktop.control.enums.MergeQueryTypeEnum;
import de.tud.plt.r43ples.client.desktop.model.Difference;
import de.tud.plt.r43ples.client.desktop.model.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.TreeNodeObject;
import de.tud.plt.r43ples.client.desktop.ui.ApplicationUI;
import de.tud.plt.r43ples.client.desktop.ui.NonEditableCellEditor;
import de.tud.plt.r43ples.client.desktop.ui.StartMergingDialog;

/**
 * The controller.
 * 
 * @author Stephan Hensel
 *
 */
public class Controller {

	/** The start merging dialog instance. **/
	private static StartMergingDialog dialog = new StartMergingDialog();
	/** The difference model. **/
	private static DifferenceModel differenceModel = new DifferenceModel();;
	
	
	/**
	 * Show the merging dialog.
	 * 
	 * @throws IOException 
	 */
	public static void showStartMergingDialog() throws IOException {
		
		// Get all possible graphs under revision control
		ArrayList<String> elements = Management.getAllGraphsUnderRevisionControl();
		// Remove all existing elements
		StartMergingDialog.getcBModelGraph().removeAllElements();
		// Add current elements
		Iterator<String> iteElements = elements.iterator();
		while (iteElements.hasNext()) {
			StartMergingDialog.getcBModelGraph().addElement(iteElements.next());
		}
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		dialog.setModal(true);
		dialog.setVisible(true);
	}

	
	/**
	 * Update the revision combo boxes. 
	 * 
	 * @throws IOException 
	 */
	public static void updateRevisionComboBoxes() throws IOException {
		// Get the selected graph
		String graphName = (String) StartMergingDialog.getcBModelGraph().getSelectedItem();
		// Get all branch names
		ArrayList<String> elements = Management.getAllBranchNamesOfGraph(graphName);
		// Remove all existing elements
		StartMergingDialog.getcBModelRevisionA().removeAllElements();
		StartMergingDialog.getcBModelRevisionB().removeAllElements();
		// Add current elements
		Iterator<String> iteElements = elements.iterator();
		while (iteElements.hasNext()) {
			String currentElement = iteElements.next();
			StartMergingDialog.getcBModelRevisionA().addElement(currentElement);
			StartMergingDialog.getcBModelRevisionB().addElement(currentElement);
		}
	}


	/**
	 * Start new merge process when the selected branches are not equal.
	 * 
	 * @throws IOException 
	 */
	public static void startNewMergeProcess() throws IOException {
		
		if (StartMergingDialog.getcBModelRevisionA().getSelectedItem().equals(StartMergingDialog.getcBModelRevisionB().getSelectedItem())) {
			// Show error message dialog when selected branches are equal
			JOptionPane.showMessageDialog(dialog, "The selected branches must be different!", "Error: Selected branches are equal.", JOptionPane.ERROR_MESSAGE);
		} else if (StartMergingDialog.getTfUser().getText().equals("")) {
			// Show error message when no user was specified
			JOptionPane.showMessageDialog(dialog, "A user must be specified!", "Error: No user specified.", JOptionPane.ERROR_MESSAGE);
		} else if (StartMergingDialog.getTextAreaMessage().getText().equals("")) {
			// Show error message when no message was specified allowed
			JOptionPane.showMessageDialog(dialog, "An empty message is not allowed!", "Error: No message specified.", JOptionPane.ERROR_MESSAGE);
		} else {
			// Start new merging process
			// Get the selected graph
			String graphName = (String) StartMergingDialog.getcBModelGraph().getSelectedItem();
			
			String user = StartMergingDialog.getTfUser().getText();
			String commitMessage = StartMergingDialog.getTextAreaMessage().getText();
			
			String branchNameA = (String) StartMergingDialog.getcBModelRevisionA().getSelectedItem();
			String branchNameB = (String) StartMergingDialog.getcBModelRevisionB().getSelectedItem();

			MergeQueryTypeEnum type = null;
			if (StartMergingDialog.getRdbtnMergingMethodAUTO().isSelected()) {
				type = MergeQueryTypeEnum.AUTO;
			} else if (StartMergingDialog.getRdbtnMergingMethodCOMMON().isSelected()) {
				type = MergeQueryTypeEnum.COMMON;
			} else {
				type = MergeQueryTypeEnum.MANUAL;
			}
	
			Management.executeMergeQuery(graphName, user, commitMessage, type, branchNameA, branchNameB, null, differenceModel);
			
			// TODO close dialog after execution + error handling
			
			dialog.setVisible(false);
			
			// Update application UI
			updateDifferencesTree();
			
			
			
		}
	}
	
	
	/**
	 * Update the differences tree.
	 */
	public static void updateDifferencesTree() {
		
		DefaultMutableTreeNode rootNode = Management.createDifferencesTree(differenceModel);
		ApplicationUI.getTreeModelDifferencesDivision().setRoot(rootNode);
		
	}


	/**
	 * Selection of differences tree was changed.
	 * @throws IOException 
	 */
	public static void selectionChangedDifferencesTree() throws IOException {
		
		// Array list which stores all selected tree node objects
		ArrayList<TreeNodeObject> list = new ArrayList<TreeNodeObject>();
		
		// Get the selected nodes
		TreePath[] treePaths = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
		for (TreePath treePath : treePaths) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			TreeNodeObject treeNodeObject = (TreeNodeObject) node.getUserObject();
			list.add(treeNodeObject);
		}
		
		// Update the triple table
		updateTableResolutionTriples(list);
		
	}
	
	
	/**
	 * Update the resolution triples table.
	 * 
	 * @param selectedElements the array list of all selected differences
	 * @throws IOException 
	 */
	public static void updateTableResolutionTriples(ArrayList<TreeNodeObject> selectedElements) throws IOException {
		// Remove the old rows
		ApplicationUI.getTableModelResolutionTriples().getDataVector().removeAllElements();
		
		for (TreeNodeObject nodeObject : selectedElements) {
			// Contains the row data
			Object[] rowData = null;
			if (nodeObject.getObject() != null) {
				if (nodeObject.getObject().getClass().equals(DifferenceGroup.class)) {
					// Difference group found
					
					//TODO
					
				} else if (nodeObject.getObject().getClass().equals(Difference.class)) {
					// Difference found
					Difference difference = (Difference) nodeObject.getObject();
					// Get the difference group
					DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
					// Create the row data
					rowData = Management.createRowDataResolutionTriples(nodeObject.getResolutionState(), difference, differenceGroup);
				}
			}
			ApplicationUI.getTableModelResolutionTriples().addRow(rowData);
			ApplicationUI.getTableResolutionTriples().getColumn("Subject").setCellEditor(new NonEditableCellEditor(new JTextField()));
			ApplicationUI.getTableResolutionTriples().getColumn("Predicate").setCellEditor(new NonEditableCellEditor(new JTextField()));
			ApplicationUI.getTableResolutionTriples().getColumn("Object").setCellEditor(new NonEditableCellEditor(new JTextField()));
			ApplicationUI.getTableResolutionTriples().getColumn("State A (Revision)").setCellEditor(new NonEditableCellEditor(new JTextField()));
			ApplicationUI.getTableResolutionTriples().getColumn("State B (Revision)").setCellEditor(new NonEditableCellEditor(new JTextField()));
			
		}
	}
	
	
	/**
	 * Initialize the resolution triples table.
	 */
	public static void initializeTableResolutionTriples() {
		
		// Create the columns
		ApplicationUI.getTableModelResolutionTriples().addColumn("Subject");
		ApplicationUI.getTableModelResolutionTriples().addColumn("Predicate");
		ApplicationUI.getTableModelResolutionTriples().addColumn("Object");
		ApplicationUI.getTableModelResolutionTriples().addColumn("State A (Revision)");
		ApplicationUI.getTableModelResolutionTriples().addColumn("State B (Revision)");
		ApplicationUI.getTableModelResolutionTriples().addColumn("Resolution State");
		
	}
	
	
	

}
