package de.tud.plt.r43ples.client.desktop.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import de.tud.plt.r43ples.client.desktop.model.table.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.TableModelSummaryReport;
import javax.swing.ListSelectionModel;

/**
 * Report dialog displays the resolution state.
 * User can start the merging process with manually generated changes,
 * 
 * @author Stephan Hensel
 *
 */
public class ReportDialog extends JDialog {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** The content panel. **/
	private final JPanel contentPanel = new JPanel();
	/** The graph text field. **/
	private static JTextField tfGraph;
	/** The revision A text field. **/
	private static JTextField tfRevisionA;
	/** The revision B text field. **/
	private static JTextField tfRevisionB;
	/** The user text field. **/
	private static JTextField tfUser;
	/** The message text area. **/
	private static JTextArea textAreaMessage;
	/** The table. **/
	private static JTable table;
	/** The table model. **/
	private static TableModelSummaryReport tableModel = new TableModelSummaryReport(new ArrayList<TableEntrySummaryReport>());


	/**
	 * Create the dialog.
	 */
	public ReportDialog() {
		setTitle("Summary Report");
		setBounds(100, 100, 887, 758);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelQuerySummary = new JPanel();
			panelQuerySummary.setPreferredSize(new Dimension(10, 250));
			contentPanel.add(panelQuerySummary, BorderLayout.NORTH);
			panelQuerySummary.setLayout(null);
			
			JLabel lblSummaryHeader = new JLabel("Common merge query");
			lblSummaryHeader.setBounds(10, 11, 135, 20);
			panelQuerySummary.add(lblSummaryHeader);
			
			JLabel lblGraph = new JLabel("Graph");
			lblGraph.setBounds(10, 42, 70, 20);
			panelQuerySummary.add(lblGraph);
			
			tfGraph = new JTextField();
			tfGraph.setEnabled(false);
			tfGraph.setBounds(90, 42, 300, 20);
			panelQuerySummary.add(tfGraph);
			tfGraph.setColumns(10);
			
			JLabel lblMerge = new JLabel("MERGE");
			lblMerge.setBounds(10, 73, 70, 20);
			panelQuerySummary.add(lblMerge);
			
			tfRevisionA = new JTextField();
			tfRevisionA.setEnabled(false);
			tfRevisionA.setColumns(10);
			tfRevisionA.setBounds(90, 73, 100, 20);
			panelQuerySummary.add(tfRevisionA);
			
			JLabel lblInto = new JLabel("INTO");
			lblInto.setHorizontalAlignment(SwingConstants.CENTER);
			lblInto.setBounds(200, 73, 80, 20);
			panelQuerySummary.add(lblInto);
			
			tfRevisionB = new JTextField();
			tfRevisionB.setEnabled(false);
			tfRevisionB.setColumns(10);
			tfRevisionB.setBounds(290, 73, 100, 20);
			panelQuerySummary.add(tfRevisionB);
			
			JLabel lblUser = new JLabel("USER");
			lblUser.setBounds(10, 104, 60, 20);
			panelQuerySummary.add(lblUser);
			
			tfUser = new JTextField();
			tfUser.setColumns(10);
			tfUser.setBounds(90, 104, 300, 20);
			panelQuerySummary.add(tfUser);
			
			textAreaMessage = new JTextArea();
			textAreaMessage.setBounds(90, 135, 300, 100);
			panelQuerySummary.add(textAreaMessage);
			
			JLabel lblMessage = new JLabel("MESSAGE");
			lblMessage.setBounds(10, 135, 60, 20);
			panelQuerySummary.add(lblMessage);
		}
		
		JScrollPane scrollPaneTable = new JScrollPane();
		contentPanel.add(scrollPaneTable, BorderLayout.CENTER);
		
		table = new JTable();
		table.setModel(tableModel);
		table.setRowSelectionAllowed(true);
		scrollPaneTable.setViewportView(table);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Start Merge");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}


	/**
	 * @return the tfGraph
	 */
	public static JTextField getTfGraph() {
		return tfGraph;
	}


	/**
	 * @param tfGraph the tfGraph to set
	 */
	public static void setTfGraph(JTextField tfGraph) {
		ReportDialog.tfGraph = tfGraph;
	}


	/**
	 * @return the tfRevisionA
	 */
	public static JTextField getTfRevisionA() {
		return tfRevisionA;
	}


	/**
	 * @param tfRevisionA the tfRevisionA to set
	 */
	public static void setTfRevisionA(JTextField tfRevisionA) {
		ReportDialog.tfRevisionA = tfRevisionA;
	}


	/**
	 * @return the tfRevisionB
	 */
	public static JTextField getTfRevisionB() {
		return tfRevisionB;
	}


	/**
	 * @param tfRevisionB the tfRevisionB to set
	 */
	public static void setTfRevisionB(JTextField tfRevisionB) {
		ReportDialog.tfRevisionB = tfRevisionB;
	}


	/**
	 * @return the tfUser
	 */
	public static JTextField getTfUser() {
		return tfUser;
	}


	/**
	 * @param tfUser the tfUser to set
	 */
	public static void setTfUser(JTextField tfUser) {
		ReportDialog.tfUser = tfUser;
	}


	/**
	 * @return the textAreaMessage
	 */
	public static JTextArea getTextAreaMessage() {
		return textAreaMessage;
	}


	/**
	 * @param textAreaMessage the textAreaMessage to set
	 */
	public static void setTextAreaMessage(JTextArea textAreaMessage) {
		ReportDialog.textAreaMessage = textAreaMessage;
	}


	/**
	 * @return the table
	 */
	public static JTable getTable() {
		return table;
	}


	/**
	 * @param table the table to set
	 */
	public static void setTable(JTable table) {
		ReportDialog.table = table;
	}


	/**
	 * @return the tableModel
	 */
	public static TableModelSummaryReport getTableModel() {
		return tableModel;
	}


	/**
	 * @param tableModel the tableModel to set
	 */
	public static void setTableModel(TableModelSummaryReport tableModel) {
		ReportDialog.tableModel = tableModel;
	}
	
}
