package com.therotherithethethe.presentation.customcontrols;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

/**
 * Represents a custom CheckBox control that displays an image along with file information.
 * This control extends the standard CheckBox class.
 */
public class ImageCheckBox extends CheckBox {

    public static double width = 100;
    public static double height = 100;
    @FXML
    public ImageView contentImageImgView;
    @FXML
    public Label fileNameLbl;
    @FXML
    public Label fileCreationDateLbl;
    @Getter
    private String fullPath;

    /**
     * Constructs an ImageCheckBox with the specified file path, width, and height.
     *
     * @param filePath The path of the image file to be displayed.
     * @param width    The width of the image.
     * @param height   The height of the image.
     * @throws IOException If an I/O error occurs.
     */
    public ImageCheckBox(String filePath, double width, double height) throws IOException {
        initializeFxml();
        initializeInformation(filePath, width, height);
    }
    /**
     * Constructs an ImageCheckBox with the specified file path.
     * Uses default width and height.
     *
     * @param filePath The path of the image file to be displayed.
     * @throws IOException If an I/O error occurs.
     */
    public ImageCheckBox(String filePath) throws IOException {
        initializeFxml();
        initializeInformation(filePath, ImageCheckBox.width, ImageCheckBox.height);
    }
    /**
     * Increases the size of the checkbox and its content by the specified width and height.
     *
     * @param width  The width to be added.
     * @param height The height to be added.
     */
    public void addSize(double width, double height) {
        if(getWidth() + width <= getMinWidth() || getHeight() + height <= getMinHeight())
            return;
        
        this.setPrefWidth(this.getPrefWidth() + width);
        this.setPrefHeight(this.getPrefHeight() + height);
        contentImageImgView.setFitHeight(contentImageImgView.getFitHeight() + height);
        contentImageImgView.setFitWidth(contentImageImgView.getFitWidth() + width);
    }

    private void initializeInformation(String filePath, double width, double height)
        throws IOException {
        File file = new File(filePath);
        String pathUrl = file.toURI().toURL().toString();
        fullPath = filePath;
        fileNameLbl.setText(file.getName());
        this.setPrefWidth(width + 30);
        this.setPrefHeight(height + 30);
        Image image = new Image(pathUrl, width, height - 10, true, false);
        contentImageImgView.setFitHeight(height - 10);
        contentImageImgView.setFitWidth(width);
        contentImageImgView.setImage(image);

        Path path = Paths.get(filePath);
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        LocalDateTime creationTime = attrs.creationTime()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        fileCreationDateLbl.setText(
            creationTime.toLocalDate().format(dateFormatter) + " " + creationTime.toLocalTime()
                .format(timeFormatter));
    }

    private void initializeFxml() {
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getClassLoader().getResource("customcontrols/ImageCheckBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(ImageCheckBox.this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes file for file that represents this control.
     */
    public void deleteFile() {
        new File(fullPath).delete();
    }
}
