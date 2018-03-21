package astar;

import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * <h1>Astar</h1> A simple 2D pathfinding program that uses the Astar algorithm.
 * 
 * @author Nikolaos Kouroumalos
 * @version 1.0
 */
public class Astar {

	static JFrame frmAstar;
	public JPanel panel;
	public JPanel topPanel;
	public static JPanel centerPanel;
	public JButton btnAstar;
	public JButton btnResetGrid;
	public JTextField gridCols;
	public JTextField gridRows;
	private JLabel lblGridSize;
	public JButton btnSetStart;

	private int minGridSize = 5;
	private int maxGridSize = 20;

	private int gridRow;
	private int gridCol;

	public JButton btnSetEnd;
	public JCheckBox chckbxDiagonalMovement;
	private JPanel bottomPanel;
	public JMenuBar menuBar;
	public JMenu mnMenu;
	public JMenuItem mntmExit;
	public JMenuItem mntmAbout;
	public JCheckBox chckbxCuttingObstacles;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Astar();
					Astar.frmAstar.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Astar() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAstar = new JFrame();
		frmAstar.setTitle("Astar");
		frmAstar.setBounds(100, 100, 750, 500);
		frmAstar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		topPanel = new JPanel();
		frmAstar.getContentPane().add(topPanel, BorderLayout.NORTH);

		lblGridSize = new JLabel("Grid Size:");
		lblGridSize.setHorizontalAlignment(SwingConstants.LEFT);
		topPanel.add(lblGridSize);

