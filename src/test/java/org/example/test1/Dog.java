package org.example.test1;

public class Dog extends Mammal implements AnimalAction { // Inheritance (<|--) and Realization (<|..)

    public Dog(String name) {
        super(name);
    }

    @Override
    public void sound() {
        System.out.println("Bark");
    }

    @Override
    public void eat(String food) {
        System.out.println(name + " eats " + food);
    }

    @Override
    public void sleep(int hours) {
        System.out.println(name + " sleeps for " + hours + " hours.");
    }
}
