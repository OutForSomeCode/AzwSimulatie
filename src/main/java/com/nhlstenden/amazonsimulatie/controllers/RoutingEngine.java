package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Grid;
import com.nhlstenden.amazonsimulatie.models.Node;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class RoutingEngine {
  private Node start;
  private Node end;
  private Queue<Node> frontier = new LinkedList<>();
  private Deque<Node> path = new LinkedList<>();
  private HashMap<Node, Node> cameFrom = new HashMap<>();


  public Deque<Node> generateRoute(Node start, Node end) {
    frontier.clear();
    path.clear();
    cameFrom.clear();
    this.start = start;
    this.end = end;
    frontier.add(this.start);
    cameFrom.put(this.start, null);
    scanGrid();
    getPath(this.end);
    return path;
  }

  private int heuristic(Node a, Node b) {
    return Math.abs(a.getGridX() - b.getGridX()) + Math.abs(a.getGridY() - b.getGridY());
  }

  private void scanGrid() {
    Grid grid = MessageBroker.Instance().getGrid();
    while (!frontier.isEmpty()) {
      Node current = frontier.remove();
      if (current == end) {
        break;
      }
      for (Node next : grid.getNeighbours(current)) {
        if (!cameFrom.containsKey(next)) {
          if (!next.isOccupied() || next == end) {
            frontier.add(next);
            cameFrom.put(next, current);
          }
        }
      }
    }
  }

  private void getPath(Node current) {
    if (current == start || current == null)
      return;
    path.addFirst(current);
    getPath(cameFrom.get(current));
  }
}
