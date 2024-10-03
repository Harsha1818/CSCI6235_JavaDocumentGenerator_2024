package org.example.TestFolder;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.example.JavaUMLParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class MethodTest {

    static String firstMethod;

    @Test
    public void testMethodParsing() throws IOException {
        // Expected method name in the test file
        String expectedMethod = "sound";

        // Create instance of JavaUMLParser and parse the interface or class file
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classesAndInterfaces = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Animal.java"));

        // Loop through declarations and extract methods
        for (ClassOrInterfaceDeclaration declaration : classesAndInterfaces) {
            List<MethodDeclaration> methods = declaration.getMethods();

            // If methods exist, capture the first method
            if (!methods.isEmpty()) {
                firstMethod = methods.get(0).getNameAsString();
                assertEquals(expectedMethod,firstMethod);
                break;
            }
        }

        // Assert the expected method name matches the actual parsed method name
        assertEquals(expectedMethod, firstMethod);
    }
}
