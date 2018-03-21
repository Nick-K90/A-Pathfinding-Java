package astar;
/**
 * <h1>Node</h1>
 * The Node class allows for the creation of nodes that store all the information needed for the calculation of the path.
 * It provides the heuristicCost (0 by default), finalCost (0 by default) and the row/column of the node.
 * It also provides a parent node to all nodes in order to trace back the path.
 * @author Nikolaos Kouroumalos
 * @version 1.0
 */
public class Node {

	int heuristicCost = 0;
	int finalCost = 0;
	int row, col; // position
	Node parent; // Storing the parent nodes to trace back the path the start

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
