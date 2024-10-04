package org.example.TestFolder1;


    public class Cat extends Animal {

        // Constructor
        public Cat(String name) {
            super(name);
        }

        // Method Overriding
        @Override
        public void sound() {
            System.out.println(name + " says: Meow");
        }
    }


