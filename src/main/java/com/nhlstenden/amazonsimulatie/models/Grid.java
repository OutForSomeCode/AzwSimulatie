package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.base.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
  private final int[][] checkInts = {
    {0, 1},
    {1, 0},
    {0, -1},
    {-1, 0}
  };
  private int gridSizeX = 30;
  private int gridSizeY;
  private World world;
  private Node[][] grid;

  public Grid(World world) {
    this.world = world;
    this.gridSizeY = (6 * Data.modules);
    createGrid();
  }

  private void createGrid() {
    grid = new Node[gridSizeX][gridSizeY];

    for (int x = 0; x < gridSizeX; x++) {
      for (int y = 0; y < gridSizeY; y++) {
        grid[x][y] = new Node(x, y, false);
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
  public World getWorld() {
    return world;
  }
}
