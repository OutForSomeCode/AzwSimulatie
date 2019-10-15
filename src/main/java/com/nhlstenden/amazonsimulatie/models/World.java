package com.nhlstenden.amazonsimulatie.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Deze class is een versie van het model van de simulatie. In dit geval is het
 * de 3D wereld die we willen modelleren (magazijn). De zogenaamde domain-logic,
 * de logica die van toepassing is op het domein dat de applicatie modelleerd, staat
 * in het model. Dit betekent dus de logica die het magazijn simuleert.
 */
public class World implements WorldModel {
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
  private List<Object3D> worldObjects;
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
  public World() {
    this.worldObjects = new ArrayList<>();
    grid = new Grid();
  }

  public Robot findIdleRobot() {
    List<Robot> returnList = getRobots();

    for (Robot robot : returnList) {
      if (!robot.inUse())
        return robot;
    }
    return null;
  }

  public void addRobot(int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      this.worldObjects.add(new Robot(grid, x, y));
    }
  }

  public List<Robot> getRobots() {
    List<Robot> returnList = new ArrayList<>();

    for (Object3D object : this.worldObjects) {
      if (object instanceof Robot)
        returnList.add((Robot) object);
    }
    return returnList;
  }

  public List<Rack> getRacks() {
    List<Rack> returnList = new ArrayList<>();

    for (Object3D object : this.worldObjects) {
      if (object instanceof Rack)
        returnList.add((Rack) object);
    }
    return returnList;
  }

  public List<Rack> getUsedRacks() {
    List<Rack> returnList = new ArrayList<>();

    for (Object3D object : this.worldObjects) {
      if (object instanceof Rack && ((Rack) object).inUse())
        returnList.add((Rack) object);
    }
    return returnList;
  }

  public Rack findUnusedRack() {
    List<Rack> returnList = getRacks();

    for (Rack rack : returnList) {
      if (!rack.inUse())
        return rack;
    }
    return null;
  }

  public void addRack(String type, int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      Rack rack = findUnusedRack();
      if (rack == null) {
        rack = new Rack(type, grid);
        this.worldObjects.add(rack);
      }
      rack.updatePosition(x, y);
      grid.getNode(x, y).updateOccupation(rack);
    }
  }

  public Rack getUnusedRack(String type) {
    Rack rack = findUnusedRack();
    if (rack == null) {
      rack = new Rack(type, grid);
      this.worldObjects.add(rack);
    }
    return rack;
  }

  public void removeRack(int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      Rack r = (Rack) grid.getNode(x, y).getOccupation();
      r.putInPool();
      grid.getNode(x, y).updateOccupation(null);
    }
  }

  public void addWall(int x, int y) {
    if (x < grid.getGridSizeX() && y < grid.getGridSizeY()) {
      grid.getNode(x, y).updateOccupation(new Wall());
    }
  }

  /*
   * Deze methode wordt gebruikt om de wereld te updaten. Wanneer deze methode wordt aangeroepen,
   * wordt op elk object in de wereld de methode update aangeroepen. Wanneer deze true teruggeeft
   * betekent dit dat het onderdeel daadwerkelijk geupdate is (er is iets veranderd, zoals een positie).
   * Als dit zo is moet dit worden geupdate, en wordt er via het pcs systeem een notificatie gestuurd
   * naar de controller die luisterd. Wanneer de updatemethode van het onderdeel false teruggeeft,
   * is het onderdeel niet veranderd en hoeft er dus ook geen signaal naar de controller verstuurd
   * te worden.
   */
  @Override
  public void update() {
    for (Object3D object : this.worldObjects) {
      if (object instanceof Updatable) {
        if (((Updatable) object).update()) {
          controlerObserver.firePropertyChange(WorldModel.UPDATE_COMMAND, null, new ProxyObject3D(object));
        }
      }
    }
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
  @Override
  public List<Object3D> getWorldObjectsAsList() {
    ArrayList<Object3D> returnList = new ArrayList<>();

    for (Object3D object : this.worldObjects) {
      returnList.add(new ProxyObject3D(object));
    }

    return returnList;
  }
}
