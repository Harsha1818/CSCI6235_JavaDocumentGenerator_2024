package org.example.test1;

public class Veterinarian { // Simple class (C)
    private String vetName;

    public Veterinarian(String vetName) {
        this.vetName = vetName;
    }

    public String getVetName() {
        return vetName;
    }

    // Association: A veterinarian can check up on an animal
    public void checkAnimal(Animal animal) { // Association relationship --> Animal
        System.out.println(vetName + " is checking " + animal.getName());
    }
}
