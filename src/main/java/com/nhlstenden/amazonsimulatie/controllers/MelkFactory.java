package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Destination;
import com.nhlstenden.amazonsimulatie.models.Rack;
import com.nhlstenden.amazonsimulatie.models.Resource;
import com.nhlstenden.amazonsimulatie.models.Waybill;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.*;

public class MelkFactory implements Resource {
  boolean flipflop;
  private int time = 0;
  private Random ran = new Random();

  public void update() {
    time++;
    if (time < 10)
      return;
    flipflop = !flipflop;
    if (flipflop) {
      int ra = ran.nextInt(9) + 1;
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        List<Rack> pooledRacks = session.query(Rack.class)
          .whereEquals("status", Rack.RackStatus.POOLED)
          .take(ra).toList();
        if (pooledRacks.size() <= ra) {
          ra -= pooledRacks.size();

          try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
            for (int i = 0; i < ra; i++) {
              Rack r = new Rack("kaas", UUID.randomUUID().toString());
              r.setStatus(Rack.RackStatus.WAITING);
              bulkInsert.store(r, r.getUUID());
              pooledRacks.add(r);
            }
          }
        }

        List<String> tmp = new ArrayList<>();
        for (Rack r : pooledRacks) {
          r.setStatus(Rack.RackStatus.WAITING);
          tmp.add(r.getUUID());
        }
        session.saveChanges();
        Waybill dump = new Waybill(new ArrayDeque<>(tmp), Destination.WAREHOUSE);
        WaybillResolver.Instance().StoreResource(dump);
      }
    } else {
      // Request goods
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        int s = session.query(Rack.class).whereEquals("status", Rack.RackStatus.STORED).count();
        if (s > 100)
          WaybillResolver.Instance().RequestResource("kaas", ran.nextInt(30));
      }

    }
    time = 0;
  }

  @Override
  public void RequestResource(String resource, int amount) {

  }

  @Override
  public void StoreResource(Waybill waybill) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      for (String r : waybill.getRacks()) {
        Rack rack = session.load(Rack.class, r);
        MessageBroker.Instance().getGrid().getNode(rack.getX(), rack.getY()).updateOccupation(false);
        rack.updatePosition(rack.getX(), rack.getY(), -5);
        rack.setStatus(Rack.RackStatus.POOLED);
        MessageBroker.Instance().updateObject(rack);
      }
      session.saveChanges();
    }
  }
}
