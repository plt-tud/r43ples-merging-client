package de.tud.plt.r43ples.client.desktop.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryFilter;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelFilter;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentAllIndividuals;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentIndividualTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.tree.TreeCellRendererDifferences;

/**
 * The application UI of the merging client.
 * 
 * @author Stephan Hensel
 *
 */
public class ApplicationUI {

	/** The application frame. **/
	public static JFrame frmRplesMergingClient;
	/** The application tabbed pane. **/
	private static JTabbedPane tabbedPaneApplication;
	/** The differences tree (division). **/
	private static JTree treeDifferencesDivision;
	/** The differences tree model (division). **/
	private static DefaultTreeModel treeModelDifferencesDivision = new DefaultTreeModel(null);
	/** The graph scroll pane. **/
	private static JScrollPane scrollPaneGraph;
	/** The graph scroll pane of semantic enrichment pane. **/
	private static JScrollPane scrollPaneGraphSemanticEnrichment;
	/** The graph scroll pane of high level changes pane. **/
	private static JScrollPane scrollPaneGraphHighLevelChanges;
	/** The resolution triples table. **/
	private static JTable tableResolutionTriples;
	/** The resolution triples table model. **/
	private static TableModelResolutionTriples tableModelResolutionTriples = new TableModelResolutionTriples(new ArrayList<TableEntry>());
	/** The semantic enrichment table all individuals. **/
	private static JTable tableResolutionSemanticEnrichmentAllIndividuals;
	/** The semantic enrichment table model all individuals. **/
	private static TableModelSemanticEnrichmentAllIndividuals tableModelSemanticEnrichmentAllIndividuals = new TableModelSemanticEnrichmentAllIndividuals(new ArrayList<TableEntrySemanticEnrichmentAllIndividuals>());
	/** The semantic enrichment table individual triples. **/
	private static JTable tableResolutionSemanticEnrichmentIndividualTriples;
	/** The semantic enrichment table model individual triples. **/
	private static TableModelSemanticEnrichmentIndividualTriples tableModelSemanticEnrichmentIndividualTriples = new TableModelSemanticEnrichmentIndividualTriples(new ArrayList<TableEntrySemanticEnrichmentIndividualTriples>());
	/** The resolution high level changes table. **/
	private static JTable tableResolutionHighLevelChanges;
	/** The resolution high level changes table model. **/
	private static TableModelResolutionHighLevelChanges tableModelResolutionHighLevelChanges = new TableModelResolutionHighLevelChanges(new ArrayList<TableEntryHighLevelChanges>());
	/** The filter table. **/
	private static JTable tableFilter;
	/** The filter table model. **/
	private static TableModelFilter tableModelFilter = new TableModelFilter(new ArrayList<TableEntryFilter>());
	/** The application split pane. **/
	private static JSplitPane splitPaneTriples;
	/** The preferences split pane. **/
	private static JSplitPane splitPanePreferences;
	/** The push button. **/
	private static JButton btnPush;
	

