package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MelkFactory {
  private boolean flipflop;
  private int time = 0;
  private Random ran = new Random();

  public void update() {
    time++;
    if (time < 10)
      return;
    flipflop = !flipflop;
    if (flipflop) {
      sendWaybill();
    } else {
      // Request goods
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        int s = session.query(Rack.class).whereEquals("status", Rack.Status.STORED).count();
        if (s > 150)
          requestWaybill();
      }

    }
    time = 0;
  }

  void sendWaybill() {
    int ra = ran.nextInt(9) + 1;
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> pooledRacks = session.query(Rack.class)
        .whereEquals("status", Rack.Status.POOLED)
        .take(ra).toList();
      if (pooledRacks.size() <= ra) {
        ra -= pooledRacks.size();

        try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
          for (int i = 0; i < ra; i++) {
            Rack r = new Rack();
            r.setItem("kaas");
            r.setStatus(Rack.Status.WAITING);
            bulkInsert.store(r);
            pooledRacks.add(r);
          }
        }
      }

      List<String> tmp = new ArrayList<>();
      for (Rack r : pooledRacks) {
        r.setStatus(Rack.Status.WAITING);
        tmp.add(r.getId());
      }

      Waybill dump = new Waybill();
      session.store(dump);
      dump.setRacks(tmp);
      dump.setDestination(Waybill.Destination.WAREHOUSE);
      dump.setStatus(Waybill.Status.UNRESOLVED);
      session.saveChanges();
    }
  }

  void requestWaybill() {
    int ra = ran.nextInt(9) + 1;
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      List<Rack> storedRacks = session.query(Rack.class)
        .whereEquals("status", Rack.Status.STORED)
        .take(ra).toList();

      List<String> tmp = new ArrayList<>();
      for (Rack r : storedRacks) {
        r.setStatus(Rack.Status.WAITING);
        tmp.add(r.getId());
      }

      Waybill dump = new Waybill();
      session.store(dump);
      dump.setRacks(tmp);
      dump.setDestination(Waybill.Destination.MELKFACTORY);
      dump.setStatus(Waybill.Status.UNRESOLVED);
      session.saveChanges();
    }
  }

}
