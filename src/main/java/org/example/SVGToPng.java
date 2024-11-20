package org.example;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SVGToPng {

    public static void convertSVGToPNG() {
        String outputFilePath = "outputImage.png";
        try (FileInputStream inputStream = new FileInputStream("/Users/manojsrinivasa/Desktop/Projects/CSCI6235_JavaDocumentGenerator_2024/output.svg");
             FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {

            // Create a transcoder for PNG
            Transcoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);

            // Set the input and output for transcoding
            TranscoderInput input = new TranscoderInput(inputStream);
            TranscoderOutput output = new TranscoderOutput(outputStream);

            // Perform the transcoding
            transcoder.transcode(input, output);

            System.out.println("SVG file successfully converted to PNG: " + outputFilePath);

        } catch (IOException | TranscoderException e) {
            System.err.println("Error while converting SVG to PNG: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String svgFilePath = "diagram.svg"; // Path to your SVG file
        String outputFilePath = "diagram.png"; // Desired output PNG file path
        convertSVGToPNG();
    }
}
