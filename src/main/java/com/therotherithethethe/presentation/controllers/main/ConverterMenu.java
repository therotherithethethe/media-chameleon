package com.therotherithethethe.presentation.controllers.main;

import com.therotherithethethe.presentation.controllers.customcontrol.ImageButton;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterMenu implements Initializable {

    @FXML
    public FlowPane btnContainerFlowPane;
    @FXML
    public Button addNewButtonToHboxBtn;
    private final Logger LOGGER = LoggerFactory.getLogger(ConverterMenu.class);
    @FXML
    public TextField directoryPathTextF;
    @FXML
    public Label accountNameLbl;
    Task<Void> loadImagesTask = null;

    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String initializePath = "C:\\Users\\zhenya\\Pictures\\Screenshots";
        loadImagesAsync(initializePath);
        directoryPathTextF.setText(initializePath);
        directoryPathTextF.textProperty().addListener((_, _, path) ->  {
            btnContainerFlowPane.getChildren().clear();
            loadImagesAsync(path);
        });
    }

    private void loadImagesAsync(String filepath) {
        Task<Void> loadImagesTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File directory = new File(filepath);

                final String[] formats = {".jpg", ".png", ".bmp"};
                File[] files = Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .filter(file -> file.isFile() && Arrays.stream(formats)
                        .anyMatch(format -> file.getName().endsWith(format)))
                    .toArray(File[]::new);

                for (File file : files) {
                    try {
                        ImageButton button = new ImageButton(file.getAbsolutePath());
                        Platform.runLater(
                            () -> btnContainerFlowPane.getChildren().add(button));
                    } catch (Throwable _) {}
                }
                return null;
            }
        };

        loadImagesTask.setOnFailed(_ -> {
            Throwable th = loadImagesTask.getException();
            LOGGER.error(th.getMessage());
        });

        new Thread(loadImagesTask).start();
    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String initializePath = "C:\\Users\\zhenya\\Pictures\\Screenshots";
        loadImagesAsync(initializePath);
        directoryPathTextF.setText(initializePath);

        directoryPathTextF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                updateImageLoading(newValue);
            }
        });
    }

    private void updateImageLoading(String newPath) {
        btnContainerFlowPane.getChildren().clear();
        if (loadImagesTask != null && loadImagesTask.isRunning()) {
            loadImagesTask.cancel();
        }
        loadImagesAsync(newPath);
    }

    private void loadImagesAsync(String filepath) {
        loadImagesTask = new Task<Void>() {
            @Override
            protected Void call() throws IOException {
                File directory = new File(filepath);

                if (!directory.exists() || !directory.isDirectory()) {
                    return null;
                }

                final String[] formats = {".jpg", "jpeg", ".png", ".bmp"};
                File[] files = Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .filter(file -> file.isFile() && Arrays.stream(formats)
                        .anyMatch(file.getName()::endsWith))
                    .toArray(File[]::new);

                for (File file : files) {
                    if (isCancelled()) {
                        break;
                    }
                    ImageButton imageButton = new ImageButton(file.getAbsolutePath());
                    imageButton.setOnAction(event -> {
                        try {
                            File outputFile = new File(file.getParent(), file.getName().replaceAll("\\.[^.]+$", ".jpg"));
                            convertToJpeg(file, outputFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Platform.runLater(() -> {
                        if (!isCancelled()) {

                            btnContainerFlowPane.getChildren().add(imageButton);
                        }
                    });
                }
                return null;
            }
        };
        new Thread(loadImagesTask).start();
    }

    private void convertToJpeg(File inputFile, File outputFile) throws IOException {
        // Read the input image
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Get a JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No writers found for JPEG format.");
        }
        ImageWriter writer = writers.next();

        // Set up the output stream
        ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile);
        writer.setOutput(ios);

        // Set the compression quality
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);  // 50% compression
        }

        // Write the image
        writer.write(null, new IIOImage(inputImage, null, null), param);

        // Cleanup
        ios.close();
        writer.dispose();
    }
}
