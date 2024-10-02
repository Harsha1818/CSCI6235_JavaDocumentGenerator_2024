package org.example.TestFolder;

public class Manoj {
    public static void main(String args[]){
        System.out.println("Testing Manoj Class");
    }

    @Deprecated
    void testAnnotation(){
        System.out.println("Testing Annotation");
    }
}

 class A {
    private int id;
    public void print() {}
}

 class B extends A {
    public void printDetails() {}
}

