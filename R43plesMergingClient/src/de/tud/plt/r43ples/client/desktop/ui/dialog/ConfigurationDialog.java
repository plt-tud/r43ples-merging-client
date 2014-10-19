package de.tud.plt.r43ples.client.desktop.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.configuration.ConfigurationException;

import de.tud.plt.r43ples.client.desktop.control.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The configuration dialog.
 * 
 * @author Stephan Hensel
 *
 */
public class ConfigurationDialog {

	/** The configuration dialog. **/
	public static JDialog dialog;
	/** The content panel. **/
	private final JPanel contentPanel = new JPanel();
	/** The R43ples SPARQL endpoint text field. **/
	private static JTextField tfR43plesSparqlEndpoint;
	/** The R43ples revision graph text field. **/
	private static JTextField tfR43plesRevisionGraph;
	/** The R43ples SDD graph text field. **/
	private static JTextField tfR43plesSddGraph;
	/** The prefix mappings table. **/
	private static JTable tablePrefixMappings;
	/** The prefix mappings table model. **/
	private static DefaultTableModel tableModelPrefixMappings = new DefaultTableModel();

	
	/**
	 * Create the dialog.
	 */
	public ConfigurationDialog() {
		dialog = new JDialog();
		dialog.setTitle("Properties configuration");
		dialog.setResizable(false);
		dialog.setBounds(100, 100, 521, 551);
		dialog.getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panelRemote = new JPanel();
		panelRemote.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Remote", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelRemote.setBounds(10, 11, 494, 170);
		contentPanel.add(panelRemote);
		panelRemote.setLayout(null);
		
		JLabel lblR43plesSparqlEndpoint = new JLabel("R43ples SPARQL endpoint");
		lblR43plesSparqlEndpoint.setBounds(10, 22, 474, 14);
		panelRemote.add(lblR43plesSparqlEndpoint);
		
		tfR43plesSparqlEndpoint = new JTextField();
		tfR43plesSparqlEndpoint.setBounds(10, 40, 474, 20);
		panelRemote.add(tfR43plesSparqlEndpoint);
		tfR43plesSparqlEndpoint.setColumns(10);
		
		JLabel lblR43plesRevisionGraph = new JLabel("R43ples revision graph");
		lblR43plesRevisionGraph.setBounds(10, 71, 474, 14);
		panelRemote.add(lblR43plesRevisionGraph);
		
		tfR43plesRevisionGraph = new JTextField();
		tfR43plesRevisionGraph.setColumns(10);
		tfR43plesRevisionGraph.setBounds(10, 89, 474, 20);
		panelRemote.add(tfR43plesRevisionGraph);
		
		JLabel lblR43plesSddGraph = new JLabel("R43ples SDD graph");
		lblR43plesSddGraph.setBounds(10, 120, 474, 14);
		panelRemote.add(lblR43plesSddGraph);
		
		tfR43plesSddGraph = new JTextField();
		tfR43plesSddGraph.setColumns(10);
		tfR43plesSddGraph.setBounds(10, 138, 474, 20);
		panelRemote.add(tfR43plesSddGraph);
		
		JPanel panelLocal = new JPanel();
		panelLocal.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Local", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelLocal.setBounds(10, 192, 494, 288);
		contentPanel.add(panelLocal);
		panelLocal.setLayout(null);
		
		JLabel lblClientPrefixMapping = new JLabel("Client prefix mapping");
		lblClientPrefixMapping.setBounds(10, 22, 474, 14);
		panelLocal.add(lblClientPrefixMapping);
		
		JScrollPane scrollPanePrefixMappings = new JScrollPane();
		scrollPanePrefixMappings.setBounds(10, 40, 474, 200);
		panelLocal.add(scrollPanePrefixMappings);
		
		tablePrefixMappings = new JTable();
		tablePrefixMappings.setRowHeight(25);
		tablePrefixMappings.getTableHeader().setReorderingAllowed(false);
		tablePrefixMappings.setModel(tableModelPrefixMappings);
		scrollPanePrefixMappings.setViewportView(tablePrefixMappings);
		
		JButton btnCreateNewMapping = new JButton("Create new mapping");
		btnCreateNewMapping.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent arg0) {
				Controller.createNewPrefixMappingEntry();
			}
		});
		btnCreateNewMapping.setBounds(10, 251, 235, 23);
		panelLocal.add(btnCreateNewMapping);
		
		JButton btnDeleteSelectedMapping = new JButton("Delete selected mapping");
		btnDeleteSelectedMapping.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				Controller.deleteSelectedPrefixMapping();
			}
		});
		btnDeleteSelectedMapping.setBounds(249, 251, 235, 23);
		panelLocal.add(btnDeleteSelectedMapping);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			dialog.getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					/* (non-Javadoc)
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e) {
						try {
							Controller.writeConfigurationToFile();
						} catch (ConfigurationException e1) {
							Controller.showConfigurationExceptionDialog(dialog);
							e1.printStackTrace();
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
					public void actionPerformed(ActionEvent e) {
						Controller.closeConfigurationDialog();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	
	/**
	 * Get the R43ples SPARQL endpoint text field. 
	 * 
	 * @return the R43ples SPARQL endpoint text field
	 */
	public static JTextField getTfR43plesSparqlEndpoint() {
		return tfR43plesSparqlEndpoint;
	}


	/**
	 * Set the R43ples SPARQL endpoint text field.
	 * 
	 * @param tfR43plesSparqlEndpoint the R43ples SPARQL endpoint text field to set
	 */
	public static void setTfR43plesSparqlEndpoint(JTextField tfR43plesSparqlEndpoint) {
		ConfigurationDialog.tfR43plesSparqlEndpoint = tfR43plesSparqlEndpoint;
	}


	/**
	 * Get the R43ples revision graph text field.
	 * 
	 * @return the R43ples revision graph text field
	 */
	public static JTextField getTfR43plesRevisionGraph() {
		return tfR43plesRevisionGraph;
	}


	/**
	 * Set the R43ples revision graph text field.
	 * 
	 * @param tfR43plesRevisionGraph the R43ples revision graph text field to set
	 */
	public static void setTfR43plesRevisionGraph(JTextField tfR43plesRevisionGraph) {
		ConfigurationDialog.tfR43plesRevisionGraph = tfR43plesRevisionGraph;
	}


	/**
	 * Get the R43ples SDD graph text field.
	 * 
	 * @return the R43ples SDD graph text field
	 */
	public static JTextField getTfR43plesSddGraph() {
		return tfR43plesSddGraph;
	}


	/**
	 * Set the R43ples SDD graph text field.
	 * 
	 * @param tfR43plesSddGraph the R43ples SDD graph text field to set
	 */
	public static void setTfR43plesSddGraph(JTextField tfR43plesSddGraph) {
		ConfigurationDialog.tfR43plesSddGraph = tfR43plesSddGraph;
	}


	/**
	 * Get the prefix mappings table.
	 * 
	 * @return the prefix mappings table
	 */
	public static JTable getTablePrefixMappings() {
		return tablePrefixMappings;
	}

	
	/**
	 * Set the prefix mappings table.
	 * 
	 * @param tablePrefixMappings the prefix mappings table to set
	 */
	public static void setTablePrefixMappings(JTable tablePrefixMappings) {
		ConfigurationDialog.tablePrefixMappings = tablePrefixMappings;
	}


	/**
	 * Get the prefix mappings table model.
	 * 
	 * @return the prefix mappings table model
	 */
	public static DefaultTableModel getTableModelPrefixMappings() {
		return tableModelPrefixMappings;
	}


	/**
	 * Set the prefix mappings table model.
	 * 
	 * @param tableModelPrefixMappings the prefix mappings table model to set
	 */
	public static void setTableModelPrefixMappings(DefaultTableModel tableModelPrefixMappings) {
		ConfigurationDialog.tableModelPrefixMappings = tableModelPrefixMappings;
	}
	
}
