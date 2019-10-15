package com.nhlstenden.amazonsimulatie.models;

public interface Poolable {
  public boolean inUse();
  public void putInPool();
}
