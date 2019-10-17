package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.UUID;

public class Rack implements Object3D, Updatable, Poolable {
  public int x;
  public int y;
  int z;
  private UUID uuid;
  private String item;
  private Grid grid;
  private boolean fireUpdate;
  private RackStatus status = RackStatus.POOLED;

  @JsonCreator
  public Rack(@JsonProperty("x")
                int x,
              @JsonProperty("y")
                int y,
              @JsonProperty("z")
                int z,
              @JsonProperty("rotationX")
                int rotationX,
              @JsonProperty("rotationY")
                int rotationY,
              @JsonProperty("rotationZ")
                int rotationZ,
              @JsonProperty("uuid")
                  UUID uuid,
              @JsonProperty("item")
                String item,
              @JsonProperty("status")
                RackStatus status) {
    this.uuid = uuid;
    this.status = status;
    this.item = item;

    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Rack(String type, Grid grid) {
    this.uuid = uuid.randomUUID();
    this.item = type;
    this.grid = grid;
  }

  public void updatePosition(int x, int y) {
    fireUpdate = true;
    this.x = x;
    this.y = y;
    this.z = 0;
  }

  public void updatePosition(int z) {
    fireUpdate = true;
    this.z = z;
  }

  public String getItem() {
    return item;
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
    if (fireUpdate) {
      fireUpdate = false;
      return true;
    }
    return false;
  }

  @Override
  public boolean inUse() {
    return status == RackStatus.POOLED;
  }

  @Override
  public void putInPool() {
    updateStatus(RackStatus.POOLED);
    fireUpdate = true;
    this.z = -10;
  }

  public RackStatus getStatus() {
    return status;
  }

  public void updateStatus(RackStatus s) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      session.advanced().patch(uuid.toString(), "status", s);
      this.status = s;
      session.saveChanges();
    }
  }

  public enum RackStatus {
    POOLED,
    STORED,
    MOVING
  }
}
