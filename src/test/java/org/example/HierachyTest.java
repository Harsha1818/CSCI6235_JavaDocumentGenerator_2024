package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HierachyTest {

    @Test
    public void testParseJavaFilesAndExtractPackageStructure() throws IOException {
        // List of files in the TestFolder
        String[] files = {
                "src/test/java/org/example/TestFolder/Animal.java",
                "src/test/java/org/example/TestFolder/Cat.java",
                "src/test/java/org/example/TestFolder/Dog.java",
                "src/test/java/org/example/TestFolder/Main.java",
                "src/test/java/org/example/TestFolder/PetOwner.java"
        };

        for (String filePath : files) {
            File javaFile = new File(filePath);
            parseAndVerifyFile(javaFile);
        }
    }

    // Helper method to parse and verify individual files
    public void parseAndVerifyFile(File javaFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            // Create a new instance of JavaParser
            JavaParser javaParser = new JavaParser();
            // Parse the file and handle ParseResult
            ParseResult<CompilationUnit> result = javaParser.parse(fis);

            // Check if parsing was successful
            assertTrue(result.isSuccessful());

            // Get the CompilationUnit (AST root node)
            Optional<CompilationUnit> cuOptional = result.getResult();
            assertTrue(cuOptional.isPresent());  // Ensure CompilationUnit is present

            CompilationUnit cu = cuOptional.get();

            // Check package declaration
            Optional<PackageDeclaration> packageDeclaration = cu.getPackageDeclaration();
            assertTrue(packageDeclaration.isPresent());  // Check that a package is declared
            assertEquals("org.example.TestFolder", packageDeclaration.get().getNameAsString());  // Ensure correct package

            // Extract and check classes/interfaces
            List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
            assertNotNull(classes);  // Ensure classes are not null
            assertFalse(classes.isEmpty());  // Ensure the file contains at least one class or interface

            // Print extracted class names (for demonstration)
            for (ClassOrInterfaceDeclaration classDecl : classes) {
                System.out.println("Class found: " + classDecl.getNameAsString());
            }

            // Check imports (dependencies)
            List<ImportDeclaration> imports = cu.getImports();
            assertNotNull(imports);  // Ensure imports are not null
            // Optionally, check if certain imports are present (based on your class design)
            for (ImportDeclaration importDecl : imports) {
                System.out.println("Import found: " + importDecl.getNameAsString());
            }
        }
    }
}
