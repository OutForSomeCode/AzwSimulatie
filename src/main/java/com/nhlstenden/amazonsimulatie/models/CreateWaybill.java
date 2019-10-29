package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateWaybill {
    protected void createWaybill(List<Rack> racks, IDocumentSession session, Waybill.Destination destination) {
    List<String> tmp = new ArrayList<>();
    for (Rack rack : racks) {
      rack.setStatus(Rack.Status.WAITING);
      tmp.add(rack.getId());
    }

    Waybill dump = new Waybill();
    session.store(dump);
    dump.setRacks(tmp);
    dump.setDestination(destination);
    dump.setStatus(Waybill.Status.UNRESOLVED);
    session.saveChanges();
  }
}
