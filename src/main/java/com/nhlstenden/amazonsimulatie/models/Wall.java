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
  public int getX() {
    return 0;
  }

  @Override
  public int getY() {
    return 0;
  }

  @Override
  public int getZ() {
    return 0;
  }

  @Override
  public int getRotationX() {
    return 0;
  }

  @Override
  public int getRotationY() {
    return 0;
  }

  @Override
  public int getRotationZ() {
    return 0;
  }
}
