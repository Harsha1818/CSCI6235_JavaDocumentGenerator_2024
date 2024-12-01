package org.example.test1;

import java.util.List;

public class Zoo { // Simple class (C)
    private String zooName;
    private List<Habitat> habitats; // Aggregation (o--)

    public Zoo(String zooName, List<Habitat> habitats) {
        this.zooName = zooName;
        this.habitats = habitats;
    }

    public String getZooName() {
        return zooName;
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }
}
