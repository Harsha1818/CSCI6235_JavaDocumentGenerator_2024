package org.example;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class PumlEntityManager {
    private static final String PREVIOUS_PUML_FILE = "previous.puml"; // Stores the previous state
    private static final Set<String> currentEntities = new HashSet<>();
    private static final Set<String> missingEntities = new HashSet<>();

    /**
     * Create previous.puml if it doesn't exist.
     * @param currentPumlFilePath Path to the current.puml file.
     */
    public static void createPreviousPuml(String currentPumlFilePath) {
        try {
            File currentFile = new File(currentPumlFilePath);
            File previousFile = new File(PREVIOUS_PUML_FILE);

            if (!previousFile.exists()) {
                Files.copy(currentFile.toPath(), previousFile.toPath());
                System.out.println("Created previous.puml from current.puml.");
            }
        } catch (IOException e) {
            System.err.println("Error creating previous.puml: " + e.getMessage());
        }
    }


    /**
     * Update entities and detect missing entities by comparing current.puml with previous.puml.
     * @param newPumlFilePath Path to the newly generated PUML file.
     */
    public static void updateEntities(String newPumlFilePath) {
        File newPumlFile = new File(newPumlFilePath);
        if (!newPumlFile.exists()) {
            System.err.println("New PUML file not found: " + newPumlFilePath);
            return;
        }

        try {
            // Ensure previous.puml exists
            File previousFile = new File(PREVIOUS_PUML_FILE);
            if (!previousFile.exists()) {
                System.out.println("No previous.puml file found. Creating one from current.puml.");
                createPreviousPuml(newPumlFilePath);
            }

            // Load entities from current.puml
            currentEntities.clear();
            currentEntities.addAll(parseEntities(newPumlFilePath));

            // Load entities from previous.puml
            Set<String> previousEntities = parseEntities(PREVIOUS_PUML_FILE);

            // Detect missing entities
            missingEntities.clear();
            missingEntities.addAll(previousEntities);
            missingEntities.removeAll(currentEntities);

            // Log detected missing entities
            System.out.println("Missing entities: " + missingEntities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse entity names from a PUML file.
     * @param pumlFilePath Path to the PUML file.
     * @return A set of entity names.
     */
    private static Set<String> parseEntities(String pumlFilePath) throws IOException {
        Set<String> entities = new HashSet<>();
        File pumlFile = new File(pumlFilePath);

        if (!pumlFile.exists()) {
            System.err.println("Error: PUML file not found at " + pumlFilePath);
            return entities;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(pumlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("class") || line.startsWith("interface")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length > 1) {
                        entities.add(parts[1]); // Add class or interface name
                    }
                }
            }
        }

        return entities;
    }

    /**
     /**
     * Save the current PUML file as the previous.puml file explicitly.
     * @param currentPumlFilePath Path to the current.puml file.
     */
    public static void saveCurrentAsPrevious(String currentPumlFilePath) {
        try {
            File currentFile = new File(currentPumlFilePath);
            File previousFile = new File(PREVIOUS_PUML_FILE);

            Files.copy(currentFile.toPath(), previousFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Updated previous.puml with the latest content.");
        } catch (IOException e) {
            System.err.println("Error saving current.puml as previous.puml: " + e.getMessage());
        }
    }


    /**
     * Get the set of missing entities.
     * @return A set of missing entities.
     */
    public static Set<String> getMissingEntities() {
        return new HashSet<>(missingEntities);
    }

    public static void saveCommentsToPuml(String pumlFilePath, Map<String, List<String>> entityComments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pumlFilePath, true))) {
            for (Map.Entry<String, List<String>> entry : entityComments.entrySet()) {
                String entity = entry.getKey();
                List<String> comments = entry.getValue();

                writer.write("\nnote right of " + entity + "\n");
                for (String comment : comments) {
                    writer.write("    " + comment + "\n");
                }
                writer.write("end note\n");
            }
            System.out.println("Comments saved to .puml file.");
        } catch (IOException e) {
            System.err.println("Error saving comments to .puml file: " + e.getMessage());
        }
    }



}
