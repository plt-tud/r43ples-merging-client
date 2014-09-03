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
	private JRadioButton rdbtnMergingMethodAUTO;
	/** The radio button of merging method MANUAL. **/
	private JRadioButton rdbtnMergingMethodMANUAL;
	/** The radio button of merging method COMMON. **/
	private JRadioButton rdbtnMergingMethodCOMMON;
	
	/** The combo box model of the graph names. **/
	private static DefaultComboBoxModel<String> cBModelGraph = new DefaultComboBoxModel<String>();
	/** The combo box model of the possible revision numbers for A. **/
	private static DefaultComboBoxModel<String> cBModelRevisionA = new DefaultComboBoxModel<String>();
	/** The combo box model of the possible revision numbers for B. **/
	private static DefaultComboBoxModel<String> cBModelRevisionB = new DefaultComboBoxModel<String>();
	

	/**
	 * Create the dialog.
	 */
	public StartMergingDialog() {
		setTitle("Start Merging");
		setBounds(100, 100, 450, 220);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Select the merging method", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 414, 50);
		contentPanel.add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));
		
		rdbtnMergingMethodAUTO = new JRadioButton("AUTO");
		buttonGroupMergingMethod.add(rdbtnMergingMethodAUTO);
		rdbtnMergingMethodAUTO.setSelected(true);
		panel.add(rdbtnMergingMethodAUTO);
		
		rdbtnMergingMethodMANUAL = new JRadioButton("MANUAL");
		rdbtnMergingMethodMANUAL.setEnabled(false);
		buttonGroupMergingMethod.add(rdbtnMergingMethodMANUAL);
		panel.add(rdbtnMergingMethodMANUAL);
		
		rdbtnMergingMethodCOMMON = new JRadioButton("Common");
		buttonGroupMergingMethod.add(rdbtnMergingMethodCOMMON);
		panel.add(rdbtnMergingMethodCOMMON);
		
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
		
		JLabel lblMerge = new JLabel("MERGE");
		lblMerge.setBounds(13, 103, 60, 20);
		contentPanel.add(lblMerge);
		
		JComboBox<String> comboBoxRevisionA = new JComboBox<String>();
		comboBoxRevisionA.setBounds(119, 103, 110, 20);
		comboBoxRevisionA.setModel(cBModelRevisionA);
		contentPanel.add(comboBoxRevisionA);
		
		JLabel lblInto = new JLabel("INTO");
		lblInto.setHorizontalAlignment(SwingConstants.CENTER);
		lblInto.setBounds(239, 103, 65, 20);
		contentPanel.add(lblInto);
		
		JComboBox<String> comboBoxRevisionB = new JComboBox<String>();
		comboBoxRevisionB.setBounds(314, 103, 110, 20);
		comboBoxRevisionB.setModel(cBModelRevisionB);
		contentPanel.add(comboBoxRevisionB);
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
						Controller.startNewMergeProcess();
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
	
	
	public static DefaultComboBoxModel<String> getcBModelGraph() {
		return cBModelGraph;
	}



	public static void setcBModelGraph(DefaultComboBoxModel<String> cBModelGraph) {
		StartMergingDialog.cBModelGraph = cBModelGraph;
	}



	public static DefaultComboBoxModel<String> getcBModelRevisionA() {
		return cBModelRevisionA;
	}



	public static void setcBModelRevisionA(DefaultComboBoxModel<String> cBModelRevisionA) {
		StartMergingDialog.cBModelRevisionA = cBModelRevisionA;
	}



	public static DefaultComboBoxModel<String> getcBModelRevisionB() {
		return cBModelRevisionB;
	}



	public static void setcBModelRevisionB(DefaultComboBoxModel<String> cBModelRevisionB) {
		StartMergingDialog.cBModelRevisionB = cBModelRevisionB;
	}


	public JRadioButton getRdbtnMergingMethodAUTO() {
		return rdbtnMergingMethodAUTO;
	}


	public void setRdbtnMergingMethodAUTO(JRadioButton rdbtnMergingMethodAUTO) {
		this.rdbtnMergingMethodAUTO = rdbtnMergingMethodAUTO;
	}


	public JRadioButton getRdbtnMergingMethodMANUAL() {
		return rdbtnMergingMethodMANUAL;
	}


	public void setRdbtnMergingMethodMANUAL(JRadioButton rdbtnMergingMethodMANUAL) {
		this.rdbtnMergingMethodMANUAL = rdbtnMergingMethodMANUAL;
	}


	public JRadioButton getRdbtnMergingMethodCOMMON() {
		return rdbtnMergingMethodCOMMON;
	}


	public void setRdbtnMergingMethodCOMMON(JRadioButton rdbtnMergingMethodCOMMON) {
		this.rdbtnMergingMethodCOMMON = rdbtnMergingMethodCOMMON;
	}
}
