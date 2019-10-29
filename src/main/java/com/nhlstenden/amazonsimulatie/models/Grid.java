package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Node;

import java.util.ArrayList;
import java.util.List;

public class Grid {
  private final int[][] checkInts = {
    {0, 1},
    {1, 0},
    {0, -1},
    {-1, 0}
  };
  private int gridSizeX;//= 30;
  private int gridSizeY;
  private Node[][] grid;

  public Grid(int gridSizeX, int gridSizeY) {
    this.gridSizeX = gridSizeX;
    this.gridSizeY = gridSizeY;//(6 * Data.modules);
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

  public int getGridSizeX() {
    return gridSizeX;
  }

  public int getGridSizeY() {
    return gridSizeY;
  }

  public Node getNode(int x, int y) {
    return grid[x][y];
  }
}
