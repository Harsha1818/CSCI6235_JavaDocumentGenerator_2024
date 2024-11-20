package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Upload extends Application {
    String pathInput = new String();
    String pumlFile = new String();


    @Override
    public void start(Stage primaryStage) {
        // Create a TextField for path input
        TextField textField = new TextField();
        textField.setPromptText("Enter a file or directory path...");
        textField.setPrefWidth(250); // Make the TextField smaller

        // Create a Label for error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // Red text for error message

        // Create an Upload button
        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(event -> {
            pathInput = textField.getText();
            String validationMessage = validatePath(pathInput);
            if (validationMessage == null) {
                errorLabel.setText("Path is valid!");
                errorLabel.setStyle("-fx-text-fill: green;");
            } else {
                errorLabel.setText(validationMessage);
                errorLabel.setStyle("-fx-text-fill: red;");
            }
            JavaUMLParser umlParser = new JavaUMLParser();
            try {
                pumlFile =  umlParser.starter(pathInput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PlantUML plantUML = new PlantUML();
            System.out.println("Path = :"+plantUML.generateDiagram(pumlFile));
        });

        // Set up an HBox layout for TextField and Upload button to be next to each other
        HBox inputLayout = new HBox(10); // 10 px spacing between components
        inputLayout.getChildren().addAll(textField, uploadButton);
        inputLayout.setAlignment(Pos.CENTER); // Center horizontally in the HBox

        // Set up a VBox layout to hold the HBox and the error label
        VBox layout = new VBox(10); // 10 px spacing
        layout.getChildren().addAll(inputLayout, errorLabel);
        layout.setAlignment(Pos.CENTER); // Center the children in the VBox

        // Get the screen size (for MacBook Air or any screen)
        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Create the Scene, setting its width and height to cover 50% of the screen size
        Scene scene = new Scene(layout, screenWidth * 0.5, screenHeight * 0.5); // 50% of screen width and height

        // Set up the Stage
        primaryStage.setTitle("Java UML Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to validate the path entered in the TextField
    private String validatePath(String pathString) {
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
