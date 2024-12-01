package org.example.test1;

import java.util.List;
import java.io.BufferedOutputStream;

public class Habitat { // Simple class (C)
    private String location;
    private List<Animal> animals; // Aggregation (o--)

    public Habitat(String location, List<Animal> animals) {
        this.location = location;
        this.animals = animals;
    }

    public String getLocation() {
        return location;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
}
