package com.nhlstenden.amazonsimulatie.models;

import java.util.UUID;

public class Rack implements Object3D, Updatable, Poolable {
  private UUID uuid;
  private String item;
  private Grid grid;
  private boolean fireUpdate;
  private boolean inUse;

  public int x;
  public int y;
  int z;

  public Rack(String type, Grid grid) {
    this.uuid = uuid.randomUUID();
    this.item = type;
    this.grid = grid;
  }

  public void updatePosition(int x, int y) {
    inUse = true;
    fireUpdate = true;
    this.x = x;
    this.y = y;
    this.z = 0;
  }
  public void updatePosition(int z) {
    inUse = true;
    fireUpdate = true;
    this.z = z;
  }

  @Override
  public String getUUID() {
    return uuid.toString();
  }

  @Override
  public String getType() {
    return Rack.class.getSimpleName().toLowerCase();
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return z;
  }

  @Override
  public double getZ() {
    return y;
  }

  @Override
  public double getRotationX() {
    return 0;
  }

  @Override
  public double getRotationY() {
    return 0;
  }

  @Override
  public double getRotationZ() {
    return 0;
  }

  @Override
  public boolean update() {
    if (fireUpdate) {
      fireUpdate = false;
      return true;
    }
    return false;
  }

  @Override
  public boolean inUse() {
    return inUse;
  }

  @Override
  public void putInPool() {
    inUse = false;
    fireUpdate = true;
    this.z = -10;
  }
}
