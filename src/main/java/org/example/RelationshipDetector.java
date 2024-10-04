package org.example;

import com.github.javaparser.ast.body.FieldDeclaration;

public class RelationshipDetector {

    /*
    Helper method to detect if a field is a composition
    Composition means the contained object cannot exist without the parent.
    */
    public static boolean isComposition(FieldDeclaration field) {
        return isCustomClass(field) && !isCollection(field);
    }

    /*
    Helper method to detect if a field is an aggregation
    Aggregation means the contained object can exist independently.
    */
    public static boolean isAggregation(FieldDeclaration field) {
        return isCollection(field);
    }

    /*
    Helper method to detect if a field is an association
    Association is a simple reference between classes with no strong lifecycle binding.
    */
    public static boolean isAssociation(FieldDeclaration field) {
        return isPrimitiveType(field) || isExternalReference(field);
    }

    /*
    Utility method to check if a field is a collection
    (which would likely represent an aggregation).
    */
    private static boolean isCollection(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return fieldType.contains("List") || fieldType.contains("Set") || fieldType.contains("Map");
    }

    /*
    Utility method to check if a field is a primitive type
    */
    private static boolean isPrimitiveType(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return fieldType.equals("int") || fieldType.equals("double") || fieldType.equals("boolean") || fieldType.equals("char") || fieldType.equals("float")
                || fieldType.equals("long") || fieldType.equals("byte") || fieldType.equals("short") || fieldType.equals("String");
    }

    /*
    Utility method to check if a field is an external reference
    (for example, a class from standard libraries or third-party libraries).
    */
    private static boolean isExternalReference(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        // Check if the field type belongs to a common external package (like java.lang or other libraries).
        return fieldType.startsWith("java.") || fieldType.startsWith("javax.") || fieldType.equals("String");
    }

    /*
    Utility method to check if a field is a custom class
    (i.e., belongs to the user's project and not external libraries).
    */
    private static boolean isCustomClass(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        // Assuming custom classes are within the project namespace (adjust this based on your project)
        return !isPrimitiveType(field) && !isExternalReference(field);
    }
}
