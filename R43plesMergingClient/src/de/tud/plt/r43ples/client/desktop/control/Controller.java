package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import org.apache.commons.lang.SystemUtils;
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
import de.tud.plt.r43ples.client.desktop.model.structure.IndividualModel;
import de.tud.plt.r43ples.client.desktop.model.structure.IndividualStructure;
import de.tud.plt.r43ples.client.desktop.model.structure.Difference;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceGroup;
import de.tud.plt.r43ples.client.desktop.model.structure.DifferenceModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HighLevelChangeModel;
import de.tud.plt.r43ples.client.desktop.model.structure.HighLevelChangeRenaming;
import de.tud.plt.r43ples.client.desktop.model.structure.HttpResponse;
import de.tud.plt.r43ples.client.desktop.model.structure.ReportResult;
import de.tud.plt.r43ples.client.desktop.model.structure.SemanticDefinitionResult;
import de.tud.plt.r43ples.client.desktop.model.structure.Triple;
import de.tud.plt.r43ples.client.desktop.model.structure.TripleIndividualStructure;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryFilter;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelFilter;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.model.tree.TreeNodeObject;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ApplicationUI;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ConfigurationDialog;
import de.tud.plt.r43ples.client.desktop.ui.dialog.ReportDialog;
import de.tud.plt.r43ples.client.desktop.ui.dialog.StartMergingDialog;
import de.tud.plt.r43ples.client.desktop.ui.editor.table.CustomComboBoxEditor;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellCheckBoxRendererResolutionHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellComboBoxRendererSemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellPanelRendererResolutionTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellPanelRendererSemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererFilter;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererResolutionHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererResolutionTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererSemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererSemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellPanelRendererSummaryReport;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellCheckBoxRendererResolutionTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.table.TableCellRendererSummaryReport;

/**
 * The controller.
 * 
 * @author Stephan Hensel
 *
 */
public class Controller {

	/** The logger. */
	private static Logger logger = Logger.getLogger(Controller.class);
	/** The revision number of the branch A. **/
	private static String revisionNumberBranchA;
	/** The revision number of the branch B. **/
	private static String revisionNumberBranchB;
	/** The difference model. **/
	private static DifferenceModel differenceModel = new DifferenceModel();
	/** The high level change model. **/
	private static HighLevelChangeModel highLevelChangeModel = new HighLevelChangeModel();
	/** The individual model of branch A. **/
	private static IndividualModel individualModelBranchA;
	/** The individual model of branch B. **/
	private static IndividualModel individualModelBranchB;
	/** The grappa graph which contains the revision tree. **/
	private static Graph graph;
	/** The grappa panel which holds the visualization of the graph. **/
	private static GrappaPanel gp;
	/** The grappa panel which holds the visualization of the graph for the semantic enrichment panel. **/
	private static GrappaPanel gpSemanticEnrichment;
	/** The grappa panel which holds the visualization of the graph for the high level panel. **/
	private static GrappaPanel gpHighLevel;
	/** The currently highlighted node name A in the revision graph. **/
	private static String highlightedNodeNameA = null;
	/** The currently highlighted node name B in the revision graph. **/
	private static String highlightedNodeNameB = null;
	/** The report result. **/
	private static ReportResult reportResult = null;
	/** The properties array list. **/
	private static ArrayList<String> propertyList;
	/** The wait cursor. **/
	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	/** The default cursor. **/
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	/** The divider location of application split pane. **/
	private static int dividerLocationSplitPaneApplication = 300;
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## General.                                                                                                                                                             ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Converts a triple string to a string in which URIs are replaced by prefixes which were specified in the configuration.
	 * If no prefix was found or if input string is a literal the input string will be returned.
	 * 
	 * @param tripleString the triple string (subject or predicate or object) to convert
	 * @return the converted triple string or input string
	 */
	public static String convertTripleStringToPrefixTripleString(String tripleString) {
		return Management.convertTripleStringToPrefixTripleString(tripleString);
	}
	
	
	/**
	 * Get the subject of triple.
	 * 
	 * @param triple the triple
	 * @return the formatted subject
	 */
	public static String getSubject(Triple triple) {
		return Management.getSubject(triple);
	}
	
	
	/**
	 * Get the predicate of triple. If predicate equals rdf:type 'a' will be returned.
	 * 
	 * @param triple the triple
	 * @return the formatted predicate
	 */
	public static String getPredicate(Triple triple) {
		return Management.getPredicate(triple);
	}
	
	
	/**
	 * Get the object of triple.
	 * 
	 * @param triple the triple
	 * @return the formatted object
	 */
	public static String getObject(Triple triple) {
		return Management.getObject(triple);
	}


	/**
	 * The application tab selection changed. Update the graphs.
	 */
	public static void applicationTabSelectionChanged() {
		switch (ApplicationUI.getTabbedPaneApplication().getSelectedIndex()) {
		case 0:
			tableResolutionTriplesSelectionChanged();
			break;
		
		case 1:
			tableResolutionSemanticEnrichmentIndividualTriplesSelectionChanged();
			break;
			
		case 2:
			tableResolutionHighLevelChangesSelectionChanged();
			break;

		default:
			break;
		}
	}
	

