package com.therotherithethethe.presentation.controllers.main;

import com.therotherithethethe.domain.services.SessionService;
import com.therotherithethethe.domain.services.SignupService;
import com.therotherithethethe.presentation.customcontrols.ImageCheckBox;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterMenuController implements Initializable {

    @FXML
    public FlowPane imageContainerFlowPane;
    @FXML
    public Button addNewButtonToHboxBtn;
    private final Logger LOGGER = LoggerFactory.getLogger(ConverterMenuController.class);
    @FXML
    public TextField directoryPathTextF;
    @FXML
    public Label accountNameLbl;
    @FXML
    public VBox leftPanelVbox;
    @FXML
    public Button increaseImageSizeBtn;
    @FXML
    public Button decreaseImageSizeBtn;
    @FXML
    public Button reloadImagesBtn;
    private final SessionService sessionService = SessionService.getInstance();
    private final SignupService signupService = SignupService.getInstance();
    Task<Void> loadImagesTask = null;

    private static final int RESIZE_MARGIN = 10;
    private boolean isResizingRight;
    private boolean isResizingBottom;
    private double clickOffsetX;
    private double clickOffsetY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String initializePath = "C:\\Users\\zhenya\\Downloads\\TestFolder";
        initializePathAndImages(initializePath);
        sessionService.createSession(signupService.curAcc);
    }

    private void initializePathAndImages(String initializePath) {
        loadImagesAsync(initializePath);
        directoryPathTextF.setText(initializePath);

        directoryPathTextF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                updateImageLoading(newValue);
            }
        });
    }

    private void updateImageLoading(String newPath) {
        imageContainerFlowPane.getChildren().clear();
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
                    ImageCheckBox imageButton = new ImageCheckBox(file.getAbsolutePath());
                    Platform.runLater(() -> {
                        if (!isCancelled()) {

                            imageContainerFlowPane.getChildren().add(imageButton);
                        }
                    });
                }
                return null;
            }
        };
        new Thread(loadImagesTask).start();
    }

    public void convertToJpeg(File inputFile, File outputFile) throws IOException {
        // Read the input image
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Remove alpha channel if it exists
        BufferedImage processedImage = removeAlphaChannel(inputImage);

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
        writer.write(null, new IIOImage(processedImage, null, null), param);

        // Cleanup
        ios.close();
        writer.dispose();
    }

    private BufferedImage removeAlphaChannel(BufferedImage img) {
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

    private BufferedImage createImage(int width, int height, boolean hasAlpha) {
        return new BufferedImage(width, height,
            hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    }

    private File createOutputFile(File inputFile) {
        String newFileName = generateNewFileName(inputFile);
        return new File(inputFile.getParent(), newFileName);
    }

    private String generateNewFileName(File inputFile) {
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

    @FXML
    private void convert(ActionEvent ev) {
        List<ImageCheckBox> selectedCheckBoxes = new java.util.ArrayList<>(
            imageContainerFlowPane.getChildren().stream()
                .map(ImageCheckBox.class::cast)
                .filter(ImageCheckBox::isSelected)
                .toList());

        Collections.reverse(selectedCheckBoxes);

        selectedCheckBoxes.forEach(inputCheckBox -> {
            File inputFile = new File(inputCheckBox.getFullPath());
            File outputFile = createOutputFile(inputFile);

            try {
                convertToJpeg(inputFile, outputFile);
                ImageCheckBox newCheckBox = new ImageCheckBox(outputFile.getPath());
                imageContainerFlowPane.getChildren().add(newCheckBox);
            } catch (IOException e) {
                throw new RuntimeException("Failed to convert file: " + inputFile.getPath(), e);
            }
        });
    }

    @FXML
    private void imageContainerFlowPane_KeyPressed(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.A) {
            if(isAllCheckBoxesSelected()) {
                forEachForAllCheckBoxes(el -> el.setSelected(false));
                return;
            }
            forEachForAllCheckBoxes(el -> el.setSelected(true));
        }
    }


    private void forEachForAllCheckBoxes(Consumer<? super ImageCheckBox> consumer) {
        imageContainerFlowPane.getChildren()
            .stream()
            .map(ImageCheckBox.class::cast)
            .forEach(consumer);
    }

    private boolean isAllCheckBoxesSelected() {
        return imageContainerFlowPane.getChildren()
            .stream()
            .map(ImageCheckBox.class::cast)
            .allMatch(CheckBox::isSelected);
    }

    @FXML
    private void onMousePressed(MouseEvent event) {
        if (isInResizeMarginRight(event)) {
            isResizingRight = true;
            clickOffsetX = event.getX();
        }
        if (isInResizeMarginBottom(event)) {
            isResizingBottom = true;
            clickOffsetY = event.getY();
        }
    }
    @FXML
    private void onMouseDragged(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        if (isResizingRight) {
            double newWidth = event.getX() - clickOffsetX + pane.getWidth();
            pane.setPrefWidth(newWidth);
        }
        if (isResizingBottom) {
            double newHeight = event.getY() - clickOffsetY + pane.getHeight();
            pane.setPrefHeight(newHeight);
        }
    }

    @FXML
    private void onMouseMoved(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        if (isInResizeMarginRight(event)) {
            pane.setCursor(javafx.scene.Cursor.E_RESIZE);
        } else if (isInResizeMarginBottom(event)) {
            pane.setCursor(javafx.scene.Cursor.S_RESIZE);
        } else if (isInResizeMarginBottomRight(event)) {
            pane.setCursor(javafx.scene.Cursor.SE_RESIZE);
        } else {
            pane.setCursor(javafx.scene.Cursor.DEFAULT);
        }
    }

    private boolean isInResizeMarginRight(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        return event.getX() >= pane.getWidth() - RESIZE_MARGIN
            && event.getY() <= pane.getHeight() - RESIZE_MARGIN;
    }

    private boolean isInResizeMarginBottom(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        return event.getY() >= pane.getHeight() - RESIZE_MARGIN
            && event.getX() <= pane.getWidth() - RESIZE_MARGIN;
    }

    private boolean isInResizeMarginBottomRight(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        return event.getX() >= pane.getWidth() - RESIZE_MARGIN
            && event.getY() >= pane.getHeight() - RESIZE_MARGIN;
    }

    private void resetResizeFlags() {
        isResizingRight = false;
        isResizingBottom = false;
    }

    @FXML
    private void increaseImageSizeBtn_Click(ActionEvent event) {
        changeSizeOfImages(25D);
    }

    private void changeSizeOfImages(final double changeSizeNum) {
        ImageCheckBox.width += changeSizeNum;
        ImageCheckBox.height += changeSizeNum;
        imageContainerFlowPane.getChildren().stream().map(ImageCheckBox.class::cast)
            .forEach(node -> node.addSize(changeSizeNum, changeSizeNum));
    }

    @FXML
    public void decreaseImageSizeBtn_Click(ActionEvent event) {
        changeSizeOfImages(-25D);
    }
    @FXML
    private void reloadImagesBtn_Click(ActionEvent event) {
        imageContainerFlowPane.getChildren().clear();
        loadImagesAsync(directoryPathTextF.getText());
    }
}
