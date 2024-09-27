package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
                // Path to your Java file
                String javaFilePath = "src/main/java/org/example/Manoj.java"; // Adjust path as necessary

                // Create an instance of JavaParser
                JavaParser parser = new JavaParser();
                try {
                    CompilationUnit cu = parser.parse(new File(javaFilePath)).getResult().orElseThrow();

                    // Extract and print class names from the CompilationUnit
                    cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                        System.out.println("Class name from Manoj.java: " + cls.getNameAsString());
                    });
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }