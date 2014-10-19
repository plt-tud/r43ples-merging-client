package de.tud.plt.r43ples.client.desktop.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

import de.tud.plt.r43ples.client.desktop.control.Controller;
import de.tud.plt.r43ples.client.desktop.model.table.entry.TableEntrySummaryReport;
import de.tud.plt.r43ples.client.desktop.model.table.model.TableModelSummaryReport;

/**
 * Report dialog displays the resolution state.
 * User can start the merging process with manually generated changes,
 * 
 * @author Stephan Hensel
 *
 */
public class ReportDialog {

	/** The summary report dialog. **/
	public static JDialog dialog;
	/** The content panel. **/
	private final JPanel contentPanel = new JPanel();
	/** The graph text field. **/
	private static JTextField tfGraph;
	/** The SDD text field. **/
	private static JTextField tfSDD;
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
	/** The OK/Push button. **/
	private static JButton okButton;


	/**
	 * Create the dialog.
	 */
	public ReportDialog() {
		initialize();
	}

	
	/**
	 * Initialize the contents of the dialog.
	 */
	private void initialize() {
		dialog = new JDialog();
		dialog.setTitle("Summary Report");
		dialog.setBounds(100, 100, 887, 758);
		dialog.setMinimumSize(new Dimension(445, 480));
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelQuerySummary = new JPanel();
			panelQuerySummary.setPreferredSize(new Dimension(10, 280));
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
			
			JLabel lblSDD = new JLabel("SDD");
			lblSDD.setBounds(10, 73, 70, 20);
			panelQuerySummary.add(lblSDD);
			
			tfSDD = new JTextField();
			tfSDD.setEnabled(false);
			tfSDD.setColumns(10);
			tfSDD.setBounds(90, 73, 300, 20);
			panelQuerySummary.add(tfSDD);
			
			JLabel lblMerge = new JLabel("MERGE");
			lblMerge.setBounds(10, 104, 70, 20);
			panelQuerySummary.add(lblMerge);
			
			tfRevisionA = new JTextField();
			tfRevisionA.setEnabled(false);
			tfRevisionA.setColumns(10);
			tfRevisionA.setBounds(90, 104, 100, 20);
			panelQuerySummary.add(tfRevisionA);
			
			JLabel lblInto = new JLabel("INTO");
			lblInto.setHorizontalAlignment(SwingConstants.CENTER);
			lblInto.setBounds(200, 104, 80, 20);
			panelQuerySummary.add(lblInto);
			
			tfRevisionB = new JTextField();
			tfRevisionB.setEnabled(false);
			tfRevisionB.setColumns(10);
			tfRevisionB.setBounds(290, 104, 100, 20);
			panelQuerySummary.add(tfRevisionB);
			
			JLabel lblUser = new JLabel("USER");
			lblUser.setBounds(10, 135, 60, 20);
			panelQuerySummary.add(lblUser);
			
			tfUser = new JTextField();
			tfUser.setColumns(10);
			tfUser.setBounds(90, 135, 300, 20);
			panelQuerySummary.add(tfUser);
			
			textAreaMessage = new JTextArea();
			textAreaMessage.setBounds(90, 166, 300, 100);
			panelQuerySummary.add(textAreaMessage);
			
			JLabel lblMessage = new JLabel("MESSAGE");
			lblMessage.setBounds(10, 166, 60, 20);
			panelQuerySummary.add(lblMessage);
		}
		
		JScrollPane scrollPaneTable = new JScrollPane();
		contentPanel.add(scrollPaneTable, BorderLayout.CENTER);
		
		table = new JTable();
		table.setRowHeight(25);
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(tableModel);
		table.setRowSelectionAllowed(true);
		scrollPaneTable.setViewportView(table);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Push");
				okButton.addActionListener(new ActionListener() {
					
					/* (non-Javadoc)
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent arg0) {
						try {
							Controller.pushToRemoteRepository();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				dialog.getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					/* (non-Javadoc)
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent arg0) {
						Controller.closeSummaryReport();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}


	/**
	 * Get the graph text field.
	 * 
	 * @return the graph text field
	 */
	public static JTextField getTfGraph() {
		return tfGraph;
	}


	/**
	 * Set the graph text field.
	 * 
	 * @param tfGraph the graph text field to set
	 */
	public static void setTfGraph(JTextField tfGraph) {
		ReportDialog.tfGraph = tfGraph;
	}


	/**
	 * Get the SDD text field.
	 * 
	 * @return the SDD text field
	 */
	public static JTextField getTfSDD() {
		return tfSDD;
	}


	/**
	 * Set the SDD text field.
	 * 
	 * @param tfSDD the SDD text field to set
	 */
	public static void setTfSDD(JTextField tfSDD) {
		ReportDialog.tfSDD = tfSDD;
	}


	/**
	 * Get the revision A text field.
	 * 
	 * @return the revision A text field
	 */
	public static JTextField getTfRevisionA() {
		return tfRevisionA;
	}


	/**
	 * Set the revision A text field.
	 * 
	 * @param tfRevisionA the revision A text field to set
	 */
	public static void setTfRevisionA(JTextField tfRevisionA) {
		ReportDialog.tfRevisionA = tfRevisionA;
	}


	/**
	 * Get the revision B text field.
	 * 
	 * @return the revision B text field
	 */
	public static JTextField getTfRevisionB() {
		return tfRevisionB;
	}


	/**
	 * Set the revision B text field.
	 * 
	 * @param tfRevisionB the revision B text field to set
	 */
	public static void setTfRevisionB(JTextField tfRevisionB) {
		ReportDialog.tfRevisionB = tfRevisionB;
	}


	/**
	 * Get the user text field.
	 * 
	 * @return the user text field
	 */
	public static JTextField getTfUser() {
		return tfUser;
	}


	/**
	 * Set the user text field.
	 * 
	 * @param tfUser the user text field to set
	 */
	public static void setTfUser(JTextField tfUser) {
		ReportDialog.tfUser = tfUser;
	}


	/**
	 * Get the message text area.
	 * 
	 * @return the message text area
	 */
	public static JTextArea getTextAreaMessage() {
		return textAreaMessage;
	}


	/**
	 * Set the message text area.
	 * 
	 * @param textAreaMessage the message text area to set
	 */
	public static void setTextAreaMessage(JTextArea textAreaMessage) {
		ReportDialog.textAreaMessage = textAreaMessage;
	}


	/**
	 * Get the table.
	 * 
	 * @return the table
	 */
	public static JTable getTable() {
		return table;
	}


	/**
	 * Set the table.
	 * 
	 * @param table the table to set
	 */
	public static void setTable(JTable table) {
		ReportDialog.table = table;
	}


	/**
	 * Get the table model.
	 * 
	 * @return the table model
	 */
	public static TableModelSummaryReport getTableModel() {
		return tableModel;
	}


	/**
	 * Set the table model.
	 * 
	 * @param tableModel the table model to set
	 */
	public static void setTableModel(TableModelSummaryReport tableModel) {
		ReportDialog.tableModel = tableModel;
	}


	/**
	 * Get the OK button.
	 * 
	 * @return the OK button
	 */
	public static JButton getOkButton() {
		return okButton;
	}


	/**
	 * Set the OK button.
	 * 
	 * @param okButton the OK button to set
	 */
	public static void setOkButton(JButton okButton) {
		ReportDialog.okButton = okButton;
	}
	
}
