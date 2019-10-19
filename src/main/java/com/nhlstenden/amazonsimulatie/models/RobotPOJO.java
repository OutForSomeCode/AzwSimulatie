package com.nhlstenden.amazonsimulatie.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RobotPOJO {

  @JsonProperty("uuid")
  private String uuid;
  @JsonProperty("status")
  private Robot.RobotStatus status;
  @JsonProperty("rackUUID")
  private String rackUUID;
  @JsonProperty("x")
  private Integer x = 0;
  @JsonProperty("y")
  private Integer y = 0;
  @JsonProperty("z")
  private Integer z = 0;

  @JsonCreator
  public RobotPOJO(@JsonProperty("uuid")
                     String uuid,
                   @JsonProperty("rackUUID")
                     String rackUUID,
                   @JsonProperty("status")
                     Robot.RobotStatus status,
                   @JsonProperty("x")
                     int x,
                   @JsonProperty("y")
                     int y,
                   @JsonProperty("z")
                     int z) {
    this.uuid = uuid;
    this.rackUUID = rackUUID;
    this.status = status;

    this.x = x;
    this.y = y;
    this.z = z;

  }

  public RobotPOJO(String uuid) {
    this.uuid = uuid;
    this.status = Robot.RobotStatus.IDLE;
  }

  public RobotPOJO(String uuid, int x, int y) {
    this(uuid);
    this.x = x;
    this.y = y;
  }

  @JsonProperty("uuid")
  public String getUUID() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void getUUID(String uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("status")
  public Robot.RobotStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Robot.RobotStatus status) {
    this.status = status;
  }

  @JsonProperty("rackUUID")
  public String getRackUUID() {
    return rackUUID;
  }

  @JsonProperty("rackUUID")
  public void setRackUUID(String rackUUID) {
    this.rackUUID = rackUUID;
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
