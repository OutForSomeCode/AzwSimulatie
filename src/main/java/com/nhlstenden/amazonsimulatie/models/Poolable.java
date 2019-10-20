package com.nhlstenden.amazonsimulatie.models;

public interface Poolable {
  boolean inUse();
  void putInPool();
}
