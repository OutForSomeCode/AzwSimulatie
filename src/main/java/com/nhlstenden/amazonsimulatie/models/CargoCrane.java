package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import com.nhlstenden.amazonsimulatie.controllers.MessageBroker;
import com.nhlstenden.amazonsimulatie.controllers.WarehouseManager;
import com.nhlstenden.amazonsimulatie.models.generated.Object3D;
import com.nhlstenden.amazonsimulatie.models.generated.Waybill;
import net.ravendb.client.documents.session.IDocumentSession;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

/*
 * instead of a truck we use a cargo crane to get the racks in and out the warehouse
 */
public class CargoCrane extends Object3D {
  private int currentPos = this.getY();
  private ContainerData currentContainer;
  private int destination;
  private int tweenTime;
  private ContainerData.Task containerTask;
  private WarehouseManager warehouseManager;
  private Queue<ContainerData> containers = new LinkedList<>();

  public CargoCrane(WarehouseManager warehouseManager) {
    this.setId(UUID.randomUUID().toString());
    this.setX(27);
    this.setZ(8);
    this.setY(0);
    this.warehouseManager = warehouseManager;
  }

  // adds a container to the list of containers the crane has to handle
  public void addContainer(ContainerData.Task placingContainer, String waybillId, int loadingBay) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      // get the given waybill
      Waybill waybill = session.load(Waybill.class, waybillId);
      waybill.setStatus(Waybill.Status.MOVING);
      waybill.setLoadingBay(loadingBay);
      containers.add(new ContainerData(placingContainer, waybill));
      if (!containers.isEmpty() && containerTask == null)
        setContainer();

      session.saveChanges();
    }
  }

  // get the first container from the list and get the information out of it
  private void setContainer() {
    currentContainer = containers.remove();
    currentPos = this.getY();
    destination = (currentContainer.getWaybill().getLoadingBay() * 6);
    containerTask = currentContainer.isPlacingContainer();
    tweenTime = Math.abs(currentPos - destination) * Data.tickRate;
    this.setRotationZ(tweenTime);

    this.setY(destination);
    MessageBroker.Instance().updateObject(this);
  }

  // update the crane position and give the command to load/unload the container when destination is reached
  public void update() {
    if (currentPos != destination) {
      if (currentPos < destination)
        currentPos++;
      if (currentPos > destination)
        currentPos--;
    } else {
      if (containerTask != ContainerData.Task.MOVING && containerTask != null) {
        warehouseManager.processWaybill(currentContainer.getWaybill().getId(), destination);
        containerTask = null;
        if (!containers.isEmpty()) {
          setContainer();
        }
      }
    }
  }

  // container data class
  public static class ContainerData {
    private Task task;
    private Waybill waybill;

    ContainerData(Task task, Waybill waybill) {
      this.task = task;
      this.waybill = waybill;
    }

    Task isPlacingContainer() {
      return task;
    }

    Waybill getWaybill() {
      return waybill;
    }

    public enum Task {
      PICKUP,
      DROP,
      MOVING
    }
  }
}
