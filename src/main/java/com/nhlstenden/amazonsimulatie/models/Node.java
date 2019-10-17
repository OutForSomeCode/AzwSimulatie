package com.nhlstenden.amazonsimulatie.models;

public class Node {
  private int gridX;
  private int gridY;
  private boolean isOccupied;

  public Node(int x, int y) {
    this(x, y, false);
  }

  public Node(int x, int y, boolean object) {
    gridX = x;
    gridY = y;
    isOccupied = object;
  }

  public void updateOccupation(boolean yes) {
    this.isOccupied = yes;
  }

  public boolean isOccupied() {
    return isOccupied;
  }

  // Overriding equals() to compare two Complex objects
  @Override
  public boolean equals(Object o) {
    // If the object is compared with itself then return true
    if (o == this) {
      return true;
    }

    // Check if o is an instance of Complex or not
    //"null instanceof [type]" also returns false
    if (!(o instanceof Node)) {
      return false;
    }

    // typecast o to Complex so that we can compare data members
    Node c = (Node) o;

    // Compare the data members and return accordingly
    return Double.compare(gridX, c.gridX) == 0
      && Double.compare(gridY, c.gridY) == 0;
  }

  public int getGridX() {
    return gridX;
  }

  public int getGridY() {
    return gridY;
  }
}
