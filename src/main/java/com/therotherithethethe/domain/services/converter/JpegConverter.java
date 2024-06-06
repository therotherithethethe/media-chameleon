package com.therotherithethethe.domain.services.converter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
/**
 * The {@code JpegConverter} class provides methods for converting BMP files to JPEG format.
 */
public class JpegConverter {
    private static final int[][] QUANTIZATION_MATRIX = { // 50%
        {16, 11, 10, 16, 24, 40, 51, 61},
        {12, 12, 14, 19, 26, 58, 60, 55},
        {14, 13, 16, 24, 40, 57, 69, 56},
        {14, 17, 22, 29, 51, 87, 80, 62},
        {18, 22, 37, 56, 68, 109, 103, 77},
        {24, 35, 55, 64, 81, 104, 113, 92},
        {49, 64, 78, 87, 103, 121, 120, 101},
        {72, 92, 95, 98, 112, 100, 103, 99}
    };
    /**
     * Converts the given BMP file to JPEG format.
     *
     * @param bmpFile the BMP file to be converted
     * @throws IOException if an I/O error occurs
     */
    public static void convert(File bmpFile) throws IOException {
        BufferedImage image = ImageIO.read(bmpFile);
        int width = image.getWidth();
        int height = image.getHeight();

        short[][] YCbCrs = getYCbCrFromBmpRgb(image);
        short[][] resizedCbCr = desizeAndResizeCbCr(YCbCrs[1], YCbCrs[2], width, height);
        byte[][] dctY = discreteCosineTransform(YCbCrs[0], height, width);
        byte[][] dctCb = discreteCosineTransform(resizedCbCr[0], height, width);
        byte[][] dctCr = discreteCosineTransform(resizedCbCr[1], height, width);

        List<String> huffmanEncodedData = new ArrayList<>();

        for (int y = 0; y < height; y += 8) {
            for (int x = 0; x < width; x += 8) {
                byte[][] blockY = extractBlock(dctY, x, y);
                byte[][] blockCb = extractBlock(dctCb, x, y);
                byte[][] blockCr = extractBlock(dctCr, x, y);

                huffmanEncodedData.addAll(Arrays.asList(encodeRLE(blockY)));
                huffmanEncodedData.addAll(Arrays.asList(encodeRLE(blockCb)));
                huffmanEncodedData.addAll(Arrays.asList(encodeRLE(blockCr)));
            }
        }

        File jpegFile = new File("output.jpg");
        try (FileOutputStream fos = new FileOutputStream(jpegFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            writeJPEGHeader(bos, width, height);
            writeHuffmanData(bos, huffmanEncodedData);
            writeJPEGFooter(bos);
        }
    }

    private static short[][] getYCbCrFromBmpRgb(BufferedImage image) {
        short[] yLumaValues;
        short[] cbValues;
        short[] crValues;

        int width = image.getWidth();
        int height = image.getHeight();
        int countOfPixels = width * height;
        yLumaValues = new short[countOfPixels];
        cbValues = new short[countOfPixels];
        crValues = new short[countOfPixels];

        int pixelCount = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                double Y = 0.299 * red + 0.587 * green + 0.114 * blue;
                double cb = -0.168736 * red - 0.331264 * green + 0.5 * blue + 128;
                double cr = 0.5 * red - 0.418688 * green - 0.081312 * blue + 128;
                yLumaValues[pixelCount] = (short) Y;
                cbValues[pixelCount] = (short) cb;
                crValues[pixelCount] = (short) cr;

                pixelCount++;
            }
        }
        return new short[][]{yLumaValues, cbValues, crValues};
    }

    private static short[][] desizeAndResizeCbCr(short[] cbs, short[] crs, int width, int height) {
        int downsampledWidth = width / 2;
        int downsampledHeight = height / 2;
        int countOfDownsampledPixels = downsampledWidth * downsampledHeight;
        short[] averageCbs = new short[countOfDownsampledPixels];
        short[] averageCrs = new short[countOfDownsampledPixels];

        int indexCount = 0;
        for (int y = 0; y < height; y += 2) {
            for (int x = 0; x < width; x += 2) {
                int topLeftIndex = y * width + x;
                int topRightIndex = topLeftIndex + 1;
                int bottomLeftIndex = topLeftIndex + width;
                int bottomRightIndex = bottomLeftIndex + 1;

                int sumCb = cbs[topLeftIndex] + cbs[topRightIndex] + cbs[bottomLeftIndex]
                    + cbs[bottomRightIndex];
                int sumCr = crs[topLeftIndex] + crs[topRightIndex] + crs[bottomLeftIndex]
                    + crs[bottomRightIndex];

                averageCbs[indexCount] = (short) (sumCb / 4);
                averageCrs[indexCount] = (short) (sumCr / 4);
                indexCount++;
            }
        }

        // replicate each downsampled pixel into 4 pixels
        short[] resizedAverageCbs = new short[width * height];
        short[] resizedAverageCrs = new short[width * height];

        for (int y = 0; y < downsampledHeight; y++) {
            for (int x = 0; x < downsampledWidth; x++) {
                int downsampledIndex = y * downsampledWidth + x;
                short cbValue = averageCbs[downsampledIndex];
                short crValue = averageCrs[downsampledIndex];

                for (int dy = 0; dy < 2; dy++) {
                    for (int dx = 0; dx < 2; dx++) {
                        int upsampledIndex = (2 * y + dy) * width + (2 * x + dx);
                        resizedAverageCbs[upsampledIndex] = cbValue;
                        resizedAverageCrs[upsampledIndex] = crValue;
                    }
                }
            }
        }

        return new short[][]{resizedAverageCbs, resizedAverageCrs};
    }

    private static byte[][] discreteCosineTransform(short[] yLuminance, int height, int width) {
        byte[][] transformedYLuminance = new byte[height][width];

        for (int y = 0; y < height; y += 8) {
            for (int x = 0; x < width; x += 8) {
                for (int u = 0; u < 8; u++) {
                    for (int v = 0; v < 8; v++) {
                        double sum = 0.0;
                        for (int i = 0; i < 8; i++) {
                            for (int j = 0; j < 8; j++) {
                                if (y + i < height && x + j < width) {
                                    int pixelIndex = (y + i) * width + (x + j);
                                    byte pixelValue = (byte) (yLuminance[pixelIndex] - 128);
                                    sum += pixelValue *
                                        Math.cos((2 * i + 1) * u * Math.PI / 16) *
                                        Math.cos((2 * j + 1) * v * Math.PI / 16);
                                }
                            }
                        }
                        double au = u == 0 ? 1.0 / Math.sqrt(2) : 1.0;
                        double av = v == 0 ? 1.0 / Math.sqrt(2) : 1.0;
                        sum *= 0.25 * au * av;
                        if (y + u < height && x + v < width) {
                            transformedYLuminance[y + u][x + v] = (byte) Math.round(
                                (sum / QUANTIZATION_MATRIX[u][v]));
                        }
                    }
                }
            }
        }
        return transformedYLuminance;
    }

    private static byte[][] extractBlock(byte[][] data, int x, int y) {
        byte[][] block = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            if (y + i < data.length) {
                System.arraycopy(data[y + i], x, block[i], 0, Math.min(8, data[y + i].length - x));
            }
        }
        return block;
    }

    private static String[] encodeRLE(byte[][] data) {
        List<String> encodedData = new ArrayList<>();
        for (byte[] datum : data) {
            int count = 0;
            boolean zeroRun = false;
            for (byte value : datum) {
                if (value == 0) {
                    if (!zeroRun) {
                        zeroRun = true;
                        count = 1;
                    } else {
                        count++;
                    }
                } else {
                    if (zeroRun) {
                        encodedData.add("0:" + count);
                        count = 0;
                        zeroRun = false;
                    }
                    encodedData.add(String.valueOf(value));
                }
            }
            if (zeroRun) {
                encodedData.add("0:" + count);
            }
        }
        return encodedData.toArray(new String[0]);
    }

    private static void writeJPEGHeader(BufferedOutputStream bos, int width, int height)
        throws IOException {
        bos.write(new byte[]{
            (byte) 0xFF, (byte) 0xD8,
            (byte) 0xFF, (byte) 0xE0,
            0x00, 0x10,
            'J', 'F', 'I', 'F', 0x00,
            0x01, 0x01,
            0x00,
            0x00, 0x01,
            0x00, 0x01,
            0x00, 0x00
        });

        bos.write(new byte[]{
            (byte) 0xFF, (byte) 0xC0,
            0x00, 0x11,
            0x08,
            (byte) (height >> 8), (byte) height,
            (byte) (width >> 8), (byte) width,
            0x03,
            0x01, 0x11, 0x00,
            0x02, 0x11, 0x01,
            0x03, 0x11, 0x01
        });
    }

    private static void writeHuffmanData(BufferedOutputStream bos, List<String> encodedData)
        throws IOException {
        bos.write(new byte[]{
            (byte) 0xFF, (byte) 0xC4,
            0x01, (byte) 0xA2,
            0x00,
            0x00, 0x01, 0x05, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B,
            0x10,
            0x00, 0x02, 0x01, 0x03, 0x03, 0x02, 0x04, 0x03, 0x05, 0x05, 0x04, 0x04, 0x00, 0x00,
            0x01, 0x7D,
            0x01, 0x02, 0x03, 0x00, 0x04, 0x11, 0x05, 0x12, 0x21, 0x31, 0x41, 0x06, 0x13, 0x51,
            0x61,
            0x07, 0x22, 0x71, 0x14, 0x32, (byte) 0x81, (byte) 0x91, (byte) 0xA1, 0x08, 0x23, 0x42,
            (byte) 0xB1, (byte) 0xC1,
            0x15, 0x52, (byte) 0xD1, (byte) 0xF0, 0x24, 0x33, 0x62, 0x72, (byte) 0x82, 0x09, 0x0A,
            0x16, 0x17,
            0x18, 0x19, 0x1A, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x34, 0x35, 0x36, 0x37, 0x38,
            0x39, 0x3A, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x53, 0x54, 0x55, 0x56,
            0x57, 0x58, 0x59, 0x5A, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x73, 0x74,
            0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, (byte) 0x83, (byte) 0x84, (byte) 0x85, (byte) 0x86,
            (byte) 0x87,
            (byte) 0x88, (byte) 0x89, (byte) 0x8A, (byte) 0x92, (byte) 0x93, (byte) 0x94,
            (byte) 0x95, (byte) 0x96,
            (byte) 0x97, (byte) 0x98, (byte) 0x99, (byte) 0x9A, (byte) 0xA2, (byte) 0xA3,
            (byte) 0xA4, (byte) 0xA5,
            (byte) 0xA6, (byte) 0xA7, (byte) 0xA8, (byte) 0xA9, (byte) 0xAA, (byte) 0xB2,
            (byte) 0xB3, (byte) 0xB4,
            (byte) 0xB5, (byte) 0xB6, (byte) 0xB7, (byte) 0xB8, (byte) 0xB9, (byte) 0xBA,
            (byte) 0xC2, (byte) 0xC3,
            (byte) 0xC4, (byte) 0xC5, (byte) 0xC6, (byte) 0xC7, (byte) 0xC8, (byte) 0xC9,
            (byte) 0xCA, (byte) 0xD2,
            (byte) 0xD3, (byte) 0xD4, (byte) 0xD5, (byte) 0xD6, (byte) 0xD7, (byte) 0xD8,
            (byte) 0xD9, (byte) 0xDA,
            (byte) 0xE1, (byte) 0xE2, (byte) 0xE3, (byte) 0xE4, (byte) 0xE5, (byte) 0xE6,
            (byte) 0xE7, (byte) 0xE8,
            (byte) 0xE9, (byte) 0xEA, (byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4,
            (byte) 0xF5, (byte) 0xF6,
            (byte) 0xF7, (byte) 0xF8, (byte) 0xF9, (byte) 0xFA
        });

        for (String data : encodedData) {
            bos.write(data.getBytes());
        }
    }

    private static void writeJPEGFooter(BufferedOutputStream bos) throws IOException {
        bos.write(new byte[]{
            (byte) 0xFF, (byte) 0xD9 // EOI
        });
    }
    /**
     * Converts the given input file to JPEG format.
     *
     * @param inputFile the input file to be converted
     * @param outputFile the output file to save the JPEG data
     * @throws IOException if an I/O error occurs
     */
    public static void convertToJpeg(File inputFile, File outputFile) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Remove alpha channel if it exists
        BufferedImage processedImage = removeAlphaChannel(inputImage);

        // Get a JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPEG format.");
        }
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);  // 50% compression
        }
        writer.write(null, new IIOImage(processedImage, null, null), param);

        ios.close();
        writer.dispose();
    }

    private static BufferedImage removeAlphaChannel(BufferedImage img) {
        if (!img.getColorModel().hasAlpha()) {
            return img;
        }

        BufferedImage target = createImage(img.getWidth(), img.getHeight(), false);
        Graphics2D g = target.createGraphics();
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return target;
    }

    private static BufferedImage createImage(int width, int height, boolean hasAlpha) {
        return new BufferedImage(width, height,
            hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    }
    /**
     * Creates an output file based on the input file.
     *
     * @param inputFile the input file to be converted
     * @return the output file
     */
    public static File createOutputFile(File inputFile) {
        String newFileName = generateNewFileName(inputFile);
        return new File(inputFile.getParent(), newFileName);
    }

    private static String generateNewFileName(File inputFile) {
        String originalFileName = inputFile.getName();
        String baseName = originalFileName.replaceAll("\\.[^.]+$", "");
        String extension = ".jpg";
        String newFileName = baseName + extension;

        File newFile = new File(inputFile.getParent(), newFileName);
        if (newFile.exists()) {
            newFileName = baseName + "__new" + extension;
            newFile = new File(inputFile.getParent(), newFileName);

            int counter = 1;
            while (newFile.exists()) {
                newFileName = baseName + "__new(" + counter + ")" + extension;
                newFile = new File(inputFile.getParent(), newFileName);
                counter++;
            }
        }
        return newFileName;
    }

}