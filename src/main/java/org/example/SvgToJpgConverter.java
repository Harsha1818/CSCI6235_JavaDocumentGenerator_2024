package org.example;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class SvgToJpgConverter {

    private static final Logger logger = Logger.getLogger(SvgToJpgConverter.class.getName());
    public static void main(String[] args) {

    }

    String convertSVGtoJPG(String svgPath){
        String svgFilePath = svgPath;
        String jpgFilePath = "output.JPG";

        try {
            // Create a JPEGTranscoder
            Transcoder transcoder = new JPEGTranscoder();

            // Set the transcoding hints
            transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.9f); // Set image quality (0.0 to 1.0)

            // Create the TranscoderInput and TranscoderOutput
            FileInputStream svgInputStream = new FileInputStream(new File(svgFilePath));
            TranscoderInput input = new TranscoderInput(svgInputStream);

            FileOutputStream jpgOutputStream = new FileOutputStream(new File(jpgFilePath));
            TranscoderOutput output = new TranscoderOutput(jpgOutputStream);

            // Perform the conversion
            transcoder.transcode(input, output);

            // Close the streams
            svgInputStream.close();
            jpgOutputStream.close();

            logger.info("SVG file successfully converted to JPG.");
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        return jpgFilePath;
    }
}
