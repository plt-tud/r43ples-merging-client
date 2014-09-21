package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.jena.atlas.web.HttpException;
import org.apache.log4j.Logger;

import att.grappa.Graph;
import att.grappa.GrappaAdapter;
import att.grappa.GrappaPanel;
import att.grappa.GrappaSupport;
import de.tud.plt.r43ples.client.desktop.control.enums.MergeQueryTypeEnum;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.Difference;
import de.tud.plt.r43ples.client.desktop.model.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.TreeNodeObject;
import de.tud.plt.r43ples.client.desktop.ui.ApplicationUI;
import de.tud.plt.r43ples.client.desktop.ui.ReportDialog;
import de.tud.plt.r43ples.client.desktop.ui.StartMergingDialog;

/**
 * The controller.
 * 
 * @author Stephan Hensel
 *
 */
public class Controller {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Controller.class);
	/** The start merging dialog instance. **/
	private static StartMergingDialog dialog = new StartMergingDialog();
	/** The difference model. **/
	private static DifferenceModel differenceModel = new DifferenceModel();;
	/** The summary report dialog instance. **/
	private static ReportDialog report = new ReportDialog();
	/** The grappa graph which contains the revision tree. **/
	private static Graph graph;
	/** The grappa panel which holds the visualization of the graph. **/
	private static GrappaPanel gp;
	/** The currently highlighted node name A in the revision graph. **/
	private static String highlightedNodeNameA = null;
	/** The currently highlighted node name B in the revision graph. **/
	private static String highlightedNodeNameB = null;
	
	
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
			
			// Create the revision graph
			createGraph(graphName);
			
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
	 * 
	 * @throws IOException 
	 */
	public static void selectionChangedDifferencesTree() throws IOException {
		
		// Array list which stores all selected tree node objects
		ArrayList<TreeNodeObject> list = new ArrayList<TreeNodeObject>();
		
		// Get the selected nodes
		TreePath[] treePaths = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
		if (treePaths != null) {
			for (TreePath treePath : treePaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				TreeNodeObject treeNodeObject = (TreeNodeObject) node.getUserObject();
				list.add(treeNodeObject);
			}
		}
		
		// Update the triple table
		updateTableResolutionTriples(list);
		tableResolutionTriplesSelectionChanged();
		
	}
	
	
	/**
	 * Update the resolution triples table.
	 * 
	 * @param selectedElements the array list of all selected differences
	 * @throws IOException 
	 */
	public static void updateTableResolutionTriples(ArrayList<TreeNodeObject> selectedElements) throws IOException {
		// Remove the old rows
		ApplicationUI.getTableModelResolutionTriples().removeAllElements();
		
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
					// Create the table entry
					TableEntry entry = new TableEntry(difference, nodeObject, rowData);
					ApplicationUI.getTableModelResolutionTriples().addRow(entry);
				}
			}

//			TableEntry entry = new TableEntry(difference, nodeObject, rowData);
//			ApplicationUI.getTableModelResolutionTriples().addRow(entry);
			
		}
		ApplicationUI.getTableResolutionTriples().updateUI();
	}


	/**
	 * Approve selected entries of resolution triples table.
	 */
	public static void approveSelectedEntriesResolutionTriples() {
		
		int[] selectedRows = ApplicationUI.getTableResolutionTriples().getSelectedRows();
		for (int i = 0; i<selectedRows.length; i++) {
			ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRows[i]).getNodeObject().setResolutionState(ResolutionState.RESOLVED);
			// Propagate changes to difference model
			Boolean checkBoxStateBool = (Boolean) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRows[i]).getRowData()[5];
			if (checkBoxStateBool.booleanValue()) {
				((Difference) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRows[i]).getNodeObject().getObject()).setResolutionState(SDDTripleStateEnum.ADDED);
			} else {
				((Difference) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRows[i]).getNodeObject().getObject()).setResolutionState(SDDTripleStateEnum.ADDED);
			}
		}

		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		ApplicationUI.getTreeDifferencesDivision().updateUI();
		
	}
	
	
