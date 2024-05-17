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
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
/**
 * Custom button class for displaying images with additional metadata.
 */
public class ImageButton extends Button {

    private ImageView imageView;
    private String filename;
    private String filePath;
    private LocalDateTime date;
    /**
     * Constructs an ImageButton with the specified file path.
     *
     * @param filePath the file path of the image
     * @throws IOException if an I/O error occurs
     */
    public ImageButton(String filePath) throws IOException {

        File file = new File(filePath);
        String pathUrl = file.toURI().toURL().toString();
        this.filePath = pathUrl;
        String fileName = file.getName();
        this.filename = fileName;
        Image image = new Image(pathUrl, 100, 100, true, false);
        imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);

        VBox vbox = new VBox();
        this.getChildren().add(vbox);
        vbox.getChildren().add(imageView);
        VBox.setVgrow(imageView, Priority.ALWAYS);
        vbox.setMaxWidth(120);
        vbox.setMinWidth(120);
        vbox.setPrefWidth(120);
        vbox.setMinHeight(90);
        vbox.setMinHeight(90);
        vbox.setPrefHeight(90);
        vbox.setAlignment(Pos.CENTER);

        Path path = Paths.get(filePath);
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        LocalDateTime creationTime = attrs.creationTime()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        this.date = creationTime;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Label dateLbl = new Label(creationTime.toLocalDate().format(dateFormatter));
        Label timeLbl = new Label(creationTime.toLocalTime().format(timeFormatter));
        dateLbl.setAlignment(Pos.CENTER);
        timeLbl.setAlignment(Pos.CENTER);

        
        Label filenameLbl = new Label(fileName);
        filenameLbl.setAlignment(Pos.CENTER);
        filenameLbl.setContentDisplay(ContentDisplay.CENTER);
        filenameLbl.maxWidth(1.7976931348623157E308);
        vbox.getChildren().add(filenameLbl);

        HBox hBox = new HBox(2d);
        hBox.setAlignment(Pos.CENTER);
        hBox.maxWidth(1.7976931348623157E308);
        hBox.getChildren().add(dateLbl);
        hBox.getChildren().add(timeLbl);
        vbox.getChildren().add(hBox);
        this.setGraphic(vbox);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getFilename() {
        return filename;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getFilePath() {
        return filePath;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    /**
     * Gets the file name from a URL.
     *
     * @param imagePath the image path
     * @return the file name
     */
    private static String getFileNameFromUrl(String imagePath) {
        String[] filenameUrl = imagePath.split("\\\\\\\\[^\\\\\\\\]+$");
        return filenameUrl[filenameUrl.length - 1];
    }


}
