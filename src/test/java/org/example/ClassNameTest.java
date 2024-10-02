package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClassNameTest {
    static String firstClass;
    @Test
    public void testClassParsing() throws IOException {

        String expected = "Manoj";

        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classes = parser.parseJavaFile(new File("src/test/java/org/example/TestFolder/Manoj.java"));
        firstClass = classes.get(0).getNameAsString();
        // Assert that the expected string matches the actual string
        assertEquals(expected, firstClass);
    }
}