		gridRows = new JTextField();
		gridRows.setHorizontalAlignment(SwingConstants.LEFT);
		gridRows.setText("5");
		topPanel.add(gridRows);
		gridRows.setColumns(2);
		// Using a document listener to grab the changes in the text field as
		// they happen.
		gridRows.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			/**
			 * Checks if the value entered by the user is a positive integer or
			 * not. If it is, it will call {@link astar.Astar#createGrid()}. If
			 * the value is invalid (character, decimal, negative integer) it
			 * will throw an exception.
			 * 
			 * <p>
			 * No error message will be displayed to the user. Because the user
			 * should be allowed to enter the numbers 1 and 2 (otherwise invalid
			 * under the minimum grid size) without a message disturbing him in
			 * order to finish typing a number between 10-19 or 20.
			 * </p>
			 */
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				try {

					if (Integer.parseInt(gridRows.getText()) >= 3) {
						createGrid();
					}
				} catch (NumberFormatException grid) {

				}

			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

		});
		gridCols = new JTextField();
		gridCols.setHorizontalAlignment(SwingConstants.LEFT);
		gridCols.setText("5");
		topPanel.add(gridCols);
		gridCols.setColumns(2);
		// Using a document listener to grab the changes in the text field as
		// they happen.
		gridCols.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			/**
			 * Checks if the value entered by the user is a positive integer or
			 * not. If it is, it will call {@link astar.Astar#createGrid()}. If
			 * the value is invalid (character, decimal, negative integer) it
			 * will throw an exception.
			 * 
			 * <p>
			 * No error message will be displayed to the user. Because the user
			 * should be allowed to enter the numbers 1 and 2 (otherwise invalid
			 * under the minimum grid size) without a message disturbing him in
			 * order to finish typing a number between 10-19 or 20.
			 * </p>
			 */
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				try {

					if (Integer.parseInt(gridCols.getText()) >= 3) {
						createGrid();
					}
				} catch (NumberFormatException grid) {

				}

			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

		});

		bottomPanel = new JPanel();
		frmAstar.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[] { 40, 30, 30, 0, 0, 0, 0, 0 };
		gbl_bottomPanel.rowHeights = new int[] { 28, 0 };
		gbl_bottomPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gbl_bottomPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		bottomPanel.setLayout(gbl_bottomPanel);

		btnSetEnd = new JButton("Set End");
		btnSetEnd.addActionListener(new ActionListener() {
			/**
			 * Allows the user to specify an ending position by calling
			 * {@link astar.Grid#setBuildingStatus(int status)}
			 */
			public void actionPerformed(ActionEvent arg0) {
				Grid buildingStatus = new Grid();
				buildingStatus.setBuildingStatus(2);

			}
		});

		btnSetStart = new JButton("Set Start");
		btnSetStart.addActionListener(new ActionListener() {
			/**
			 * Allows the user to specify a starting position by calling
			 * {@link astar.Grid#setBuildingStatus(int status)}
			 */
			public void actionPerformed(ActionEvent e) {
				Grid buildingStatus = new Grid();
				buildingStatus.setBuildingStatus(1);
			}
		});

		btnAstar = new JButton("Astar");
		btnAstar.addActionListener(new ActionListener() {
			/**
			 * Calls {@link astar.Grid#Astar()} to calculate the path.
			 */
			public void actionPerformed(ActionEvent arg0) {
				Grid astar = new Grid();
				astar.Astar();
			}
		});

		btnResetGrid = new JButton("Reset Grid");
		btnResetGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createGrid();
			}
		});
		GridBagConstraints gbc_btnResetGrid = new GridBagConstraints();
		gbc_btnResetGrid.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnResetGrid.insets = new Insets(0, 0, 0, 5);
		gbc_btnResetGrid.gridx = 2;
		gbc_btnResetGrid.gridy = 0;
		bottomPanel.add(btnResetGrid, gbc_btnResetGrid);
		GridBagConstraints gbc_btnAstar = new GridBagConstraints();
		gbc_btnAstar.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnAstar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAstar.gridx = 3;
		gbc_btnAstar.gridy = 0;
		bottomPanel.add(btnAstar, gbc_btnAstar);
		GridBagConstraints gbc_btnSetStart = new GridBagConstraints();
		gbc_btnSetStart.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSetStart.insets = new Insets(0, 0, 0, 5);
		gbc_btnSetStart.gridx = 4;
		gbc_btnSetStart.gridy = 0;
		bottomPanel.add(btnSetStart, gbc_btnSetStart);
		GridBagConstraints gbc_btnSetEnd = new GridBagConstraints();
		gbc_btnSetEnd.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSetEnd.insets = new Insets(0, 0, 0, 5);
		gbc_btnSetEnd.gridx = 5;
		gbc_btnSetEnd.gridy = 0;
		bottomPanel.add(btnSetEnd, gbc_btnSetEnd);

		chckbxDiagonalMovement = new JCheckBox("Diagonal Movement ON");
		chckbxDiagonalMovement.setSelected(true);
		chckbxDiagonalMovement.addActionListener(new ActionListener() {
			/**
			 * Enables/disables diagonal movement.
			 */
			public void actionPerformed(ActionEvent arg0) {
				Grid diagonalMove = new Grid();
				Grid.diagonalMovement = chckbxDiagonalMovement.isSelected();
				if (chckbxDiagonalMovement.isSelected() == false) {
					chckbxDiagonalMovement.setText("Diagonal Movement OFF");
				} else {
					chckbxDiagonalMovement.setText("Diagonal Movement ON");
				}
				diagonalMove.Astar();
			}

		});
		GridBagConstraints gbc_chckbxDiagonalMovement = new GridBagConstraints();
		gbc_chckbxDiagonalMovement.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxDiagonalMovement.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxDiagonalMovement.gridx = 6;
		gbc_chckbxDiagonalMovement.gridy = 0;
		bottomPanel.add(chckbxDiagonalMovement, gbc_chckbxDiagonalMovement);

		centerPanel = new JPanel();
		frmAstar.getContentPane().add(centerPanel, BorderLayout.CENTER);

		menuBar = new JMenuBar();
		frmAstar.setJMenuBar(menuBar);

		mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		chckbxCuttingObstacles = new JCheckBox("Cutting Obstacles ON");
		chckbxCuttingObstacles.setSelected(true);
		mnMenu.add(chckbxCuttingObstacles);
		chckbxCuttingObstacles.addActionListener(new ActionListener() {
			/**
			 * Enables/disables cutting through obstacles.
			 */
			public void actionPerformed(ActionEvent arg0) {
				Grid cuttingObstacles = new Grid();
				Grid.cuttingObstacles = chckbxCuttingObstacles.isSelected();
				if (chckbxCuttingObstacles.isSelected() == false) {
					chckbxCuttingObstacles.setText("Cutting Obstacles OFF");
				} else {
					chckbxCuttingObstacles.setText("Cutting Obstacles ON");
				}
				cuttingObstacles.Astar();
			}

		});
		mntmAbout = new JMenuItem("About");
		mnMenu.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				about();
			}

		});

		mntmExit = new JMenuItem("Exit");
		mnMenu.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		createGrid();
	}

	/**
	 * <p>
	 * checkAndUpdateGrid is responsible for almost all the error checking
	 * regarding the grid size values and start/end positions. It starts by
	 * checking if the grid values entered by the user through the grid text
	 * fields are valid and if they are within the permitted minimum/maximum
	 * size. It then proceeds by checking if the start/end positions are within
	 * the grid and they don't overlap each other.
	 * </p>
	 * If all the error checks are satisfied it will then return true; which
	 * allows the generation of a new grid. If an error check is not satisfied
	 * it will either throw an error (pop up message) to the user or fix it.
	 * 
	 * @return
	 *         <p>
	 *         Returns true if all error checks are satisfied.
	 *         </p>
	 *         Returns false if an error check was not satisfied and needs to be
	 *         fixed by the user.
	 */
	private boolean checkAndUpdateGrid() {

		try {
			gridRow = Integer.parseInt(gridRows.getText());
			gridCol = Integer.parseInt(gridCols.getText());
			if (gridRow >= minGridSize && gridRow <= maxGridSize && gridCol >= minGridSize && gridCol <= maxGridSize) {
				if (Grid.startPosRow < gridRow && Grid.startPosRow >= 0 && Grid.startPosCol < gridCol
						&& Grid.startPosCol >= 0) {
					if (Grid.endPosRow < gridRow && Grid.endPosRow >= 0 && Grid.endPosCol < gridCol
							&& Grid.endPosCol >= 0) {
						if (Grid.startPosRow != Grid.endPosRow || Grid.startPosCol != Grid.endPosCol) {
							return true;
						} else {
							JOptionPane.showMessageDialog(null, "Start position and end position can not be the same.",
									"Grid Size", JOptionPane.WARNING_MESSAGE);
							return false;
						}
					} else {
						if (Grid.endPosCol >= gridCol) {
							Grid.endPosCol = gridCol - 1;
						}
						if (Grid.endPosRow >= gridRow) {
							Grid.endPosRow = gridRow - 1;
						}

						return checkAndUpdateGrid();
					}

				} else {
					if (Grid.startPosCol >= gridCol) {
						Grid.startPosCol = gridCol - 1;
					}
					if (Grid.startPosRow >= gridRow) {
						Grid.startPosRow = gridRow - 1;
					}

					return checkAndUpdateGrid();
				}

			} else {
				JOptionPane.showMessageDialog(null, "Choose a size between 5 and 20", "Grid Size",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
		} catch (NumberFormatException genGrid) {
			JOptionPane.showMessageDialog(null, "Only whole numbers between 5 and 20", "Grid Size",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	/**
	 * Calls {@link astar.Astar#checkAndUpdateGrid()} to check if all the values
	 * are valid and if they are it proceeds with generating a grid.
	 */
	private void createGrid() {
		Grid createGrid = new Grid();
		if (checkAndUpdateGrid()) {
			createGrid.generateGrid(gridRow, gridCol);
		}
	}

	/**
	 * Creates the "about" frame and initializes the contents of it.
	 */
	private void about() {
		JFrame aboutWindow = new JFrame();
		aboutWindow.setSize(350, 350);
		aboutWindow.getContentPane().setBackground(new Color(238, 238, 238));
		aboutWindow.setTitle("Astar - About");
		aboutWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutWindow.setVisible(true);
		aboutWindow.setLocationRelativeTo(frmAstar);
		aboutWindow.setResizable(false);

		JPanel gifPanel = new JPanel();
		aboutWindow.getContentPane().add(gifPanel, BorderLayout.NORTH);

		URL url = getClass().getResource("/images/GLaDos.gif");
		Icon icon = new ImageIcon(url);
		JLabel gif = new JLabel(icon);
		gifPanel.add(gif);

		JPanel aboutPanel = new JPanel();
		aboutWindow.getContentPane().add(aboutPanel, BorderLayout.SOUTH);
		JLabel aboutLabel = new JLabel();
		aboutLabel.setText(
				"<html>A simple 2D pathfinding program <p>that uses the Astar algorithm.</p><br>Author: Nikolaos Kouroumalos</br><p>Version: 1.0</p></html>");
		aboutLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aboutLabel.setVerticalAlignment(SwingConstants.TOP);
		aboutPanel.add(aboutLabel);
	}

}
