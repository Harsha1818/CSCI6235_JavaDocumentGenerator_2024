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
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static org.example.Constants.CURRENT_PUML_FILE;
import java.util.Set;

public class JavaUMLParser {

    private static final Logger logger = Logger.getLogger(JavaUMLParser.class.getName());
    public static void main(String args[]) throws IOException {
//        List<ClassOrInterfaceDeclaration> classList = new ArrayList<>();
//
//        // Specify the folder containing Java files
//        File folder = new File("/Users/sarvanthvedula/Documents/3rd sem MS/component based/project/CSCI6235_JavaDocumentGenerator_2024/src/test/java/org/example/TestFolder");
//        // File folder = new File(args[0]);
//        parseFolder(folder, classList);
//
//        // Output PlantUML diagram to a file
//        try (FileWriter writer = new FileWriter("output1.puml")) {
//            writer.write("@startuml\n");
//
//            // Generate PlantUML from parsed classes
//            for (ClassOrInterfaceDeclaration classDecl : classList) {
//                CompilationUnit cu = getCompilationUnitForClass(classDecl);
//                generatePlantUMLForClass(classDecl, writer, cu);
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

        File outputFile = new File(CURRENT_PUML_FILE);
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
                CompilationUnit cu = getCompilationUnitForClass(classDecl);
                generatePlantUMLForClass(classDecl, writer, cu);
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

    public static CompilationUnit getCompilationUnitForClass(ClassOrInterfaceDeclaration classDecl) {
        Node current = classDecl;
        while (current != null && !(current instanceof CompilationUnit)) {
            current = current.getParentNode().orElse(null);
        }
        return (CompilationUnit) current;
    }

    /*
    generatePlantUMLForClass generates PlantUML-readable file for the parsed data in .puml format.
    */
    public static void generatePlantUMLForClass(ClassOrInterfaceDeclaration classDecl, FileWriter writer, CompilationUnit cu) throws IOException {

        // Determine whether it's a class, interface, or abstract class
        String className = classDecl.getNameAsString();
        String classType = classDecl.isInterface() ? "interface" : (classDecl.isAbstract() ? "abstract class" : "class");

        // Get the package name
        Set<String> printedPackages = new HashSet<>();
        String packageName = getPackageName(classDecl);
        if (!packageName.isEmpty() && !printedPackages.contains(packageName)) {
            writer.write("package " + packageName + " {\n");
            printedPackages.add(packageName);
        }

        writer.write(classType + " " + className + " {\n");
        // Include import statements as class attributes
        if (cu != null) {
            Set<String> processedImports = new HashSet<>();
            for (var importDecl : cu.getImports()) {
                String importName = importDecl.getNameAsString();
                if (!processedImports.contains(importName)) {
                    writer.write("    + @import " + importName + "\n");
                    processedImports.add(importName);
                }
            }
        }

        // Print class annotations
        if (!classDecl.getAnnotations().isEmpty()) {
            for (var annotation : classDecl.getAnnotations()) {
                writer.write("    @" + annotation.getNameAsString() + "\n");
            }
        }

        List<String> processedRelationships = new ArrayList<>(); // Track processed relationships
        Set<String> processedFields = new HashSet<>();

        // Fields (attributes)
        for (FieldDeclaration field : classDecl.getFields()) {
            String fieldName = field.getVariables().get(0).getNameAsString();
            String fieldType = sanitizeGenericType(field.getCommonType().asString()); // Sanitize field type

            if (!processedFields.contains(fieldName)) {
                processedFields.add(fieldName);
                writer.write("    + " + fieldName + " : " + fieldType + "\n");

                // Determine relationships
                String relationship = null;
                if (RelationshipDetector.isComposition(field)) {
                    relationship = className + " *-- " + fieldType;
                } else if (RelationshipDetector.isAggregation(field)) {
                    relationship = className + " o-- " + fieldType;
                } else if (RelationshipDetector.isAssociation(field)) {
                    relationship = className + " --> " + fieldType;
                }

                if (relationship != null && !processedRelationships.contains(relationship)) {
                    processedRelationships.add(relationship);
                }
            }
        }

        // Parsing constructors
        for (ConstructorDeclaration constructor : classDecl.getConstructors()) {
            StringBuilder annotations = new StringBuilder();
            constructor.getAnnotations().forEach(annotation -> {
                annotations.append("@" + annotation.getNameAsString() + " ");
            });

            String constructorName = constructor.getNameAsString();
            String constructorParams = constructor.getParameters().stream()
                    .map(parameter -> sanitizeGenericType(parameter.getType().asString())) // Sanitize parameter types
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");

            writer.write("    + " + annotations.toString().trim() + " " + constructorName + "(" + constructorParams + ")\n");
        }

        // Methods
        Set<String> processedMethods = new HashSet<>();
        for (MethodDeclaration method : classDecl.getMethods()) {
            StringBuilder annotations = new StringBuilder();
            method.getAnnotations().forEach(annotation -> {
                annotations.append("@" + annotation.getNameAsString() + " ");
            });

            String methodName = method.getNameAsString();
            String returnType = sanitizeGenericType(method.getType().asString()); // Sanitize return type

            // Handle method parameters
            List<String> paramTypes = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                String paramType = sanitizeGenericType(param.getType().asString());
                paramTypes.add(paramType);

                if (RelationshipDetector.isCustomClass(param.getType().asString())) {
                    String relationship = className + " --> " + paramType;

                    // Avoid duplicate relationships if already defined by fields
                    if (!processedRelationships.contains(relationship) &&
                            !processedRelationships.stream().anyMatch(rel -> rel.contains(paramType))) {
                        processedRelationships.add(relationship);
                    }
                }

            }

            // Construct method signature
            String methodSignature = methodName + "(" + String.join(", ", paramTypes) + ")";

            // Avoid duplicate methods
            if (processedMethods.contains(methodSignature)) {
                continue;
            }
            processedMethods.add(methodSignature);

            // Print the method
            writer.write("    + " + annotations.toString().trim() + " " + methodSignature + " : " + returnType + "\n");
        }


        // Close class definition
        writer.write("}\n");

        // Print all relationships after fields and methods
        for (String rel : processedRelationships) {
            writer.write(rel + "\n");
        }

        // Handle inheritance and implementation (extends, implements)
        for (ClassOrInterfaceType extendedClass : classDecl.getExtendedTypes()) {
            writer.write(className + " <|-- " + extendedClass.getNameAsString() + "\n");
        }

        for (ClassOrInterfaceType implementedInterface : classDecl.getImplementedTypes()) {
            writer.write(className + " <|.. " + implementedInterface.getNameAsString() + "\n");
        }

        // Close package block
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

    private static String sanitizeGenericType(String type) {
        if (type.contains("<")) {
            type = type.replaceAll(".*<(.+?)>.*", "$1"); // Extract generic type (e.g., Animal)
        }
        return type.replace("[]", "Array").replace("<", "").replace(">", "").trim(); // Remove brackets
    }
}
