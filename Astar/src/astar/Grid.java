package astar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.PriorityQueue;
import java.util.Timer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates the grid and starts the Astar algorithm.
 * 
 * @author Nikolaos Kouroumalos
 * @version 1.0
 */
public class Grid {
	private static final int V_H_COST = 10; // Vertical and horizontal cost
	private static final int DIAGONAL_COST = 14;

	public static int endPosRow = 4;
	public static int endPosCol = 4;

	public static int startPosRow = 0;
	public static int startPosCol = 0;

	private static int gridRows = 5;
	private static int gridCols = 5;

	private static int buildingStatus = 0;

	public static boolean diagonalMovement = true;
	public static boolean cuttingObstacles = true;

	static boolean closedList[][];

	Timer timer = new Timer();

	private Color tileColor;
	static Tile[][] tile;
	static Node[][] grid;

	static PriorityQueue<Node> openList;

	/**
	 * Sets an obstacle by changing the value of the specific position inside
	 * the grid to null.
	 * 
	 * @param row
	 *            The row where the obstacle will be placed.
	 * @param col
	 *            The row column the obstacle will be placed.
	 */
	public void setBlockedNode(int row, int col) {
		grid[row][col] = null;
	}

	/**
	 * Sets the starting position's row and column and proceeds with generating
	 * a new grid.
	 * 
	 * @param row
	 *            The row where the starting position will be placed.
	 * @param col
	 *            The column where the starting position will be placed.
	 */
	public void setStartingPosition(int row, int col) {
		startPosRow = row;
		startPosCol = col;
		generateGrid(gridRows, gridCols);
	}

	/**
	 * Sets the building status of the mouse.
	 * 
	 * @param status
	 *            0 for obstacles, 1 for starting position, 2 for end position.
	 **/
	public void setBuildingStatus(int status) {
		buildingStatus = status;
	}

	/**
	 * Sets the ending position's row and column and proceeds with generating a
	 * new grid.
	 * 
	 * @param row
	 *            The row where the ending position will be placed.
	 * @param col
	 *            The column where the ending position will be placed.
	 */
	public void setEndingPosition(int row, int col) {
		endPosRow = row;
		endPosCol = col;
		generateGrid(gridRows, gridCols);
	}

