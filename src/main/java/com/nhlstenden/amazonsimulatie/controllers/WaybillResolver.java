package com.nhlstenden.amazonsimulatie.controllers;


import com.nhlstenden.amazonsimulatie.base.Destination;
import com.nhlstenden.amazonsimulatie.models.Resource;
import com.nhlstenden.amazonsimulatie.models.Waybill;

import java.util.ArrayList;
import java.util.List;

public class WaybillResolver implements Resource {

  // static variable single_instance of type Singleton
  private static WaybillResolver instance = null;
  private WarehouseManager warehouseManager;
  private MelkFactory melkFactory;
  private List<String> unresolvedResources = new ArrayList<>();

  // private constructor restricted to this class itself
  private WaybillResolver() {
  }

  // static method to create instance of Singleton class
  public static WaybillResolver Instance() {
    // To ensure only one instance is created
    if (instance == null)
      instance = new WaybillResolver();

    return instance;
  }

  public void Register(Resource resource) {
    if (resource instanceof WarehouseManager) {
      this.warehouseManager = (WarehouseManager) resource;
    } else if (resource instanceof MelkFactory) {
      this.melkFactory = (MelkFactory) resource;
    }
  }

  @Override
  public void RequestResource(String resource, int amount) {
    //unresolvedResources.add(resource);
    this.warehouseManager.RequestResource(resource, amount);
  }

  @Override
  public void StoreResource(Waybill waybill) {
    if (waybill.getDestination() == Destination.WAREHOUSE)
      this.warehouseManager.StoreResource(waybill);
    else if (waybill.getDestination() == Destination.MELKFACTORY)
      this.melkFactory.StoreResource(waybill);
  }
}
