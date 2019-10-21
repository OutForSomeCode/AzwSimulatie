package com.nhlstenden.amazonsimulatie.models;

import java.util.Comparator;

public class GreedyComparator implements Comparator<Greedy> {

  @Override
  public int compare(Greedy g1, Greedy g2) {
    return Integer.compare(g1.getPriority(), g2.getPriority());
  }
}
