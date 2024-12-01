package org.example.test1;

public class Mammal extends Animal { // Inheritance (<|--)

    public Mammal(String name) {
        super(name);
    }

    @Override
    public void sound() {
        System.out.println("Generic mammal sound");
    }
}
