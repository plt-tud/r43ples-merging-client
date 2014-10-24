package de.tud.plt.r43ples.client.desktop.control;

import java.awt.Color;

/*
 * The color definitions.
 *
 * @author Stephan Hensel
 *
 */
public class ColorDefinitions {

	/** Red color. (Saturation: 180) **/
	public static final Color RED = new Color(223, 32, 32);
	/** Green color. (Saturation: 180) **/
	public static final Color GREEN = new Color(23, 223, 32);
	/** Orange color. (Saturation: 180) **/
	public static final Color ORANGE = new Color(223, 128, 32);
	/** Orange color. (Saturation: 180) **/
	public static final Color BLUE = new Color(32, 32, 223);
	
	/** Background color of selected rows. **/
	public static final Color backgroundColorSelectedRow = Color.DARK_GRAY;
	/** Border color of selected rows. **/
	public static final Color borderColorSelectedRow = new Color(0, 0, 128);
	
	/** The color of conflicting rows. **/
	public static final Color conflictingRowColor = RED;
	/** The color of non conflicting rows. **/
	public static final Color nonConflictingRowColor = ORANGE;
	/** The color of approved rows. **/
	public static final Color approvedRowColor = GREEN;
	
	/** The color of not approved but currently changed rows. **/
	public static final Color notApprovedButChangedRowColor = Color.LIGHT_GRAY;
	/** The color of already approved but currently changed rows. **/
	public static final Color approvedButChangedRowColor = Color.BLUE;
	
}
