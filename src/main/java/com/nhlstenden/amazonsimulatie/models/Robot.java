package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.base.RoutingsEngine;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.UUID;

/*
 * Deze class stelt een robot voor. Hij impelementeerd de class Object3D, omdat het ook een
 * 3D object is. Ook implementeerd deze class de interface Updatable. Dit is omdat
 * een robot geupdate kan worden binnen de 3D wereld om zich zo voort te bewegen.
 */
public class Robot implements Object3D, Updatable {
  private RoutingsEngine routingsEngine;
  private UUID uuid;
  private ArrayList<RobotTask> taskQueue = new ArrayList<>();
  private RobotTask currentTask;
  private Deque<Node> pathToTask = new LinkedList<>();
  ;

  private int x = 0;
  private int y = 0;
  private int z = 0;

  private int rotationX = 0;
  private int rotationY = 0;
  private int rotationZ = 0;

  public Robot(Grid grid) {
    this.uuid = UUID.randomUUID();
    routingsEngine = new RoutingsEngine(grid);
  }

  public Robot(Grid grid, int x, int y) {
    this(grid);
    this.x = x;
    this.z = y;
  }

  public void assignTask(ArrayList<RobotTask> tasks) {
    taskQueue = tasks;
    executeNextTask();
  }

  private void executeNextTask() {
    currentTask = taskQueue.remove(0);
    pathToTask = routingsEngine.generateRoute(new Node(x, z), currentTask.getDestination());
  }

  public boolean isBusy() {
    return !taskQueue.isEmpty();
  }

  /*
   * Deze update methode wordt door de World aangeroepen wanneer de
   * World zelf geupdate wordt. Dit betekent dat elk object, ook deze
   * robot, in de 3D wereld steeds een beetje tijd krijgt om een update
   * uit te voeren. In de updatemethode hieronder schrijf je dus de code
   * die de robot steeds uitvoert (bijvoorbeeld positieveranderingen). Wanneer
   * de methode true teruggeeft (zoals in het voorbeeld), betekent dit dat
   * er inderdaad iets veranderd is en dat deze nieuwe informatie naar de views
   * moet worden gestuurd. Wordt false teruggegeven, dan betekent dit dat er niks
   * is veranderd, en de informatie hoeft dus niet naar de views te worden gestuurd.
   * (Omdat de informatie niet veranderd is, is deze dus ook nog steeds hetzelfde als
   * in de view)
   */
  @Override
  public boolean update() {
    if (!pathToTask.isEmpty()) {
      Node current = pathToTask.remove();
      this.x = current.getGridX();
      this.z = current.getGridY();
      return true;
    }
    if (!taskQueue.isEmpty()) {
      executeNextTask();
    }
    return false;
  }

  @Override
  public String getUUID() {
    return this.uuid.toString();
  }

  @Override
  public String getType() {
    /*
     * Dit onderdeel wordt gebruikt om het type van dit object als stringwaarde terug
     * te kunnen geven. Het moet een stringwaarde zijn omdat deze informatie nodig
     * is op de client, en die verstuurd moet kunnen worden naar de browser. In de
     * javascript code wordt dit dan weer verder afgehandeld.
     */
    return Robot.class.getSimpleName().toLowerCase();
  }

  @Override
  public double getX() {
    return this.x;
  }

  @Override
  public double getY() {
    return this.y;
  }

  @Override
  public double getZ() {
    return this.z;
  }

  @Override
  public double getRotationX() {
    return this.rotationX;
  }

  @Override
  public double getRotationY() {
    return this.rotationY;
  }

  @Override
  public double getRotationZ() {
    return this.rotationZ;
  }
}
