package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.CreateWaybill;
import com.nhlstenden.amazonsimulatie.models.Data;
import com.nhlstenden.amazonsimulatie.models.Factory;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

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
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> pooledRacks = session.query(Rack.class)
        .whereEquals("status", Rack.Status.POOLED)
        .take(numberOfRacks).toList();
      if (pooledRacks.size() <= numberOfRacks) {
        numberOfRacks -= pooledRacks.size();

        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int i = 0; i < numberOfRacks; i++) {
            Rack rack = new Rack();
            rack.setItem("kaas");
            rack.setStatus(Rack.Status.WAITING);
            bulkInsert.store(rack);
            pooledRacks.add(rack);
          }
        }
      }

      this.createWaybill(pooledRacks, session, Waybill.Destination.WAREHOUSE);
    }
  }

  @Override
  public void requestWaybill() {
    int numberOfRacks = ran.nextInt(5) + 5;
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> storedRacks = session.query(Rack.class)
        .whereEquals("status", Rack.Status.STORED)
        .take(numberOfRacks).toList();

      this.createWaybill(storedRacks, session, Waybill.Destination.MELKFACTORY);
    }
  }
}
