package com.nhlstenden.amazonsimulatie.models;

import java.util.HashMap;
import java.util.List;

public class Grid {
    private int gridX = 24;
    private int gridZ;
    private Node[][] grid;

    public Grid(int modules) {
        this.gridZ = (6 * modules);
    }

    public void createGrid(List<Object3D> worldObjects){
        grid = new Node[gridX][gridZ];

        for(int x = 0; x < gridX; x++){
            for(int z = 0; z < gridZ; z++){
              Robot r = new Robot(x, z);
              worldObjects.add(r);
                grid[x][z] = new Node(x, z, r);
            }
        }
    }
}
