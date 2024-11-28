package org.example;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaUMLParser {

    public static void main(String args[]) throws IOException {
//        List<ClassOrInterfaceDeclaration> classList = new ArrayList<>();

        // Specify the folder containing Java files
//        File folder = new File("/Users/manojsrinivasa/Desktop/Projects/CSCI6235_JavaDocumentGenerator_2024/src");
//        File folder = new File(args[0]);
//        parseFolder(folder, classList);
//
//        // Output PlantUML diagram to a file
//        try (FileWriter writer = new FileWriter("output1.puml")) {
//            writer.write("@startuml\n");
//
//            // Generate PlantUML from parsed classes
//            for (ClassOrInterfaceDeclaration classDecl : classList) {
//                generatePlantUMLForClass(classDecl, writer);
//            }
//
//            writer.write("@enduml\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    String starter(String path) throws IOException {
        List<ClassOrInterfaceDeclaration> classList = new ArrayList<>();
        File folder = new File(path);
        File outputFile = new File("output.puml");
        try {
            parseFolder(folder, classList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Output PlantUML diagram to a file
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write("@startuml\n");

            // Generate PlantUML from parsed classes
            for (ClassOrInterfaceDeclaration classDecl : classList) {
                generatePlantUMLForClass(classDecl, writer);
            }

            writer.write("@enduml\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFile.getAbsolutePath();
    }

    public static List<ClassOrInterfaceDeclaration> parseFolder(File folder, List<ClassOrInterfaceDeclaration> classList) throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    parseFolder(file, classList);  // Recursively parse subfolders
                } else if (file.getName().endsWith(".java")) {
                    List<ClassOrInterfaceDeclaration> parsedClasses = parseJavaFile(file);
                    classList.addAll(parsedClasses);
                }
            }
        }
        return classList;
    }

    public static List<ClassOrInterfaceDeclaration> parseJavaFile(File javaFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            // Create an instance of JavaParser
            JavaParser javaParser = new JavaParser();
            CompilationUnit cu = javaParser.parse(fis).getResult().orElse(null);

            if (cu != null) {
                // Find all class declarations
                return cu.findAll(ClassOrInterfaceDeclaration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /*
    generatePlantUMLForClass generates PlantUML-readable file for the parsed data in .puml format.
    */
    public static void generatePlantUMLForClass(ClassOrInterfaceDeclaration classDecl, FileWriter writer) throws IOException {
        // Determine whether it's a class or interface
        String className = classDecl.getNameAsString();
        String classType = classDecl.isInterface() ? "interface" : (classDecl.isAbstract() ? "abstract class" : "class");

        // Get the package name
        String packageName = getPackageName(classDecl);
        if (packageName != null && !packageName.isEmpty()) {
            writer.write("package " + packageName + " {\n");
        }

        writer.write(classType + " " + className + " {\n");

        // Print class annotations
        if (!classDecl.getAnnotations().isEmpty()) {
            for (var annotation : classDecl.getAnnotations()) {
                writer.write("    @" + annotation.getNameAsString() + "\n");
            }
        }

        List<String> processedRelationships = new ArrayList<>(); // Track processed relationships

        // Fields (attributes)
        for (FieldDeclaration field : classDecl.getFields()) {
            String fieldName = field.getVariables().get(0).getNameAsString();
            String fieldType = field.getCommonType().asString(); // Get the field type

            // Handle generic types like List<Class>
            if (fieldType.contains("List") || fieldType.contains("Set") || fieldType.contains("Map")) {
                fieldType = fieldType.replaceAll(".*<(.+)>.*", "$1");  // Extracts "Animal" from "List<Animal>"
            }

            // Print field in class body
            writer.write("    + " + fieldName + " : " + fieldType + "\n");

            // Handle relationships (Composition, Aggregation, Association)
            String relationship = null;
            if (RelationshipDetector.isComposition(field)) {
                relationship = className + " *-- " + fieldType;
            } else if (RelationshipDetector.isAggregation(field)) {
                relationship = className + " o-- " + fieldType;
            } else if (!RelationshipDetector.isAssociation(field)) {
                relationship = className + " --> " + fieldType;
            }

            // Only print relationships if not already processed
            if (relationship != null && !processedRelationships.contains(relationship)) {
                processedRelationships.add(relationship);
            }
        }

        // parsing constructors
        for (ConstructorDeclaration constructor : classDecl.getConstructors()) {
            StringBuilder annotations = new StringBuilder();
            constructor.getAnnotations().forEach(annotation -> {
                annotations.append("@" + annotation.getNameAsString() + " ");
            });

            String constructorName = constructor.getNameAsString();
            String constructorParams = constructor.getParameters().stream()
                    .map(Parameter::getType)
                    .map(Object::toString)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            writer.write("    + " + annotations.toString().trim() + " " + constructorName + "(" + constructorParams + ")\n");
        }

        // Methods
        for (MethodDeclaration method : classDecl.getMethods()) {
            // Print method annotations
            StringBuilder annotations = new StringBuilder();
            method.getAnnotations().forEach(annotation -> {
                annotations.append("@" + annotation.getNameAsString() + " ");
            });

            String methodName = method.getNameAsString();
            String returnType = method.getType().asString();

            // Handle generic return types
            if (returnType.contains("List") || returnType.contains("Set") || returnType.contains("Map")) {
                returnType = returnType.replaceAll(".*<(.+)>.*", "$1");  // Extracts "Animal" from "List<Animal>"
            }

            writer.write("    + " + annotations.toString().trim() + " " + methodName + "() : " + returnType + "\n");

            // Print parameter annotations and types
            for (Parameter parameter : method.getParameters()) {
                if (!parameter.getAnnotations().isEmpty()) {
                    for (var annotation : parameter.getAnnotations()) {
                        writer.write("    @" + annotation.getNameAsString() + " " + parameter.getNameAsString() + "\n");
                    }
                }

                String paramType = parameter.getType().asString();
                if (paramType.contains("List") || paramType.contains("Set") || paramType.contains("Map")) {
                    paramType = paramType.replaceAll(".*<(.+)>.*", "$1");  // Extracts "Animal" from "List<Animal>"
                }
                writer.write("    + " + parameter.getNameAsString() + " : " + paramType + "\n");
            }
        }

        // Close class/interface definition
        writer.write("}\n");

        // Print all relationships after fields and methods to avoid duplication
        for (String rel : processedRelationships) {
            writer.write(rel + "\n");
        }

        // Handle inheritance and implementation (extends, implements)
        for (ClassOrInterfaceType extendedClass : classDecl.getExtendedTypes()) {
            writer.write(className + " <|-- " + extendedClass.getNameAsString() + "\n"); // Inheritance
        }
        for (ClassOrInterfaceType implementedInterface : classDecl.getImplementedTypes()) {
            writer.write(className + " <|.. " + implementedInterface.getNameAsString() + "\n"); // Realization
        }

        // Close the package block if package name exists
        if (packageName != null && !packageName.isEmpty()) {
            writer.write("}\n");
        }
    }

    // Method to extract the package name from the class declaration
    public static String getPackageName(ClassOrInterfaceDeclaration classDecl) {
        // Traverse up the parent nodes to find the CompilationUnit
        Node current = classDecl;
        while (current != null && !(current instanceof CompilationUnit)) {
            current = current.getParentNode().orElse(null);
        }

        if (current instanceof CompilationUnit) {
            CompilationUnit cu = (CompilationUnit) current;
            return cu.getPackageDeclaration()
                    .map(pd -> pd.getNameAsString())
                    .orElse(""); // Return an empty string if no package declaration exists
        }

        return ""; // Return an empty string if CompilationUnit is not found
    }

}
