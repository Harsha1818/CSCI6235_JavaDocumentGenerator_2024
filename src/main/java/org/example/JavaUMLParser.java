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
import java.util.List;

public class JavaUMLParser {

    public static void main(String[] args) {
        // Specify the folder containing Java files
        File folder = new File("src/main/java/org/example");

        // Output PlantUML diagram to a file
        try (FileWriter writer = new FileWriter("output.puml")) {
            writer.write("@startuml\n");

            // Recursively parse the folder and generate PlantUML
            parseFolder(folder, writer);

            writer.write("@enduml\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parseFolder(File folder, FileWriter writer) throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    parseFolder(file, writer);  // Recursively parse subfolders
                } else if (file.getName().endsWith(".java")) {
                    parseJavaFile(file, writer);  // Parse individual Java files
                }
            }
        }
    }

    public static void parseJavaFile(File javaFile, FileWriter writer) throws IOException {
        try (FileInputStream fis = new FileInputStream(javaFile)) {
            // Create an instance of JavaParser
            JavaParser javaParser = new JavaParser();
            CompilationUnit cu = javaParser.parse(fis).getResult().orElse(null);

            if (cu != null) {
                // Find all class declarations
                List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
                // This is for debugging classname only
                String ans = classes.get(0).getNameAsString();
                System.out.println("ClassName = "+ans);
                // End of debugging classname

                // Generate PlantUML from parsed classes
                for (ClassOrInterfaceDeclaration classDecl : classes) {
                    generatePlantUMLForClass(classDecl, writer);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
