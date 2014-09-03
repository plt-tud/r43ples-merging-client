package de.tud.plt.r43ples.client.desktop.control;

import javax.swing.JDialog;

import de.tud.plt.r43ples.client.desktop.ui.ApplicationUI;
import de.tud.plt.r43ples.client.desktop.ui.StartMergingDialog;

public class Controller {

	
	public static void showStartMergingDialog() {
		
		StartMergingDialog dialog = new StartMergingDialog();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(ApplicationUI.frmRplesMergingClient);
		dialog.setModal(true);
		dialog.setVisible(true);
		
	}
	
	
}
