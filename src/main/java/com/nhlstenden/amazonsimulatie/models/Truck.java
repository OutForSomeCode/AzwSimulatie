package com.nhlstenden.amazonsimulatie.models;

import java.util.List;
import java.util.UUID;

public class Truck implements Object3D, Updatable{
  private UUID uuid;
  private List<Rack> cargo;

  private int x;
  private int y;
  private int z;

  private int rotationX = 0;
  private int rotationY = 0;
  private int rotationZ = 0;

  public Truck() {
    this.uuid = UUID.randomUUID();
  }

  public List<Rack> getCargo() {
    return cargo;
  }

  public void setCargo(List<Rack> cargo) {
    this.cargo = cargo;
  }

  @Override
  public String getUUID() {
    return this.uuid.toString();
  }

  @Override
  public String getType() {
    return Truck.class.getSimpleName().toLowerCase();
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
    return rotationX;
  }

  @Override
  public double getRotationY() {
    return rotationY;
  }

  @Override
  public double getRotationZ() {
    return rotationZ;
  }

  @Override
  public boolean update() {
    return false;
  }
}
