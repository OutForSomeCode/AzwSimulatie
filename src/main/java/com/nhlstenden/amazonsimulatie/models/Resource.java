package com.nhlstenden.amazonsimulatie.models;

public interface Resource {
  public void RequestResource(String resource, int amount);
  public void StoreResource(Waybill waybill);
}
