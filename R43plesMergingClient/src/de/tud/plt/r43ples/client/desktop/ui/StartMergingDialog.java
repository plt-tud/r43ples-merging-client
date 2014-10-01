package de.tud.plt.r43ples.client.desktop.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import de.tud.plt.r43ples.client.desktop.control.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JTextField;
import javax.swing.JTextArea;

/**
 * The start merging dialog. Creates the MERGE query.
 * 
 * @author Stephan Hensel
 *
 */
public class StartMergingDialog extends JDialog {

	/** The default serial version UID. **/
	private static final long serialVersionUID = 1L;
	/** The content panel. **/
	private final JPanel contentPanel = new JPanel();
	/** The button group of the radio buttons for selection of the merging method. **/
	private final ButtonGroup buttonGroupMergingMethod = new ButtonGroup();
	/** The radio button of merging method AUTO. **/
	private static JRadioButton rdbtnMergingMethodAUTO;
	/** The radio button of merging method MANUAL. **/
	private static JRadioButton rdbtnMergingMethodMANUAL;
	/** The radio button of merging method COMMON. **/
	private static JRadioButton rdbtnMergingMethodCOMMON;
	/** The user text field. **/
	private static JTextField tfUser;
	/** The commit message text area. **/
	private static JTextArea textAreaMessage;
	
	/** The combo box model of the graph names. **/
	private static DefaultComboBoxModel<String> cBModelGraph = new DefaultComboBoxModel<String>();
	/** The combo box model of the SDDs. **/
	private static DefaultComboBoxModel<String> cBModelSDD = new DefaultComboBoxModel<String>();
	/** The combo box model of the possible revision numbers for A. **/
	private static DefaultComboBoxModel<String> cBModelRevisionA = new DefaultComboBoxModel<String>();
	/** The combo box model of the possible revision numbers for B. **/
	private static DefaultComboBoxModel<String> cBModelRevisionB = new DefaultComboBoxModel<String>();
	

