package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.WorldModel;
import com.nhlstenden.amazonsimulatie.views.View;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/*
 * Dit is de abstracte controller class. Deze class is abstract omdat het een soort
 * standaard controller is. De class heeft standaardfunctionaliteit voor het managen
 * van een model en een aantal views. Ook kun je de controller aanzetten via de start()
 * methode. Wanneer dit gebeurd wordt een nieuwe thread opgestart waarin de controller
 * de run() methode uitvoerd. Je kunt de controller dus "runnen" binnen een aparte thread.
 * De rest van de functionaliteit, zoals wat de controller precies moet doen in de run()
 * methode, kun je later zelf nog invullen door verder te bouwen op deze class.
 */
public abstract class Controller implements Runnable, PropertyChangeListener {
  private List<View> views;
  private WorldModel worldModel;
  private Queue queue;

  public Controller(WorldModel worldModel) {
    this(worldModel, new ArrayList<View>());
  }

  public Controller(WorldModel worldModel, List<View> views) {
    this.worldModel = worldModel;
    this.worldModel.addObserver(this); //Automatisch wordt deze controller toegevoegd aan het model om updates te ontvangen.
    this.views = new ArrayList<>(views);
    this.queue = new Queue();
  }

  public void addView(View view) {
    this.views.add(view);
    this.onViewAdded(view);
  }

  /*
   * Deze methode kan later geïmplementeerd worden om aan te geven wat er moet
   * gebeuren met een view die nieuw toegevoegd wordt aan de controller.
   */
  protected abstract void onViewAdded(View view);

  /**
   * Returns the views list. Be advised that this is the internal list, for use by the controller only.
   *
   * @return The internal list of views.
   */
  protected List<View> getViews() {
    return this.views;
  }

  protected void removeView(View view) {
    this.views.remove(view);
  }

  /**
   * Returns the internal model used by the controller.
   *
   * @return The internal model.
   */
  protected WorldModel getWorldModel() {
    return this.worldModel;
  }


  protected Queue getQueue() {
    return this.queue;
  }

  /**
   * Method to start the controller in a new thread.
   */
  public final void start() {
    new Thread(this).start();
    ;
  }

  public abstract void run();
}
