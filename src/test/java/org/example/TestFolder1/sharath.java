package org.example.TestFolder1;

public class sharath {
    public static void main(String[] args) {
        System.out.println("Testing sharath Class");

        // Creating instances of A and B
        A classA = new A();
        B classB = new B();

        // Testing the methods
        classA.print();
        classB.printDetails();

        // Testing deprecated method
        sharath instance = new sharath();
        instance.testAnnotation();
    }

    // Deprecated method for annotation testing
    @Deprecated
    void testAnnotation() {
        System.out.println("Testing deprecated method.");
    }
}
 class A {
    private int id; // Example property

    // Example method
    public void print() {
        System.out.println("Printing from class A.");
    }
}
 class B extends A {
    // Example method
    public void printDetails() {
        System.out.println("Printing details from class B.");
    }
}


