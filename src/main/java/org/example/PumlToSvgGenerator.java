package org.example;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;

import java.io.*;
import java.nio.file.*;
import java.awt.Desktop;

public class PumlToSvgGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Directory to monitor
        Path dirToWatch = Paths.get("/Users/siddarthsrivastava/Desktop/CSCI6235_JavaDocumentGenerator_2024"); // Change to your directory

        // Create a WatchService
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dirToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Watching directory: " + dirToWatch);

        // Create an instance of PumlToSvgGenerator
        PumlToSvgGenerator generator = new PumlToSvgGenerator();

        // Infinite loop to monitor events
        while (true) {
            WatchKey key = watchService.take(); // Wait for a file event

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // If a new or modified .puml file is detected
                if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    Path fileName = (Path) event.context();
                    if (fileName.toString().endsWith(".puml")) {
                        Path fullPath = dirToWatch.resolve(fileName);
                        System.out.println("Processing .puml file: " + fullPath);

                        // Generate SVG
                        File svgFile = generator.generateSvg(fullPath.toFile());

                        // Open the SVG in the default viewer
                        /*if (svgFile != null && svgFile.exists()) {
                            System.out.println("Opening SVG: " + svgFile.getAbsolutePath());
                            Desktop.getDesktop().open(svgFile);
                        }*/
                    }
                }
            }

            // Reset the key (important for continued watching)
            if (!key.reset()) {
                break;
            }
        }
    }

    // Method to generate SVG from PUML file
    public File generateSvg(File pumlFile) {
        System.out.println("Generating SVG from PUML file: " + pumlFile.getAbsolutePath());
        try {
            // Read PlantUML content from the .puml file
            String pumlContent = new String(Files.readAllBytes(pumlFile.toPath()));

            // Prepare the SVG output file
            File svgFile = new File(pumlFile.getParent(), pumlFile.getName().replace(".puml", ".svg"));

            // Generate the SVG using PlantUML
            SourceStringReader reader = new SourceStringReader(pumlContent);
            try (OutputStream outputStream = new FileOutputStream(svgFile)) {
                reader.generateImage(outputStream, new FileFormatOption(FileFormat.SVG));
            }

            System.out.println("SVG file generated: " + svgFile.getAbsolutePath());
            return svgFile;
        } catch (Exception e) {
            System.err.println("Error generating SVG: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