	/**
	 * Create the application.
	 */
	public ApplicationUI() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRplesMergingClient = new JFrame();
		frmRplesMergingClient.setTitle("R43ples Merging Client");
		frmRplesMergingClient.setBounds(100, 100, 1116, 862);
		frmRplesMergingClient.setLocationRelativeTo(null);
		frmRplesMergingClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmRplesMergingClient.setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmNewMerge = new JMenuItem("New Merge");
		mntmNewMerge.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				try {
					Controller.showStartMergingDialog();
				} catch (IOException e) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e.printStackTrace();
				}
			}
		});
		mnMenu.add(mntmNewMerge);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmProperties = new JMenuItem("Properties");
		mntmProperties.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.showConfigurationDialog();
			}
		});
		mnEdit.add(mntmProperties);
		
		tabbedPaneApplication = new JTabbedPane(JTabbedPane.BOTTOM);
		frmRplesMergingClient.getContentPane().add(tabbedPaneApplication, BorderLayout.CENTER);
		
		JPanel panelResolutionTriples = new JPanel();
		tabbedPaneApplication.addTab("Triple view", null, panelResolutionTriples, null);
		panelResolutionTriples.setLayout(new BorderLayout(0, 0));
		
		splitPaneTriples = new JSplitPane();
		panelResolutionTriples.add(splitPaneTriples);
		splitPaneTriples.setResizeWeight(0.3);
		splitPaneTriples.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			
			/* (non-Javadoc)
			 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
			 */
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				Controller.splitPaneApplicationDividerLocationChanged();
			}
		});
		
		splitPanePreferences = new JSplitPane();
		splitPanePreferences.setResizeWeight(0.5);
		splitPanePreferences.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneTriples.setLeftComponent(splitPanePreferences);
		
		JPanel panelFilter = new JPanel();
		splitPanePreferences.setRightComponent(panelFilter);
		panelFilter.setLayout(new BorderLayout(0, 0));
		
		JPanel panelFilterHeader = new JPanel();
		panelFilter.add(panelFilterHeader, BorderLayout.NORTH);
		panelFilterHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblFilterHeader = new JLabel(" Filters");
		lblFilterHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelFilterHeader.add(lblFilterHeader, BorderLayout.NORTH);
		
		JPanel panelFilterContent = new JPanel();
		panelFilter.add(panelFilterContent, BorderLayout.CENTER);
		panelFilterContent.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneFilterContent = new JScrollPane();
		panelFilterContent.add(scrollPaneFilterContent, BorderLayout.CENTER);
		
		tableFilter = new JTable();
		tableFilter.addMouseListener(new MouseAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Controller.updateDifferencesTree();
			}
		});
		tableFilter.addKeyListener(new KeyAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent arg0) {
				Controller.updateDifferencesTree();
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				Controller.updateDifferencesTree();
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		tableFilter.setPreferredScrollableViewportSize(new Dimension(100, 150));
		tableFilter.setRowHeight(25);
		tableFilter.getTableHeader().setReorderingAllowed(false);
		tableFilter.setModel(tableModelFilter);
		tableFilter.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Controller.updateDifferencesTree();
			}
		});
		scrollPaneFilterContent.setViewportView(tableFilter);
		
		JPanel panelDifferences = new JPanel();
		splitPanePreferences.setLeftComponent(panelDifferences);
		panelDifferences.setLayout(new BorderLayout(0, 0));
		
		JPanel panelDifferenceHeader = new JPanel();
		panelDifferences.add(panelDifferenceHeader, BorderLayout.NORTH);
		panelDifferenceHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDifferenceHeader = new JLabel(" Differences");
		lblDifferenceHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDifferenceHeader.add(lblDifferenceHeader);
		
		JPanel panelDifferencesDivision = new JPanel();
		panelDifferences.add(panelDifferencesDivision, BorderLayout.CENTER);
		panelDifferencesDivision.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneDifferencesDivision = new JScrollPane();
		panelDifferencesDivision.add(scrollPaneDifferencesDivision, BorderLayout.CENTER);
		
		treeDifferencesDivision = new JTree();
		treeDifferencesDivision.setMinimumSize(new Dimension(72, 64));
		treeDifferencesDivision.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		treeDifferencesDivision.setModel(treeModelDifferencesDivision);
		treeDifferencesDivision.setCellRenderer(new TreeCellRendererDifferences());
		treeDifferencesDivision.addKeyListener(new KeyAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent arg0) {
				try {
					Controller.selectionChangedDifferencesTree();
				} catch (IOException e1) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e1.printStackTrace();
				}
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Controller.selectionChangedDifferencesTree();
				} catch (IOException e2) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e2.printStackTrace();
				}
			}
			
			/* (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					Controller.selectionChangedDifferencesTree();
				} catch (IOException e3) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e3.printStackTrace();
				}
			}
		});
		treeDifferencesDivision.addMouseListener(new MouseAdapter() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Controller.selectionChangedDifferencesTree();
				} catch (IOException e) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e.printStackTrace();
				}
			}
		});
		scrollPaneDifferencesDivision.setViewportView(treeDifferencesDivision);
		
		JSplitPane splitPaneResolution = new JSplitPane();
		splitPaneResolution.setResizeWeight(0.5);
		splitPaneResolution.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneTriples.setRightComponent(splitPaneResolution);
		
		JPanel panelResolution = new JPanel();
		splitPaneResolution.setRightComponent(panelResolution);
		panelResolution.setLayout(new BorderLayout(0, 0));
		
		JPanel panelResolutionHeader = new JPanel();
		panelResolutionHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelResolution.add(panelResolutionHeader, BorderLayout.NORTH);
		panelResolutionHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblResolutionHeader = new JLabel(" Resolution");
		panelResolutionHeader.add(lblResolutionHeader, BorderLayout.NORTH);
		
		JPanel panelResolutionTriplesContent = new JPanel();
		panelResolution.add(panelResolutionTriplesContent, BorderLayout.CENTER);
		panelResolutionTriplesContent.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionTriples = new JScrollPane();
		panelResolutionTriplesContent.add(scrollPaneResolutionTriples, BorderLayout.CENTER);
		
		tableResolutionTriples = new JTable();
		tableResolutionTriples.setRowHeight(25);
		tableResolutionTriples.getTableHeader().setReorderingAllowed(false);
		tableResolutionTriples.setModel(tableModelResolutionTriples);
		tableResolutionTriples.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Controller.tableResolutionTriplesSelectionChanged();				
			}
		});
		scrollPaneResolutionTriples.setViewportView(tableResolutionTriples);
		
		JToolBar toolBarResolutionTriples = new JToolBar();
		panelResolutionTriplesContent.add(toolBarResolutionTriples, BorderLayout.NORTH);
		
		JButton btnResolutionTriplesApproveSelected = new JButton("Approve selected");
		btnResolutionTriplesApproveSelected.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.approveSelectedEntriesResolutionTriples();
			}
		});
		toolBarResolutionTriples.add(btnResolutionTriplesApproveSelected);
		
		JButton btnResolutionTriplesSelectAll = new JButton("Select all");
		btnResolutionTriplesSelectAll.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.selectAllEntriesResolutionTriples();
			}
		});
		toolBarResolutionTriples.add(btnResolutionTriplesSelectAll);
		
		JPanel panelRevisionGraph = new JPanel();
		splitPaneResolution.setLeftComponent(panelRevisionGraph);
		panelRevisionGraph.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRevisionGraphHeader = new JPanel();
		panelRevisionGraphHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelRevisionGraph.add(panelRevisionGraphHeader, BorderLayout.NORTH);
		panelRevisionGraphHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRevisionGraphHeader = new JLabel(" Revision graph");
		panelRevisionGraphHeader.add(lblRevisionGraphHeader);
		
		scrollPaneGraph = new JScrollPane();
		scrollPaneGraph.setPreferredSize(new Dimension(2, 100));
		panelRevisionGraph.add(scrollPaneGraph, BorderLayout.CENTER);
		splitPaneResolution.setDividerLocation(300);
		
		JPanel panelResolutionSemanticEnrichment = new JPanel();
		tabbedPaneApplication.addTab("Individual view", null, panelResolutionSemanticEnrichment, null);
		panelResolutionSemanticEnrichment.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPaneResolutionSemanticEnrichment = new JSplitPane();
		splitPaneResolutionSemanticEnrichment.setResizeWeight(0.5);
		splitPaneResolutionSemanticEnrichment.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelResolutionSemanticEnrichment.add(splitPaneResolutionSemanticEnrichment);
		
		JPanel panelRevisionGraphSemanticEnrichment = new JPanel();
		splitPaneResolutionSemanticEnrichment.setLeftComponent(panelRevisionGraphSemanticEnrichment);
		panelRevisionGraphSemanticEnrichment.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRevisionGraphSemanticEnrichmentHeader = new JPanel();
		panelRevisionGraphSemanticEnrichmentHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelRevisionGraphSemanticEnrichment.add(panelRevisionGraphSemanticEnrichmentHeader, BorderLayout.NORTH);
		panelRevisionGraphSemanticEnrichmentHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRevisionGraphSemanticEnrichmentHeader = new JLabel(" Revision graph");
		panelRevisionGraphSemanticEnrichmentHeader.add(lblRevisionGraphSemanticEnrichmentHeader, BorderLayout.NORTH);
		
		scrollPaneGraphSemanticEnrichment = new JScrollPane();
		scrollPaneGraphSemanticEnrichment.setPreferredSize(new Dimension(2, 100));
		panelRevisionGraphSemanticEnrichment.add(scrollPaneGraphSemanticEnrichment, BorderLayout.CENTER);
		
		JPanel panelResolutionSemanticEnrichmentContent = new JPanel();
		splitPaneResolutionSemanticEnrichment.setRightComponent(panelResolutionSemanticEnrichmentContent);
		panelResolutionSemanticEnrichmentContent.setLayout(new BorderLayout(0, 0));
		
		JPanel panelResolutionSemanticEnrichmentAllIndividuals = new JPanel();
		panelResolutionSemanticEnrichmentContent.add(panelResolutionSemanticEnrichmentAllIndividuals, BorderLayout.NORTH);
		panelResolutionSemanticEnrichmentAllIndividuals.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionSemanticEnrichmentAllIndividuals = new JScrollPane();
		panelResolutionSemanticEnrichmentAllIndividuals.add(scrollPaneResolutionSemanticEnrichmentAllIndividuals);
		
		tableResolutionSemanticEnrichmentAllIndividuals = new JTable();
		tableResolutionSemanticEnrichmentAllIndividuals.setPreferredScrollableViewportSize(new Dimension(450, 150));
		tableResolutionSemanticEnrichmentAllIndividuals.setRowHeight(25);
		tableResolutionSemanticEnrichmentAllIndividuals.getTableHeader().setReorderingAllowed(false);
		tableResolutionSemanticEnrichmentAllIndividuals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableResolutionSemanticEnrichmentAllIndividuals.setModel(tableModelSemanticEnrichmentAllIndividuals);
		tableResolutionSemanticEnrichmentAllIndividuals.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					Controller.updateTableModelSemanticEnrichmentTriples();
				} catch (IOException e1) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e1.printStackTrace();
				}				
			}
		});
		scrollPaneResolutionSemanticEnrichmentAllIndividuals.setViewportView(tableResolutionSemanticEnrichmentAllIndividuals);
		
		JPanel panelResolutionSemanticEnrichmentIndividualTriples = new JPanel();
		panelResolutionSemanticEnrichmentContent.add(panelResolutionSemanticEnrichmentIndividualTriples, BorderLayout.CENTER);
		panelResolutionSemanticEnrichmentIndividualTriples.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionSemanticEnrichmentIndividualTriples = new JScrollPane();
		panelResolutionSemanticEnrichmentIndividualTriples.add(scrollPaneResolutionSemanticEnrichmentIndividualTriples);
		
		tableResolutionSemanticEnrichmentIndividualTriples = new JTable();
		tableResolutionSemanticEnrichmentIndividualTriples.setRowHeight(25);
		tableResolutionSemanticEnrichmentIndividualTriples.getTableHeader().setReorderingAllowed(false);
		tableResolutionSemanticEnrichmentIndividualTriples.setModel(tableModelSemanticEnrichmentIndividualTriples);
		tableResolutionSemanticEnrichmentIndividualTriples.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Controller.tableResolutionSemanticEnrichmentIndividualTriplesSelectionChanged();
			}
		});
		scrollPaneResolutionSemanticEnrichmentIndividualTriples.setViewportView(tableResolutionSemanticEnrichmentIndividualTriples);
		
		JToolBar toolBarResolutionSemanticEnrichmentIndividualTriples = new JToolBar();
		panelResolutionSemanticEnrichmentIndividualTriples.add(toolBarResolutionSemanticEnrichmentIndividualTriples, BorderLayout.NORTH);
		
		JButton btnResolutionSemanticEnrichmentIndividualTriplesApproveSelected = new JButton("Approve selected");
		btnResolutionSemanticEnrichmentIndividualTriplesApproveSelected.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.approveSelectedEntriesResolutionSemanticEnrichmentIndividualTriples();
			}
		});
		toolBarResolutionSemanticEnrichmentIndividualTriples.add(btnResolutionSemanticEnrichmentIndividualTriplesApproveSelected);
		
		JButton btnResolutionSemanticEnrichmentIndividualTriplesSelectAll = new JButton("Select all");
		btnResolutionSemanticEnrichmentIndividualTriplesSelectAll.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				Controller.selectAllEntriesResolutionSemanticEnrichmentIndividualTriples();
			}
		});
		toolBarResolutionSemanticEnrichmentIndividualTriples.add(btnResolutionSemanticEnrichmentIndividualTriplesSelectAll);
		splitPaneResolutionSemanticEnrichment.setDividerLocation(300);
		
		JPanel panelResolutionHighLevelChanges = new JPanel();
		tabbedPaneApplication.addTab("High level view", null, panelResolutionHighLevelChanges, null);
		panelResolutionHighLevelChanges.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPaneResolutionHighLevelChanges = new JSplitPane();
		splitPaneResolutionHighLevelChanges.setResizeWeight(0.5);
		splitPaneResolutionHighLevelChanges.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelResolutionHighLevelChanges.add(splitPaneResolutionHighLevelChanges, BorderLayout.CENTER);
		
		JPanel panelRevisionGraphHighLevelChanges = new JPanel();
		splitPaneResolutionHighLevelChanges.setLeftComponent(panelRevisionGraphHighLevelChanges);
		panelRevisionGraphHighLevelChanges.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRevisionGraphHighLevelChangesHeader = new JPanel();
		panelRevisionGraphHighLevelChangesHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelRevisionGraphHighLevelChanges.add(panelRevisionGraphHighLevelChangesHeader, BorderLayout.NORTH);
		panelRevisionGraphHighLevelChangesHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRevisionGraphHighLevelChangesHeader = new JLabel(" Revision graph");
		panelRevisionGraphHighLevelChangesHeader.add(lblRevisionGraphHighLevelChangesHeader, BorderLayout.NORTH);
		
		scrollPaneGraphHighLevelChanges = new JScrollPane();
		scrollPaneGraphHighLevelChanges.setPreferredSize(new Dimension(2, 100));
		panelRevisionGraphHighLevelChanges.add(scrollPaneGraphHighLevelChanges);
		
		JPanel panelResolutionHighLevelChangesContent = new JPanel();
		splitPaneResolutionHighLevelChanges.setRightComponent(panelResolutionHighLevelChangesContent);
		panelResolutionHighLevelChangesContent.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionHighLevelChanges = new JScrollPane();
		panelResolutionHighLevelChangesContent.add(scrollPaneResolutionHighLevelChanges);
		
		tableResolutionHighLevelChanges = new JTable();
		tableResolutionHighLevelChanges.setRowHeight(25);
		tableResolutionHighLevelChanges.getTableHeader().setReorderingAllowed(false);
		tableResolutionHighLevelChanges.setModel(tableModelResolutionHighLevelChanges);
		tableResolutionHighLevelChanges.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Controller.tableResolutionHighLevelChangesSelectionChanged();				
			}
		});
		scrollPaneResolutionHighLevelChanges.setViewportView(tableResolutionHighLevelChanges);
		
		JToolBar toolBarResolutionHighLevelChanges = new JToolBar();
		panelResolutionHighLevelChangesContent.add(toolBarResolutionHighLevelChanges, BorderLayout.NORTH);
		
		JButton btnResolutionHighLevelChangesApproveSelected = new JButton("Approve selected");
		btnResolutionHighLevelChangesApproveSelected.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.approveSelectedEntriesResolutionHighLevelChanges();
			}
		});
		toolBarResolutionHighLevelChanges.add(btnResolutionHighLevelChangesApproveSelected);
		
		JButton btnResolutionHighLevelChangesSelectAll = new JButton("Select all");
		btnResolutionHighLevelChangesSelectAll.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.selectAllEntriesResolutionHighLevelChanges();
			}
		});
		toolBarResolutionHighLevelChanges.add(btnResolutionHighLevelChangesSelectAll);
		splitPaneResolutionHighLevelChanges.setDividerLocation(300);
		
		JToolBar toolBar = new JToolBar();
		frmRplesMergingClient.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewMerge = new JButton("New MERGE");
		btnNewMerge.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				try {
					Controller.showStartMergingDialog();
				} catch (IOException e) {
					Controller.showIOExceptionDialog(frmRplesMergingClient);
					e.printStackTrace();
				}
			}
		});
		toolBar.add(btnNewMerge);
		
		btnPush = new JButton("Push");
		btnPush.setEnabled(false);
		btnPush.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.executePush();
			}
		});
		toolBar.add(btnPush);
		tableModelResolutionTriples.addTableModelListener(new TableModelListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
			 */
			@Override
			public void tableChanged(TableModelEvent e) {
				 SwingUtilities.invokeLater(new Runnable() {
					
					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						tableResolutionTriples.updateUI();
					}
				});
			}
		});
		tableModelSemanticEnrichmentIndividualTriples.addTableModelListener(new TableModelListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
			 */
			@Override
			public void tableChanged(TableModelEvent e) {
				 SwingUtilities.invokeLater(new Runnable() {
					
					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						tableResolutionSemanticEnrichmentIndividualTriples.updateUI();
					}
				});
			}
		});
		tableModelResolutionHighLevelChanges.addTableModelListener(new TableModelListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
			 */
			@Override
			public void tableChanged(TableModelEvent e) {
				 SwingUtilities.invokeLater(new Runnable() {
					
					/* (non-Javadoc)
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						tableResolutionHighLevelChanges.updateUI();
					}
				});
			}
		});
		
		tabbedPaneApplication.setSelectedIndex(0);
		tabbedPaneApplication.setEnabledAt(1, false);
		tabbedPaneApplication.setEnabledAt(2, false);
		
		tabbedPaneApplication.addChangeListener(new ChangeListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				Controller.applicationTabSelectionChanged();
			}
		});
	}


	/**
	 * Get the tree differences division.
	 * 
	 * @return get the tree differences division
	 */
	public static JTree getTreeDifferencesDivision() {
		return treeDifferencesDivision;
	}


	/**
	 * Set the tree differences division.
	 * 
	 * @param treeDifferencesDivision the tree differences division
	 */
	public static void setTreeDifferencesDivision(JTree treeDifferencesDivision) {
		ApplicationUI.treeDifferencesDivision = treeDifferencesDivision;
	}


	/**
	 * Get the tree model differences division.
	 * 
	 * @return the tree model differences division
	 */
	public static DefaultTreeModel getTreeModelDifferencesDivision() {
		return treeModelDifferencesDivision;
	}


	/**
	 * Set the tree model differences division.
	 * 
	 * @param treeModelDifferencesDivision the tree model differences division
	 */
	public static void setTreeModelDifferencesDivision(DefaultTreeModel treeModelDifferencesDivision) {
		ApplicationUI.treeModelDifferencesDivision = treeModelDifferencesDivision;
	}


	/**
	 * Get the table resolution triples.
	 * 
	 * @return the table resolution triples
	 */
	public static JTable getTableResolutionTriples() {
		return tableResolutionTriples;
	}


	/**
	 * Set the table resolution triples.
	 * 
	 * @param tableResolutionTriples the table resolution triples
	 */
	public static void setTableResolutionTriples(JTable tableResolutionTriples) {
		ApplicationUI.tableResolutionTriples = tableResolutionTriples;
	}


	/**
	 * Get the table model resolution triples.
	 * 
	 * @return the table model resolution triples
	 */
	public static TableModelResolutionTriples getTableModelResolutionTriples() {
		return tableModelResolutionTriples;
	}


	/**
	 * Set the table model resolution triples.
	 * 
	 * @param tableModelResolutionTriples the table model resolution triples
	 */
	public static void setTableModelResolutionTriples(TableModelResolutionTriples tableModelResolutionTriples) {
		ApplicationUI.tableModelResolutionTriples = tableModelResolutionTriples;
	}


	/**
	 * Get the graph scroll pane.
	 * 
	 * @return the graph scroll pane
	 */
	public static JScrollPane getScrollPaneGraph() {
		return scrollPaneGraph;
	}


	/**
	 * Set the graph scroll pane.
	 * 
	 * @param scrollPaneGraph the graph scroll pane to set
	 */
	public static void setScrollPaneGraph(JScrollPane scrollPaneGraph) {
		ApplicationUI.scrollPaneGraph = scrollPaneGraph;
	}
	
	
	/**
	 * Get the graph scroll pane of semantic enrichment pane.
	 * 
	 * @return the scrollPaneGraphSemanticEnrichment
	 */
	public static JScrollPane getScrollPaneGraphSemanticEnrichment() {
		return scrollPaneGraphSemanticEnrichment;
	}


	/**
	 * Set the graph scroll pane of semantic enrichment pane.
	 * 
	 * @param scrollPaneGraphSemanticEnrichment the graph scroll pane of semantic enrichment pane to set
	 */
	public static void setScrollPaneGraphSemanticEnrichment(JScrollPane scrollPaneGraphSemanticEnrichment) {
		ApplicationUI.scrollPaneGraphSemanticEnrichment = scrollPaneGraphSemanticEnrichment;
	}
	
	
	/**
	 * Get the graph scroll pane of high level changes pane.
	 * 
	 * @return the scrollPaneGraphHighLevelChanges
	 */
	public static JScrollPane getScrollPaneGraphHighLevelChanges() {
		return scrollPaneGraphHighLevelChanges;
	}


	/**
	 * Set the graph scroll pane of high level changes pane.
	 * 
	 * @param scrollPaneGraphHighLevelChanges the graph scroll pane of high level changes pane to set
	 */
	public static void setScrollPaneGraphHighLevelChanges(JScrollPane scrollPaneGraphHighLevelChanges) {
		ApplicationUI.scrollPaneGraphHighLevelChanges = scrollPaneGraphHighLevelChanges;
	}


	/**
	 * Get the application tabbed pane.
	 * 
	 * @return the application tabbed pane
	 */
	public static JTabbedPane getTabbedPaneApplication() {
		return tabbedPaneApplication;
	}


	/**
	 * Set the application tabbed pane.
	 * 
	 * @param tabbedPaneApplication the application tabbed pane to set
	 */
	public static void setTabbedPaneApplication(JTabbedPane tabbedPaneApplication) {
		ApplicationUI.tabbedPaneApplication = tabbedPaneApplication;
	}


	/**
	 * Get the table semantic enrichment all individuals.
	 * 
	 * @return the table semantic enrichment all individuals
	 */
	public static JTable getTableResolutionSemanticEnrichmentAllIndividuals() {
		return tableResolutionSemanticEnrichmentAllIndividuals;
	}


	/**
	 * Set the table semantic enrichment all individuals.
	 * 
	 * @param tableResolutionSemanticEnrichmentAllIndividuals the table semantic enrichment all individuals to set
	 */
	public static void setTableResolutionSemanticEnrichmentAllIndividuals(JTable tableResolutionSemanticEnrichmentAllIndividuals) {
		ApplicationUI.tableResolutionSemanticEnrichmentAllIndividuals = tableResolutionSemanticEnrichmentAllIndividuals;
	}


	/**
	 * Get the table model semantic enrichment all individuals.
	 * 
	 * @return the table model semantic enrichment all individuals
	 */
	public static TableModelSemanticEnrichmentAllIndividuals getTableModelSemanticEnrichmentAllIndividuals() {
		return tableModelSemanticEnrichmentAllIndividuals;
	}


	/**
	 * Set the table model semantic enrichment all individuals.
	 * 
	 * @param tableModelSemanticEnrichmentAllIndividuals the table model semantic enrichment all individuals to set
	 */
	public static void setTableModelSemanticEnrichmentAllIndividuals(TableModelSemanticEnrichmentAllIndividuals tableModelSemanticEnrichmentAllIndividuals) {
		ApplicationUI.tableModelSemanticEnrichmentAllIndividuals = tableModelSemanticEnrichmentAllIndividuals;
	}


	/**
	 * Get the table model semantic enrichment individual triples.
	 * 
	 * @return the table model semantic enrichment individual triples
	 */
	public static TableModelSemanticEnrichmentIndividualTriples getTableModelSemanticEnrichmentIndividualTriples() {
		return tableModelSemanticEnrichmentIndividualTriples;
	}


	/**
	 * Set the table model semantic enrichment individual triples.
	 * 
	 * @param tableModelSemanticEnrichmentIndividualTriples the table model semantic enrichment individual triples to set
	 */
	public static void setTableModelSemanticEnrichmentIndividualTriples(TableModelSemanticEnrichmentIndividualTriples tableModelSemanticEnrichmentIndividualTriples) {
		ApplicationUI.tableModelSemanticEnrichmentIndividualTriples = tableModelSemanticEnrichmentIndividualTriples;
	}


	/**
	 * Get the table semantic enrichment individual triples.
	 * 
	 * @return the table model semantic enrichment individual triples
	 */
	public static JTable getTableResolutionSemanticEnrichmentIndividualTriples() {
		return tableResolutionSemanticEnrichmentIndividualTriples;
	}


	/**
	 * Set table semantic enrichment individual triples.
	 * 
	 * @param tableResolutionSemanticEnrichmentIndividualTriples the table semantic enrichment individual triples to set
	 */
	public static void setTableResolutionSemanticEnrichmentIndividualTriples(JTable tableResolutionSemanticEnrichmentIndividualTriples) {
		ApplicationUI.tableResolutionSemanticEnrichmentIndividualTriples = tableResolutionSemanticEnrichmentIndividualTriples;
	}
	
	
	/**
	 * Get the table resolution high level changes.
	 * 
	 * @return the table resolution high level changes
	 */
	public static JTable getTableResolutionHighLevelChanges() {
		return tableResolutionHighLevelChanges;
	}


	/**
	 * Set the table resolution high level changes.
	 * 
	 * @param tableResolutionHighLevelChanges the table resolution high level changes
	 */
	public static void setTableResolutionHighLevelChanges(JTable tableResolutionHighLevelChanges) {
		ApplicationUI.tableResolutionHighLevelChanges = tableResolutionHighLevelChanges;
	}


	/**
	 * Get the table model resolution high level changes.
	 * 
	 * @return the table model resolution high level changes
	 */
	public static TableModelResolutionHighLevelChanges getTableModelResolutionHighLevelChanges() {
		return tableModelResolutionHighLevelChanges;
	}


	/**
	 * Set the table model resolution high level changes.
	 * 
	 * @param tableModelResolutionHighLevelChanges the table model resolution high level changes
	 */
	public static void setTableModelResolutionHighLevelChanges(TableModelResolutionHighLevelChanges tableModelResolutionHighLevelChanges) {
		ApplicationUI.tableModelResolutionHighLevelChanges = tableModelResolutionHighLevelChanges;
	}


	/**
	 * Get the filter table.
	 * 
	 * @return the filter table
	 */
	public static JTable getTableFilter() {
		return tableFilter;
	}


	/**
	 * Set the filter table
	 * 
	 * @param tableFilter the filter table to set
	 */
	public static void setTableFilter(JTable tableFilter) {
		ApplicationUI.tableFilter = tableFilter;
	}


	/**
	 * Get the filter table model.
	 * 
	 * @return the filter table model
	 */
	public static TableModelFilter getTableModelFilter() {
		return tableModelFilter;
	}


	/**
	 * Set the filter table model.
	 * 
	 * @param tableModelFilter the filter table model to set
	 */
	public static void setTableModelFilter(TableModelFilter tableModelFilter) {
		ApplicationUI.tableModelFilter = tableModelFilter;
	}


	/**
	 * Get the application split pane.
	 * 
	 * @return the application split pane
	 */
	public static JSplitPane getSplitPaneApplication() {
		return splitPaneTriples;
	}
	
	
	/**
	 * Get the preferences split pane.
	 * 
	 * @return the preferences split pane
	 */
	public static JSplitPane getSplitPanePreferences() {
		return splitPanePreferences;
	}


	/**
	 * Get the push button.
	 * 
	 * @return the push button
	 */
	public static JButton getBtnPush() {
		return btnPush;
	}

}
