package org.example.TestFolder;

import java.util.List;

public class PetOwner implements OwnerActions {
    private String ownerName;
    private List<Animal> pets;  // Aggregation: PetOwner can have multiple pets

    private PetCollar collar;

    // Constructor Overloading: Single pet version
    public PetOwner(String ownerName, Animal pet) {
        this.ownerName = ownerName;
        this.pets = List.of(pet);  // Convert single pet to a List for consistency
    }

    // Constructor Overloading: Multiple pets version
    public PetOwner(String ownerName, List<Animal> pets) {
        this.ownerName = ownerName;
        this.pets = pets;  // Aggregation: PetOwner holds a list of pets
    }

    // Composition example: PetOwner creates a PetCollar, which cannot exist independently
    public PetOwner(String ownerName, Animal pet, String collarType) {
        this.ownerName = ownerName;
        this.pets = List.of(pet);  // Convert single pet to a List for consistency
        this.collar = new PetCollar(collarType);  // Composition: collar cannot exist outside PetOwner
    }

    // Encapsulation (getter and setter methods)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<Animal> getPets() {
        return pets;
    }

    public void setPets(List<Animal> pets) {
        this.pets = pets;
    }

    // Updated method to show info for multiple pets
    @Override
    public void showPetInfo() {
        for (Animal pet : pets) {
            System.out.println(ownerName + " owns " + pet.name);
            pet.sound();
        }
    }

    // Updated method to feed multiple pets
    @Override
    public void feedPet() {
        for (Animal pet : pets) {
            System.out.println(ownerName + " is feeding " + pet.name);
            pet.eat();
        }
    }
}
