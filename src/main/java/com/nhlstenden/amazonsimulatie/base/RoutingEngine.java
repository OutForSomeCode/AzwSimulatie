package com.nhlstenden.amazonsimulatie.base;

import com.nhlstenden.amazonsimulatie.models.*;

import java.util.*;

public class RoutingEngine {
  private Grid grid;
  private Node start;
  private Node end;
  private Node current;
  private int priority;
  private PriorityQueue<Greedy> frontier = new PriorityQueue<>(999, new GreedyComparator());
  private Deque<Node> path = new LinkedList<>();
  private HashMap<Node, Node> cameFrom = new HashMap<>();
  public RoutingEngine(Grid grid) {
    this.grid = grid;
  }

  public Deque<Node> generateRoute(Node start, Node end) {
    frontier.clear();
    path.clear();
    cameFrom.clear();
    this.start = start;
    this.end = end;
    frontier.add(new Greedy(this.start, 0));
    cameFrom.put(this.start, null);
    scanGrid();
    getPath(this.end);
    return path;
  }

  private int heuristic(Node a, Node b) {
    return Math.abs(a.getGridX() - b.getGridX()) + Math.abs(a.getGridY() - b.getGridY());
  }

  private void scanGrid() {
    while (!frontier.isEmpty()) {
      current = frontier.remove().getNode();
      if (current == end) {
        break;
      }
      for (Node next : grid.getNeighbours(current)) {
        if (!cameFrom.containsKey(next)) {
          if (next.getOccupation() == null || next == end) {
            priority = heuristic(next, end);
            frontier.add(new Greedy(next, priority));
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
