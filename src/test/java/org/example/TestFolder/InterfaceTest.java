package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class InterfaceTest {

    static String firstInterface;

    @Test
    public void testInterfaceParsing() throws IOException {
        // Expected interface name in the test file
        String expectedInterface = "OwnerActions";

        // Create instance of JavaUMLParser and parse the interface file
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classesAndInterfaces = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/OwnerActions.java"));

        // Loop through declarations to find the first interface
        for (ClassOrInterfaceDeclaration declaration : classesAndInterfaces) {
            if (declaration.isInterface()) {
                firstInterface = declaration.getNameAsString();
                assertEquals(expectedInterface,firstInterface);
                break;
            }
        }

        // Assert the expected interface name matches the actual parsed interface name
        assertEquals(expectedInterface, firstInterface);
    }
}