	/**
	 * The split pane application divider location changed.
	 */
	public static void splitPaneApplicationDividerLocationChanged() {
		if (dividerLocationSplitPaneApplication != -1) {
			dividerLocationSplitPaneApplication = ApplicationUI.getSplitPaneApplication().getDividerLocation();
		}
	}
	
	
	/**
	 * Get the difference group of difference.
	 * 
	 * @param difference the difference
	 * @return the difference group
	 */
	public static DifferenceGroup getDifferenceGroupOfDifference(Difference difference) {
		return Management.getDifferenceGroupOfDifference(difference, differenceModel);
	}
	
	
	/**
	 * Get the highest resolution state of table entry of semantic enrichment all individuals table.
	 * 
	 * @param individualStructureA the individual structure A
	 * @param individualStructureB the individual structure B
	 * @return the highest resolution state
	 */
	public static ResolutionState getResolutionStateOfTableEntrySemanticEnrichmentAllIndividuals(IndividualStructure individualStructureA, IndividualStructure individualStructureB) {
		return Management.getResolutionStateOfTableEntrySemanticEnrichmentAllIndividuals(individualStructureA, individualStructureB);
	}
	
	
	/**
	 * Show IO exception dialog.
	 * 
	 * @param parentComponent the parent component
	 */
	public static void showIOExceptionDialog(Component parentComponent) {
		if (ApplicationUI.frmRplesMergingClient != null)
			ApplicationUI.frmRplesMergingClient.setCursor(defaultCursor);
		if (StartMergingDialog.dialog != null)
			StartMergingDialog.dialog.setCursor(defaultCursor);
		if (ReportDialog.dialog != null)
			ReportDialog.dialog.setCursor(defaultCursor);
		if (ConfigurationDialog.dialog != null)
			ConfigurationDialog.dialog.setCursor(defaultCursor);
		
		JOptionPane.showMessageDialog(parentComponent, "Server is currently not available. Please check your connection!", "IOException occurred.", JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * Show configuration exception dialog.
	 * 
	 * @param parentComponent the parent component
	 */
	public static void showConfigurationExceptionDialog(Component parentComponent) {
		ApplicationUI.frmRplesMergingClient.setCursor(defaultCursor);
		StartMergingDialog.dialog.setCursor(defaultCursor);
		ReportDialog.dialog.setCursor(defaultCursor);
		ConfigurationDialog.dialog.setCursor(defaultCursor);
		
		JOptionPane.showMessageDialog(parentComponent, "Configuration is not writeable!", "ConfigurationException occurred.", JOptionPane.ERROR_MESSAGE);
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Configuration.                                                                                                                                                       ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
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
		ConfigurationDialog.getTfR43plesJsonRevisedGraphs().setText(Config.r43ples_json_revisedgraphs);
		ConfigurationDialog.getTfR43plesRevisionGraph().setText(Config.r43ples_revision_graph);
		ConfigurationDialog.getTfR43plesSddGraph().setText(Config.r43ples_sdd_graph);		
		// Update the local table
		updateTableModelConfigurationPrefixMapping();
		
		ConfigurationDialog.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		ConfigurationDialog.dialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		ConfigurationDialog.dialog.setModal(true);
		ConfigurationDialog.dialog.setVisible(true);
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
		Config.r43ples_json_revisedgraphs = ConfigurationDialog.getTfR43plesJsonRevisedGraphs().getText();
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
		
		// Update the corresponding tables and trees
		ApplicationUI.getTreeDifferencesDivision().updateUI();
		ApplicationUI.getTableResolutionTriples().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
		ApplicationUI.getTableFilter().updateUI();
		
		ConfigurationDialog.dialog.setVisible(false);
	}


	/**
	 * Close configuration dialog without changes.
	 */
	public static void closeConfigurationDialog() {
		ConfigurationDialog.dialog.setVisible(false);
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Filter.                                                                                                                                                              ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
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
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Start merging dialog.                                                                                                                                                ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Show the merging dialog.
	 * 
	 * @throws IOException 
	 */
	public static void showStartMergingDialog() throws IOException {
		ApplicationUI.frmRplesMergingClient.setCursor(waitCursor);
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
		
		StartMergingDialog.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		StartMergingDialog.dialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		StartMergingDialog.dialog.setModal(true);
		StartMergingDialog.dialog.setVisible(true);
		ApplicationUI.frmRplesMergingClient.setCursor(defaultCursor);
	}

	
	/**
	 * Update the revision combo boxes. 
	 * 
	 * @throws IOException 
	 */
	public static void updateRevisionComboBoxes() throws IOException {
		StartMergingDialog.dialog.setCursor(waitCursor);
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
		StartMergingDialog.dialog.setCursor(defaultCursor);
	}


	/**
	 * Start new merge process when the selected branches are not equal.
	 * 
	 * @throws IOException 
	 */
	public static void startNewMergeProcess() throws IOException {
		if (StartMergingDialog.getcBModelRevisionA().getSelectedItem().equals(StartMergingDialog.getcBModelRevisionB().getSelectedItem())) {
			// Show error message dialog when selected branches are equal
			JOptionPane.showMessageDialog(StartMergingDialog.dialog, "The selected branches must be different!", "Error: Selected branches are equal.", JOptionPane.ERROR_MESSAGE);
		} else if (StartMergingDialog.getTfUser().getText().equals("")) {
			// Show error message when no user was specified
			JOptionPane.showMessageDialog(StartMergingDialog.dialog, "A user must be specified!", "Error: No user specified.", JOptionPane.ERROR_MESSAGE);
		} else if (StartMergingDialog.getTextAreaMessage().getText().equals("")) {
			// Show error message when no message was specified allowed
			JOptionPane.showMessageDialog(StartMergingDialog.dialog, "An empty message is not allowed!", "Error: No message specified.", JOptionPane.ERROR_MESSAGE);
		} else {
			// Start new merging process
			StartMergingDialog.dialog.setCursor(waitCursor);
			// Get the selected graph
			String graphName = (String) StartMergingDialog.getcBModelGraph().getSelectedItem();
			String sdd = (String) StartMergingDialog.getcBModelSDD().getSelectedItem();
			
			String user = StartMergingDialog.getTfUser().getText();
			String commitMessage = StartMergingDialog.getTextAreaMessage().getText();
			
			String branchNameA = (String) StartMergingDialog.getcBModelRevisionA().getSelectedItem();
			String branchNameB = (String) StartMergingDialog.getcBModelRevisionB().getSelectedItem();

			// Disable the push button
			ApplicationUI.getBtnPush().setEnabled(false);
			
			MergeQueryTypeEnum type = null;
			if (StartMergingDialog.getRdbtnMergingMethodAUTO().isSelected()) {
				type = MergeQueryTypeEnum.AUTO;
			} else if (StartMergingDialog.getRdbtnMergingMethodCOMMON().isSelected()) {
				type = MergeQueryTypeEnum.COMMON;
			} else {
				type = MergeQueryTypeEnum.MANUAL;
			}
			
			// Set the column headers
			ApplicationUI.getTableModelResolutionTriples().removeAllElements();
			ApplicationUI.getTableResolutionTriples().getTableHeader().getColumnModel().getColumn(3).setHeaderValue("State " + branchNameA + " (Revision)");
			ApplicationUI.getTableResolutionTriples().getTableHeader().getColumnModel().getColumn(4).setHeaderValue("State " + branchNameB + " (Revision)");
			ApplicationUI.getTableResolutionTriples().updateUI();
			
			ApplicationUI.getTableModelSemanticEnrichmentAllIndividuals().removeAllElements();
			ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getTableHeader().getColumnModel().getColumn(0).setHeaderValue("Individuals of " + branchNameA);
			ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getTableHeader().getColumnModel().getColumn(1).setHeaderValue("Individuals of " + branchNameB);
			ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
			
			ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().removeAllElements();
			ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getTableHeader().getColumnModel().getColumn(3).setHeaderValue("State " + branchNameA + " (Revision)");
			ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getTableHeader().getColumnModel().getColumn(4).setHeaderValue("State " + branchNameB + " (Revision)");
			ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
			
			ApplicationUI.getTableModelResolutionHighLevelChanges().removeAllElements();
			ApplicationUI.getTableResolutionHighLevelChanges().updateUI();
			
			ReportDialog.getTableModel().removeAllElements();
			ReportDialog.getTable().getTableHeader().getColumnModel().getColumn(3).setHeaderValue("State " + branchNameA + " (Revision)");
			ReportDialog.getTable().getTableHeader().getColumnModel().getColumn(4).setHeaderValue("State " + branchNameB + " (Revision)");
			ReportDialog.getTable().updateUI();
			
			HttpResponse response = Management.executeMergeQuery(graphName, sdd, user, commitMessage, type, branchNameA, branchNameB, null, differenceModel);
			
			if (response.getStatusCode() == HttpURLConnection.HTTP_CONFLICT) {
				// There was a conflict
				logger.info("Merge query produced conflicts.");
				// Read the difference model to java model
				Management.readDifferenceModel(response.getBody(), differenceModel);
				// Create the high level change (renaming) model
				Management.createHighLevelChangeRenamingModel(highLevelChangeModel, differenceModel);
				// Save the current revision numbers
				revisionNumberBranchA = Management.getRevisionNumberOfBranchAHeaderParameter(response, graphName);
				revisionNumberBranchB = Management.getRevisionNumberOfBranchBHeaderParameter(response, graphName);
				
				// Create the individual models of both branches
				individualModelBranchA = Management.createIndividualModelOfRevision(graphName, branchNameA, differenceModel);
				individualModelBranchB = Management.createIndividualModelOfRevision(graphName, branchNameB, differenceModel);
				
				// Create the property list of revisions
				propertyList = Management.getPropertiesOfRevision(graphName, branchNameA, branchNameB);
				
				// Enable the push button
				ApplicationUI.getBtnPush().setEnabled(true);
				
				
				// Close dialog after execution and update UI
				initializeCompleteUI(graphName);
				
				// Check existence of individuals
				if (individualModelBranchA.getIndividualStructures().isEmpty() && individualModelBranchB.getIndividualStructures().isEmpty()) {
					// Disable semantic enrichment individuals tab
					ApplicationUI.getTabbedPaneApplication().setSelectedIndex(0);
					ApplicationUI.getTabbedPaneApplication().setEnabledAt(1, false);
				} else {
					// Enable semantic enrichment individuals tab
					ApplicationUI.getTabbedPaneApplication().setEnabledAt(1, true);
				}
				
				// Check existence of high level changes
				if (highLevelChangeModel.getHighLevelChangesRenaming().isEmpty()) {
					// Disable high level changes tab
					ApplicationUI.getTabbedPaneApplication().setSelectedIndex(0);
					ApplicationUI.getTabbedPaneApplication().setEnabledAt(2, false);
				} else {
					// Enable high level changes tab
					ApplicationUI.getTabbedPaneApplication().setEnabledAt(2, true);
				}
				
				StartMergingDialog.dialog.setVisible(false);
				StartMergingDialog.dialog.setCursor(defaultCursor);
				
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query produced conflicts. Please resolve conflicts manually.", "Info", JOptionPane.INFORMATION_MESSAGE);
			} else if (response.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
				// There was no conflict merged revision was created
				logger.info("Merge query produced no conflicts. Merged revision was created.");
				// Create an empty difference model
				differenceModel = new DifferenceModel();
				// Create empty individual models
				individualModelBranchA = new IndividualModel();
				individualModelBranchB = new IndividualModel();
				// Create an empty high level change model
				highLevelChangeModel = new HighLevelChangeModel();
				// Create an empty property list
				propertyList = new ArrayList<String>();
				
				// Get the revision number
				String newRevisionNumber = Management.getRevisionNumberOfNewRevisionHeaderParameter(response, graphName);
				
				// Close dialog after execution and update UI
				initializeCompleteUI(graphName);
				
				// Disable tabs
				ApplicationUI.getTabbedPaneApplication().setSelectedIndex(0);
				ApplicationUI.getTabbedPaneApplication().setEnabledAt(1, false);
				ApplicationUI.getTabbedPaneApplication().setEnabledAt(2, false);
				
				StartMergingDialog.dialog.setVisible(false);
				StartMergingDialog.dialog.setCursor(defaultCursor);
				
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query successfully executed. Revision number of merged revision: " + newRevisionNumber, "Info", JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Error occurred
				// Create an empty difference model
				differenceModel = new DifferenceModel();
				// Create empty individual models
				individualModelBranchA = new IndividualModel();
				individualModelBranchB = new IndividualModel();
				// Create an empty high level change model
				highLevelChangeModel = new HighLevelChangeModel();
				// Create an empty property list
				propertyList = new ArrayList<String>();
								
				// Close dialog after execution and update UI
				initializeCompleteUI(graphName);
				
				// Disable tabs
				ApplicationUI.getTabbedPaneApplication().setSelectedIndex(0);
				ApplicationUI.getTabbedPaneApplication().setEnabledAt(1, false);
				ApplicationUI.getTabbedPaneApplication().setEnabledAt(2, false);
				
				StartMergingDialog.dialog.setVisible(false);
				StartMergingDialog.dialog.setCursor(defaultCursor);
				
				JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query could not be executed. Undefined error occurred", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	/**
	 * Initializes the complete UI after creation of a new merge prozess.
	 * 
	 * @param graphName the graph name
	 * @throws IOException 
	 * @throws HttpException 
	 */
	private static void initializeCompleteUI(String graphName) throws HttpException, IOException {
		ApplicationUI.frmRplesMergingClient.setCursor(waitCursor);
		// Set the cell renderer
		TableCellRendererResolutionTriples renderer = new TableCellRendererResolutionTriples();
		TableCellPanelRendererResolutionTriples rendererPanel = new TableCellPanelRendererResolutionTriples();
		for (int i = 0; i < ApplicationUI.getTableModelResolutionTriples().getColumnCount() - 1; i++) {
			if ((i != 3) && (i != 4)) {
				ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
		}
		ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(3).setCellRenderer(rendererPanel);
		ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(4).setCellRenderer(rendererPanel);
		ApplicationUI.getTableResolutionTriples().getColumnModel().getColumn(6).setCellRenderer(new TableCellCheckBoxRendererResolutionTriples());
		
		TableCellRendererSemanticEnrichmentAllIndividuals rendererSemanticIndividuals = new TableCellRendererSemanticEnrichmentAllIndividuals();
		for (int i = 0; i < ApplicationUI.getTableModelSemanticEnrichmentAllIndividuals().getColumnCount(); i++) {
			ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getColumnModel().getColumn(i).setCellRenderer(rendererSemanticIndividuals);
		}
		
		TableCellRendererSemanticEnrichmentIndividualTriples rendererSemanticTriples = new TableCellRendererSemanticEnrichmentIndividualTriples();
		for (int i = 0; i < ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getColumnCount() - 1; i++) {
			if ((i != 3) && (i != 4)) {
				ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getColumnModel().getColumn(i).setCellRenderer(rendererSemanticTriples);
			}
		}
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getColumnModel().getColumn(3).setCellRenderer(new TableCellPanelRendererSemanticEnrichmentIndividualTriples());
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getColumnModel().getColumn(4).setCellRenderer(new TableCellPanelRendererSemanticEnrichmentIndividualTriples());
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getColumnModel().getColumn(7).setCellRenderer(new TableCellComboBoxRendererSemanticEnrichmentIndividualTriples());
		
		TableCellRendererResolutionHighLevelChanges rendererHighLevelChanges = new TableCellRendererResolutionHighLevelChanges();
		for (int i = 0; i < ApplicationUI.getTableModelResolutionHighLevelChanges().getColumnCount() - 1; i++) {
			ApplicationUI.getTableResolutionHighLevelChanges().getColumnModel().getColumn(i).setCellRenderer(rendererHighLevelChanges);
		}
		ApplicationUI.getTableResolutionHighLevelChanges().getColumnModel().getColumn(4).setCellRenderer(new TableCellCheckBoxRendererResolutionHighLevelChanges());
		
		TableCellRendererFilter rendererFilter = new TableCellRendererFilter();
		ApplicationUI.getTableFilter().getColumnModel().getColumn(0).setCellRenderer(rendererFilter);
		
		// Set the cell editor
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getColumnModel().getColumn(7).setCellEditor(new CustomComboBoxEditor());
	
		// Update filter table
		updateTableModelFilter();
		
		// Update application UI
		updateTableResolutionTriples(new HashMap<TreeNodeObject,TreePath>());
		updateDifferencesTree();
		
		// Create the revision graph
		createGraph(graphName);
		
		/** The semantic definitions **/
		new SemanticDefinitions();
		
		// Update the semantic enrichment
		updateTableModelSemanticEnrichmentAllIndividuals();
		
		// Update the high level change table
		updateTableModelHighLevelChanges();
		
		ApplicationUI.frmRplesMergingClient.setCursor(defaultCursor);
	}
	

	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Graph.                                                                                                                                                               ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */

	
	/**
	 * Create the revision graph.
	 * 
	 * @param graphName the graph name
	 * @throws HttpException
	 * @throws IOException
	 */
	public static void createGraph(String graphName) throws HttpException, IOException {
		graph = Management.getDotGraph(graphName);	

		// Generate layout of the graph is only available at windows OS
		// GraphViz version 2.20 is needed
		if (SystemUtils.IS_OS_WINDOWS) {
			String [] processArgs = {"app/dot"};
			Process formatProcess = Runtime.getRuntime().exec(processArgs, null, null);
			boolean formatResult = GrappaSupport.filterGraph(graph, formatProcess);
			logger.debug("Format result of graph: " + formatResult);
			formatProcess.getOutputStream().close();
		}
		// TODO: Add Dot also for Linux
		
		StringWriter sw = new StringWriter();
		graph.printGraph(sw);
		logger.debug(sw.toString());

		// Create the grappa panel
		gp = new GrappaPanel(graph);
		gp.addGrappaListener(new GrappaAdapter());
		gp.setScaleToFit(true);
		gpSemanticEnrichment = new GrappaPanel(graph);
		gpSemanticEnrichment.addGrappaListener(new GrappaAdapter());
		gpSemanticEnrichment.setScaleToFit(true);
		gpHighLevel = new GrappaPanel(graph);
		gpHighLevel.addGrappaListener(new GrappaAdapter());
		gpHighLevel.setScaleToFit(true);
		
		ApplicationUI.getScrollPaneGraph().setViewportView(gp);
		ApplicationUI.getScrollPaneGraphSemanticEnrichment().setViewportView(gpSemanticEnrichment);
		ApplicationUI.getScrollPaneGraphHighLevelChanges().setViewportView(gpHighLevel);

		gp.updateUI();
		gpSemanticEnrichment.updateUI();
		gpHighLevel.updateUI();
	}
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Difference tree.                                                                                                                                                     ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
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
		// Select sub nodes
		// Get the selected nodes
		TreePath[] treePaths = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
		if (treePaths != null) {
			for (TreePath treePath : treePaths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				if (!node.isLeaf()) {
					selectSubNodes(node);
				}
			}
		}
		// Show leafs in table
		// Hash map which stores all selected tree node objects and the corresponding tree paths
		HashMap<TreeNodeObject, TreePath> map = new HashMap<TreeNodeObject, TreePath>();
		
		// Get the selected nodes
		TreePath[] treePaths1 = ApplicationUI.getTreeDifferencesDivision().getSelectionPaths();
		if (treePaths1 != null) {
			for (TreePath treePath : treePaths1) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
				if (node.isLeaf()) {
					TreeNodeObject treeNodeObject = (TreeNodeObject) node.getUserObject();
					map.put(treeNodeObject, treePath);
				}
			}
		}
		// Update the triple table
		updateTableResolutionTriples(map);
		ApplicationUI.getTableResolutionTriples().clearSelection();
		tableResolutionTriplesSelectionChanged();
	}
	
	
	/**
	 * Selects recursively all sub nodes of current node.
	 * 
	 * @param node the start node
	 */
	public static void selectSubNodes(DefaultMutableTreeNode node) {
		for (int i = 0; i < node.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			ApplicationUI.getTreeDifferencesDivision().addSelectionPath(new TreePath(child.getPath()));
			selectSubNodes(child);
			ApplicationUI.getTreeDifferencesDivision().updateUI();
		}
	}
	
	
	/**
	 * Remove the node selection by tree path. Updates parent nodes.
	 * 
	 * @param treePath the tree path
	 */
	public static void removeNodeSelection(TreePath treePath) {
		ApplicationUI.getTreeDifferencesDivision().removeSelectionPath(treePath);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
		if (node.getParent() != null) {
			DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
			TreePath parentTreePath = new TreePath(parentNode.getPath());
			removeNodeSelection(parentTreePath);
		}
	}
		
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Resolution triples table.                                                                                                                                            ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Update the resolution triples table.
	 * 
	 * @param selectedElements the hash map of all selected differences and the corresponding tree paths
	 * @throws IOException 
	 */
	public static void updateTableResolutionTriples(HashMap<TreeNodeObject, TreePath> selectedElements) throws IOException {
		// Remove the old rows
		ApplicationUI.getTableModelResolutionTriples().removeAllElements();
		ApplicationUI.getTableResolutionTriples().getSelectionModel().clearSelection();
		
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
			
//			Do not remove the approved entry from table and tree selection
//			// Remove selection of the approved row in the tree
//			TableEntry entry = ApplicationUI.getTableModelResolutionTriples().getTableEntry(selectedRow);
//			removeNodeSelection(entry.getTreePath());
//			
//			// Remove the approved row
//			ApplicationUI.getTableModelResolutionTriples().removeRow(selectedRow);
		}
		ApplicationUI.getTableResolutionTriples().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().clearSelection();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
		ApplicationUI.getTableResolutionHighLevelChanges().updateUI();
		
		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		
		ApplicationUI.getTreeDifferencesDivision().updateUI();
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
		gpSemanticEnrichment.updateUI();
		gpHighLevel.updateUI();
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
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Semantic enrichment - individuals.                                                                                                                                   ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Update the semantic enrichment table model all individuals.
	 */
	public static void updateTableModelSemanticEnrichmentAllIndividuals() {
		// Get the table model
		TableModelSemanticEnrichmentAllIndividuals tableModel = ApplicationUI.getTableModelSemanticEnrichmentAllIndividuals();
		// Remove old entries
		tableModel.removeAllElements();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getSelectionModel().clearSelection();
		
		// Get key sets
		ArrayList<String> keySetIndividualModelBranchA = new ArrayList<String>(individualModelBranchA.getIndividualStructures().keySet());
		ArrayList<String> keySetIndividualModelBranchB = new ArrayList<String>(individualModelBranchB.getIndividualStructures().keySet());
		
		// Iterate over all individual URIs of branch A
		@SuppressWarnings("unchecked")
		Iterator<String> iteKeySetIndividualModelBranchA = ((ArrayList<String>) keySetIndividualModelBranchA.clone()).iterator();
		while (iteKeySetIndividualModelBranchA.hasNext()) {
			String currentKeyBranchA = iteKeySetIndividualModelBranchA.next();
			
			// Add all individual URIs to table model which are in both branches
			if (keySetIndividualModelBranchB.contains(currentKeyBranchA)) {
				TableEntrySemanticEnrichmentAllIndividuals tableEntry = new TableEntrySemanticEnrichmentAllIndividuals(individualModelBranchA.getIndividualStructures().get(currentKeyBranchA), individualModelBranchB.getIndividualStructures().get(currentKeyBranchA), new Object[]{currentKeyBranchA, currentKeyBranchA});
				tableModel.addRow(tableEntry);
				// Remove key from branch A key set copy
				keySetIndividualModelBranchA.remove(currentKeyBranchA);
				// Remove key from branch B key set copy
				keySetIndividualModelBranchB.remove(currentKeyBranchA);
			}
		}
		
		// Iterate over all individual URIs of branch A (will only contain the individuals which are not in B)
		Iterator<String> iteKeySetIndividualModelBranchAOnly = keySetIndividualModelBranchA.iterator();
		while (iteKeySetIndividualModelBranchAOnly.hasNext()) {
			String currentKeyBranchA = iteKeySetIndividualModelBranchAOnly.next();
			TableEntrySemanticEnrichmentAllIndividuals tableEntry = new TableEntrySemanticEnrichmentAllIndividuals(individualModelBranchA.getIndividualStructures().get(currentKeyBranchA), new IndividualStructure(null), new Object[]{currentKeyBranchA, ""});
			tableModel.addRow(tableEntry);
		}
		
		// Iterate over all individual URIs of branch B (will only contain the individuals which are not in A)
		Iterator<String> iteKeySetIndividualModelBranchBOnly = keySetIndividualModelBranchB.iterator();
		while (iteKeySetIndividualModelBranchBOnly.hasNext()) {
			String currentKeyBranchB = iteKeySetIndividualModelBranchBOnly.next();
			TableEntrySemanticEnrichmentAllIndividuals tableEntry = new TableEntrySemanticEnrichmentAllIndividuals(new IndividualStructure(null), individualModelBranchB.getIndividualStructures().get(currentKeyBranchB), new Object[]{"", currentKeyBranchB});
			tableModel.addRow(tableEntry);
		}
		
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
	}
	
	
	/**
	 * Update the semantic enrichment table model triples.
	 * 
	 * @throws IOException 
	 */
	public static void updateTableModelSemanticEnrichmentTriples() throws IOException {
		// Get the table model
		TableModelSemanticEnrichmentIndividualTriples tableModel = ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples();
		// Clear the table model
		tableModel.removeAllElements();
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getSelectionModel().clearSelection();
		
		if (ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getSelectedRow() != -1) {
		
			// Get the currently selected table entry	
			TableEntrySemanticEnrichmentAllIndividuals currentTableEntry = ApplicationUI.getTableModelSemanticEnrichmentAllIndividuals().getTableEntry(ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().getSelectedRow());
			String currentIndividualUriA = currentTableEntry.getIndividualStructureA().getIndividualUri();
			String currentIndividualUriB = currentTableEntry.getIndividualStructureB().getIndividualUri();
			
			// Triple key set lists of both branches
			ArrayList<String> keySetTriplesBranchA = new ArrayList<String>();
			ArrayList<String> keySetTriplesBranchB = new ArrayList<String>();
	
			String individualUri = null;
			if (currentIndividualUriA != null) {
				individualUri = currentIndividualUriA;
				keySetTriplesBranchA = new ArrayList<String>(individualModelBranchA.getIndividualStructures().get(individualUri).getTriples().keySet());
				if (currentIndividualUriB != null) {
					keySetTriplesBranchB = new ArrayList<String>(individualModelBranchB.getIndividualStructures().get(individualUri).getTriples().keySet());
				}
			} else {
				individualUri = currentIndividualUriB;
				keySetTriplesBranchB = new ArrayList<String>(individualModelBranchB.getIndividualStructures().get(individualUri).getTriples().keySet());
			}
			
			// Iterate over all triples of branch A
			@SuppressWarnings("unchecked")
			Iterator<String> iteKeySetTriplesBranchA = ((ArrayList<String>) keySetTriplesBranchA.clone()).iterator();
			while (iteKeySetTriplesBranchA.hasNext()) {
				String currentKeyBranchA = iteKeySetTriplesBranchA.next();
				
				// Add all triples to table model which are in both branches
				if (keySetTriplesBranchB.contains(currentKeyBranchA)) {
					TripleIndividualStructure currentTriple = individualModelBranchA.getIndividualStructures().get(individualUri).getTriples().get(currentKeyBranchA);
					Difference difference = currentTriple.getDifference();
					if (difference != null) {
						DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
						SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
						TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(
																						difference,
																						semanticDefinitionResult.getSemanticDescription(),
																						semanticDefinitionResult.getSemanticResolutionOptions(),
																						semanticDefinitionResult.getSelectedSemanticResolutionOption(),
																						Management.createRowDataSemanticEnrichmentIndividualTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
						tableModel.addRow(tableEntry);
						// Remove key from branch A key set copy
						keySetTriplesBranchA.remove(currentKeyBranchA);
						// Remove key from branch B key set copy
						keySetTriplesBranchB.remove(currentKeyBranchA);
					} else {
						// No difference is specified for current triple
						TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(null, "", new ArrayList<String>(), -1, 
																						Management.createRowDataSemanticEnrichmentIndividualTriplesWithoutDifference(currentTriple.getTriple()));
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
				TripleIndividualStructure currentTriple = individualModelBranchA.getIndividualStructures().get(individualUri).getTriples().get(currentKeyBranchA);
				Difference difference = currentTriple.getDifference();
				if (difference != null) {
					DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
					SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
					TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(
																					difference,
																					semanticDefinitionResult.getSemanticDescription(),
																					semanticDefinitionResult.getSemanticResolutionOptions(),
																					semanticDefinitionResult.getSelectedSemanticResolutionOption(),
																					Management.createRowDataSemanticEnrichmentIndividualTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
					tableModel.addRow(tableEntry);
				} else {
					// No difference is specified for current triple
					TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(null, "", new ArrayList<String>(), -1, 
																					Management.createRowDataSemanticEnrichmentIndividualTriplesWithoutDifference(currentTriple.getTriple()));
					tableModel.addRow(tableEntry);
				}
			}
			
			// Iterate over all triples of branch B (will only contain the triples which are not in A)
			Iterator<String> iteKeySetTriplesBranchBOnly = keySetTriplesBranchB.iterator();
			while (iteKeySetTriplesBranchBOnly.hasNext()) {
				String currentKeyBranchB = iteKeySetTriplesBranchBOnly.next();
				TripleIndividualStructure currentTriple = individualModelBranchB.getIndividualStructures().get(individualUri).getTriples().get(currentKeyBranchB);
				Difference difference = currentTriple.getDifference();
				if (difference != null) {
					DifferenceGroup differenceGroup = Management.getDifferenceGroupOfDifference(difference, differenceModel);
					SemanticDefinitionResult semanticDefinitionResult = SemanticDefinitions.getSemanticDefinitionResult(difference, differenceGroup);
					TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(
																					difference,
																					semanticDefinitionResult.getSemanticDescription(),
																					semanticDefinitionResult.getSemanticResolutionOptions(),
																					semanticDefinitionResult.getSelectedSemanticResolutionOption(),
																					Management.createRowDataSemanticEnrichmentIndividualTriples(difference, differenceGroup, semanticDefinitionResult.getSemanticDescription()));
					tableModel.addRow(tableEntry);
				} else {
					// No difference is specified for current triple
					TableEntrySemanticEnrichmentIndividualTriples tableEntry = new TableEntrySemanticEnrichmentIndividualTriples(null, "", new ArrayList<String>(), -1, 
																					Management.createRowDataSemanticEnrichmentIndividualTriplesWithoutDifference(currentTriple.getTriple()));
					tableModel.addRow(tableEntry);
				}
			}
	
			// Clear selection
			ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getSelectionModel().clearSelection();
		}
		
		// Update triples table
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
	}
	
	
	/**
	 * The table resolution semantic enrichment individual triples selection changed.
	 * Update the revision graph. The referenced revisions of the selected table row will be highlighted in the graph.
	 */
	public static void tableResolutionSemanticEnrichmentIndividualTriplesSelectionChanged() {
		int[] rows = ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getSelectedRows();
		if (rows.length == 1) {
			int index = ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().convertRowIndexToModel(rows[0]);
			TableEntrySemanticEnrichmentIndividualTriples entry = ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getTableEntry(index);
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
		gpSemanticEnrichment.updateUI();
		gpHighLevel.updateUI();
	}


	/**
	 * Approve selected entries of resolution semantic enrichment individual triples table.
	 */
	public static void approveSelectedEntriesResolutionSemanticEnrichmentIndividualTriples() {
		int[] selectedRows = ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().getSelectedRows();
		// Sort the array
		Arrays.sort(selectedRows);
		
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			int selectedRow = ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().convertRowIndexToModel(selectedRows[i]);
			Difference difference = ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getTableEntry(selectedRow).getDifference();
			difference.setResolutionState(ResolutionState.RESOLVED);
			// Propagate changes to difference model
			int selectedOption = ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getTableEntry(selectedRow).getSelectedSemanticResolutionOption();
			if (selectedOption == 1) {
				difference.setTripleResolutionState(SDDTripleStateEnum.ADDED);
			} else {
				difference.setTripleResolutionState(SDDTripleStateEnum.DELETED);
			}
		}
		ApplicationUI.getTableResolutionTriples().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
		ApplicationUI.getTableResolutionHighLevelChanges().updateUI();
		
		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		
		ApplicationUI.getTreeDifferencesDivision().updateUI();
		
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
	}
	
	
	/**
	 * Select all entries of resolution semantic enrichment individual triples table.
	 */
	public static void selectAllEntriesResolutionSemanticEnrichmentIndividualTriples() {
		if (ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getRowCount() > 0) {
			ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().setRowSelectionInterval(0, ApplicationUI.getTableModelSemanticEnrichmentIndividualTriples().getRowCount() - 1);
		}		
	}

	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## High level change generation.                                                                                                                                        ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
	/**
	 * Update the resolution high level change table model.
	 */
	public static void updateTableModelHighLevelChanges() {
		// Remove the old rows
		ApplicationUI.getTableModelResolutionHighLevelChanges().removeAllElements();
		ApplicationUI.getTableResolutionHighLevelChanges().getSelectionModel().clearSelection();
		
		Iterator<String> iteKeys = highLevelChangeModel.getHighLevelChangesRenaming().keySet().iterator();
		while (iteKeys.hasNext()) {
			String currentKey = iteKeys.next();
			HighLevelChangeRenaming highLevelChangeRenaming = highLevelChangeModel.getHighLevelChangesRenaming().get(currentKey);
			Object[] rowData = Management.createRowDataResolutionHighLevelChanges(highLevelChangeRenaming);
			TableEntryHighLevelChanges entry = new TableEntryHighLevelChanges(highLevelChangeRenaming, rowData);
			ApplicationUI.getTableModelResolutionHighLevelChanges().addRow(entry);
		}
		
		ApplicationUI.getTableResolutionHighLevelChanges().updateUI();
	}
	
	
	
	/**
	 * The table resolution high level changes selection changed.
	 * Update the revision graph. The referenced revisions of the selected table row will be highlighted in the graph.
	 */
	public static void tableResolutionHighLevelChangesSelectionChanged() {
		int[] rows = ApplicationUI.getTableResolutionHighLevelChanges().getSelectedRows();
		if (rows.length == 1) {
			int index = ApplicationUI.getTableResolutionHighLevelChanges().convertRowIndexToModel(rows[0]);
			TableEntryHighLevelChanges entry = ApplicationUI.getTableModelResolutionHighLevelChanges().getTableEntry(index);
			logger.debug("Selected Entry: A - " + entry.getHighLevelChangeRenaming().getAdditionDifference().getReferencedRevisionA());
			logger.debug("Selected Entry: B - " + entry.getHighLevelChangeRenaming().getDeletionDifference().getReferencedRevisionB());
			
			// Remove highlighting of already highlighted nodes
			Management.removeHighlighting(graph, highlightedNodeNameA);
			Management.removeHighlighting(graph, highlightedNodeNameB);
			
			// Highlight the new currently selected nodes and save them to currently selected node name variables
			highlightedNodeNameA = entry.getHighLevelChangeRenaming().getAdditionDifference().getReferencedRevisionA();
			highlightedNodeNameB = entry.getHighLevelChangeRenaming().getDeletionDifference().getReferencedRevisionB();
			
			Color color = Color.RED;
			DifferenceGroup differenceGroupDel = Management.getDifferenceGroupOfDifference(entry.getHighLevelChangeRenaming().getDeletionDifference(), differenceModel);
			DifferenceGroup differenceGroupAdd = Management.getDifferenceGroupOfDifference(entry.getHighLevelChangeRenaming().getAdditionDifference(), differenceModel);
			if (!differenceGroupDel.isConflicting() && !differenceGroupAdd.isConflicting()) {
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
		gpSemanticEnrichment.updateUI();
		gpHighLevel.updateUI();
	}


	/**
	 * Approve selected entries of resolution high level changes table.
	 */
	public static void approveSelectedEntriesResolutionHighLevelChanges() {
		int[] selectedRows = ApplicationUI.getTableResolutionHighLevelChanges().getSelectedRows();
		// Sort the array
		Arrays.sort(selectedRows);
		
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			int selectedRow = ApplicationUI.getTableResolutionHighLevelChanges().convertRowIndexToModel(selectedRows[i]);
			Difference differenceAdd = ApplicationUI.getTableModelResolutionHighLevelChanges().getTableEntry(selectedRow).getHighLevelChangeRenaming().getAdditionDifference();
			Difference differenceDel = ApplicationUI.getTableModelResolutionHighLevelChanges().getTableEntry(selectedRow).getHighLevelChangeRenaming().getDeletionDifference();
			differenceAdd.setResolutionState(ResolutionState.RESOLVED);
			differenceDel.setResolutionState(ResolutionState.RESOLVED);
			// Propagate changes to difference model
			Boolean checkBoxStateBool = (Boolean) ApplicationUI.getTableModelResolutionHighLevelChanges().getTableEntry(selectedRow).getRowData()[4];
			if (checkBoxStateBool.booleanValue()) {
				// Rename - yes
				differenceAdd.setTripleResolutionState(SDDTripleStateEnum.ADDED);
				differenceDel.setTripleResolutionState(SDDTripleStateEnum.DELETED);
			} else {
				// Rename - no
				differenceAdd.setTripleResolutionState(SDDTripleStateEnum.DELETED);
				differenceDel.setTripleResolutionState(SDDTripleStateEnum.ADDED);
			}
		}
		ApplicationUI.getTableResolutionTriples().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().clearSelection();
		ApplicationUI.getTableResolutionSemanticEnrichmentAllIndividuals().updateUI();
		ApplicationUI.getTableResolutionSemanticEnrichmentIndividualTriples().updateUI();
		ApplicationUI.getTableResolutionHighLevelChanges().updateUI();
		
		Management.refreshParentNodeStateDifferencesTree((DefaultMutableTreeNode) ApplicationUI.getTreeModelDifferencesDivision().getRoot());
		
		ApplicationUI.getTreeDifferencesDivision().updateUI();
	}

	
	/**
	 * Select all entries of resolution high level changes table.
	 */
	public static void selectAllEntriesResolutionHighLevelChanges() {
		if (ApplicationUI.getTableModelResolutionHighLevelChanges().getRowCount() > 0) {
			ApplicationUI.getTableResolutionHighLevelChanges().setRowSelectionInterval(0, ApplicationUI.getTableModelResolutionHighLevelChanges().getRowCount() - 1);
		}
	}	
	
	
	/**
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 * ##                                                                                                                                                                      ##
	 * ## Summary report.                                                                                                                                                      ##
	 * ##                                                                                                                                                                      ##
	 * ##########################################################################################################################################################################
	 * ##########################################################################################################################################################################
	 */
	
	
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
		TableCellPanelRendererSummaryReport rendererPanel = new TableCellPanelRendererSummaryReport();
		for (int i = 0; i < ReportDialog.getTableModel().getColumnCount(); i++) {
			if ((i != 3) && (i != 4)) {
				ReportDialog.getTable().getColumnModel().getColumn(i).setCellRenderer(renderer);
			}
		}
		ReportDialog.getTable().getColumnModel().getColumn(3).setCellRenderer(rendererPanel);
		ReportDialog.getTable().getColumnModel().getColumn(4).setCellRenderer(rendererPanel);

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
		
		ReportDialog.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		ReportDialog.dialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		ReportDialog.dialog.setModal(true);
		ReportDialog.dialog.setVisible(true);
	}
	
	
	/**
	 * Close the summary report.
	 */
	public static void closeSummaryReport() {
		ReportDialog.dialog.setVisible(false);
	}
	
	
	/**
	 * Push the changes to the remote repository.
	 * 
	 * @throws IOException 
	 */
	public static void pushToRemoteRepository() throws IOException {
		ReportDialog.dialog.setCursor(waitCursor);
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
					Model wholeContentModel = Management.getWholeContentOfRevision(graphName, revisionNumberBranchB);
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
				ReportDialog.dialog.setCursor(defaultCursor);
				ReportDialog.dialog.setVisible(false);
				
				// Show message dialog
				if ((response != null) && (response.getStatusCode() == HttpURLConnection.HTTP_CREATED)) {
					logger.info("Merge query successfully executed.");
					String newRevisionNumber = Management.getRevisionNumberOfNewRevisionHeaderParameter(response, graphName);
					JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query successfully executed. Revision number of merged revision: " + newRevisionNumber, "Info", JOptionPane.INFORMATION_MESSAGE);
					ReportDialog.dialog.setCursor(waitCursor);
					// Create empty difference model
					differenceModel = new DifferenceModel();
					// Update application UI
					updateDifferencesTree();
					// Create the revision graph
					createGraph(graphName);
					ReportDialog.dialog.setCursor(defaultCursor);
				} else {
					logger.error("Merge query could not be executed.");
					JOptionPane.showMessageDialog(ApplicationUI.frmRplesMergingClient, "Merge query could not be executed. Maybe another user committed changes to one of the branches to merge.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

}
