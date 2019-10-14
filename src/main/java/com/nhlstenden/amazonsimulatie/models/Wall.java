package com.nhlstenden.amazonsimulatie.models;

import java.util.UUID;

public class Wall implements Object3D {
  private UUID uuid;

  public Wall() {
    this.uuid = UUID.randomUUID();
  }

  @Override
  public String getUUID() {
    return uuid.toString();
  }

  @Override
  public String getType() {
    return Wall.class.getSimpleName().toLowerCase();
  }

  @Override
  public double getX() {
    return 0;
  }

  @Override
  public double getY() {
    return 0;
  }

  @Override
  public double getZ() {
    return 0;
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
}
