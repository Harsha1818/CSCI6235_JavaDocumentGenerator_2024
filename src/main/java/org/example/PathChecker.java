package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PathChecker {

    private static final Logger logger = Logger.getLogger(PathChecker.class.getName());
    public static void main(String args[]){

    }

    // Method to validate the path entered in the TextField
    String validatePath(String pathString) {
        // Check if the entered path is empty
        if (pathString == null || pathString.trim().isEmpty()) {
            return "The entered path is empty. Please enter a valid path.";
        }

        try {
            Path path = Paths.get(pathString);

            // Check if the path exists
            if (!Files.exists(path)) {
                return "The entered path does not exist. Please enter a valid path.";
            }

            // Check if the path is a directory containing .java files
            File directory = path.toFile();
            if (directory.isDirectory()) {
                boolean hasJavaFiles = containsJavaFilesRecursively(path);
                if (!hasJavaFiles) {
                    return "No .java files found in the entered directory or its subdirectories. Please enter a valid path.";
                }
            } else {
                return "The entered path is not a directory. Please enter a valid directory path.";
            }

        } catch (Exception e) {
            return "Invalid path format. Please enter a correct path.";
        }

        // Path is valid
        return null;
    }

    /**
     * Checks if the given directory or its subdirectories contain .java files.
     *
     * @param directoryPath The root directory to start the search.
     * @return True if any .java files are found, false otherwise.
     */
    private boolean containsJavaFilesRecursively(Path directoryPath) {
        try (Stream<Path> paths = Files.walk(directoryPath)) {
            return paths.anyMatch(path -> Files.isRegularFile(path) && path.toString().endsWith(".java"));
        } catch (Exception e) {
            return false; // Handle exceptions gracefully, e.g., lack of permissions
        }
    }

}
