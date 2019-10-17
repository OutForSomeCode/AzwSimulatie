package com.nhlstenden.amazonsimulatie.controllers;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;

public class DocumentStoreHolder {

  private static IDocumentStore store;

  static {
    store = new DocumentStore(new String[]{ "http://localhost:8181" }, "AmazonSimulatie");
  }

  public static IDocumentStore getStore() {
    return store;
  }
}
