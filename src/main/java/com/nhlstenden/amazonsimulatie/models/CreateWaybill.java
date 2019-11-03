package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.session.IDocumentSession;


/*
 * creates and fills a waybill with the given data and stores it in the database
 */
public abstract class CreateWaybill {
  protected void createWaybill(int racksAmount, String racksType, Waybill.Destination destination) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Waybill pooledWaybill = session.query(Waybill.class)
        .whereEquals("status", Waybill.Status.POOLED)
        .firstOrDefault();
      if (pooledWaybill == null) {
        Waybill dump = new Waybill();
        session.store(dump);
        //dump.setRacks(racks);
        dump.setRacksAmount(racksAmount);
        dump.setRacksType(racksType);
        dump.setDestination(destination);
        dump.setStatus(Waybill.Status.UNRESOLVED);
      } else {
        session.store(pooledWaybill);
        //pooledWaybill.setRacks(racks);
        pooledWaybill.setRacksAmount(racksAmount);
        pooledWaybill.setRacksType(racksType);
        pooledWaybill.setDestination(destination);
        pooledWaybill.setStatus(Waybill.Status.UNRESOLVED);
      }
      session.saveChanges();
    }
  }
}
