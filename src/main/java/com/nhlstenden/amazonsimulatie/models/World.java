package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.controllers.DocumentStoreHolder;
import net.ravendb.client.documents.session.IDocumentSession;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class World implements WorldModel {
  // static variable single_instance of type Singleton
  private static World instance = null;
  /*
   * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
   * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
   */
  PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  /*
   * De wereld bestaat uit objecten, vandaar de naam worldObjects. Dit is een lijst
   * van alle objecten in de 3D wereld. Deze objecten zijn in deze voorbeeldcode alleen
   * nog robots. Er zijn ook nog meer andere objecten die ook in de wereld voor kunnen komen.
   * Deze objecten moeten uiteindelijk ook in de lijst passen (overerving). Daarom is dit
   * een lijst van Object3D onderdelen. Deze kunnen in principe alles zijn. (Robots, vrachrtwagens, etc)
   */
  //private List<Object3D> worldObjects;
  private Grid grid;

  /*
   * Dit onderdeel is nodig om veranderingen in het model te kunnen doorgeven aan de controller.
   * Het systeem werkt al as-is, dus dit hoeft niet aangepast te worden.
   */
  private PropertyChangeSupport controlerObserver = new PropertyChangeSupport(this);

  /*
   * De wereld maakt een lege lijst voor worldObjects aan. Daarin wordt nu één robot gestopt.
   * Deze methode moet uitgebreid worden zodat alle objecten van de 3D wereld hier worden gemaakt.
   */
  // private constructor restricted to this class itself
  private World() {
    grid = new Grid();
  }


  // static method to create instance of Singleton class
  public static World Instance() {
    // To ensure only one instance is created
    if (instance == null)
      instance = new World();

    return instance;
  }

  public void updateObject(Object3D object) {
    controlerObserver.firePropertyChange(WorldModel.UPDATE_COMMAND, null, new ProxyObject3D(object));
  }

  public Grid getGrid() {
    return grid;
  }

  /*
   * Standaardfunctionaliteit. Hoeft niet gewijzigd te worden.
   */
  @Override
  public void addObserver(PropertyChangeListener pcl) {
    controlerObserver.addPropertyChangeListener(pcl);
  }

  /*
   * Deze methode geeft een lijst terug van alle objecten in de wereld. De lijst is echter wel
   * van ProxyObject3D objecten, voor de veiligheid. Zo kan de informatie wel worden gedeeld, maar
   * kan er niks aangepast worden.
   */
  /*@Override
  public List<Object3D> getWorldObjectsAsList() {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      ArrayList<Object3D> returnList = new ArrayList<>();

      for (Object3D object : session.query(Robot.class).toList()) {
        returnList.add(new ProxyObject3D(object));
      }
      for (Object3D object : session.query(Rack.class).toList()) {
        returnList.add(new ProxyObject3D(object));
      }
      return returnList;
    }
  }*/

  /*public Rack getUnusedRack(String s) {
    try (IDocumentSession session = DocumentStoreHolder.getStore().openSession()) {
      Rack r = session.query(Rack.class)
        .whereEquals("status", Rack.RackStatus.POOLED)
        //.whereEquals("item", s)
        .firstOrDefault();
      if (r != null) {
        r.setStatus(Rack.RackStatus.WAITING);
      } else {
        r = new Rack(s);
        r.setStatus(Rack.RackStatus.WAITING);
        session.store(r, r.getUUID());
      }
      session.saveChanges();
      return r;
    }
  }

  public void addRack(String type, int x, int y) {
    Rack r = getUnusedRack(type);
    //r.updatePosition(x, y, 0);
  }*/

  public void RegisterRobot(Robot r) {
    r.registerGrid(grid);
  }

  public void addWall(int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      grid.getNode(x, y).updateOccupation(true);
    }
  }
}
