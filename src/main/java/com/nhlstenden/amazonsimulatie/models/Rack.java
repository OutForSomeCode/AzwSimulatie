package com.nhlstenden.amazonsimulatie.models;

import java.util.UUID;

public class Rack implements Object3D, Updatable {
    private UUID uuid;
    private String item;
    //private String[] type = {"kaas", "doos", "boter"};

    public Rack(String type) {
        this.uuid = uuid.randomUUID();
        this.item = type;
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

  @Override
  public boolean update() {
    return false;
  }
}
