package com.nhlstenden.amazonsimulatie.models;

import java.util.ArrayList;
import java.util.List;

public class Grid {
  private int gridSizeX = 24;
  private int gridSizeY;
  private Node[][] grid;

  public Grid(int modules) {
    this.gridSizeY = (6 * modules);
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
    List<Node> neighbours = new ArrayList<Node>();
    for(int x = -1; x <=1; x++){
      for(int y = -1; y <= 1; y++){
        if(x != 0 && y != 0){
          int checkX = (node.getGridX() + x);
          int checkY = (node.getGridY() + y);
          if(checkX >= 0 && checkX < gridSizeX && checkY >= 0 && checkY < gridSizeY) {
            neighbours.add(grid[checkX][checkY]);
          }
        }
      }
    }
    return neighbours;
  }
}
