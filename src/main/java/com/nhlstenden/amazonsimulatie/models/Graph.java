package com.nhlstenden.amazonsimulatie.models;

public class Graph {
    private boolean adjacencyMatrix[][];
    private int numberOfVertices;

    public Graph(int numberOfVertices){
        this.numberOfVertices = numberOfVertices;
        adjacencyMatrix = new boolean[numberOfVertices][numberOfVertices];
    }

    public void addEdge(int from, int to){
        adjacencyMatrix[from][to] = true;
        adjacencyMatrix[to][from] = true;
    }

    public void addOnWayEdge(int from, int to){
        adjacencyMatrix[from][to] = true;
        adjacencyMatrix[to][from] = false;
    }

    public void removeEdge(int from, int to){
        adjacencyMatrix[from][to] = false;
        adjacencyMatrix[to][from] = false;
    }

    public boolean isEdge(int from, int to){
        return adjacencyMatrix[from][to];
    }
}
