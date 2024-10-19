package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageAnnotator extends Application {

    private final List<Text> annotations = new ArrayList<>();  // List to store all text annotations
    private final String annotationsFilePath = "C:/Users/Shiddarth/Desktop/CSCI6235_JavaDocumentGenerator_2024/annotations.txt";  // Custom path for annotation file

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Application started");

        // Load your PNG file
        Image image = new Image("file:C:/Users/Shiddarth/Desktop/CSCI6235_JavaDocumentGenerator_2024/testuml.png");
        ImageView imageView = new ImageView(image);

        // Create a Pane to allow adding annotations
        Pane pane = new Pane();
        pane.getChildren().add(imageView);  // Add image to pane

        // Set up click event to add a text annotation
        pane.setOnMouseClicked(event -> {
            System.out.println("Pane clicked at " + event.getX() + ", " + event.getY());
            addTextField(pane, event.getX(), event.getY());
        });

        // Load any saved annotations
        loadAnnotations(pane);

        // Create a scene and show the stage
        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Image Annotator");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Save annotations when closing
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Closing application...");  // Debug statement
            saveAnnotations();
        });
    }

    // Add a TextField for users to input or edit text annotation
    private void addTextField(Pane pane, double x, double y) {
        TextField textField = new TextField();
        textField.setLayoutX(x);  // Set position of TextField where user clicked
        textField.setLayoutY(y);

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {  // When user presses "Enter"
                String textContent = textField.getText();
                if (!textContent.isEmpty()) {
                    // Create a Text node to replace the TextField
                    Text text = new Text(x, y, textContent);
                    text.setOnMouseClicked(e -> editAnnotation(pane, text));  // Allow editing of the annotation on double-click

                    System.out.println("Adding annotation: " + text.getText() + " at (" + x + ", " + y + ")");

                    annotations.add(text);  // Save annotation in the list
                    System.out.println("Current number of annotations: " + annotations.size());  // Print the size of the list

                    pane.getChildren().add(text);  // Add text to pane
                    pane.getChildren().remove(textField);  // Remove TextField after input
                }
            }
        });

        pane.getChildren().add(textField);  // Add TextField to the pane
        textField.requestFocus();  // Automatically focus on the new TextField
    }

    // Enable editing an annotation on double-click
    private void editAnnotation(Pane pane, Text text) {
        TextField textField = new TextField(text.getText());
        textField.setLayoutX(text.getX());
        textField.setLayoutY(text.getY());

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newText = textField.getText();
                if (!newText.isEmpty()) {
                    text.setText(newText);  // Update the text
                    pane.getChildren().remove(textField);  // Remove the TextField after editing
                    pane.getChildren().add(text);  // Re-add the updated text to the pane
                }
            }
        });

        pane.getChildren().add(textField);  // Add editable TextField on top of the text
        pane.getChildren().remove(text);  // Temporarily remove the original Text
        textField.requestFocus();  // Automatically focus on the TextField
    }

    // Save annotations to a file
    private void saveAnnotations() {
        System.out.println("Attempting to save annotations.");  // Debug statement
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(annotationsFilePath))) {
            for (Text text : annotations) {
                System.out.println("Saving annotation: " + text.getText() + " at (" + text.getX() + ", " + text.getY() + ")");
                writer.write(text.getText() + "," + text.getX() + "," + text.getY());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load saved annotations from a file
    private void loadAnnotations(Pane pane) {
        File file = new File(annotationsFilePath);
        if (!file.exists()) {
            System.out.println("No annotations found to load.");
            return;  // If no saved annotations, return
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String textContent = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);

                // Debugging: Print loaded annotation
                System.out.println("Loading annotation: " + textContent + " at (" + x + ", " + y + ")");

                // Re-create Text annotations and add them to the pane
                Text text = new Text(x, y, textContent);
                text.setOnMouseClicked(e -> editAnnotation(pane, text));  // Allow editing of the annotation
                annotations.add(text);
                pane.getChildren().add(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
