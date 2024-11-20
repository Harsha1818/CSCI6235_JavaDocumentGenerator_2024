package org.example;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceFileReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlantUML {

    public static String generateDiagram(String pumlFilePath) {
        File outputFile = new File("output.svg");
        try {
            // Create a File object for the .puml file
            File sourceFile = new File(pumlFilePath);
            // Check if the file exists
            if (!sourceFile.exists()) {
                throw new IllegalArgumentException("The specified .puml file does not exist: " + pumlFilePath);
            }

            // Specify the output directory


            // Use SourceFileReader to generate the diagram
//            SourceFileReader reader = new SourceFileReader(sourceFile);
            SourceFileReader reader = new SourceFileReader(sourceFile, sourceFile.getParentFile(), new FileFormatOption(FileFormat.SVG));

            // Process and save the diagrams
            List<net.sourceforge.plantuml.GeneratedImage> images = reader.getGeneratedImages();
            for (net.sourceforge.plantuml.GeneratedImage image : images) {
                outputFile = image.getPngFile(); // Get the generated PNG file
                System.out.println("Diagram generated at: " + outputFile.getAbsolutePath());
            }


        } catch (IOException e) {
            System.err.println("An error occurred while generating the diagram: " + e.getMessage());
            e.printStackTrace();
        }
        return outputFile.getAbsolutePath();
    }

    public static void main(String[] args) {
    }
}
