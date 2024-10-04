package org.example.TestFolder1;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestMethodsAndProperties {
    public static void main(String[] args) {
        testClassPropertiesAndMethods(Animal.class);
        testClassPropertiesAndMethods(Cat.class);
        testClassPropertiesAndMethods(Dog.class);
        testClassPropertiesAndMethods(PetOwner.class);
        testClassPropertiesAndMethods(A.class);
        testClassPropertiesAndMethods(B.class);
        testClassPropertiesAndMethods(sharath.class);
    }

    // Method to identify properties and methods within a class using reflection
    public static void testClassPropertiesAndMethods(Class<?> className) {
        System.out.println("\n--- Class: " + className.getSimpleName() + " ---");

        // Get class fields (properties)
        Field[] fields = className.getDeclaredFields();
        if (fields.length > 0) {
            System.out.println("Properties:");
            for (Field field : fields) {
                System.out.println("- " + field.getName() + " (" + field.getType().getSimpleName() + ")");
            }
        } else {
            System.out.println("No properties found.");
        }

        // Get class methods
        Method[] methods = className.getDeclaredMethods();
        if (methods.length > 0) {
            System.out.println("Methods:");
            for (Method method : methods) {
                System.out.println("- " + method.getName() + " (Return Type: " + method.getReturnType().getSimpleName() + ")");
            }
        } else {
            System.out.println("No methods found.");
        }
    }
}
