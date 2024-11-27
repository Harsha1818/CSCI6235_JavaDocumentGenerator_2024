package org.example;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
public class JavaDocGen extends Application {

    private WebEngine webEngine;
    private final Map<String, List<String>> entityComments = new HashMap<>();
     String currentEntity = new String();
    private Label commentLabel;
    private TextArea commentArea;
    private int editingCommentIndex = -1;
    String pathInput = new String();
    String pumlFile = new String();

    // Add this method in your Upload class or as a separate class
    private Scene createRenderSVGScene(Stage primaryStage, String svgFilePath) {
        if (svgFilePath == null || svgFilePath.isEmpty()) {
            System.err.println("SVG file path not provided.");
            return null;
        }

        // Create a WebView to load the SVG
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.load("file://" + svgFilePath);

        // Enable JavaScript interaction
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this);

                String script = "document.querySelectorAll('rect, path, line, text').forEach(function(element) {" +
                        "    element.addEventListener('click', function(event) {" +
                        "        var elementId = event.target.id || 'No ID';" +
                        "        var rect = event.target.getBoundingClientRect();" +
                        "        var x = rect.left + window.scrollX;" +
                        "        var y = rect.top + window.scrollY;" +
                        "        javaApp.handleClick(elementId, x, y);" +
                        "    });" +
                        "});";
                webEngine.executeScript(script);
            }
        });

        // Set up the comment section
        commentLabel = new Label("Comments for: ");
        commentArea = new TextArea();
        commentArea.setPrefRowCount(3);
        commentArea.setPromptText("Enter your comment here...");
        Button saveButton = new Button("Save Comment");

        saveButton.setOnAction(event -> {
            String comment = commentArea.getText().trim();
            if (!currentEntity.isEmpty() && !comment.isEmpty()) {
                entityComments.putIfAbsent(currentEntity, new ArrayList<>());

                if (editingCommentIndex == -1) {
                    entityComments.get(currentEntity).add(comment);
                    appendCommentToSVG(currentEntity, comment);
                } else {
                    entityComments.get(currentEntity).set(editingCommentIndex - 1, comment);
                    updateComment(currentEntity, comment);
                }

                commentArea.clear();
                editingCommentIndex = -1;
            } else {
                System.out.println("No entity selected or comment is empty. Cannot save.");
            }
        });

        // Add a "Back" button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> primaryStage.setScene(createUploadScene(primaryStage)));

        // Add a "Save as Image" button
        Button saveImageButton = new Button("Save as Image");
        saveImageButton.setOnAction(event -> {
            WritableImage snapshot = webView.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            try {
                // Save the image without the comment section or buttons
                File outputFile = new File("scene_snapshot.jpg");
                ImageIO.write(bufferedImage, "png", outputFile);
                System.out.println("Scene saved as: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving the image: " + e.getMessage());
            }
        });

        // Create a horizontal box for the top-right buttons
        HBox topRightButtons = new HBox(10, saveImageButton, backButton);
        topRightButtons.setAlignment(Pos.CENTER_RIGHT);
        topRightButtons.setPadding(new Insets(10));
        BorderPane.setAlignment(topRightButtons, Pos.TOP_RIGHT);

        // Layout for the bottom section
        HBox commentSection = new HBox(10, commentLabel, commentArea, saveButton);
        commentSection.setPadding(new Insets(10));
        commentSection.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // Set up the main layout
        BorderPane root = new BorderPane();
        root.setCenter(webView);
        root.setBottom(commentSection);
        root.setTop(topRightButtons); // Add buttons to the top

        // Create the Scene and maximize the stage
        Scene scene = new Scene(root);
        primaryStage.setMaximized(true); // Maximizes the window
        primaryStage.setFullScreen(true); // Optionally sets to full screen

        return scene;
    }


    private void updateComment(String currentEntity, String comment) {

    }

    public void handleClick(String elementId, double x, double y) {
        System.out.println("Clicked element ID: " + elementId);

        if (entityComments.containsKey(elementId)) {
            List<String> comments = entityComments.get(elementId);

            String script = "var choice = confirm('Do you want to add a new comment for " + elementId + "? Click OK for new comment, Cancel to edit existing comments.'); choice;";
            try {
                Object result = webEngine.executeScript(script);
                boolean addNewComment = Boolean.parseBoolean(result.toString());

                if (addNewComment) {
                    commentArea.clear();
                    editingCommentIndex = -1;
                } else {
                    if (!comments.isEmpty()) {
                        StringBuilder optionsScript = new StringBuilder("var index = prompt('Select a comment to edit:\n");
                        for (int i = 0; i < comments.size(); i++) {
                            optionsScript.append(i + 1).append(": ").append(comments.get(i).replace("'", "\\'")).append("\n");
                        }
                        optionsScript.append("Enter the number of the comment to edit.'); index;");

                        Object selectedOption = webEngine.executeScript(optionsScript.toString());
                        if (selectedOption != null && !selectedOption.toString().isEmpty()) {
                            try {
                                int selectedIndex = Integer.parseInt(selectedOption.toString()) - 1;
                                if (selectedIndex >= 0 && selectedIndex < comments.size()) {
                                    commentArea.setText(comments.get(selectedIndex));
                                    editingCommentIndex = selectedIndex + 1;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input for comment selection.");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            commentArea.clear();
            editingCommentIndex = -1;
        }

        currentEntity = elementId;
        commentLabel.setText("Comments for " + currentEntity + ":");
    }

    private void appendCommentToSVG(String elementId, String comment) {
// Escape the comment text to make it safe for JavaScript
        String escapedComment = comment
                .replace("\\", "\\\\") // Escape backslashes
                .replace("'", "\\'")   // Escape single quotes
                .replace("\"", "\\\"") // Escape double quotes
                .replace("\n", "\\n")  // Escape newlines
                .replace("\r", "");    // Remove carriage returns

        // JavaScript to dynamically create and manage comment boxes
        String script = "var svg = document.querySelector('svg');" +
                "if (svg) {" +
                "    var entity = document.getElementById('" + elementId + "');" +
                "    if (entity) {" +
                "        var rect = entity.getBoundingClientRect();" +
                "        var boxWidth = rect.width;" +
                "        var baseX = rect.left + window.scrollX - 1;" +
                "        var baseY = rect.top + window.scrollY - 50;" + // Position above the entity
                "        var group = document.querySelector('[data-comments=\"" + elementId + "\"]');" +

                // Check if the group for comments exists; if not, create it
                "        if (!group) {" +
                "            group = document.createElementNS('http://www.w3.org/2000/svg', 'g');" +
                "            group.setAttribute('data-comments', '" + elementId + "');" +

                // Create the background rectangle for the comment box
                "            var background = document.createElementNS('http://www.w3.org/2000/svg', 'rect');" +
                "            background.setAttribute('x', baseX);" +
                "            background.setAttribute('y', baseY);" +
                "            background.setAttribute('width', boxWidth);" +
                "            background.setAttribute('height', 100);" + // Default height
                "            background.setAttribute('fill', '#f9f9f9');" +
                "            background.setAttribute('stroke', '#000');" +
                "            background.setAttribute('stroke-width', '1');" +
                "            group.appendChild(background);" +

                // Add the title text for the comment box
                "            var title = document.createElementNS('http://www.w3.org/2000/svg', 'text');" +
                "            title.setAttribute('x', baseX + 5);" +
                "            title.setAttribute('y', baseY + 15);" +
                "            title.setAttribute('fill', 'black');" +
                "            title.setAttribute('font-size', '12');" +
                "            title.textContent = 'Comments for " + elementId + "';" +
                "            group.appendChild(title);" +

                // Append the group to the SVG
                "            svg.appendChild(group);" +
                "        }" +

                // Update the background height to fit new comments
                "        var background = group.querySelector('rect');" +
                "        var comments = group.querySelectorAll('[data-comment]');" +
                "        var commentY = (comments.length * 15) + baseY + 30;" + // Calculate new Y position for the next comment
                "        var newHeight = (comments.length + 1) * 15 + 20;" +   // Adjust background height
                "        background.setAttribute('height', newHeight);" +

                // Add the new comment to the existing group
                "        var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');" +
                "        text.setAttribute('x', baseX + 5);" +
                "        text.setAttribute('y', commentY);" +
                "        text.setAttribute('fill', 'black');" +
                "        text.setAttribute('font-size', '10');" +
                "        text.setAttribute('data-comment', 'true');" +
                "        text.textContent = '" + escapedComment + "';" +

                // Add event listener for editing
                "        text.addEventListener('click', function() {" +
                "            console.log('Text clicked for editing:', text.textContent);" +
                "            var input = document.createElement('input');" +
                "            input.type = 'text';" +
                "            input.value = text.textContent;" +
                "            input.style.position = 'absolute';" +
                "            input.style.left = (baseX + 5) + 'px';" +
                "            input.style.top = (commentY - 10) + 'px';" +
                "            input.style.fontSize = '10px';" +
                "            input.style.zIndex = 1000;" +
                "            document.body.appendChild(input);" +
                "            input.addEventListener('blur', function() {" +
                "                console.log('Input blur: saving content');" +
                "                text.textContent = input.value;" +
                "                document.body.removeChild(input);" +
                "                javaApp.updateComment('" + elementId + "', input.value);" +
                "            });" +
                "            input.addEventListener('keydown', function(event) {" +
                "                if (event.key === 'Enter') {" +
                "                    input.blur();" +
                "                }" +
                "            });" +
                "            input.focus();" +
                "        });" +

                "        group.appendChild(text);" +
                "    }" +
                "}";

        try {
            webEngine.executeScript(script);
        } catch (Exception e) {
            System.err.println("Error executing script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Scene createUploadScene(Stage primaryStage) {
        // Create a TextField for path input
        TextField textField = new TextField();
        textField.setPromptText("Enter a file or directory path...");
        textField.setPrefWidth(400); // Adjust TextField width for better visibility

        // Create a Label for error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;"); // Red text for error messages

        // Create an Upload button
        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(event -> {
            pathInput = textField.getText();
            PathChecker pathCheckerObj = new PathChecker();
            String validationMessage = pathCheckerObj.validatePath(pathInput);
            if (validationMessage == null) {
                errorLabel.setText("Given Path is valid!");
                System.out.println("Given Path is valid, Path = " + pathInput);
                errorLabel.setStyle("-fx-text-fill: green;");
                JavaUMLParser umlParser = new JavaUMLParser();
                try {
                    pumlFile = umlParser.starter(pathInput);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Step 2: Ensure previous.puml exists
                File previousPumlFile = new File("previous.puml");
                if (!previousPumlFile.exists()) {
                    System.out.println("No previous.puml found. Creating it from current.puml.");
                    PumlEntityManager.createPreviousPuml(pumlFile);
                }

                // Step 3: Update entities and detect missing entities
                System.out.println("Updating entities and detecting missing elements...");
                PumlEntityManager.updateEntities(pumlFile);
                Set<String> missingEntities = PumlEntityManager.getMissingEntities();
                System.out.println("Missing entities: " + missingEntities);

                // Step 4: Convert .puml to .svg
                PumlToSvgGenerator svgGenerator = new PumlToSvgGenerator();
                File svgFile = svgGenerator.generateSvg(new File(pumlFile));
                System.out.println("Generated SVG file: " + svgFile.getAbsolutePath());

                // Transition to the Render SVG page
                primaryStage.setScene(createRenderSVGScene(primaryStage, svgFile.getAbsolutePath()));
            } else {
                errorLabel.setText(validationMessage);
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        });

        // Set up an HBox layout for TextField and Upload button to be next to each other
        HBox inputLayout = new HBox(10); // 10 px spacing between components
        inputLayout.getChildren().addAll(textField, uploadButton);
        inputLayout.setAlignment(Pos.CENTER); // Center horizontally in the HBox

        // Set up a VBox layout to hold the HBox and the error label
        VBox layout = new VBox(20); // 20 px spacing for better spacing in fullscreen
        layout.getChildren().addAll(inputLayout, errorLabel);
        layout.setAlignment(Pos.CENTER); // Center the children in the VBox

        // Create the Scene
        Scene scene = new Scene(layout);

        // Set Stage to fullscreen
        primaryStage.setMaximized(true); // Maximizes the window

        return scene;
    }


    @Override
    public void start(Stage primaryStage) {
        // Set the initial scene to the upload page
        primaryStage.setTitle("Java UML Generator");
        primaryStage.setScene(createUploadScene(primaryStage));
        primaryStage.show();
    }

}
