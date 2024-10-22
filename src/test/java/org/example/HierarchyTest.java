package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class HierarchyTest {
    @Test
    public void testMethodParsing() throws IOException {
        // Expected method name in the test file
        String expectedPackageName = "org.example.TestFolder";
        String actualPackageName = new String();



        // Create instance of JavaUMLParser and parse the interface or class file
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classesAndInterfaces = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Animal.java"));

        // Loop through declarations and extract methods
        for (ClassOrInterfaceDeclaration declaration : classesAndInterfaces) {
            actualPackageName =  parser.getPackageName(declaration);
        }

        // Assert the expected method name matches the actual parsed method name
        assertEquals(expectedPackageName, actualPackageName);
    }
}
