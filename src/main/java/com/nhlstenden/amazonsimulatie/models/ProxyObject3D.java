package com.nhlstenden.amazonsimulatie.models;

import com.nhlstenden.amazonsimulatie.models.generated.Object3D;
import com.nhlstenden.amazonsimulatie.models.generated.Rack;

/*
 * Deze class wordt gebruikt om informatie van het model aan de view te kunnen geven. Dit
 * gaat via het zogenaamde proxy design pattern. Een proxy pattern is een manier om alleen
 * de getters van een object open te stellen voor andere objecten, maar de setters niet.
 * Hieronder zie je dat ook gebeuren. De class ProxyObject3D implementeerd wel de Object3D
 * interface, maar verwijsd door naar een Object3D dat hij binnen in zich houdt. Dit
 * Object3D, met de naam object (zie code hier direct onder), kan in principe van alles zijn.
 * Dat object kan ook setters hebben of een updatemethode, etc. Deze methoden mag de view niet
 * aanroepen, omdat de view dan direct het model veranderd. Dat is niet toegestaan binnen onze
 * implementatie van MVC. Op deze manier beschermen we daartegen, omdat de view alleen maar ProxyObject3D
 * objecten krijgt. Hiermee garanderen we dat de view dus alleen objecten krijgt die alleen maar getters
 * hebben. Zouden we dit niet doen, en bijvoorbeeld een Robot object aan de view geven, dan zouden er
 * mogelijkheden kunnen zijn zodat de view toch bij de updatemethode van de robot kan komen. Deze mag
 * alleen de World class aanroepen, dus dat zou onveilige software betekenen.
 */
public class ProxyObject3D {
  private Object3D object;

  public ProxyObject3D(Object3D object) {
    this.object = object;
  }

  public String getId() {
    return this.object.getId();
  }

  public String getType() {
    return this.object.getClass().getSimpleName().toLowerCase();
  }

  public String getItem() {
    if(object instanceof Rack)
      return ((Rack) object).getItem();
    else
      return null;
  }

  public int getX() {
    return this.object.getX();
  }

  public int getY() {
    return this.object.getY();
  }

  public int getZ() {
    return this.object.getZ();
  }

  public int getRotationX() {
    return this.object.getRotationX();
  }

  public int getRotationY() {
    return this.object.getRotationY();
  }

  public int getRotationZ() {
    return this.object.getRotationZ();
  }
}
