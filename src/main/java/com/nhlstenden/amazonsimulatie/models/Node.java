package com.nhlstenden.amazonsimulatie.models;

public class Node {
  private int gridX;
  private int gridY;
  private Object3D occupation;

  public Node(int x, int y) {
    this(x, y, null);
  }

  public Node(int x, int y, Object3D object) {
    gridX = x;
    gridY = y;
    occupation = object;
  }

  public void updateOccupation(Object3D object) {
    this.occupation = object;
  }

  public Object3D getOccupation() {
    return occupation;
  }

  // Overriding equals() to compare two Complex objects
  @Override
  public boolean equals(Object o) {
    // If the object is compared with itself then return true
    if (o == this) {
      return true;
    }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
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
