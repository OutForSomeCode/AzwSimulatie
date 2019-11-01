package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.CreateWaybill;
import com.nhlstenden.amazonsimulatie.models.Data;
import com.nhlstenden.amazonsimulatie.models.Factory;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MilkFactory extends CreateWaybill implements Factory {
  private boolean flipFlop;
  private int time = 0;
  private Random ran = new Random();

  @Override
  public void update() {
    time++;
    if (time < 30)
      return;
    flipFlop = !flipFlop;
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      int storedRacks = session.query(Rack.class).whereEquals("status", Rack.Status.STORED).count();
      if (flipFlop) {
        if (storedRacks < (Data.modules * 30))
          sendWaybill();
      } else {
        if (storedRacks > (Data.modules * 20))
          requestWaybill();
      }
    }
    time = 0;
  }

  @Override
  public void sendWaybill() {
    int numberOfRacks = ran.nextInt(5) + 5;
    /*List<String> tmp = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      tmp.add("kaas");
    }*/
    this.createWaybill(numberOfRacks,"kaas", Waybill.Destination.WAREHOUSE);

  }

  @Override
  public void requestWaybill() {
    int numberOfRacks = ran.nextInt(5) + 5;
    /*List<String> tmp = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      tmp.add("kaas");
    }*/
    this.createWaybill(numberOfRacks,"kaas", Waybill.Destination.MELKFACTORY);
  }
}

