package org.example.test1;

public abstract class Animal { // Abstract class (A)
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public abstract void sound(); // Abstract method

    public String getName() {
        return name;
    }
}
