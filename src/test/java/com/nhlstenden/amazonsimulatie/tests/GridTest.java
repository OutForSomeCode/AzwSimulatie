package com.nhlstenden.amazonsimulatie.tests;

import com.nhlstenden.amazonsimulatie.models.Grid;
import com.nhlstenden.amazonsimulatie.models.generated.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
  private Grid grid = new Grid(5, 5);

  @Test
  public void testGridSize() {
    assertEquals(5, grid.getGridSizeX());
    assertEquals(5, grid.getGridSizeY());
  }

  @Test
  public void testGridOccupation() {
    grid.getNode(2, 2).setOccupied(true);

    assertTrue(grid.getNode(2, 2).isOccupied());
    assertFalse(grid.getNode(3, 3).isOccupied());
  }

  @Test
  public void testGridGetNeighbours() {
    List<Node> ls = grid.getNeighbours(grid.getNode(0, 0));
    assertEquals(2, ls.size());

    ls = grid.getNeighbours(grid.getNode(2, 0));
    assertEquals(3, ls.size());

    ls = grid.getNeighbours(grid.getNode(2, 2));
    assertEquals(4, ls.size());
  }
}
