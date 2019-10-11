package com.nhlstenden.amazonsimulatie.models;

import java.util.HashMap;

public class Node {
  private int gridX;
  private int gridZ;
  private Object3D occupation;

  public Node(int x, int y) {
    this(x,y,null);
  }

  public Node(int x, int y, Object3D object) {
    gridX = x;
    gridZ = y;
    occupation = object;
  }

  public void updateOccupation(Object3D object) {
    this.occupation = object;
  }

  public Object3D getOccupation() {
    return occupation;
  }

  public int getGridX() {
    return gridX;
  }

  public int getGridY() {
    return gridZ;
  }
}
