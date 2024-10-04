package org.example;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RelationTest {

    @Test
    public void testComposition() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/PetOwner.java"));

        ClassOrInterfaceDeclaration petOwnerClass = classes.get(0);
        FieldDeclaration[] fields = petOwnerClass.getFields().toArray(new FieldDeclaration[0]);

        // Check for the presence of PetCollar in the fields
        boolean hasPetCollarField = false;
        for (FieldDeclaration field : fields) {
            String fieldType = field.getCommonType().asString();
            if (fieldType.equals("PetCollar")) {
                hasPetCollarField = true;
                break;
            }
        }

        assertTrue("PetOwner should have a composition with PetCollar", hasPetCollarField);
    }

    @Test
    public void testAggregation() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/PetOwner.java"));

        ClassOrInterfaceDeclaration petOwnerClass = classes.get(0);
        FieldDeclaration[] fields = petOwnerClass.getFields().toArray(new FieldDeclaration[0]);

        // Check for the presence of List<Animal> in the fields
        boolean hasAnimalField = false;
        for (FieldDeclaration field : fields) {
            String fieldType = field.getCommonType().asString();
            if (fieldType.contains("List<Animal>")) {
                hasAnimalField = true;
                break;
            }
        }

        assertTrue("PetOwner should have aggregation with Animal", hasAnimalField);
    }

    @Test
    public void testAssociation() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/PetOwner.java"));

        ClassOrInterfaceDeclaration petOwnerClass = classes.get(0);
        FieldDeclaration[] fields = petOwnerClass.getFields().toArray(new FieldDeclaration[0]);

        // Check for the presence of String in the fields
        boolean hasStringField = false;
        for (FieldDeclaration field : fields) {
            String fieldType = field.getCommonType().asString();
            if (fieldType.equals("String")) {
                hasStringField = true;
                break;
            }
        }

        assertTrue("PetOwner should have an association with String", hasStringField);
    }

    @Test
    public void testRealization() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/PetOwner.java"));

        ClassOrInterfaceDeclaration petOwnerClass = classes.get(0);
        NodeList<ClassOrInterfaceType> implementedInterfaces = petOwnerClass.getImplementedTypes();

        // Check if the PetOwner implements OwnerActions
        boolean implementsOwnerActions = false;
        for (ClassOrInterfaceType interfaceType : implementedInterfaces) {
            if (interfaceType.getNameAsString().equals("OwnerActions")) {
                implementsOwnerActions = true;
                break;
            }
        }

        assertTrue("PetOwner should realize OwnerActions", implementsOwnerActions);
    }

    @Test
    public void testInheritance() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();

        // Test for Cat class
        List<ClassOrInterfaceDeclaration> classesCat = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Cat.java"));
        ClassOrInterfaceDeclaration catClass = classesCat.get(0);
        NodeList<ClassOrInterfaceType> extendedClassesCat = catClass.getExtendedTypes();

        // Check if Cat extends Animal
        boolean extendsAnimalCat = extendedClassesCat.stream()
                .anyMatch(extendedClass -> extendedClass.getNameAsString().equals("Animal"));

        assertTrue("Cat should extend Animal", extendsAnimalCat);

        // Test for Dog class
        List<ClassOrInterfaceDeclaration> classesDog = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Dog.java"));
        ClassOrInterfaceDeclaration dogClass = classesDog.get(0);
        NodeList<ClassOrInterfaceType> extendedClassesDog = dogClass.getExtendedTypes();

        // Check if Dog extends Animal
        boolean extendsAnimalDog = extendedClassesDog.stream()
                .anyMatch(extendedClass -> extendedClass.getNameAsString().equals("Animal"));

        assertTrue("Dog should extend Animal", extendsAnimalDog);
    }

}
