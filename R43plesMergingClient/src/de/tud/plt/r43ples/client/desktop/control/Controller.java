package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.jena.atlas.web.HttpException;
import org.apache.log4j.Logger;

import att.grappa.Graph;
import att.grappa.GrappaAdapter;
import att.grappa.GrappaPanel;
import att.grappa.GrappaSupport;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.update.UpdateAction;

import de.tud.plt.r43ples.client.desktop.control.enums.MergeQueryTypeEnum;
import de.tud.plt.r43ples.client.desktop.control.enums.ResolutionState;
import de.tud.plt.r43ples.client.desktop.control.enums.SDDTripleStateEnum;
import de.tud.plt.r43ples.client.desktop.model.structure.ClassModel;
import de.tud.plt.r43ples.client.desktop.model.structure.ClassStructure;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HttpResponse;
import de.tud.plt.r43ples.client.desktop.model.structure.ReportResult;
import de.tud.plt.r43ples.client.desktop.model.structure.SemanticDefinitionResult;
import de.tud.plt.r43ples.client.desktop.model.structure.TripleClassStructure;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryFilter;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentAllClasses;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelFilter;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentAllClasses;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.tree.TreeNodeObject;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ApplicationUI;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ConfigurationDialog;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ReportDialog;
import de.tud.plt.r43ples.client.desktop.ui.dialog.StartMergingDialog;
import de.tud.plt.r43ples.client.desktop.ui.editor.table.CustomComboBoxEditor;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererFilter;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererResolutionTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererSemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererSummaryReport;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCheckBoxRendererResolutionTriples;

/**
 * The controller.
 * 
 * @author Stephan Hensel
 *
 */