//	TODO Maybe it is better to use a check box tree and not use selection of nodes
//	/**
//	 * Deselect approved entries of differences tree.
//	 * 
//	 * @throws IOException 
//	 */
//	public static void deselectApprovedEntriesOfDifferencesTree(List<Triple> tripleList) throws IOException {
//		
//		// Array list which stores all selected tree node objects
//		ArrayList<TreeNodeObject> list = new ArrayList<TreeNodeObject>();
//		
//		// Get the selected nodes
//		TreePath[] treePaths = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
//		if (treePaths != null) {
//			for (TreePath treePath : treePaths) {
//				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
//				TreeNodeObject treeNodeObject = (TreeNodeObject) node.getUserObject();
//				if (tripleList.contains(treeNodeObject.ge)
//				list.add(treeNodeObject);
//			}
//		}
//		
//		// Update the triple table
//		updateTableResolutionTriples(list);
//		
//	}
	
	
	public static void executePush() {
		// Set the texts
		ReportDialog.getTfGraph().setText(StartMergingDialog.getcBModelGraph().getSelectedItem().toString());
		ReportDialog.getTfRevisionA().setText(StartMergingDialog.getcBModelRevisionA().getSelectedItem().toString());
		ReportDialog.getTfRevisionB().setText(StartMergingDialog.getcBModelRevisionB().getSelectedItem().toString());
		ReportDialog.getTfUser().setText(StartMergingDialog.getTfUser().getText());
		ReportDialog.getTextAreaMessage().setText(StartMergingDialog.getTextAreaMessage().getText());
		
		// TODO Create Table
		
		report.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		report.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		report.setModal(true);
		report.setVisible(true);

	}
	
	
	/**
	 * The table resolution triples selection changed.
	 * Update the revision graph. The referenced revisions of the selected table row will be highlighted in the graph.
	 */
	public static void tableResolutionTriplesSelectionChanged() {
		int[] rows = ApplicationUI.getTableResolutionTriples().getSelectedRows();
		if (rows.length == 1) {
			int index = ApplicationUI.getTableResolutionTriples().convertRowIndexToModel(rows[0]);
			TableEntry entry = ApplicationUI.getTableModelResolutionTriples().getTableEntry(index);
			logger.debug("Selected Entry: A - " + entry.getDifference().getReferencedRevisionA());
			logger.debug("Selected Entry: B - " + entry.getDifference().getReferencedRevisionB());
			
			// Remove highlighting of already highlighted nodes
			Management.removeHighlighting(graph, highlightedNodeNameA);
			Management.removeHighlighting(graph, highlightedNodeNameB);
			
			// Highlight the new currently selected nodes and save them to currently selected node name variables
			highlightedNodeNameA = entry.getDifference().getReferencedRevisionA();
			highlightedNodeNameB = entry.getDifference().getReferencedRevisionB();
			
			Color color = Color.RED;
			DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(entry.getDifference(), differenceModel);
			if (!differenceGroup.isConflicting()) {
				color = Color.ORANGE;
			}
			
			Management.highlightNode(graph, highlightedNodeNameA, color);
			Management.highlightNode(graph, highlightedNodeNameB, color);
			
			gp.updateUI();
		}
	}

	
	/**
	 * Create the revision graph.
	 * 
	 * @param graphName the graph name
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void createGraph(String graphName) throws HttpException, IOException {
		
		graph = Management.getDotGraph(graphName);	

		// Layout the graph
		String [] processArgs = {"app/dot"};
		Process formatProcess = Runtime.getRuntime().exec(processArgs, null, null);
		System.out.println(GrappaSupport.filterGraph(graph, formatProcess));
		formatProcess.getOutputStream().close();
		
		StringWriter sw = new StringWriter();
		graph.printGraph(sw);
		logger.debug(sw.toString());

		// Create the grappa panel
		gp = new GrappaPanel(graph);
		gp.addGrappaListener(new GrappaAdapter());
		gp.setScaleToFit(false);
		
		ApplicationUI.getScrollPaneGraph().setViewportView(gp);

		gp.updateUI();
	}

}
