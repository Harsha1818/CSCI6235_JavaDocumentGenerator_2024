package org.example.TestFolder;

public interface OwnerActions {
    // Property (constant) in the interface
    String OWNER_NAME = "DefaultOwner"; // All fields in an interface are implicitly public, static, and final

    // Methods in the interface
    void showPetInfo(); // Display information about the pet
    void feedPet();     // New method to feed the pet
}
