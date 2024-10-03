package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClassNameTest {
    static String firstClass;
    static String firstMethod;
    @Test
    public void testClassParsing() throws IOException {

        String expected = "Manoj";

        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Manoj.java"));
        firstClass = classes.get(0).getNameAsString();
        // Assert that the expected string matches the actual string
        assertEquals(expected, firstClass);
    }

    @Test
    public void testFirstMethodParsing() throws IOException {
        String expectedMethodName = "sound";  // Expected first method name in Dog class

        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Dog.java"));

        // Get the first class
        ClassOrInterfaceDeclaration dogClass = classes.get(0);

        // Retrieve all methods of the class
        List<MethodDeclaration> methods = dogClass.getMethods();

        // Get the first method name
        firstMethod = methods.get(0).getNameAsString();

        // Assert that the expected method name matches the actual first method name
        assertEquals(expectedMethodName, firstMethod);
    }


    @Test
    public void testOverrideAnnotationInDogClass() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Dog.java"));

        // Get the first class
        ClassOrInterfaceDeclaration dogClass = classes.get(0);

        // Retrieve all methods of the class
        List<MethodDeclaration> methods = dogClass.getMethods();

        // Check if the 'sound' method has the @Override annotation
        MethodDeclaration soundMethod = methods.stream()
                .filter(m -> m.getNameAsString().equals("sound"))
                .findFirst()
                .orElse(null);

        // Ensure that the method exists
        assertEquals(true, soundMethod != null);

        // Assert that the @Override annotation is present
        assertEquals(true,
                soundMethod.isAnnotationPresent(Override.class));

//        if (soundMethod.isAnnotationPresent(Override.class)) {
//            System.out.println("Dog class: The method '" + soundMethod.getNameAsString() +
//                    "' has the @Override annotation.");
//        }
    }

    @Test
    public void testOverrideAnnotationInCatClass() throws IOException {
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Cat.java"));

        // Get the first class
        ClassOrInterfaceDeclaration catClass = classes.get(0);

        // Retrieve all methods of the class
        List<MethodDeclaration> methods = catClass.getMethods();

        // Check if the 'sound' method has the @Override annotation
        MethodDeclaration soundMethod = methods.stream()
                .filter(m -> m.getNameAsString().equals("sound"))
                .findFirst()
                .orElse(null);

        // Ensure that the method exists
        assertEquals(true, soundMethod != null);

        // Assert that the @Override annotation is present
        assertEquals(true,
                soundMethod.isAnnotationPresent(Override.class));

//        if (soundMethod.isAnnotationPresent(Override.class)) {
//            System.out.println("Cat class: The method '" + soundMethod.getNameAsString() +
//                    "' has the @Override annotation." );
//        }
    }
}
