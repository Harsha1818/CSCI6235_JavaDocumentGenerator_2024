package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaUMLParser {


    public static void main(String[] args) throws IOException {
        List<ClassOrInterfaceDeclaration> classList = new ArrayList<>();
        // Specify the folder containing Java files
        File folder = new File("src/main/java/org/example");
        parseFolder(folder, classList);

        // Output PlantUML diagram to a file
        try (FileWriter writer = new FileWriter("output.puml")) {
            writer.write("@startuml\n");

            // Generate PlantUML from parsed classes
            for (ClassOrInterfaceDeclaration classDecl : classList) {
                generatePlantUMLForClass(classDecl, writer);
            }

            writer.write("@enduml\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseFolder(File folder,List<ClassOrInterfaceDeclaration> classList) throws IOException {
        File[] files = folder.listFiles();

        List<ClassOrInterfaceDeclaration> parsedClasses = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    parseFolder(file,classList);  // Recursively parse subfolders
                } else if (file.getName().endsWith(".java")) {
                    parsedClasses = parseJavaFile(file);
                    for (ClassOrInterfaceDeclaration classd : parsedClasses)
                    {
                classList.add(classd);
                    }
                }
            }
        }
    }

    public static List<ClassOrInterfaceDeclaration> parseJavaFile(File javaFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            // Create an instance of JavaParser
            JavaParser javaParser = new JavaParser();
            CompilationUnit cu = javaParser.parse(fis).getResult().orElse(null);

            if (cu != null) {
                // Find all class declarations
                List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
                return classes;

            }
            else
                return (null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return (null);
    }

    /*
    generatePlantUMLForClass generates Pantuml readable file for the parsed data in .puml extension
     */
    public static void generatePlantUMLForClass(ClassOrInterfaceDeclaration classDecl, FileWriter writer) throws IOException {
        // Class name
        String className = classDecl.getNameAsString();

        // Check if the class is an interface
        String classType = classDecl.isInterface() ? "interface" : "class";
        writer.write(classType + " " + className + " {\n");

        // Fields
        for (FieldDeclaration field : classDecl.getFields()) {
            String fieldName = field.getVariables().get(0).getNameAsString();
            String fieldType = field.getCommonType().asString();
            writer.write("    + " + fieldName + " : " + fieldType + "\n");
        }

        // Methods
        for (MethodDeclaration method : classDecl.getMethods()) {
            String methodName = method.getNameAsString();
            String returnType = method.getType().asString();
            writer.write("    + " + methodName + "() : " + returnType + "\n");
        }

        // Close class/interface definition
        writer.write("}\n");

        // Handle inheritance and implementation (extends, implements)
        for (ClassOrInterfaceType extendedClass : classDecl.getExtendedTypes()) {
            writer.write(className + " <|-- " + extendedClass.getNameAsString() + "\n");
        }
        for (ClassOrInterfaceType implementedInterface : classDecl.getImplementedTypes()) {
            writer.write(className + " <|.. " + implementedInterface.getNameAsString() + "\n");
        }
    }
}
