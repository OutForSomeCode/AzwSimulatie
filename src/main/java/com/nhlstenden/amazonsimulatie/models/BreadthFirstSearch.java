package com.nhlstenden.amazonsimulatie.models;

import java.util.*;

public class BreadthFirstSearch {
  Queue<Node> frontier = new LinkedList<Node>();
  List<Node> visited = new ArrayList<Node>();

  public BreadthFirstSearch(Node node) {
    frontier.add(node);

  }
}
