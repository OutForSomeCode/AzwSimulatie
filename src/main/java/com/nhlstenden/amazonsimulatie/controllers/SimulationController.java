package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.CargoCrane;
import com.nhlstenden.amazonsimulatie.models.Data;
import com.nhlstenden.amazonsimulatie.models.ProxyObject3D;
import com.nhlstenden.amazonsimulatie.models.ProxyRobot3D;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;
import com.nhlstenden.amazonsimulatie.models.generated.Robot;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import com.nhlstenden.amazonsimulatie.views.View;
import net.ravendb.client.documents.session.IDocumentSession;

import java.beans.PropertyChangeEvent;

/*
 * Dit is de controller class die de simulatie beheerd. Deze class erft van
 * een generieke class Controller. Hierdoor krijgt SimulationController gratis
 * functionaliteit mee voor het managen van views en een model.
 */
public class SimulationController extends Controller {
  private WarehouseManager warehouseManager;
  private MilkFactory milkFactory;

  public SimulationController() {
    this.warehouseManager = new WarehouseManager();
    this.milkFactory = new MilkFactory();
    //WaybillResolver.Instance().Register(this.warehouseManager);
    //WaybillResolver.Instance().Register(this.melkFactory);
  }

  /*
   * Deze methode wordt aangeroepen wanneer de controller wordt gestart. De methode start een infinite
   * while-loop op in de thread van de controller. Normaal loopt een applicatie vast in een infinite
   * loop, maar omdat de controller een eigen thread heeft loopt deze loop eigenlijk naast de rest
   * van het programma. Elke keer wordt een Thread.sleep() gedaan met 100 als parameter. Dat betekent
   * 100 miliseconden rust, en daarna gaat de loop verder. Dit betekent dat ongeveer 10 keer per seconden
   * de wereld wordt geupdate. Dit is dus in feite 10 frames per seconde.
   */
  @Override
  public void run() {
    while (true) {
      this.milkFactory.update();
      this.warehouseManager.update();
      this.getQueue().flush(this.getViews());
      try {
        Thread.sleep(Data.tickRate);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void onViewAdded(final View view) {
    final Controller t = this;

    /*
     * Hier wordt een interface (Command) gebruikt om een nieuw object
     * te maken. Dit kan binnen Java en heet een anonymous inner class.
     * Op deze manier hoef je niet steeds een nieuwe class aan te maken
     * voor verschillende commando's. Zeker omdat je deze code maar één
     * keer nodig hebt.
     */
    view.onViewClose(() -> t.removeView(view));

    //ArrayList<Object3D> returnList = new ArrayList<>();
    /*
     * Dit stukje code zorgt ervoor dat wanneer een nieuwe view verbinding maakt, deze view één
     * keer alle objecten krijgt toegestuurd, ook als deze objecten niet updaten. Zo voorkom je
     * dat de view alleen objecten ziet die worden geupdate (bijvoorbeeld bewegen).
     */
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      this.getQueue().addCommandToQueue(MessageBroker.UPDATE_COMMAND, new ProxyObject3D(warehouseManager.cargoCrane));
      for (Waybill waybill : session.query(Waybill.class).whereEquals("status", Waybill.Status.RESOLVING).toList()) {
        this.getQueue().addCommandToQueue(MessageBroker.UPDATE_COMMAND, new ProxyObject3D(waybill));
      }
      for (Rack rack : session.query(Rack.class).whereNotEquals("status", Rack.Status.POOLED).toList()) {
        this.getQueue().addCommandToQueue(MessageBroker.UPDATE_COMMAND, new ProxyObject3D(rack));
      }
      for (Robot robot : session.query(Robot.class).toList()) {
        this.getQueue().addCommandToQueue(MessageBroker.UPDATE_COMMAND, new ProxyRobot3D(robot));
        if (robot.getRack() != null) {
          this.getQueue().addCommandToQueue(MessageBroker.PARENT_COMMAND,
            String.format("%s|%s", robot.getId(), robot.getRack().getId()));
        }
      }
    }

    this.getQueue().flush(view);
  }


  /*
   * Deze methode wordt aangeroepen wanneer er een update van het model binnenkomt. Zo'n "event"
   * heeft een naam en een waarde. Die worden hieronder gebruikt om een updatesignaal te sturen
   * naar de view.
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    this.getQueue().addCommandToQueue(evt.getPropertyName(), evt.getNewValue());
  }

}
