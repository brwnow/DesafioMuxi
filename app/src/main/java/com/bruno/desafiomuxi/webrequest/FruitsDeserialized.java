package com.bruno.desafiomuxi.webrequest;

/**
 * Created by Bruno on 17/08/2017.
 */

// Useful to help deserializing the json obtained through the get request
// this is a package local class
class FruitsDeserialized {
    private Fruit[] fruits;

    public FruitsDeserialized() {
    }

    public Fruit[] getFruitsArray() {
        return fruits;
    }
}