public class Controller {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Controller.class);
	/** The configuration dialog. **/
	private static ConfigurationDialog configDialog = new ConfigurationDialog();
	/** The start merging dialog instance. **/
	private static StartMergingDialog dialog = new StartMergingDialog();
	/** The revision number of the branch A. **/
	private static String revisionNumberBranchA;
	/** The revision number of the branch B. **/
	private static String revisionNumberBranchB;
	/** The difference model. **/
	private static DifferenceModel differenceModel = new DifferenceModel();
	/** The class model of branch A. **/
	private static ClassModel classModelBranchA;
	/** The class model of branch B. **/
	private static ClassModel classModelBranchB;
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
	/** The report result. **/
	private static ReportResult reportResult = null;
	/** The update table flag. Recursively used for updating the table. **/
	private static boolean updateTableFlag = false;
	/** The properties array list. **/
	private static ArrayList<String> propertyList;
	
	
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
		
		// Get all possible SDDs
		ArrayList<String> elementsSDD = Management.getAllSDDs();
		// Remove all existing elements
		StartMergingDialog.getcBModelSDD().removeAllElements();
		// Add current elements
		Iterator<String> iteElementsSDD = elementsSDD.iterator();
		while (iteElementsSDD.hasNext()) {
			StartMergingDialog.getcBModelSDD().addElement(iteElementsSDD.next());
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
			String sdd = (String) StartMergingDialog.getcBModelSDD().getSelectedItem();
			
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
	
			HttpResponse response = Management.executeMergeQuery(graphName, sdd, user, commitMessage, type, branchNameA, branchNameB, null, differenceModel);
			
			if (response.getStatusCode() == HttpURLConnection.HTTP_CONFLICT) {
				// There was a conflict
				logger.info("Merge query produced conflicts.");
				// Read the difference model to java model
				Management.readDifferenceModel(response.getBody(), differenceModel);
				// Save the current revision numbers
				revisionNumberBranchA = Management.getRevisionNumberOfBranchAHeaderParameter(response, graphName);
				revisionNumberBranchB = Management.getRevisionNumberOfBranchBHeaderParameter(response, graphName);
				
				// Create the class models of both branches
				classModelBranchA = Management.createClassModelOfRevision(graphName, branchNameA, differenceModel);
				classModelBranchB = Management.createClassModelOfRevision(graphName, branchNameB, differenceModel);
				
				// Create the property list of revision
				propertyList = Management.getPropertiesOfRevision(graphName, branchNameA, branchNameB);
				
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query produced conflicts. Please resolve conflicts manually.", "Info", JOptionPane.INFORMATION_MESSAGE);
			} else if (response.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
				// There was no conflict merged revision was created
				logger.info("Merge query produced no conflicts. Merged revision was created.");
				// Create an empty difference model
				differenceModel = new DifferenceModel();
				// Create an empty property list
				propertyList = new ArrayList<String>();
				String newRevisionNumber = Management.getRevisionNumberOfNewRevisionHeaderParameter(response, graphName);
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query successfully executed. Revision number of merged revision: " + newRevisionNumber, "Info", JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Error occurred
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query could not be executed. Undefined error occurred", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			// Close dialog after execution and update UI
			dialog.setVisible(false);

			// Set the cell renderer
			TableCellRendererResolutionTriples renderer = new TableCellRendererResolutionTriples();
			for (int i = 0; i < ApplicationUI.getTableModelResolutionTriples().getColumnCount() - 1; i++) {
				ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
			ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(6).setCellRenderer(new TableCheckBoxRendererResolutionTriples());
			
//			ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().getColumnModel().getColumn(7).setCellRenderer(new TableComboBoxRendererSemanticEnrichmentClassTriples());
			
			TableCellRendererSemanticEnrichmentClassTriples rendererSemanticTriples = new TableCellRendererSemanticEnrichmentClassTriples();
			for (int i = 0; i < ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getColumnCount() - 1; i++) {
				ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().getColumnModel().getColumn(i).setCellRenderer(rendererSemanticTriples);
			}

			TableCellRendererFilter rendererFilter = new TableCellRendererFilter();
			ApplicationUI.getTableFilter().getColumnModel().getColumn(0).setCellRenderer(rendererFilter);
			
			// Set the cell editor
			ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().getColumnModel().getColumn(7).setCellEditor(new CustomComboBoxEditor());
		
			// Update filter table
			updateTableModelFilter();
			
			// Update application UI
			updateDifferencesTree();
			
			// Create the revision graph
			createGraph(graphName);
			
			/** The semantic definitions **/
			new SemanticDefinitions();
			
			// Update the semantic enrichment
			updateTableModelSemanticEnrichmentAllClasses();
		}
	}
	
	
	/**
	 * Update the differences tree.
	 */
	public static void updateDifferencesTree() {
		DefaultMutableTreeNode rootNode = Management.createDifferencesTree(differenceModel);
		ApplicationUI.getTreeModelDifferencesDivision().setRoot(rootNode);
		for (int i = 0; i < ApplicationUI.getTreeDifferencesDivision().getRowCount(); i++) {
			ApplicationUI.getTreeDifferencesDivision().expandRow(i);
		}
		ApplicationUI.getTreeDifferencesDivision().updateUI();		
	}


	/**
	 * Selection of differences tree was changed.
	 * 
	 * @throws IOException 
	 */
	public static void selectionChangedDifferencesTree() throws IOException {
		
		// Set the update flag
		updateTableFlag = true;
		
		// Hash map which stores all selected tree node objects and the corresponding tree paths
		HashMap<TreeNodeObject, TreePath> map = new HashMap<TreeNodeObject, TreePath>();
		
		// Get the selected nodes
		TreePath[] treePaths = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
		if (treePaths != null) {
			for (TreePath treePath : treePaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				if (node.isLeaf()) {
					TreeNodeObject treeNodeObject = (TreeNodeObject) node.getUserObject();
					map.put(treeNodeObject, treePath);
				} else {
					// Select the sub nodes
					for (int i = 0; i < node.getChildCount(); i++) {
						DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
						ApplicationUI.getTreeDifferencesDivision().addSelectionPath(new TreePath(child.getPath()));
					}
					// Remove selection of parent node
					ApplicationUI.getTreeDifferencesDivision().removeSelectionPath(new TreePath(node.getPath()));
					// Set the update flag
					updateTableFlag = false;
				}
			}
		}
		
		if (updateTableFlag) {
			// Update the triple table
			updateTableResolutionTriples(map);
			ApplicationUI.getTableResolutionTriples().clearSelection();
			tableResolutionTriplesSelectionChanged();
		}
		
	}
	
	
	/**
	 * Update the resolution triples table.
	 * 
	 * @param selectedElements the hash map of all selected differences and the corresponding tree paths
	 * @throws IOException 
	 */
	public static void updateTableResolutionTriples(HashMap<TreeNodeObject, TreePath> selectedElements) throws IOException {
		// Remove the old rows
		ApplicationUI.getTableModelResolutionTriples().removeAllElements();
		logger.debug("unsorted map: " + selectedElements);
		selectedElements = (HashMap<TreeNodeObject, TreePath>) Management.sortMapByValue(selectedElements);
		logger.debug("sorted map: " + selectedElements);
		for (TreeNodeObject nodeObject : selectedElements.keySet()) {
			// Contains the row data
			Object[] rowData = null;
			if (nodeObject.getObject() != null) {
				if (nodeObject.getObject().getClass().equals(DifferenceGroup.class)) {
					// Difference group found
					// Currently do nothing
				} else if (nodeObject.getObject().getClass().equals(Difference.class)) {
					// Difference found
					Difference difference = (Difference) nodeObject.getObject();
					// Get the difference group
					DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
					// Create the row data
					rowData = Management.createRowDataResolutionTriples(difference, differenceGroup);
					// Create the table entry
					TableEntry entry = new TableEntry(difference, nodeObject, selectedElements.get(nodeObject), rowData);
					ApplicationUI.getTableModelResolutionTriples().addRow(entry);
				}
			}		
		}	
		
		ApplicationUI.getTableResolutionTriples().updateUI();
	}


	/**
	 * Approve selected entries of resolution triples table.
	 */
	public static void approveSelectedEntriesResolutionTriples() {
		
		int[] selectedRows = ApplicationUI.getTableResolutionTriples().getSelectedRows();
		// Sort the array
		Arrays.sort(selectedRows);
		
		TreePath [] treePaths = new TreePath[selectedRows.length];
		
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			int selectedRow = ApplicationUI.getTableResolutionTriples().convertRowIndexToModel(selectedRows[i]);
			ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow).getNodeObject().setResolutionState(ResolutionState.RESOLVED);
			((Difference) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow).getNodeObject().getObject()).setResolutionState(ResolutionState.RESOLVED);
			// Propagate changes to difference model
			Boolean checkBoxStateBool = (Boolean) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow).getRowData()[6];
			if (checkBoxStateBool.booleanValue()) {
				((Difference) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow).getNodeObject().getObject()).setTripleResolutionState(SDDTripleStateEnum.ADDED);
			} else {
				((Difference) ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow).getNodeObject().getObject()).setTripleResolutionState(SDDTripleStateEnum.DELETED);
			}
					
			// Remove selection of the approved row in the tree
			TableEntry entry = ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow);
			treePaths[i] = entry.getTreePath();

			// Remove the approved row
			ApplicationUI.getTableModelResolutionTriples().removeRow(selectedRow);
		}
		ApplicationUI.getTableResolutionTriples().updateUI();
		
		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		
		// Update the tree selection
		ApplicationUI.getTreeDifferencesDivision().removeSelectionPaths(treePaths);
		
		ApplicationUI.getTreeDifferencesDivision().updateUI();
	}
	
	
	/**
	 * Execute the push.
	 */
	public static void executePush() {
		// Set the texts
		ReportDialog.getTfGraph().setText(StartMergingDialog.getcBModelGraph().getSelectedItem().toString());
		ReportDialog.getTfSDD().setText(StartMergingDialog.getcBModelSDD().getSelectedItem().toString());
		ReportDialog.getTfRevisionA().setText(StartMergingDialog.getcBModelRevisionA().getSelectedItem().toString() + " (" + revisionNumberBranchA + ")");
		ReportDialog.getTfRevisionB().setText(StartMergingDialog.getcBModelRevisionB().getSelectedItem().toString() + " (" + revisionNumberBranchB + ")");
		ReportDialog.getTfUser().setText(StartMergingDialog.getTfUser().getText());
		ReportDialog.getTextAreaMessage().setText(StartMergingDialog.getTextAreaMessage().getText());
		
		// Set the cell renderer
		TableCellRendererSummaryReport renderer = new TableCellRendererSummaryReport();
		for (int i = 0; i < ReportDialog.getTableModel().getColumnCount(); i++) {
			ReportDialog.getTable().getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		// Create table content		
		reportResult = Management.createReportTable(ReportDialog.getTable(), differenceModel);
		
		// Message dialog strings
		// Header
		String errorNotApprovedHeader = "Error: Not approved conflicting differences.";
		String warningManuallyChangedHeader = "Warning: Non conflicting difference resolution states manually changed";
		String infoHeader = "Info: Summary report";
		// Body
		String errorNotApprovedBody = "Please approve all conflicting differences before executing a push. %n";
		String errorNotApprovedSingleCounterBody = "There is 1 not approved difference. %n";
		String errorNotApprovedMultipleCounterBody = "There are %s not approved differences. %n";
		String warningManuallyChangedBody = "Manual changes of non conflicting difference resolution states require the get operation of the whole dataset. This can take a few moments. %n";
		String warningManuallyChangedSingleCounterBody = "There is 1 manually changed non conflicting difference. %n";
		String warningManuallyChangedMultipleCounterBody = "There are %s manually changed non conflicting differences. %n";
		String infoBody = "";
		
		// Next steps
		String nextSteps = "Next steps: %n";
		String errorNotApprovedNextSteps = "Go back to the main screen and approve all conflicting differences. %n";
		String warningManuallyChangedNextSteps = "Use another SDD or push the changes which were made manually. %n";
		String infoNextSteps = "Push the merged revision. %n";
		
		String message = "";
		String header = "";
		int messageLevel = 0;
		
		// Create the message content
		if ((reportResult.getConflictsNotApproved() > 0) && (reportResult.getDifferencesResolutionChanged() > 0)) {
			// Not approved conflicts and manually changed non conflicting differences
			
			// Create the header
			header += errorNotApprovedHeader;
			// Set the message level
			messageLevel = JOptionPane.ERROR_MESSAGE;
			// Create the body
			// Error body
			message += errorNotApprovedBody;
			if (reportResult.getConflictsNotApproved() == 1) {
				message += errorNotApprovedSingleCounterBody;				
			} else {
				message += String.format(errorNotApprovedMultipleCounterBody, reportResult.getConflictsNotApproved());
			}
			// Warning body
			message += warningManuallyChangedBody;
			if (reportResult.getDifferencesResolutionChanged() == 1) {
				message += warningManuallyChangedSingleCounterBody;
			} else {
				message += String.format(warningManuallyChangedMultipleCounterBody, reportResult.getDifferencesResolutionChanged());
			}
			// Next steps
			message += nextSteps + errorNotApprovedNextSteps + warningManuallyChangedNextSteps;
			
			// Set the push button enable state
			ReportDialog.getOkButton().setEnabled(false);
		} else if ((reportResult.getConflictsNotApproved() > 0) && (reportResult.getDifferencesResolutionChanged() == 0)) {
			// Not approved conflicts only
			
			// Create the header
			header += errorNotApprovedHeader;
			// Set the message level
			messageLevel = JOptionPane.ERROR_MESSAGE;
			// Create the body
			// Error body
			message += errorNotApprovedBody;
			if (reportResult.getConflictsNotApproved() == 1) {
				message += errorNotApprovedSingleCounterBody;				
			} else {
				message += String.format(errorNotApprovedMultipleCounterBody, reportResult.getConflictsNotApproved());
			}
			// Next steps
			message += nextSteps + errorNotApprovedNextSteps;
			
			// Set the push button enable state
			ReportDialog.getOkButton().setEnabled(false);
		} else if ((reportResult.getConflictsNotApproved() == 0) && (reportResult.getDifferencesResolutionChanged() > 0)) {
			// Manually changed non conflicting differences only
			
			// Create the header
			header += warningManuallyChangedHeader;
			// Set the message level
			messageLevel = JOptionPane.WARNING_MESSAGE;
			// Create the body
			// Warning body
			message += warningManuallyChangedBody;
			if (reportResult.getDifferencesResolutionChanged() == 1) {
				message += warningManuallyChangedSingleCounterBody;
			} else {
				message += String.format(warningManuallyChangedMultipleCounterBody, reportResult.getDifferencesResolutionChanged());
			}
			// Next steps
			message += nextSteps + warningManuallyChangedNextSteps;
			
			// Set the push button enable state
			ReportDialog.getOkButton().setEnabled(true);
		} else if ((reportResult.getConflictsNotApproved() == 0) && (reportResult.getDifferencesResolutionChanged() == 0)) {
			// Info	
			
			// Create the header
			header += infoHeader;
			// Set the message level
			messageLevel = JOptionPane.INFORMATION_MESSAGE;
			// Create the body
			// Info body
			message += infoBody;
			// Next steps
			message += nextSteps + infoNextSteps;
			
			// Set the push button enable state
			ReportDialog.getOkButton().setEnabled(true);
		}
		
		// Show message dialog
		JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, String.format(message), header, messageLevel);
		
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
		} else {
			// Remove highlighting of already highlighted nodes
			Management.removeHighlighting(graph, highlightedNodeNameA);
			Management.removeHighlighting(graph, highlightedNodeNameB);
		}
		
		gp.updateUI();
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
	
	
	/**
	 * Close the summary report.
	 */
	public static void closeSummaryReport() {
		report.setVisible(false);
	}
	
	
	/**
	 * Push the changes to the remote repository.
	 * 
	 * @throws IOException 
	 */
	public static void pushToRemoteRepository() throws IOException {
		if (reportResult != null) {
			if (reportResult.getConflictsNotApproved() == 0) {
				// Get the definitions
				String user = ReportDialog.getTfUser().getText();
				String message = ReportDialog.getTextAreaMessage().getText();
				String graphName = ReportDialog.getTfGraph().getText();
				String sdd = ReportDialog.getTfSDD().getText();
				
				HttpResponse response = null;
				
				if (reportResult.getDifferencesResolutionChanged() > 0) {
					// Get the whole dataset
					Model wholeContentModel = Management.getWholeContentOfRevision(graphName, revisionNumberBranchA);
					logger.debug("Whole model as N-Triples: \n" + Management.writeJenaModelToNTriplesString(wholeContentModel));
			
					// Update dataset with local data
					ArrayList<String> list = Management.getAllTriplesDividedIntoInsertAndDelete(differenceModel, wholeContentModel);
					
					logger.debug("INSERT: \n" + list.get(0));
					logger.debug("DELETE: \n" + list.get(1));
					
					String updateQueryInsert = String.format(
							  "INSERT DATA { %n"
							+ "	%s %n"
							+ "}", list.get(0));
					UpdateAction.parseExecute(updateQueryInsert, wholeContentModel);
					
					String updateQueryDelete = String.format(
							  "DELETE DATA { %n"
							+ " %s %n"
							+ "}", list.get(1));
					UpdateAction.parseExecute(updateQueryDelete, wholeContentModel);
					
					String triples = Management.writeJenaModelToNTriplesString(wholeContentModel);
					logger.debug("Updated model as N-Triples: \n" + triples); 
					
					// Execute MERGE MANUAL query
					response = Management.executeMergeQuery(graphName, sdd, user, message, MergeQueryTypeEnum.MANUAL, revisionNumberBranchA, revisionNumberBranchB, triples, differenceModel);
				} else {
					String triples = Management.getTriplesOfMergeWithQuery(differenceModel);
					
					// Execute MERGE WITH query
					response = Management.executeMergeQuery(graphName, sdd, user, message, MergeQueryTypeEnum.WITH, revisionNumberBranchA, revisionNumberBranchB, triples, differenceModel);
				}
				
				report.setVisible(false);
				
				// Show message dialog
				if ((response != null) && (response.getStatusCode() == HttpURLConnection.HTTP_CREATED)) {
					logger.info("Merge query successfully executed.");
					String newRevisionNumber = Management.getRevisionNumberOfNewRevisionHeaderParameter(response, graphName);
					JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query successfully executed. Revision number of merged revision: " + newRevisionNumber, "Info", JOptionPane.INFORMATION_MESSAGE);
					// Create empty difference model
					differenceModel = new DifferenceModel();
					// Update application UI
					updateDifferencesTree();
					// Create the revision graph
					createGraph(graphName);
				} else {
					logger.error("Merge query could not be executed.");
					JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query could not be executed. Maybe another user committed changes to one of the branches to merge.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}


	/**
	 * Select all entries of resolution triples table.
	 */
	public static void selectAllEntriesResolutionTriples() {
		if (ApplicationUI.getTableModelResolutionTriples().getRowCount() > 0) {
			ApplicationUI.getTableResolutionTriples().setRowSelectionInterval(0, ApplicationUI.getTableModelResolutionTriples().getRowCount() - 1);
		}		
	}
	
	
	/**
	 * Update the semantic enrichment table model all classes.
	 */
	public static void updateTableModelSemanticEnrichmentAllClasses() {
		// Get the table model
		TableModelSemanticEnrichmentAllClasses tableModel = ApplicationUI.getTableModelSemanticEnrichmentAllClasses();
		
		// Get key sets
		ArrayList<String> keySetClassModelBranchA = new ArrayList<String>(classModelBranchA.getClassStructures().keySet());
		ArrayList<String> keySetClassModelBranchB = new ArrayList<String>(classModelBranchB.getClassStructures().keySet());
		
		// Iterate over all class URIs of branch A
		@SuppressWarnings("unchecked")
		Iterator<String> iteKeySetClassModelBranchA = ((ArrayList<String>) keySetClassModelBranchA.clone()).iterator();
		while (iteKeySetClassModelBranchA.hasNext()) {
			String currentKeyBranchA = iteKeySetClassModelBranchA.next();
			
			// Add all class URIs to table model which are in both branches
			if (keySetClassModelBranchB.contains(currentKeyBranchA)) {
				TableEntrySemanticEnrichmentAllClasses tableEntry = new TableEntrySemanticEnrichmentAllClasses(classModelBranchA.getClassStructures().get(currentKeyBranchA), classModelBranchB.getClassStructures().get(currentKeyBranchA), new Object[]{currentKeyBranchA, currentKeyBranchA});
				tableModel.addRow(tableEntry);
				// Remove key from branch A key set copy
				keySetClassModelBranchA.remove(currentKeyBranchA);
				// Remove key from branch B key set copy
				keySetClassModelBranchB.remove(currentKeyBranchA);
			}
		}
		
		// Iterate over all class URIs of branch A (will only contain the classes which are not in B)
		Iterator<String> iteKeySetClassModelBranchAOnly = keySetClassModelBranchA.iterator();
		while (iteKeySetClassModelBranchAOnly.hasNext()) {
			String currentKeyBranchA = iteKeySetClassModelBranchAOnly.next();
			TableEntrySemanticEnrichmentAllClasses tableEntry = new TableEntrySemanticEnrichmentAllClasses(classModelBranchA.getClassStructures().get(currentKeyBranchA), new ClassStructure(null), new Object[]{currentKeyBranchA, ""});
			tableModel.addRow(tableEntry);
		}
		
		// Iterate over all class URIs of branch B (will only contain the classes which are not in A)
		Iterator<String> iteKeySetClassModelBranchBOnly = keySetClassModelBranchB.iterator();
		while (iteKeySetClassModelBranchBOnly.hasNext()) {
			String currentKeyBranchB = iteKeySetClassModelBranchBOnly.next();
			TableEntrySemanticEnrichmentAllClasses tableEntry = new TableEntrySemanticEnrichmentAllClasses(new ClassStructure(null), classModelBranchB.getClassStructures().get(currentKeyBranchB), new Object[]{"", currentKeyBranchB});
			tableModel.addRow(tableEntry);
		}
		
		ApplicationUI.getTableResolutionSemanticEnrichmentAllClasses().updateUI();
	}
	
	
	/**
	 * Update the semantic enrichment table model triples.
	 * 
	 * @throws IOException 
	 */
	public static void updateTableModelSemanticEnrichmentTriples() throws IOException {
		// Get the table model
		TableModelSemanticEnrichmentClassTriples tableModel = ApplicationUI.getTableModelSemanticEnrichmentClassTriples();
		// Clear the table model
		tableModel.removeAllElements();
		
		// Get the currently selected table entry	
		TableEntrySemanticEnrichmentAllClasses currentTableEntry = ApplicationUI.getTableModelSemanticEnrichmentAllClasses().getTableEntry(ApplicationUI.getTableResolutionSemanticEnrichmentAllClasses().getSelectedRow());
		String currentClassUriA = currentTableEntry.getClassStructureA().getClassUri();
		String currentClassUriB = currentTableEntry.getClassStructureB().getClassUri();
		
		// Triple key set lists of both branches
		ArrayList<String> keySetTriplesBranchA = new ArrayList<String>();
		ArrayList<String> keySetTriplesBranchB = new ArrayList<String>();

		String classUri = null;
		if (currentClassUriA != null) {
			classUri = currentClassUriA;
			keySetTriplesBranchA = new ArrayList<String>(classModelBranchA.getClassStructures().get(classUri).getTriples().keySet());
			if (currentClassUriB != null) {
				keySetTriplesBranchB = new ArrayList<String>(classModelBranchB.getClassStructures().get(classUri).getTriples().keySet());
			}
		} else {
			classUri = currentClassUriB;
			keySetTriplesBranchB = new ArrayList<String>(classModelBranchB.getClassStructures().get(classUri).getTriples().keySet());
		}
		
		// Iterate over all triples of branch A
		@SuppressWarnings("unchecked")
		Iterator<String> iteKeySetTriplesBranchA = ((ArrayList<String>) keySetTriplesBranchA.clone()).iterator();
		while (iteKeySetTriplesBranchA.hasNext()) {
			String currentKeyBranchA = iteKeySetTriplesBranchA.next();
			
			// Add all triples to table model which are in both branches
			if (keySetTriplesBranchB.contains(currentKeyBranchA)) {
				TripleClassStructure currentTriple = classModelBranchA.getClassStructures().get(classUri).getTriples().get(currentKeyBranchA);
				Difference difference = currentTriple.getDifference();
				if (difference != null) {
					DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
					SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
					TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(
																					difference,
																					semanticDefinitionResult.getSemanticDescription(),
																					semanticDefinitionResult.getSemanticResolutionOptions(),
																					semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																					semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																					Management.createRowDataSemanticEnrichmentClassTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
					tableModel.addRow(tableEntry);
					// Remove key from branch A key set copy
					keySetTriplesBranchA.remove(currentKeyBranchA);
					// Remove key from branch B key set copy
					keySetTriplesBranchB.remove(currentKeyBranchA);
				} else {
					// No difference is specified for current triple
					TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(null, "", new ArrayList<String>(), -1, -1, 
																					Management.createRowDataSemanticEnrichmentClassTriplesWithoutDifference(currentTriple.getTriple()));
					tableModel.addRow(tableEntry);
					// Remove key from branch A key set copy
					keySetTriplesBranchA.remove(currentKeyBranchA);
					// Remove key from branch B key set copy
					keySetTriplesBranchB.remove(currentKeyBranchA);
				}
			}
		}
		
		// Iterate over all triples of branch A (will only contain the triples which are not in B)
		Iterator<String> iteKeySetTriplesBranchAOnly = keySetTriplesBranchA.iterator();
		while (iteKeySetTriplesBranchAOnly.hasNext()) {
			String currentKeyBranchA = iteKeySetTriplesBranchAOnly.next();
			TripleClassStructure currentTriple = classModelBranchA.getClassStructures().get(classUri).getTriples().get(currentKeyBranchA);
			Difference difference = currentTriple.getDifference();
			if (difference != null) {
				DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
				SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
				TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(
																				difference,
																				semanticDefinitionResult.getSemanticDescription(),
																				semanticDefinitionResult.getSemanticResolutionOptions(),
																				semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																				semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																				Management.createRowDataSemanticEnrichmentClassTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
				tableModel.addRow(tableEntry);
			} else {
				// No difference is specified for current triple
				TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(null, "", new ArrayList<String>(), -1, -1, 
																				Management.createRowDataSemanticEnrichmentClassTriplesWithoutDifference(currentTriple.getTriple()));
				tableModel.addRow(tableEntry);
			}
		}
		
		// Iterate over all triples of branch B (will only contain the triples which are not in A)
		Iterator<String> iteKeySetTriplesBranchBOnly = keySetTriplesBranchB.iterator();
		while (iteKeySetTriplesBranchBOnly.hasNext()) {
			String currentKeyBranchB = iteKeySetTriplesBranchBOnly.next();
			TripleClassStructure currentTriple = classModelBranchB.getClassStructures().get(classUri).getTriples().get(currentKeyBranchB);
			Difference difference = currentTriple.getDifference();
			if (difference != null) {
				DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
				SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
				TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(
																				difference,
																				semanticDefinitionResult.getSemanticDescription(),
																				semanticDefinitionResult.getSemanticResolutionOptions(),
																				semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																				semanticDefinitionResult.getDefaultSemanticResolutionOption(),
																				Management.createRowDataSemanticEnrichmentClassTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
				tableModel.addRow(tableEntry);
			} else {
				// No difference is specified for current triple
				TableEntrySemanticEnrichmentClassTriples tableEntry = new TableEntrySemanticEnrichmentClassTriples(null, "", new ArrayList<String>(), -1, -1, 
																				Management.createRowDataSemanticEnrichmentClassTriplesWithoutDifference(currentTriple.getTriple()));
				tableModel.addRow(tableEntry);
			}
		}

		// Update triples table
		ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().updateUI();
	}
	
	
	/**
	 * Update the table model of the configuration prefix mapping.
	 */
	private static void updateTableModelConfigurationPrefixMapping() {
		DefaultTableModel tableModel = ConfigurationDialog.getTableModelPrefixMappings();
		// Clear the table
		tableModel.setColumnCount(0);
		tableModel.getDataVector().removeAllElements();
		// Add the columns
		tableModel.addColumn("Prefix");
		tableModel.addColumn("Mapping");
		
		HashMap<String, String> mappings = Config.prefixMappings;
		Iterator<String> iteMappingKeys = mappings.keySet().iterator();
		while (iteMappingKeys.hasNext()) {
			String currentMapping = iteMappingKeys.next();
			String currentPrefix = mappings.get(currentMapping);
			tableModel.addRow(new Object[]{currentPrefix, currentMapping});
		}		
		ConfigurationDialog.getTablePrefixMappings().updateUI();
	}


	/**
	 * Show the configuation dialog.
	 */
	public static void showConfigurationDialog() {
		// Update the remote text fields
		ConfigurationDialog.getTfR43plesSparqlEndpoint().setText(Config.r43ples_sparql_endpoint);
		ConfigurationDialog.getTfR43plesRevisionGraph().setText(Config.r43ples_revision_graph);
		ConfigurationDialog.getTfR43plesSddGraph().setText(Config.r43ples_sdd_graph);		
		// Update the local table
		updateTableModelConfigurationPrefixMapping();
		
		configDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		configDialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		configDialog.setModal(true);
		configDialog.setVisible(true);
	}


	/**
	 * Create a new prefix mapping entry in corresponding table.
	 */
	public static void createNewPrefixMappingEntry() {
		DefaultTableModel tableModel = ConfigurationDialog.getTableModelPrefixMappings();
		tableModel.addRow(new Object[]{"", ""});
		ConfigurationDialog.getTablePrefixMappings().updateUI();
	}


	/**
	 * Delete the selected prefix mapping from corresponding table.
	 */
	public static void deleteSelectedPrefixMapping() {
		JTable table = ConfigurationDialog.getTablePrefixMappings();
		int[] selectedRows = table.getSelectedRows();
		// Sort the array
		Arrays.sort(selectedRows);
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			int selectedRow = table.convertRowIndexToModel(selectedRows[i]);
			ConfigurationDialog.getTableModelPrefixMappings().removeRow(selectedRow);
		}
		table.updateUI();
	}


	/**
	 * Write the current configuration to file.
	 * 
	 * @throws ConfigurationException 
	 */
	public static void writeConfigurationToFile() throws ConfigurationException {
		// Store the current configuration properties
		Config.r43ples_sparql_endpoint = ConfigurationDialog.getTfR43plesSparqlEndpoint().getText();
		Config.r43ples_revision_graph = ConfigurationDialog.getTfR43plesRevisionGraph().getText();
		Config.r43ples_sdd_graph = ConfigurationDialog.getTfR43plesSddGraph().getText();
		// Convert table model to hash map
		HashMap<String, String> map = Config.prefixMappings;
		map.clear();
		DefaultTableModel tableModel = ConfigurationDialog.getTableModelPrefixMappings();
		int rowCount = ConfigurationDialog.getTableModelPrefixMappings().getRowCount();
		for (int r = 0; r < rowCount; r++) {
			String key = (String) tableModel.getValueAt(r, 1);
			String value = (String) tableModel.getValueAt(r, 0);
			if (!key.equals("") && !value.equals("")) {
				map.put(key, value);
			}
		}
		Config.writeConfig("client.conf");
		configDialog.setVisible(false);
	}


	/**
	 * Close configuration dialog without changes.
	 */
	public static void closeConfigurationDialog() {
		configDialog.setVisible(false);
	}
	
	
	/**
	 * The table resolution semantic enrichment class triples selection changed.
	 * Update the revision graph. The referenced revisions of the selected table row will be highlighted in the graph.
	 */
	public static void tableResolutionSemanticEnrichmentClassTriplesSelectionChanged() {
		int[] rows = ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().getSelectedRows();
		if (rows.length == 1) {
			int index = ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().convertRowIndexToModel(rows[0]);
			TableEntrySemanticEnrichmentClassTriples entry = ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getTableEntry(index);
			if (entry.getDifference() != null) {
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
			}
		} else {
			// Remove highlighting of already highlighted nodes
			Management.removeHighlighting(graph, highlightedNodeNameA);
			Management.removeHighlighting(graph, highlightedNodeNameB);
		}
		
		gp.updateUI();
	}


	/**
	 * Approve selected entries of resolution semantic enrichment class triples table.
	 */
	public static void approveSelectedEntriesResolutionSemanticEnrichmentClassTriples() {
		int[] selectedRows = ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().getSelectedRows();
		// Sort the array
		Arrays.sort(selectedRows);
		
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			int selectedRow = ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().convertRowIndexToModel(selectedRows[i]);
			Difference difference = ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getTableEntry(selectedRow).getDifference();
			difference.setResolutionState(ResolutionState.RESOLVED);
			// Propagate changes to difference model
			int selectedOption = ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getTableEntry(selectedRow).getSelectedSemanticResolutionOption();
			if (selectedOption == 1) {
				difference.setTripleResolutionState(SDDTripleStateEnum.ADDED);
			} else {
				difference.setTripleResolutionState(SDDTripleStateEnum.DELETED);
			}
		}
		ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().updateUI();
		
		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		
		ApplicationUI.getTreeDifferencesDivision().updateUI();
	}
	
	
	/**
	 * Select all entries of resolution semantic enrichment class triples table.
	 */
	public static void selectAllEntriesResolutionSemanticEnrichmentClassTriples() {
		if (ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getRowCount() > 0) {
			ApplicationUI.getTableResolutionSemanticEnrichmentClassTriples().setRowSelectionInterval(0, ApplicationUI.getTableModelSemanticEnrichmentClassTriples().getRowCount() - 1);
		}		
	}
	
	
	/**
	 * Update the filter table model.
	 */
	public static void updateTableModelFilter() {
		// Get the table model
		TableModelFilter tableModel = ApplicationUI.getTableModelFilter();
		
		// Iterate over all property URIs
		Iterator<String> iteProperties = propertyList.iterator();
		while (iteProperties.hasNext()) {
			String currentProperty = iteProperties.next();
			TableEntryFilter tableEntry = new TableEntryFilter(currentProperty, true);
			tableModel.addRow(tableEntry);			
		}
		
		ApplicationUI.getTableFilter().updateUI();
	}
	
	
	/**
	 * Get the currently activated filters.
	 * 
	 * @return the array list of activated filters
	 */
	public static ArrayList<String> getActivatedFilters() {
		return ApplicationUI.getTableModelFilter().getActivatedFilters();
	}

}
