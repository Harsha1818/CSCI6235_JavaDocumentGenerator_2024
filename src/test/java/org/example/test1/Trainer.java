package org.example.test1;

public class Trainer { // Simple class (C)
    private String trainerName;
    private Animal assignedAnimal; // Composition (*--)

    public Trainer(String trainerName, Animal assignedAnimal) {
        this.trainerName = trainerName;
        this.assignedAnimal = assignedAnimal;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public Animal getAssignedAnimal() {
        return assignedAnimal;
    }
}
