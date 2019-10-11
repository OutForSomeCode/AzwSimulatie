package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//new Node(r.nextInt(5), r.nextInt(5)
public class Grid {
  private int gridSizeX = 42;
  private int gridSizeY = 42;
  private Node[][] grid;
  private Random r = new Random();

  private final int[] checkints = {-1, 1};

  public Grid(int modules) {
    //this.gridSizeY = (6 * modules);
    createGrid();
  }

  private void createGrid() {
    grid = new Node[gridSizeX][gridSizeY];

    for (int x = 0; x < gridSizeX; x++) {
      for (int y = 0; y < gridSizeY; y++) {
        grid[x][y] = new Node(x, y, null);
      }
    }
  }

  public List<Node> getNeighbours(Node node) {
    List<Node> neighbours = new ArrayList<>();
    for (int x : checkints) {
      for (int y : checkints) {
        int checkX = (node.getGridX() + x);
        int checkY = (node.getGridY() + y);
        if (checkX >= 0 && checkX < gridSizeX && checkY >= 0 && checkY < gridSizeY) {
          neighbours.add(grid[checkX][checkY]);
        }
      }
    }
    return neighbours;
  }

  public Node RandomNode() {
    return grid[r.nextInt(gridSizeX)][r.nextInt(gridSizeY)];
  }
}
