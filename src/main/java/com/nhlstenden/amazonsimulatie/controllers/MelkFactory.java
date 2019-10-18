package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.base.Destination;
import com.nhlstenden.amazonsimulatie.models.Rack;
import com.nhlstenden.amazonsimulatie.models.Resource;
import com.nhlstenden.amazonsimulatie.models.Waybill;
import com.nhlstenden.amazonsimulatie.models.World;
import net.ravendb.client.documents.BulkInsertOperation;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

public class MelkFactory implements Resource {
  boolean flipflop;
  private int time = 0;
  private Random r = new Random();

  public void update() {
    time++;
    if (time < 50)
      return;
    flipflop = !flipflop;
    if (flipflop) {
      int ra = r.nextInt(9) + 1;
      try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
        List<Rack> pooledRacks = session.query(Rack.class)
          .whereEquals("status", Rack.RackStatus.POOLED)
          .take(ra).toList();
        if (pooledRacks.size() != ra) {
          ra -= pooledRacks.size();

          try (BulkInsertOperation bulkInsert = DocumentStoreHolder.getStore().bulkInsert()) {
            for (int i = 0; i < ra; i++) {
              Rack r = new Rack("kaas");
              bulkInsert.store(r, r.getUUID());
              pooledRacks.add(r);
            }
          }
        }
        for (Rack r : pooledRacks) {
          r.setStatus(Rack.RackStatus.WAITING);
        }
        session.saveChanges();
        Waybill dump = new Waybill(new ArrayDeque<>(pooledRacks), Destination.WAREHOUSE);
        WaybillResolver.Instance().StoreResource(dump);
      }
    } else {
      // Request goods
      WaybillResolver.Instance().RequestResource("kaas", 10);
    }
    time = 0;
  }

  @Override
  public void RequestResource(String resource, int amount) {

  }

  @Override
  public void StoreResource(Waybill waybill) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      for (Rack r : waybill.getRacks()) {
        World.Instance().getGrid().getNode(r.getX(), r.getY()).updateOccupation(false);
        r.updatePosition(50, 50, -20);
        r.setStatus(Rack.RackStatus.POOLED);
        World.Instance().updateObject(r);
      }
      session.saveChanges();
    }
  }
}
