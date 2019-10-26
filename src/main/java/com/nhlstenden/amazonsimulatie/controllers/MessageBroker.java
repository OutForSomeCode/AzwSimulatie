package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Grid;
import com.nhlstenden.amazonsimulatie.models.ProxyObject3D;
import com.nhlstenden.amazonsimulatie.models.generated.Object3D;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class MessageBroker {
  static final String UPDATE_COMMAND = "update";
  static final String PARENT_COMMAND = "parent";
  private static final String UNPARENT_COMMAND = "unparent";

  // static variable single_instance of type Singleton
  private static MessageBroker instance = null;

  private Grid grid;

  /*
   * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
   * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
   */
  private PropertyChangeSupport controlerObserver = new PropertyChangeSupport(this);


  // private constructor restricted to this class itself
  private MessageBroker() {
    grid = new Grid();
  }


  // static method to create instance of Singleton class
  public static MessageBroker Instance() {
    // To ensure only one instance is created
    if (instance == null)
      instance = new MessageBroker();

    return instance;
  }

  public void updateObject(Object3D object) {
    controlerObserver.firePropertyChange(UPDATE_COMMAND, null, new ProxyObject3D(object));
  }

  public void parentObject(String uuid1, String uuid2) {
    controlerObserver.firePropertyChange(PARENT_COMMAND, null, String.format("%s|%s", uuid1, uuid2));
  }

  public void unparentObject(String uuid1, String uuid2) {
    controlerObserver.firePropertyChange(UNPARENT_COMMAND, null, String.format("%s|%s", uuid1, uuid2));
  }

  public Grid getGrid() {
    return grid;
  }

  /*
   * Standaardfunctionaliteit. Hoeft niet gewijzigd te worden.
   */
  public void addObserver(PropertyChangeListener pcl) {
    controlerObserver.addPropertyChangeListener(pcl);
  }


  public void addWall(int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      grid.getNode(x, y).updateOccupation(true);
    }
  }
}
