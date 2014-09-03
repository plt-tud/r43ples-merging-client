package de.tud.plt.r43ples.client.desktop.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

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

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		try {
//			StartMergingDialog dialog = new StartMergingDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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
		
		JRadioButton rdbtnMergingMethodAUTO = new JRadioButton("AUTO");
		buttonGroupMergingMethod.add(rdbtnMergingMethodAUTO);
		rdbtnMergingMethodAUTO.setSelected(true);
		panel.add(rdbtnMergingMethodAUTO);
		
		JRadioButton rdbtnMergingMethodMANUAL = new JRadioButton("MANUAL");
		rdbtnMergingMethodMANUAL.setEnabled(false);
		buttonGroupMergingMethod.add(rdbtnMergingMethodMANUAL);
		panel.add(rdbtnMergingMethodMANUAL);
		
		JRadioButton rdbtnMergingMethodCOMMON = new JRadioButton("Common");
		buttonGroupMergingMethod.add(rdbtnMergingMethodCOMMON);
		panel.add(rdbtnMergingMethodCOMMON);
		
		JLabel lblSelectGraph = new JLabel("Select the graph");
		lblSelectGraph.setBounds(13, 72, 96, 20);
		contentPanel.add(lblSelectGraph);
		
		JComboBox<String> cBGraph = new JComboBox<String>();
		cBGraph.setBounds(119, 72, 305, 20);
		contentPanel.add(cBGraph);
		
		JLabel lblMerge = new JLabel("MERGE");
		lblMerge.setBounds(13, 103, 60, 20);
		contentPanel.add(lblMerge);
		
		JComboBox<String> comboBoxRevisionA = new JComboBox<String>();
		comboBoxRevisionA.setBounds(119, 103, 110, 20);
		contentPanel.add(comboBoxRevisionA);
		
		JLabel lblInto = new JLabel("INTO");
		lblInto.setHorizontalAlignment(SwingConstants.CENTER);
		lblInto.setBounds(239, 103, 65, 20);
		contentPanel.add(lblInto);
		
		JComboBox<String> comboBoxRevisionB = new JComboBox<String>();
		comboBoxRevisionB.setBounds(314, 103, 110, 20);
		contentPanel.add(comboBoxRevisionB);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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
}
