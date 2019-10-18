package com.nhlstenden.amazonsimulatie.base;

import com.nhlstenden.amazonsimulatie.models.*;

import java.util.*;

public class RoutingEngine {
  private Node start;
  private Node end;
  private Node current;
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
    Grid grid = World.Instance().getGrid();
    while (!frontier.isEmpty()) {
      current = frontier.remove();
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
