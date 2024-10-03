package org.example;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class InterfaceMethodsAndPropertiesTest {

    @Test
    public void testInterfaceMethodsAndProperties() throws IOException {
        // The expected method and property in the interface
        String expectedMethod = "showPetInfo";
        String expectedProperty = "OWNER_NAME";

        // Variables to store the found method and property names
        String foundMethod = null;
        String foundProperty = null;

        // Create instance of JavaUMLParser and parse the interface file
        JavaUMLParser parser = new JavaUMLParser();
        List<ClassOrInterfaceDeclaration> classesAndInterfaces = parser.parseJavaFile(new File("/Users/harshatumuluri/CSCI6235_JavaDocumentGenerator_2024/src/test/java/org/example/TestFolder/OwnerActions.java"));

        // Find the first interface declaration
        ClassOrInterfaceDeclaration interfaceDeclaration = null;
        for (ClassOrInterfaceDeclaration declaration : classesAndInterfaces) {
            if (declaration.isInterface()) {
                interfaceDeclaration = declaration;
                break;
            }
        }

        // Check that the interface was found
        assertNotNull("No interface found", interfaceDeclaration);

        // Validate methods within the interface
        List<MethodDeclaration> methods = interfaceDeclaration.getMethods();
        boolean methodFound = false;
        for (MethodDeclaration method : methods) {
            if (method.getNameAsString().equals(expectedMethod)) {
                methodFound = true;
                foundMethod = method.getNameAsString();  // Store the found method name
                break;
            }
        }
        assertTrue("Method '" + expectedMethod + "' not found in interface", methodFound);

        // Validate properties (fields) within the interface
        List<FieldDeclaration> fields = interfaceDeclaration.getFields();
        boolean propertyFound = false;
        for (FieldDeclaration field : fields) {
            String propertyName = field.getVariables().get(0).getNameAsString();
            if (propertyName.equals(expectedProperty)) {
                propertyFound = true;
                foundProperty = propertyName;  // Store the found property name
                break;
            }
        }
        assertTrue("Property '" + expectedProperty + "' not found in interface", propertyFound);

        // Print the found method and property names at the end
        System.out.println("Found method: " + foundMethod);
        System.out.println("Found property: " + foundProperty);
    }
}
