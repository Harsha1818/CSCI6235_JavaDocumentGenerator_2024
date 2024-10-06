package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class SubPackageTest {


    @Test
    public void testInterfaceParsing() throws IOException {
        // Expected interface name in the test file
        String expectedPackageName = "org.example.TestFolder";
        List<ClassOrInterfaceDeclaration> classList = new ArrayList<>();
        JavaUMLParser parser = new JavaUMLParser();

        classList = parser.parseFolder(new File("src/test/java/"), classList);

        String packageName = parser.getPackageName(classList.get(0));

        assertEquals(expectedPackageName, packageName);
    }
}
