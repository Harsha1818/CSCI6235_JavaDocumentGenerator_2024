package org.example.test1.;

public class Lion extends Mammal { // Inheritance (<|--)

    public Lion(String name) {
        super(name);
    }

    @Override
    public void sound() {
        System.out.println("Roar");
    }
}
