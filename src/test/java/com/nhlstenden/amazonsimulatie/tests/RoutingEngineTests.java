package com.nhlstenden.amazonsimulatie.tests;

import com.nhlstenden.amazonsimulatie.controllers.RoutingEngine;
import com.nhlstenden.amazonsimulatie.models.Grid;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import org.junit.Test;

import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoutingEngineTests {
  @Test
  public void testRouting() {
    Grid grid = new Grid(5, 5);

    RoutingEngine r = new RoutingEngine(grid);

    Deque<Node> k = r.generateRoute(grid.getNode(0, 0), grid.getNode(4, 4));

    assertEquals(8, k.size());

    assertEquals(grid.getNode(0, 1), k.pop());
    assertEquals(grid.getNode(1, 1), k.pop());
    assertEquals(grid.getNode(2, 1), k.pop());
  }
}
