package com.nhlstenden.amazonsimulatie.models;

public final class Data {
  public static final String[] cargoType = {"kaas", "donut", "boter", "melk"};
  public static final int cargoSize = 10;

  public static final int modules = 10;
  public static final int[] rackPositionsX = {3, 4, 7, 8, 11, 12, 15, 16, 19, 20};

  public static final int[][] truckpost = {
    {2, 1}, {3, 1},
    {2, 2}, {3, 2},
    {2, 3}, {3, 3},
    {2, 4}, {3, 4},
    {2, 5}, {3, 5}
  };
}
