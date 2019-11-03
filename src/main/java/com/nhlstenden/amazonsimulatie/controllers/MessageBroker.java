package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Data;
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

  /*
   * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
   * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
   */
  private PropertyChangeSupport controlerObserver = new PropertyChangeSupport(this);

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

  //to parent and unparent the rack to the robot for transport to its drop location
  public void parentObject(String uuid1, String uuid2) {
    controlerObserver.firePropertyChange(PARENT_COMMAND, null, String.format("%s|%s", uuid1, uuid2));
  }

  public void unparentObject(String uuid1, String uuid2) {
    controlerObserver.firePropertyChange(UNPARENT_COMMAND, null, String.format("%s|%s", uuid1, uuid2));
  }

  /*
   * Standaardfunctionaliteit. Hoeft niet gewijzigd te worden.
   */
  public void addObserver(PropertyChangeListener pcl) {
    controlerObserver.addPropertyChangeListener(pcl);
  }
}