	/**
	 * Create the dialog.
	 */
	public StartMergingDialog() {
		setTitle("Start Merging");
		setBounds(100, 100, 450, 380);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panelMergingMethod = new JPanel();
		panelMergingMethod.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Select the merging method", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelMergingMethod.setBounds(10, 11, 414, 50);
		contentPanel.add(panelMergingMethod);
		panelMergingMethod.setLayout(new GridLayout(0, 3, 0, 0));
		
		rdbtnMergingMethodAUTO = new JRadioButton("AUTO");
		buttonGroupMergingMethod.add(rdbtnMergingMethodAUTO);
		rdbtnMergingMethodAUTO.setSelected(true);
		panelMergingMethod.add(rdbtnMergingMethodAUTO);
		
		rdbtnMergingMethodMANUAL = new JRadioButton("MANUAL");
		rdbtnMergingMethodMANUAL.setEnabled(false);
		buttonGroupMergingMethod.add(rdbtnMergingMethodMANUAL);
		panelMergingMethod.add(rdbtnMergingMethodMANUAL);
		
		rdbtnMergingMethodCOMMON = new JRadioButton("Common");
		buttonGroupMergingMethod.add(rdbtnMergingMethodCOMMON);
		panelMergingMethod.add(rdbtnMergingMethodCOMMON);
		
		JLabel lblSelectGraph = new JLabel("Select the graph");
		lblSelectGraph.setBounds(13, 72, 96, 20);
		contentPanel.add(lblSelectGraph);
		
		JComboBox<String> cBGraph = new JComboBox<String>();
		cBGraph.addActionListener(new ActionListener() {
			
			/* (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					Controller.updateRevisionComboBoxes();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		cBGraph.setBounds(119, 72, 305, 20);
		cBGraph.setModel(cBModelGraph);
		contentPanel.add(cBGraph);
		
		JLabel lblSelectSDD = new JLabel("Select the SDD");
		lblSelectSDD.setBounds(13, 103, 96, 20);
		contentPanel.add(lblSelectSDD);
		
		JComboBox<String> cBSDD = new JComboBox<String>();
		cBSDD.setBounds(119, 103, 305, 20);
		cBSDD.setModel(cBModelSDD);
		contentPanel.add(cBSDD);
		
		JLabel lblMerge = new JLabel("MERGE");
		lblMerge.setBounds(13, 134, 60, 20);
		contentPanel.add(lblMerge);
		
		JComboBox<String> comboBoxRevisionA = new JComboBox<String>();
		comboBoxRevisionA.setBounds(119, 134, 110, 20);
		comboBoxRevisionA.setModel(cBModelRevisionA);
		contentPanel.add(comboBoxRevisionA);
		
		JLabel lblInto = new JLabel("INTO");
		lblInto.setHorizontalAlignment(SwingConstants.CENTER);
		lblInto.setBounds(239, 134, 65, 20);
		contentPanel.add(lblInto);
		
		JComboBox<String> comboBoxRevisionB = new JComboBox<String>();
		comboBoxRevisionB.setBounds(314, 134, 110, 20);
		comboBoxRevisionB.setModel(cBModelRevisionB);
		contentPanel.add(comboBoxRevisionB);
		
		JLabel lblUser = new JLabel("USER");
		lblUser.setBounds(13, 165, 60, 20);
		contentPanel.add(lblUser);
		
		tfUser = new JTextField();
		tfUser.setBounds(119, 165, 305, 20);
		contentPanel.add(tfUser);
		tfUser.setColumns(10);
		
		JLabel lblMessage = new JLabel("MESSAGE");
		lblMessage.setBounds(13, 196, 60, 20);
		contentPanel.add(lblMessage);
		
		textAreaMessage = new JTextArea();
		textAreaMessage.setBounds(119, 196, 305, 100);
		contentPanel.add(textAreaMessage);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					/* (non-Javadoc)
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent arg0) {
						try {
							Controller.startNewMergeProcess();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					/* (non-Javadoc)
					 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
					 */
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	
	/**
	 * Get the graph combo box model.
	 * 
	 * @return the graph combo box model 
	 */
	public static DefaultComboBoxModel<String> getcBModelGraph() {
		return cBModelGraph;
	}


	/**
	 * Set the model of graph combo box.
	 * 
	 * @param cBModelGraph the model of graph combo box 
	 */
	public static void setcBModelGraph(DefaultComboBoxModel<String> cBModelGraph) {
		StartMergingDialog.cBModelGraph = cBModelGraph;
	}


	/**
	 * Get the SDD combo box model.
	 * 
	 * @return the SDD combo box model
	 */
	public static DefaultComboBoxModel<String> getcBModelSDD() {
		return cBModelSDD;
	}


	/**
	 * Set the SDD combo box model.
	 * 
	 * @param cBModelSDD the SDD combo box model
	 */
	public static void setcBModelSDD(DefaultComboBoxModel<String> cBModelSDD) {
		StartMergingDialog.cBModelSDD = cBModelSDD;
	}


	/**
	 * Get the revision A combo box model.
	 * 
	 * @return the revision A combo box model
	 */
	public static DefaultComboBoxModel<String> getcBModelRevisionA() {
		return cBModelRevisionA;
	}


	/**
	 * Set the revision A combo box model.
	 * 
	 * @param cBModelRevisionA the revision A combo box model
	 */
	public static void setcBModelRevisionA(DefaultComboBoxModel<String> cBModelRevisionA) {
		StartMergingDialog.cBModelRevisionA = cBModelRevisionA;
	}


	/**
	 * Get the revision B combo box model.
	 * 
	 * @return the revision B combo box model
	 */
	public static DefaultComboBoxModel<String> getcBModelRevisionB() {
		return cBModelRevisionB;
	}


	/**
	 * Set the revision B combo box model.
	 * 
	 * @param cBModelRevisionB the revision B combo box model
	 */
	public static void setcBModelRevisionB(DefaultComboBoxModel<String> cBModelRevisionB) {
		StartMergingDialog.cBModelRevisionB = cBModelRevisionB;
	}


	/**
	 * Get the merging method AUTO radio button.
	 * 
	 * @return the merging method AUTO radio button
	 */
	public static JRadioButton getRdbtnMergingMethodAUTO() {
		return rdbtnMergingMethodAUTO;
	}


	/**
	 * Set the merging method AUTO radio button.
	 * 
	 * @param rdbtnMergingMethodAUTO the merging method AUTO radio button
	 */
	public static void setRdbtnMergingMethodAUTO(JRadioButton rdbtnMergingMethodAUTO) {
		StartMergingDialog.rdbtnMergingMethodAUTO = rdbtnMergingMethodAUTO;
	}


	/**
	 * Get the merging method MANUAL radio button.
	 * 
	 * @return the merging method MANUAL radio button
	 */
	public static JRadioButton getRdbtnMergingMethodMANUAL() {
		return rdbtnMergingMethodMANUAL;
	}


	/**
	 * Set the merging method MANUAL radio button.
	 * 
	 * @param rdbtnMergingMethodMANUAL the merging method MANUAL radio button
	 */
	public static void setRdbtnMergingMethodMANUAL(JRadioButton rdbtnMergingMethodMANUAL) {
		StartMergingDialog.rdbtnMergingMethodMANUAL = rdbtnMergingMethodMANUAL;
	}


	/**
	 * Get the merging method COMMON radio button.
	 * 
	 * @return the merging method COMMON radio button
	 */
	public static JRadioButton getRdbtnMergingMethodCOMMON() {
		return rdbtnMergingMethodCOMMON;
	}


	/**
	 * Set the merging method COMMON radio button.
	 * 
	 * @param rdbtnMergingMethodCOMMON the merging method COMMON radio button
	 */
	public static void setRdbtnMergingMethodCOMMON(JRadioButton rdbtnMergingMethodCOMMON) {
		StartMergingDialog.rdbtnMergingMethodCOMMON = rdbtnMergingMethodCOMMON;
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
	 * @param tfUser the user text field
	 */
	public static void setTfUser(JTextField tfUser) {
		StartMergingDialog.tfUser = tfUser;
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
	 * @param textAreaMessage the message text area
	 */
	public static void setTextAreaMessage(JTextArea textAreaMessage) {
		StartMergingDialog.textAreaMessage = textAreaMessage;
	}
	
}
