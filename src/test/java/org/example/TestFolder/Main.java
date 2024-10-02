package org.example.TestFolder;

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

        // Encapsulation with PetOwner class
        PetOwner owner1 = new PetOwner("John", myPet);
        owner1.showPetInfo();  // Should print info about "Buddy"

        // Polymorphism with Cat
        myPet = cat1;
        owner1.setPet(myPet);
        owner1.showPetInfo();  // Should print info about "Whiskers"
    }
}
