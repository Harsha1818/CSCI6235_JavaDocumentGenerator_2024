package org.example.TestFolder;

// New class to demonstrate composition
public class PetCollar {
    private String collarType;

    public PetCollar(String collarType) {
        this.collarType = collarType;
    }

    public String getCollarType() {
        return collarType;
    }

    public void setCollarType(String collarType) {
        this.collarType = collarType;
    }

    public void showCollarInfo() {
        System.out.println("The pet has a " + collarType + " collar.");
    }
}
