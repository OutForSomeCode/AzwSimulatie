package com.nhlstenden.amazonsimulatie.models;

/*
 * contains all static data that is throughout the whole application the same
 */
public final class Data {
  public static final int tickRate = 500;

  public static final String[] cargoType = {"kaas", "donut", "boter", "melk"};
  public static final int cargoSize = 10;

  // must be dividable by 5
  public static final int modules = 5;
  public static final int moduleLength = 30;

  public static final int[] rackPositionsX = {3, 4, 7, 8, 11, 12, 15, 16, 19, 20};
  public static final int[] rackPositionsY = {0, 1, 4, 5};

  public static final int[][] rackSpawnPositions = {
    {2, 1}, {3, 1},
    {2, 2}, {3, 2},
    {2, 3}, {3, 3},
    {2, 4}, {3, 4},
    {2, 5}, {3, 5}
  };
}
