package com.nhlstenden.amazonsimulatie.controllers;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;

public class DocumentStoreHolder {

  private static IDocumentStore store;

  static {
    store = new DocumentStore(new String[]{"http://localhost:8181"}, "AmazonSimulatie");
  }

  public static IDocumentStore getStore() {
    return store;
  }

  public static String formatWtk(int x, int y) {
    return String.format("POINT (53.%03d000 6.%03d000)", x, y);
  }
}
