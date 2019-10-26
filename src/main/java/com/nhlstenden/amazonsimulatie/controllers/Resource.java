package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.generated.Waybill;

public interface Resource {
  void RequestResource(String resource, int amount);

  void StoreResource(Waybill waybill);
}
