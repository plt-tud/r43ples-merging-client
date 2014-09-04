package de.tud.plt.r43ples.client.desktop.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.jidesoft.swing.CheckBoxTree;

import de.tud.plt.r43ples.client.desktop.control.Controller;

import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

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
		
		JToolBar toolBar = new JToolBar();
		frmRplesMergingClient.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewMerge = new JButton("New MERGE");
		toolBar.add(btnNewMerge);
		
		JSplitPane splitPaneApplication = new JSplitPane();
		splitPaneApplication.setResizeWeight(0.2);
		frmRplesMergingClient.getContentPane().add(splitPaneApplication, BorderLayout.CENTER);
		
		JSplitPane splitPanePreferences = new JSplitPane();
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
		
		CheckBoxTree tree = new CheckBoxTree();
		panelFilterContent.add(tree);
		tree.getCheckBoxTreeSelectionModel().setDigIn(true);
		
		JPanel panelDifferences = new JPanel();
		splitPanePreferences.setLeftComponent(panelDifferences);
		panelDifferences.setLayout(new BorderLayout(0, 0));
		
		JPanel panelDifferenceHeader = new JPanel();
		panelDifferences.add(panelDifferenceHeader, BorderLayout.NORTH);
		panelDifferenceHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDifferenceHeader = new JLabel(" Differences");
		lblDifferenceHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDifferenceHeader.add(lblDifferenceHeader);
		
		JTabbedPane tabbedPaneDifferences = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPaneDifferences.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDifferences.add(tabbedPaneDifferences, BorderLayout.CENTER);
		
		JPanel panelDifferencesDivision = new JPanel();
		tabbedPaneDifferences.addTab("Division", null, panelDifferencesDivision, null);
		panelDifferencesDivision.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPaneDifferencesDivision = new JScrollPane();
		panelDifferencesDivision.add(scrollPaneDifferencesDivision, BorderLayout.CENTER);
		
		treeDifferencesDivision = new JTree();
		treeDifferencesDivision.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeDifferencesDivision.setModel(treeModelDifferencesDivision);
		treeDifferencesDivision.setCellRenderer(new TreeCellRendererDifferences());
		scrollPaneDifferencesDivision.setViewportView(treeDifferencesDivision);
		
		JPanel panelDifferencesTriples = new JPanel();
		tabbedPaneDifferences.addTab("Triples", null, panelDifferencesTriples, null);
		
		JSplitPane splitPaneResolution = new JSplitPane();
		splitPaneResolution.setResizeWeight(0.3);
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
		
		JTabbedPane tabbedPaneResolution = new JTabbedPane(JTabbedPane.BOTTOM);
		panelResolution.add(tabbedPaneResolution, BorderLayout.CENTER);
		
		JPanel panelResolutionTriples = new JPanel();
		tabbedPaneResolution.addTab("Triples", null, panelResolutionTriples, null);
		
		JPanel panelResolutionSemanticEnrichment = new JPanel();
		tabbedPaneResolution.addTab("Semantic Enrichment", null, panelResolutionSemanticEnrichment, null);
		
		JPanel panelRevisionGraph = new JPanel();
		splitPaneResolution.setLeftComponent(panelRevisionGraph);
		panelRevisionGraph.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRevisionGraphHeader = new JPanel();
		panelRevisionGraphHeader.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelRevisionGraph.add(panelRevisionGraphHeader, BorderLayout.NORTH);
		panelRevisionGraphHeader.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRevisionGraphHeader = new JLabel(" Revision graph");
		panelRevisionGraphHeader.add(lblRevisionGraphHeader);
	}


	public static JTree getTreeDifferencesDivision() {
		return treeDifferencesDivision;
	}


	public static void setTreeDifferencesDivision(JTree treeDifferencesDivision) {
		ApplicationUI.treeDifferencesDivision = treeDifferencesDivision;
	}


	public static DefaultTreeModel getTreeModelDifferencesDivision() {
		return treeModelDifferencesDivision;
	}


	public static void setTreeModelDifferencesDivision(
			DefaultTreeModel treeModelDifferencesDivision) {
		ApplicationUI.treeModelDifferencesDivision = treeModelDifferencesDivision;
	}

}
