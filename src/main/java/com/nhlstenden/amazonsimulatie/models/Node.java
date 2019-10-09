package com.nhlstenden.amazonsimulatie.models;

import java.util.HashMap;

public class Node {
    private HashMap<Character, Integer> index;
    private type state;
    private Object3D occupation;
    private enum type {
        RACKSPACE,
        PARKINGSPACE,
        ROAD
    }
    public Node(HashMap index, type type) {
        this.index = index;
        this.state = type;
    }

    public void updateOccupation(Object3D object){
        this.occupation = object;
    }

    public Object3D getOccupation() {
        return occupation;
    }
}
