package org.example.TestFolder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Creating instances of Dog and Cat
        Dog dog1 = new Dog("Buddy", "Golden Retriever");
        Cat cat1 = new Cat("Whiskers");

        // Polymorphism - Animal reference pointing to Dog object
        Animal myPet = dog1;
        myPet.sound();  // Should print "Buddy says: Woof Woof"

        // Method Overloading
        dog1.play();
        dog1.play("ball");

        // Encapsulation with PetOwner class - Single Pet
        PetOwner owner1 = new PetOwner("John", myPet);
        owner1.showPetInfo();  // Should print info about "Buddy"

        // Composition: PetOwner creates a collar that cannot exist independently
        PetOwner owner2 = new PetOwner("Jane", cat1, "Leather");
        owner2.showPetInfo();  // Should print info about "Whiskers" and the collar

        // Aggregation - Multiple pets for one owner
        List<Animal> petList = List.of(dog1, cat1);  // Creating a list of multiple pets
        PetOwner owner3 = new PetOwner("Alice", petList);
        owner3.showPetInfo();  // Should print info about both "Buddy" and "Whiskers"

        // Polymorphism with Cat
        myPet = cat1;
        owner1.setPets(List.of(myPet));  // Update owner1's pets to use a List
        owner1.showPetInfo();  // Should print info about "Whiskers"
    }
}
