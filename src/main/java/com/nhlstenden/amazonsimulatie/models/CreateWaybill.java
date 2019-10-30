package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateWaybill {
    protected void createWaybill(List<Rack> racks, IDocumentSession session, Waybill.Destination destination) {
      Waybill pooledWaybill = session.query(Waybill.class)
        .whereEquals("status", Waybill.Status.POOLED)
        .firstOrDefault();
      List<String> tmp = new ArrayList<>();
      if(pooledWaybill == null){
        Waybill dump = new Waybill();
        session.store(dump);
        dump.setRacks(tmp);
        dump.setDestination(destination);
        dump.setStatus(Waybill.Status.UNRESOLVED);
      }
      else {session.store(pooledWaybill);
        pooledWaybill.setRacks(tmp);
        pooledWaybill.setDestination(destination);
        pooledWaybill.setStatus(Waybill.Status.UNRESOLVED);}
    for (Rack rack : racks) {
      rack.setStatus(Rack.Status.WAITING);
      tmp.add(rack.getId());

    }
      session.saveChanges();
  }
}
