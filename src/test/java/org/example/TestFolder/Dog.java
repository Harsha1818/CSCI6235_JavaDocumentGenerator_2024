package org.example.TestFolder;

public class Dog extends Animal {
    private String breed;

    // Constructor Overloading
    public Dog(String name) {
        super(name);
    }

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    // Method Overriding
    @Override
    public void sound() {
        System.out.println(name + " says: Woof Woof");
    }

    // Additional method (Overloading)
    public void play() {
        System.out.println(name + " is playing.");
    }

    public void play(String toy) {
        System.out.println(name + " is playing with a " + toy + ".");
    }
}
