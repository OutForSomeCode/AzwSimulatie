package com.nhlstenden.amazonsimulatie.models;

public interface Resource {
  void RequestResource(String resource, int amount);

  void StoreResource(Waybill waybill);
}
