package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RobotPOJO {

  private String id;
  private Robot.RobotStatus status;
  private Rack rack;
  private Waybill waybill;
  private Integer x = 0;
  private Integer y = 0;
  private Integer z = 0;

  @JsonCreator
  public RobotPOJO(@JsonProperty("id")
                     String id,
                   @JsonProperty("rack")
                     Rack rack,
                   @JsonProperty("status")
                     Robot.RobotStatus status,
                   @JsonProperty("waybill")
                     Waybill waybill,
                   @JsonProperty("x")
                     int x,
                   @JsonProperty("y")
                     int y,
                   @JsonProperty("z")
                     int z) {
    this.id = id;
    this.rack = rack;
    this.status = status;
    this.waybill = waybill;

    this.x = x;
    this.y = y;
    this.z = z;

  }

  public RobotPOJO(String id) {
    this.id = id;
    this.status = Robot.RobotStatus.IDLE;
  }

  public RobotPOJO(String id, int x, int y) {
    this(id);
    this.x = x;
    this.y = y;
  }

  @JsonProperty("waybill")
  public Waybill getWaybill() {
    return waybill;
  }

  @JsonProperty("waybill")
  public void setWaybill(Waybill waybill) {
    this.waybill = waybill;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void getId(String uuid) {
    this.id = uuid;
  }

  @JsonProperty("status")
  public Robot.RobotStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Robot.RobotStatus status) {
    this.status = status;
  }

  @JsonProperty("rack")
  public Rack getRack() {
    return rack;
  }

  @JsonProperty("rack")
  public void setRack(Rack rack) {
    this.rack = rack;
  }

  @JsonProperty("x")
  public Integer getX() {
    return x;
  }

  @JsonProperty("x")
  public void setX(Integer x) {
    this.x = x;
  }

  @JsonProperty("y")
  public Integer getY() {
    return y;
  }

  @JsonProperty("y")
  public void setY(Integer y) {
    this.y = y;
  }

  @JsonProperty("z")
  public Integer getZ() {
    return z;
  }

  @JsonProperty("z")
  public void setZ(Integer z) {
    this.z = z;
  }

}
