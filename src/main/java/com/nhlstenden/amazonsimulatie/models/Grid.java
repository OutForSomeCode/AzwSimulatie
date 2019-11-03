package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Node;

import java.util.ArrayList;
import java.util.List;

/*
 * creates a grid filled with nodes containing x and y coordinates
 */
public class Grid {
  private final int[][] checkInts = {
    {0, 1},
    {1, 0},
    {0, -1},
    {-1, 0}
  };
  private int gridSizeX;
  private int gridSizeY;
  private Node[][] grid;

  public Grid(int gridSizeX, int gridSizeY) {
    this.gridSizeX = gridSizeX;
    this.gridSizeY = gridSizeY;
    createGrid();
  }

  private void createGrid() {
    grid = new Node[gridSizeX][gridSizeY];

    for (int x = 0; x < gridSizeX; x++) {
      for (int y = 0; y < gridSizeY; y++) {
        Node n = new Node();
        n.setGridX(x);
        n.setGridY(y);
        grid[x][y] = n;
      }
    }
  }

  // returns all direct non diagonally neighboring nodes
  public List<Node> getNeighbours(Node node) {
    List<Node> neighbours = new ArrayList<>();
    for (int[] vec : checkInts) {
      int checkX = (node.getGridX() + vec[0]);
      int checkY = (node.getGridY() + vec[1]);
      if (checkX >= 0 && checkX < gridSizeX && checkY >= 0 && checkY < gridSizeY) {
        neighbours.add(grid[checkX][checkY]);
      }
    }
    return neighbours;
  }

  // sets a node to occupied
  public void addWall(int x, int y) {
    if (x < gridSizeX && y < gridSizeY) {
      getNode(x, y).setOccupied(true);
    }
  }

  // returns x length of the grid
  public int getGridSizeX() {
    return gridSizeX;
  }

  // returns y length of the grid
  public int getGridSizeY() {
    return gridSizeY;
  }

  // returns the node on the given coordinates
  public Node getNode(int x, int y) {
    return grid[x][y];
  }
}
