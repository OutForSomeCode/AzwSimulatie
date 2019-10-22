package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rack implements Object3D {
  private int x;
  private int y;
  private int z;
  private String id;
  private String item;
  private RackStatus status;

  @JsonCreator
  public Rack(@JsonProperty("x")
                int x,
              @JsonProperty("y")
                int y,
              @JsonProperty("z")
                int z,
              @JsonProperty("item")
                String item,
              @JsonProperty("status")
                RackStatus status) {
    this.status = status;
    this.item = item;

    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Rack(String type, String id) {
    this.id = id;//UUID.randomUUID().toString();
    this.item = type;
    this.status = RackStatus.POOLED;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public RackStatus getStatus() {
    return status;
  }

  public void setStatus(RackStatus status) {
    this.status = status;
  }

  public void updatePosition(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }


  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getType() {
    return Rack.class.getSimpleName().toLowerCase();
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getZ() {
    return z;
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


  public enum RackStatus {
    WAITING,
    POOLED,
    STORED,
    MOVING
  }
}
