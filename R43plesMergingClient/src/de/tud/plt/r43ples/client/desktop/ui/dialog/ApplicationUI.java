package de.tud.plt.r43ples.client.desktop.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntry;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryFilter;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntryHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentAllClasses;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelFilter;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionHighLevelChanges;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelResolutionTriples;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentAllClasses;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSemanticEnrichmentClassTriples;
import de.tud.plt.r43ples.client.desktop.ui.renderer.tree.TreeCellRendererDifferences;

import javax.swing.ListSelectionModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The application UI of the merging client.
 * 
 * @author Stephan Hensel
 *
 */
public class ApplicationUI {

	/** The application frame. **/
	public static JFrame frmRplesMergingClient;
	/** The differences tree (division). **/
	private static JTree treeDifferencesDivision;
	/** The differences tree model (division). **/
	private static DefaultTreeModel treeModelDifferencesDivision = new DefaultTreeModel(null);
	/** The graph scroll pane. **/
	private static JScrollPane scrollPaneGraph;
	/** The graph panel. **/
	private static JPanel panelGraph;
	/** The resolution tabbed pane. **/
	private static JTabbedPane tabbedPaneResolution;	
	/** The resolution triples table. **/
	private static JTable tableResolutionTriples;
	/** The resolution triples table model. **/
	private static TableModelResolutionTriples tableModelResolutionTriples = new TableModelResolutionTriples(new ArrayList<TableEntry>());
	/** The semantic enrichment table all classes. **/
	private static JTable tableResolutionSemanticEnrichmentAllClasses;
	/** The semantic enrichment table model all classes. **/
	private static TableModelSemanticEnrichmentAllClasses tableModelSemanticEnrichmentAllClasses = new TableModelSemanticEnrichmentAllClasses(new ArrayList<TableEntrySemanticEnrichmentAllClasses>());
	/** The semantic enrichment table class triples. **/
	private static JTable tableResolutionSemanticEnrichmentClassTriples;
	/** The semantic enrichment table model class triples. **/
	private static TableModelSemanticEnrichmentClassTriples tableModelSemanticEnrichmentClassTriples = new TableModelSemanticEnrichmentClassTriples(new ArrayList<TableEntrySemanticEnrichmentClassTriples>());
	/** The resolution high level changes table. **/
	private static JTable tableResolutionHighLevelChanges;
	/** The resolution high level changes table model. **/
	private static TableModelResolutionHighLevelChanges tableModelResolutionHighLevelChanges = new TableModelResolutionHighLevelChanges(new ArrayList<TableEntryHighLevelChanges>());
	/** The filter table. **/
	private static JTable tableFilter;
	/** The filter table model. **/
	private static TableModelFilter tableModelFilter = new TableModelFilter(new ArrayList<TableEntryFilter>());
	/** The application split pane. **/
	private static JSplitPane splitPaneApplication;
	/** The preferences split pane. **/
	private static JSplitPane splitPanePreferences;
	

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
					// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		toolBar.add(btnNewMerge);
		
