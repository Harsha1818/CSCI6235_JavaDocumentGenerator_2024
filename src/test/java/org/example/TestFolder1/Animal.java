package org.example.TestFolder1;

public abstract class Animal {
    protected String name;

    // Abstract method to be implemented by subclasses
    public abstract void sound();

    // Non-abstract method
    public void eat() {
        System.out.println(name + " is eating.");
    }

    // Constructor
    public Animal(String name) {
        this.name = name;
    }
}
