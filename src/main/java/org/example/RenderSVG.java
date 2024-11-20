package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class RenderSVG extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a WebView to load the SVG
        WebView webView = new WebView();
        String svgFilePath = "file:/Users/manojsrinivasa/Desktop/Projects/CSCI6235_JavaDocumentGenerator_2024/output.svg"; // Update with your file path

        // Load the SVG file into the WebView
        WebEngine webEngine = webView.getEngine();
        webEngine.load(svgFilePath);

        // Set up JavaScript interaction with the WebView
        webEngine.setJavaScriptEnabled(true);
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // Add JavaScript code to listen for click events on SVG elements
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", this); // Set reference to the Java app

                // JavaScript code to handle clicks on <rect>, <text>, <path>, and <line> elements
                String script = "document.querySelectorAll('rect, path, line').forEach(function(element) {" +
                        "    element.addEventListener('click', function(event) {" +
                        "        var elementId = event.target.id;" +
                        "        var elementType = event.target.nodeName;" +
                        "        var rect = event.target.getBoundingClientRect();" +
                        "        var x = rect.left + window.scrollX;" + // Get accurate coordinates with scroll offset
                        "        var y = rect.top + window.scrollY;" + // Get accurate coordinates with scroll offset
                        "        javaApp.addComment(elementId, elementType, x, y);" + // Pass coordinates to Java
                        "    });" +
                        "});";
                webEngine.executeScript(script);
            }
        });

        // Set up the scene and stage
        Scene scene = new Scene(webView, 800, 600);
        primaryStage.setTitle("JavaFX SVG Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // This method will be called from JavaScript when an SVG element is clicked
    public void addComment(String elementId, String elementType, double x, double y) {
        // Handle comment addition (for simplicity, just print info)
        System.out.println("Clicked element: " + elementId + " (Type: " + elementType + ")");

        // Create a TextInputDialog to ask for the comment
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Comment");
        dialog.setHeaderText("Add a comment for element: " + elementId);
        dialog.setContentText("Comment:");

        dialog.showAndWait().ifPresent(comment -> {
            // Add the comment to the SVG at the clicked coordinates
            addCommentToSVG(x, y, comment);
        });
    }

    // Method to add the comment to the SVG at the specified coordinates
    private void addCommentToSVG(double x, double y, String comment) {
        WebEngine webEngine = new WebView().getEngine();  // Get WebEngine instance

        // JavaScript to create a new text element inside the SVG
        String script = "var svg = document.querySelector('svg');" + // Get the SVG element
                "if (svg) {" +
                "    var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');" +  // Create a text element in SVG namespace
                "    text.setAttribute('x', '" + (x + 10) + "');" +  // Set the x position with a slight offset
                "    text.setAttribute('y', '" + (y + 10) + "');" +  // Set the y position with a slight offset
                "    text.setAttribute('fill', 'black');" +  // Set text color
                "    text.setAttribute('font-size', '28');" +  // Set text font size
                "    text.textContent = '" + comment + "';" +  // Set the comment text
                "    svg.appendChild(text);" +  // Append the text to the SVG
                "}";

        // Ensure the WebEngine is used to execute the script in the context of the loaded SVG
        webEngine.executeScript(script);  // Execute the JavaScript to add the comment
    }

    public static void main(String[] args) {
        launch(args);
    }
}
