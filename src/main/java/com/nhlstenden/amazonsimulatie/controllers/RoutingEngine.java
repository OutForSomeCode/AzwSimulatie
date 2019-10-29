package com.nhlstenden.amazonsimulatie.controllers;

import com.nhlstenden.amazonsimulatie.models.Data;
import com.nhlstenden.amazonsimulatie.models.Grid;
import com.nhlstenden.amazonsimulatie.models.generated.Node;

import java.util.*;

public class RoutingEngine {
  private Grid grid;
  private Node start;
  private Node end;
  private PriorityQueue<Greedy> frontier = new PriorityQueue<>(180 * Data.modules, new GreedyComparator());
  private Deque<Node> path = new LinkedList<>();
  private HashMap<Node, Node> cameFrom = new HashMap<>();
  private HashMap<Node, Integer> currentCost = new HashMap<>();


  public RoutingEngine(Grid grid) {
    this.grid = grid;
  }

  public Deque<Node> generateRoute(Node start, Node end) {
    frontier.clear();
    path.clear();
    cameFrom.clear();
    currentCost.clear();
    this.start = start;
    this.end = end;
    frontier.add(new Greedy(this.start, 0));
    cameFrom.put(this.start, null);
    currentCost.put(this.start, 0);
    scanGrid();
    getPath(this.end);
    return path;
  }

  private int heuristic(Node a, Node b) {
    return Math.abs(a.getGridX() - b.getGridX()) + Math.abs(a.getGridY() - b.getGridY());
  }

  private void scanGrid() {
    while (!frontier.isEmpty()) {
      Node current = frontier.remove().getNode();
      if (current == end) {
        break;
      }
      for (Node next : grid.getNeighbours(current)) {
        if (!next.isOccupied() || next == end) {
          int newCost = currentCost.get(current) + 1;

          if (!currentCost.containsKey(next) || newCost < currentCost.get(next)) {
            int priority = newCost + heuristic(next, end);
            currentCost.put(next, newCost);
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

  public class Greedy {
    private Node node;
    private int priority;

    Greedy(Node node, int priority) {
      this.node = node;
      this.priority = priority;
    }

    public Node getNode() {
      return node;
    }

    int getPriority() {
      return priority;
    }
  }

  public class GreedyComparator implements Comparator<Greedy> {

    @Override
    public int compare(Greedy g1, Greedy g2) {
      return Integer.compare(g1.getPriority(), g2.getPriority());
    }
  }

}
