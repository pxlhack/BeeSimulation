package com.simulation.Bee;

import java.io.Serializable;
import java.util.ArrayList;

public class BeePackage implements Serializable {
    private ArrayList<SerializableBee> serializableBeeArrayList;

    public void setSerializableBeeArrayList(ArrayList<SerializableBee> serializableBeeArrayList) {
        this.serializableBeeArrayList = new ArrayList<>(serializableBeeArrayList);
    }

    public ArrayList<SerializableBee> getSerializableBeeArrayList() {
        return serializableBeeArrayList;
    }
}
