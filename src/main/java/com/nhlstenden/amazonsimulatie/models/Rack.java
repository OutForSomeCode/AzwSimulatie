package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.UUID;

public class Rack implements Object3D, Poolable {
  private int x;
  private int y;
  private int z;
  private UUID uuid;
  private String item;
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

  public Rack(String type) {
    this.uuid = uuid.randomUUID();
    this.item = type;
  }

  public void updatePosition(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;

    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack r = session.load(Rack.class, uuid.toString());
      r.x = x;
      r.y = y;
      r.z = z;
      session.saveChanges();
    }
    World.Instance().updateObject(this);
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
  public boolean inUse() {
    return status == RackStatus.POOLED;
  }

  @Override
  public void putInPool() {
    updateStatus(RackStatus.POOLED);
    updatePosition(x, y, -10);
  }

  public void setStatus(RackStatus status) {
    this.status = status;
  }

  public RackStatus getStatus() {
    return status;
  }

  public void updateStatus(RackStatus s) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      this.status = s;
      session.advanced().patch(uuid.toString(), "status", s);
      session.saveChanges();
    }
  }

  public enum RackStatus {
    WAITING,
    POOLED,
    STORED,
    MOVING
  }
}
