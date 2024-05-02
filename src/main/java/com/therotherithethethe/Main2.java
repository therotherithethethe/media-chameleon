package com.therotherithethethe;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Main2 {

    public static void main(String[] args) {
        String inputBmpPath = "C:\\Users\\zhenya\\IdeaProjects\\mediachameleon\\image.bmp";
        String outputJpgPath = "C:\\Users\\zhenya\\IdeaProjects\\mediachameleon\\image.jpg";
        float compressionQuality =
            0.70f; // Set your compression quality here (1.0 for 100%, 0.1 for 10%, etc.)

        try {
            // Read the BMP image
            BufferedImage bmpImage = ImageIO.read(new File(inputBmpPath));

            // Get a JPG writer
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();

            // Configure compression quality
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(compressionQuality);

            // Write the JPEG file
            try (ImageOutputStream outputStream =
                ImageIO.createImageOutputStream(new File(outputJpgPath))) {
                jpgWriter.setOutput(outputStream);
                jpgWriter.write(null, new IIOImage(bmpImage, null, null), jpgWriteParam);
            }

            System.out.println("Conversion successful! JPEG image saved at: " + outputJpgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