	/**
	 * Generates the grid using the values provided by gridRow and gridCol text
	 * fields in Astar class.
	 * 
	 * @param rows
	 *            The number of rows the grid has.
	 * @param cols
	 *            The number of columns the grid has.
	 */
	public void generateGrid(int rows, int cols) {
		// Resetting the previous grid
		Astar.frmAstar.remove(Astar.centerPanel);
		Astar.centerPanel = new JPanel();
		Astar.frmAstar.getContentPane().add(Astar.centerPanel, BorderLayout.CENTER);
		Astar.centerPanel.setLayout(new GridLayout(rows, cols));
		gridRows = rows;
		gridCols = cols;
		grid = new Node[rows][cols];
		tile = new Tile[rows][cols];
		closedList = new boolean[rows][cols];
		// creating a PriorityQueue to compare two nodes later one and keep the
		// one with the lowest final cost
		openList = new PriorityQueue<>((Object o1, Object o2) -> {
			Node n1 = (Node) o1;
			Node n2 = (Node) o2;

			return n1.finalCost < n2.finalCost ? -1 : n1.finalCost > n2.finalCost ? 1 : 0;
		});

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new Node(i, j);
				grid[i][j].heuristicCost = Math.abs(i - endPosRow) + Math.abs(j - endPosCol);
				createPanels(i, j);

				Astar.centerPanel.revalidate();
				Astar.centerPanel.repaint();
			}
		}

		grid[startPosRow][startPosCol].finalCost = 0;
	}

	/**
	 * Creates the panels (tiles) inside the grid while assigning to them a
	 * position (for easy access) and a mouse listener.
	 * 
	 * @param row
	 *            The row of the tile.
	 * @param col
	 *            The column of the tile.
	 */
	public void createPanels(int row, int col) {
		tile[row][col] = new Tile(row, col);
		tile[row][col].setBorder(BorderFactory.createLineBorder(Color.black));
		Astar.centerPanel.add(tile[row][col]);
		if (grid[row][col] == grid[endPosRow][endPosCol]) {
			tile[row][col].setBackground(Color.RED);
		} else if (grid[row][col] == grid[startPosRow][startPosCol]) {
			tile[row][col].setBackground(Color.GREEN);
		}

		JLabel tileCords = new JLabel(Integer.toString(row) + ":" + Integer.toString(col));
		tile[row][col].add(tileCords);

		// Added a mouse listener to allow the placement of obstacles and
		// start/end positions by clicking inside a panel.
		tile[row][col].addMouseListener(new MouseAdapter() {
			/**
			 * For the mouseClicked event to know what it should place (build)
			 * inside the panel it checks the
			 * {@link astar.Grid#setBuildingStatus(int)}.
			 * <p>
			 * 0 for obstacle, 1 for start position, 2 for end position.
			 * </p>
			 **/
			public void mouseClicked(MouseEvent e) {
				switch (buildingStatus) {
				case 0: {
					if (grid[row][col] != null) {
						if (grid[row][col] != grid[startPosRow][startPosCol]
								&& grid[row][col] != grid[endPosRow][endPosCol]) {

							setBlockedNode(row, col);
							tile[row][col].setBackground(Color.BLACK);
							tileColor = tile[row][col].getBackground();
							Astar();
						} else {
							JOptionPane.showMessageDialog(null,
									"You can not place an obstacle on top of the start/end position", "Astar",
									JOptionPane.WARNING_MESSAGE);
						}
					} else {
						grid[row][col] = new Node(row, col);
						tile[row][col].setBackground(new Color(238, 238, 238));
						Astar();
						tileColor = tile[row][col].getBackground();
					}
					break;
				}
				case 1: {
					if (grid[row][col] != grid[endPosRow][endPosCol]) {
						tile[startPosRow][startPosCol].setBackground(new Color(238, 238, 238));
						setStartingPosition(row, col);
						tile[startPosRow][startPosCol].setBackground(Color.GREEN);
						setBuildingStatus(0);
						Astar();
					} else {
						JOptionPane.showMessageDialog(null,
								"You can not place the star position on top of the end position", "Astar",
								JOptionPane.WARNING_MESSAGE);
					}
					break;
				}
				case 2: {
					if (grid[row][col] != grid[startPosRow][startPosCol]) {
						tile[endPosRow][endPosCol].setBackground(new Color(238, 238, 238));
						setEndingPosition(row, col);
						tile[endPosRow][endPosCol].setBackground(Color.RED);
						setBuildingStatus(0);
						Astar();
					} else {
						JOptionPane.showMessageDialog(null,
								"You can not place the end position on top of the start position", "Astar",
								JOptionPane.WARNING_MESSAGE);
					}
					break;
				}
				}

			}

			public void mouseEntered(MouseEvent e) {
				if (buildingStatus == 0) {
					tileColor = tile[row][col].getBackground();
					tile[row][col].setBackground(Color.BLUE);
				} else if (buildingStatus == 1) {
					tileColor = tile[row][col].getBackground();
					tile[row][col].setBackground(Color.GREEN);
				} else {
					tileColor = tile[row][col].getBackground();
					tile[row][col].setBackground(Color.RED);
				}
			}

			public void mouseExited(MouseEvent e) {

				tile[row][col].setBackground(tileColor);
			}
		});
	}

	/**
	 * Checks if the neighbourNode of the currentNode is a valid candidate to be
	 * added to the open list. If it is, it gets added and has its finalCost
	 * value updated as well as getting assigned a new parent node.
	 * 
	 * @param currentNode
	 *            The node that the neighbourNode came from.
	 * @param neighbourNode
	 *            A neighbour node of currentNode.
	 * @param cost
	 *            Current node's final cost plus movement cost (G cost).
	 */
	public void checkAndUpdateCost(Node currentNode, Node neighbourNode, int cost) {
		if (neighbourNode == null || closedList[neighbourNode.row][neighbourNode.col])
			return;
		int tempFinalCost = neighbourNode.heuristicCost + cost;

		boolean inOpen = openList.contains(neighbourNode);
		if (!inOpen || tempFinalCost < neighbourNode.finalCost) {
			neighbourNode.finalCost = tempFinalCost;
			neighbourNode.parent = currentNode;
			if (!inOpen)
				openList.add(neighbourNode);
		}
	}

	/**
	 * Using openList and closedList it checks all the neighbour nodes of the
	 * currentNode and sends the valid ones to
	 * {@link astar.Grid#checkAndUpdateCost(Node, Node, int)}.
	 * <p>
	 * If the end position is found to be the currentNode it calls
	 * {@link astar.Grid#tracePath()}.
	 * </p>
	 */
	public void Astar() {

		try {
			resetLists();

			openList.add(grid[startPosRow][startPosCol]);

			Node currentNode;

			while (true) {
				currentNode = openList.poll();

				if (currentNode == null)
					break;
				closedList[currentNode.row][currentNode.col] = true;

				if (currentNode.equals(grid[endPosRow][endPosCol])) {

					break;
				}

				Node neighbourNode;
				if (currentNode.row - 1 >= 0) {
					neighbourNode = grid[currentNode.row - 1][currentNode.col];
					checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + V_H_COST);

					if (diagonalMovement == true) {
						if (currentNode.col - 1 >= 0) {
							if (cuttingObstacles == false) {
								if (grid[currentNode.row - 1][currentNode.col] != null
										|| grid[currentNode.row][currentNode.col - 1] != null) {
									neighbourNode = grid[currentNode.row - 1][currentNode.col - 1];
									checkAndUpdateCost(currentNode, neighbourNode,
											currentNode.finalCost + DIAGONAL_COST);
								}
							} else {
								neighbourNode = grid[currentNode.row - 1][currentNode.col - 1];
								checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + DIAGONAL_COST);
							}
						}

						if (currentNode.col + 1 < grid[0].length) {
							if (cuttingObstacles == false) {
								if (grid[currentNode.row - 1][currentNode.col] != null
										|| grid[currentNode.row][currentNode.col + 1] != null) {
									neighbourNode = grid[currentNode.row - 1][currentNode.col + 1];
									checkAndUpdateCost(currentNode, neighbourNode,
											currentNode.finalCost + DIAGONAL_COST);
								}
							} else {
								neighbourNode = grid[currentNode.row - 1][currentNode.col + 1];
								checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + DIAGONAL_COST);
							}
						}
					}
				}

				if (currentNode.col - 1 >= 0) {
					neighbourNode = grid[currentNode.row][currentNode.col - 1];
					checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + V_H_COST);
				}

				if (currentNode.col + 1 < grid[0].length) {
					neighbourNode = grid[currentNode.row][currentNode.col + 1];
					checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + V_H_COST);

				}

				if (currentNode.row + 1 < grid.length) {
					neighbourNode = grid[currentNode.row + 1][currentNode.col];
					checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + V_H_COST);
					if (diagonalMovement == true) {
						if (currentNode.col - 1 >= 0) {
							if (cuttingObstacles == false) {
								if (grid[currentNode.row + 1][currentNode.col] != null
										|| grid[currentNode.row][currentNode.col - 1] != null) {
									neighbourNode = grid[currentNode.row + 1][currentNode.col - 1];
									checkAndUpdateCost(currentNode, neighbourNode,
											currentNode.finalCost + DIAGONAL_COST);
								}
							} else {
								neighbourNode = grid[currentNode.row + 1][currentNode.col - 1];
								checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + DIAGONAL_COST);
							}
						}

						if (currentNode.col + 1 < grid[0].length) {
							if (cuttingObstacles == false) {
								if (grid[currentNode.row + 1][currentNode.col] != null
										|| grid[currentNode.row][currentNode.col + 1] != null) {
									neighbourNode = grid[currentNode.row + 1][currentNode.col + 1];
									checkAndUpdateCost(currentNode, neighbourNode,
											currentNode.finalCost + DIAGONAL_COST);
								}
							} else {
								neighbourNode = grid[currentNode.row + 1][currentNode.col + 1];
								checkAndUpdateCost(currentNode, neighbourNode, currentNode.finalCost + DIAGONAL_COST);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Grid is missing! Generate a grid first!", "Astar",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		tracePath();
	}

	/**
	 * Traces the path back to the start by assigning to the currentNode its
	 * parent and paints the tiles.
	 */
	public void tracePath() {
		if (closedList[endPosRow][endPosCol]) {

			Node currentNode = grid[endPosRow][endPosCol];
			while (currentNode.parent != null) {
				currentNode = currentNode.parent;
				int nodeRow = currentNode.getRow();
				int nodeCol = currentNode.getCol();
				if (nodeRow != startPosRow || nodeCol != startPosCol) {
					tile[nodeRow][nodeCol].setBackground(Color.CYAN);
					Astar.centerPanel.revalidate();
					Astar.centerPanel.repaint();
				}
			}
		} else
			JOptionPane.showMessageDialog(null, "No possible path!!!", "Astar", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * It resets the open and closed lists to allow the recalculation of the
	 * path to include all the newly placed (or removed) obstacles.
	 */
	private void resetLists() {
		openList.clear();

		for (int i = 0; i < closedList.length; i++) {
			for (int j = 0; j < closedList[i].length; j++) {

				closedList[i][j] = false;
				if (tile[i][j].getBackground() == Color.CYAN) {
					tile[i][j].setBackground(new Color(238, 238, 238));
					Astar.centerPanel.revalidate();
					Astar.centerPanel.repaint();
				}
			}
		}
	}
}