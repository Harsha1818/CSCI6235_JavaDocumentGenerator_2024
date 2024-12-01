package org.example;

import com.github.javaparser.ast.body.FieldDeclaration;

public class RelationshipDetector {

    // Detect if a field is a composition
    public static boolean isComposition(FieldDeclaration field) {
        return isCustomClass(field) && field.isPrivate() && !isCollection(field);
    }

    // Detect if a field is an aggregation
    public static boolean isAggregation(FieldDeclaration field) {
        return isCustomClass(field) && isCollection(field);
    }

    // Detect if a field is an association
    public static boolean isAssociation(FieldDeclaration field) {
        return isCustomClass(field) && !isCollection(field);
    }

    // Check if a field is a collection (e.g., List, Set, Map)
    public static boolean isCollection(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return fieldType.contains("List") || fieldType.contains("Set") || fieldType.contains("Map")
                || fieldType.contains("Queue") || fieldType.contains("Stack");
    }

    // Check if a field is a primitive type (e.g., int, String, boolean)
    public static boolean isPrimitiveType(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return isPrimitiveType(fieldType);
    }

    // Overload: Check if a type is a primitive type (String input)
    public static boolean isPrimitiveType(String fieldType) {
        return fieldType.equals("int") || fieldType.equals("double") || fieldType.equals("boolean")
                || fieldType.equals("char") || fieldType.equals("float") || fieldType.equals("long")
                || fieldType.equals("byte") || fieldType.equals("short") || fieldType.equals("String");
    }

    // Check if a field is an external reference (e.g., java.lang, javax, javafx)
    public static boolean isExternalReference(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return isExternalReference(fieldType);
    }

    // Overload: Check if a type is an external reference (String input)
    public static boolean isExternalReference(String fieldType) {
        return fieldType.startsWith("java.") || fieldType.startsWith("javax.") || fieldType.startsWith("javafx.")
                || fieldType.equals("Object");
    }

    // Check if a field is a custom class (i.e., defined by the user)
    public static boolean isCustomClass(FieldDeclaration field) {
        String fieldType = field.getCommonType().asString();
        return isCustomClass(fieldType);
    }

    // Overload: Check if a type is a custom class (String input)
    public static boolean isCustomClass(String fieldType) {
        return !isPrimitiveType(fieldType) && !isExternalReference(fieldType)
                && Character.isUpperCase(fieldType.charAt(0));
    }
}
