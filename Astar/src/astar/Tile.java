package astar;

import javax.swing.JPanel;
/**
 * <h1>Tile</h1>
 * <p>The Tile class is an extension of the {@link javax.swing.JPanel#JPanel()} class and it exists only to help with the positioning of the tiles inside the grid.</p>
 * @author Nikolaos Kouroumalos
 * @version 1.0
 */
public class Tile extends JPanel {

	private static final long serialVersionUID = 1L;

	int row, col;

	public Tile(int row, int col) {
		this.row = row;
		this.col = col;
	}
}