		JButton btnPush = new JButton("Push");
		btnPush.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.executePush();
			}
		});
		toolBar.add(btnPush);
		
		splitPaneApplication = new JSplitPane();
		splitPaneApplication.setResizeWeight(0.3);
		splitPaneApplication.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			
			/* (non-Javadoc)
			 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
			 */
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				Controller.splitPaneApplicationDividerLocationChanged();
			}
		});
		frmRplesMergingClient.getContentPane().add(splitPaneApplication, BorderLayout.CENTER);
		
		splitPanePreferences = new JSplitPane();
		splitPanePreferences.setResizeWeight(0.5);
		splitPanePreferences.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneApplication.setLeftComponent(splitPanePreferences);
		
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
		treeDifferencesDivision.addTreeSelectionListener(new TreeSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
			 */
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				try {
					Controller.selectionChangedDifferencesTree();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});	
		scrollPaneDifferencesDivision.setViewportView(treeDifferencesDivision);
		
		JSplitPane splitPaneResolution = new JSplitPane();
		splitPaneResolution.setResizeWeight(0.5);
		splitPaneResolution.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneApplication.setRightComponent(splitPaneResolution);
		
		JPanel panelResolution = new JPanel();
		splitPaneResolution.setRightComponent(panelResolution);
		panelResolution.setLayout(new BorderLayout(0, 0));
		
		JPanel panelResolutionHeader = new JPanel();
		panelResolutionHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelResolution.add(panelResolutionHeader, BorderLayout.NORTH);
		panelResolutionHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblResolutionHeader = new JLabel(" Resolution");
		panelResolutionHeader.add(lblResolutionHeader, BorderLayout.NORTH);
		
		tabbedPaneResolution = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPaneResolution.addChangeListener(new ChangeListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				Controller.resolutionTabSelectionChanged();
			}
		});
		panelResolution.add(tabbedPaneResolution, BorderLayout.CENTER);
		
		JPanel panelResolutionTriples = new JPanel();
		tabbedPaneResolution.addTab("Triples", null, panelResolutionTriples, null);
		panelResolutionTriples.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionTriples = new JScrollPane();
		panelResolutionTriples.add(scrollPaneResolutionTriples, BorderLayout.CENTER);
		
		tableResolutionTriples = new JTable();
		tableResolutionTriples.setRowHeight(25);
		tableResolutionTriples.getTableHeader().setReorderingAllowed(false);
		tableResolutionTriples.setModel(tableModelResolutionTriples);
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
		panelResolutionTriples.add(toolBarResolutionTriples, BorderLayout.NORTH);
		
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
		
		JPanel panelResolutionSemanticEnrichment = new JPanel();
		tabbedPaneResolution.addTab("Semantic Enrichment", null, panelResolutionSemanticEnrichment, null);
		panelResolutionSemanticEnrichment.setLayout(new BorderLayout(0, 0));
		
		JPanel panelResolutionSemanticEnrichmentAllClasses = new JPanel();
		panelResolutionSemanticEnrichment.add(panelResolutionSemanticEnrichmentAllClasses, BorderLayout.NORTH);
		panelResolutionSemanticEnrichmentAllClasses.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionSemanticEnrichmentAllClasses = new JScrollPane();
		panelResolutionSemanticEnrichmentAllClasses.add(scrollPaneResolutionSemanticEnrichmentAllClasses);
		
		tableResolutionSemanticEnrichmentAllClasses = new JTable();
		tableResolutionSemanticEnrichmentAllClasses.setPreferredScrollableViewportSize(new Dimension(450, 150));
		tableResolutionSemanticEnrichmentAllClasses.setRowHeight(25);
		tableResolutionSemanticEnrichmentAllClasses.getTableHeader().setReorderingAllowed(false);
		tableResolutionSemanticEnrichmentAllClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableResolutionSemanticEnrichmentAllClasses.setModel(tableModelSemanticEnrichmentAllClasses);
		tableResolutionSemanticEnrichmentAllClasses.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					Controller.updateTableModelSemanticEnrichmentTriples();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
		scrollPaneResolutionSemanticEnrichmentAllClasses.setViewportView(tableResolutionSemanticEnrichmentAllClasses);
		
		JPanel panelResolutionSemanticEnrichmentClassTriples = new JPanel();
		panelResolutionSemanticEnrichment.add(panelResolutionSemanticEnrichmentClassTriples, BorderLayout.CENTER);
		panelResolutionSemanticEnrichmentClassTriples.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionSemanticEnrichmentClassTriples = new JScrollPane();
		panelResolutionSemanticEnrichmentClassTriples.add(scrollPaneResolutionSemanticEnrichmentClassTriples);
		
		tableResolutionSemanticEnrichmentClassTriples = new JTable();
		tableResolutionSemanticEnrichmentClassTriples.setRowHeight(25);
		tableResolutionSemanticEnrichmentClassTriples.getTableHeader().setReorderingAllowed(false);
		tableResolutionSemanticEnrichmentClassTriples.setModel(tableModelSemanticEnrichmentClassTriples);
		tableModelSemanticEnrichmentClassTriples.addTableModelListener(new TableModelListener() {
			
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
						tableResolutionSemanticEnrichmentClassTriples.updateUI();
					}
				});
			}
		});
		tableResolutionSemanticEnrichmentClassTriples.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			/* (non-Javadoc)
			 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
			 */
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Controller.tableResolutionSemanticEnrichmentClassTriplesSelectionChanged();
			}
		});
		scrollPaneResolutionSemanticEnrichmentClassTriples.setViewportView(tableResolutionSemanticEnrichmentClassTriples);
		
		JToolBar toolBarResolutionSemanticEnrichmentClassTriples = new JToolBar();
		panelResolutionSemanticEnrichmentClassTriples.add(toolBarResolutionSemanticEnrichmentClassTriples, BorderLayout.NORTH);
		
		JButton btnResolutionSemanticEnrichmentClassTriplesApproveSelected = new JButton("Approve selected");
		btnResolutionSemanticEnrichmentClassTriplesApproveSelected.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.approveSelectedEntriesResolutionSemanticEnrichmentClassTriples();
			}
		});
		toolBarResolutionSemanticEnrichmentClassTriples.add(btnResolutionSemanticEnrichmentClassTriplesApproveSelected);
		
		JButton btnResolutionSemanticEnrichmentClassTriplesSelectAll = new JButton("Select all");
		btnResolutionSemanticEnrichmentClassTriplesSelectAll.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				Controller.selectAllEntriesResolutionSemanticEnrichmentClassTriples();
			}
		});
		toolBarResolutionSemanticEnrichmentClassTriples.add(btnResolutionSemanticEnrichmentClassTriplesSelectAll);
		
		JPanel panelResolutionHighLevelChanges = new JPanel();
		tabbedPaneResolution.addTab("High level changes", null, panelResolutionHighLevelChanges, null);
		panelResolutionHighLevelChanges.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneResolutionHighLevelChanges = new JScrollPane();
		panelResolutionHighLevelChanges.add(scrollPaneResolutionHighLevelChanges, BorderLayout.CENTER);
		
		tableResolutionHighLevelChanges = new JTable();
		tableResolutionHighLevelChanges.setRowHeight(25);
		tableResolutionHighLevelChanges.getTableHeader().setReorderingAllowed(false);
		tableResolutionHighLevelChanges.setModel(tableModelResolutionHighLevelChanges);
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
		panelResolutionHighLevelChanges.add(toolBarResolutionHighLevelChanges, BorderLayout.NORTH);
		
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
		
		panelGraph = new JPanel();
		scrollPaneGraph.setViewportView(panelGraph);
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
	 * Get the graph panel.
	 * 
	 * @return the graph panel
	 */
	public static JPanel getPanelGraph() {
		return panelGraph;
	}


	/**
	 * Set the graph panel.
	 * 
	 * @param panelGraph the graph panel to set
	 */
	public static void setPanelGraph(JPanel panelGraph) {
		ApplicationUI.panelGraph = panelGraph;
	}


	/**
	 * Get the resolution tabbed pane.
	 * 
	 * @return the resolution tabbed pane
	 */
	public static JTabbedPane getTabbedPaneResolution() {
		return tabbedPaneResolution;
	}


	/**
	 * Set the resolution tabbed pane.
	 * 
	 * @param tabbedPaneResolution the resolution tabbed pane to set
	 */
	public static void setTabbedPaneResolution(JTabbedPane tabbedPaneResolution) {
		ApplicationUI.tabbedPaneResolution = tabbedPaneResolution;
	}


	/**
	 * Get the table semantic enrichment all classes.
	 * 
	 * @return the table semantic enrichment all classes
	 */
	public static JTable getTableResolutionSemanticEnrichmentAllClasses() {
		return tableResolutionSemanticEnrichmentAllClasses;
	}


	/**
	 * Set the table semantic enrichment all classes.
	 * 
	 * @param tableResolutionSemanticEnrichmentAllClasses the table semantic enrichment all classes to set
	 */
	public static void setTableResolutionSemanticEnrichmentAllClasses(JTable tableResolutionSemanticEnrichmentAllClasses) {
		ApplicationUI.tableResolutionSemanticEnrichmentAllClasses = tableResolutionSemanticEnrichmentAllClasses;
	}


	/**
	 * Get the table model semantic enrichment all classes.
	 * 
	 * @return the table model semantic enrichment all classes
	 */
	public static TableModelSemanticEnrichmentAllClasses getTableModelSemanticEnrichmentAllClasses() {
		return tableModelSemanticEnrichmentAllClasses;
	}


	/**
	 * Set the table model semantic enrichment all classes.
	 * 
	 * @param tableModelSemanticEnrichmentAllClasses the table model semantic enrichment all classes to set
	 */
	public static void setTableModelSemanticEnrichmentAllClasses(TableModelSemanticEnrichmentAllClasses tableModelSemanticEnrichmentAllClasses) {
		ApplicationUI.tableModelSemanticEnrichmentAllClasses = tableModelSemanticEnrichmentAllClasses;
	}


	/**
	 * Get the table model semantic enrichment class triples.
	 * 
	 * @return the table model semantic enrichment class triples
	 */
	public static TableModelSemanticEnrichmentClassTriples getTableModelSemanticEnrichmentClassTriples() {
		return tableModelSemanticEnrichmentClassTriples;
	}


	/**
	 * Set the table model semantic enrichment class triples.
	 * 
	 * @param tableModelSemanticEnrichmentClassTriples the table model semantic enrichment class triples to set
	 */
	public static void setTableModelSemanticEnrichmentClassTriples(TableModelSemanticEnrichmentClassTriples tableModelSemanticEnrichmentClassTriples) {
		ApplicationUI.tableModelSemanticEnrichmentClassTriples = tableModelSemanticEnrichmentClassTriples;
	}


	/**
	 * Get the table semantic enrichment class triples.
	 * 
	 * @return the table model semantic enrichment class triples
	 */
	public static JTable getTableResolutionSemanticEnrichmentClassTriples() {
		return tableResolutionSemanticEnrichmentClassTriples;
	}


	/**
	 * Set table semantic enrichment class triples.
	 * 
	 * @param tableResolutionSemanticEnrichmentClassTriples the table semantic enrichment class triples to set
	 */
	public static void setTableResolutionSemanticEnrichmentClassTriples(JTable tableResolutionSemanticEnrichmentClassTriples) {
		ApplicationUI.tableResolutionSemanticEnrichmentClassTriples = tableResolutionSemanticEnrichmentClassTriples;
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
		return splitPaneApplication;
	}
	
	
	/**
	 * Get the preferences split pane.
	 * 
	 * @return the preferences split pane
	 */
	public static JSplitPane getSplitPanePreferences() {
		return splitPanePreferences;
	}

}
