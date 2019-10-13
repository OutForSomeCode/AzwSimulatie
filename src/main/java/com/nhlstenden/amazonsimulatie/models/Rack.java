package com.nhlstenden.amazonsimulatie.models;

import java.util.UUID;

public class Rack implements Object3D, Updatable {
  private UUID uuid;
  private Type item;
  private Grid grid;

  private int x;
  private int y;
  private int z;

  public Rack(Type type, Grid grid, int x, int y) {
    this.uuid = uuid.randomUUID();
    this.item = type;
    this.grid = grid;
    this.x = x;
    this.z = y;
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
    return y;
  }

  @Override
  public double getZ() {
    return z;
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
    return true;
  }

  public enum Type {
    KAAS,
    DOOS,
    BOTER,
    DONUT
  }
}
