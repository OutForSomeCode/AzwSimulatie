package com.nhlstenden.amazonsimulatie.models;

public class Greedy {
  private Node node;
  private int priority;

  public Greedy(Node node, int priority) {
    this.node = node;
    this.priority = priority;
  }

  public Node getNode() {
    return node;
  }

  public int getPriority() {
    return priority;
  }
}
