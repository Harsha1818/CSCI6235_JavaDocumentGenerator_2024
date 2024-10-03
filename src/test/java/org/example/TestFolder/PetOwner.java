package org.example.TestFolder;

public class PetOwner implements OwnerActions {
    private String ownerName;
    private Animal pet;

    // Constructor Overloading
    public PetOwner(String ownerName) {
        this.ownerName = ownerName;
    }

    public PetOwner(String ownerName, Animal pet) {
        this.ownerName = ownerName;
        this.pet = pet;
    }

    // Encapsulation (getter and setter methods)
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Animal getPet() {
        return pet;
    }

    public void setPet(Animal pet) {
        this.pet = pet;
    }

    // Implementation of the interface method: showPetInfo
    @Override
    public void showPetInfo() {
        System.out.println(ownerName + " owns " + pet.name);
        pet.sound();
    }

    // Implementation of the interface method: feedPet
    @Override
    public void feedPet() {
        System.out.println(ownerName + " is feeding " + pet.name);
        pet.eat();
    }
}
