package com.therotherithethethe.presentation.controllers.main;

import com.therotherithethethe.domain.services.FileService;
import com.therotherithethethe.domain.services.SessionService;
import com.therotherithethethe.domain.services.SignupService;
import com.therotherithethethe.domain.services.converter.JpegConverter;
import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.persistance.entity.InitializationSettings;
import com.therotherithethethe.presentation.customcontrols.ImageCheckBox;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Controller class for the converter menu.
 */
public class ConverterMenuController implements Initializable {

    @FXML
    public FlowPane imageContainerFlowPane;
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
    private final FileService fileService = FileService.getInstance();
    @FXML
    public ProgressBar imageConvertationStateProgressBar;
    @FXML
    public Button convertImagesBtn;
    @FXML
    public Button deleteAccountBtn;
    @FXML
    public Button openDirectoryBtn;
    @FXML
    public TextField findByNameTextField;
    @FXML
    public Button logoutBtn;
    Task<Void> loadImagesTask = null;

    private static final int RESIZE_MARGIN = 10;
    private boolean isResizingRight;
    private boolean isResizingBottom;
    private double clickOffsetX;
    private double clickOffsetY;
    InitializationSettings initializationSettings = InitializationSettings.getInstance();
    /**
     * Initializes the controller.
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String initializePath = initializationSettings.initializeDirectory;
        initializePathAndImages(initializePath);
        var optionalAccount = new Account().findByColumn(ac -> ac.name.equals(signupService.curAcc.name)).stream().findFirst();
        signupService.curAcc = optionalAccount.get();
        sessionService.createSession(signupService.curAcc);
        accountNameLbl.setText(signupService.curAcc.name);
    }

    private void initializePathAndImages(String initializePath) {
        loadImagesAsync(initializePath);
        directoryPathTextF.setText(initializePath);

        directoryPathTextF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                updateImageLoading(newValue);
                initializationSettings.initializeDirectory = newValue;
            }
        });

        findByNameTextField.textProperty().addListener((_, _, newValue) -> {
            filterImages(newValue);
        });
    }

    private void filterImages(String filename) {
        imageContainerFlowPane.getChildren().stream().map(ImageCheckBox.class::cast).forEach(el -> {
            boolean isNameMatches = el.fileNameLbl.getText().toLowerCase()
                .contains(filename.toLowerCase());
            el.setVisible(isNameMatches);
            el.setManaged(isNameMatches);
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


    @FXML
    private void convertImagesBtn_Click(ActionEvent ev) {
        List<ImageCheckBox> selectedCheckBoxes = new java.util.ArrayList<>(
            imageContainerFlowPane.getChildren().stream()
                .map(ImageCheckBox.class::cast)
                .filter(ImageCheckBox::isSelected)
                .toList());

        Collections.reverse(selectedCheckBoxes);

        new Thread(() -> {
            produceConvertation(selectedCheckBoxes);
        }).start();
    }

    private void produceConvertation(List<ImageCheckBox> selectedCheckBoxes) {
        Platform.runLater(() -> imageConvertationStateProgressBar.setProgress(0d));

        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = (int) Math.ceil((double) selectedCheckBoxes.size() / numThreads);
        double deltaProgress = 1.0 / selectedCheckBoxes.size();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<List<ImageCheckBox>> chunks = new ArrayList<>();

        for (int i = 0; i < selectedCheckBoxes.size(); i += chunkSize) {
            int end = Math.min(selectedCheckBoxes.size(), i + chunkSize);
            chunks.add(selectedCheckBoxes.subList(i, end));
        }

        for (List<ImageCheckBox> chunk : chunks) {
            executorService.submit(() -> {
                for (ImageCheckBox inputCheckBox : chunk) {
                    String fullPath = inputCheckBox.getFullPath();
                    File inputFile = new File(fullPath);
                    File outputFile = JpegConverter.createOutputFile(inputFile);

                    Optional<com.therotherithethethe.persistance.entity.File> optionalFile = fileService.getFileByPath(fullPath);
                    com.therotherithethethe.persistance.entity.File file = optionalFile.orElseGet(
                        () -> new com.therotherithethethe.persistance.entity.File(null, fullPath));

                    file.save();
                    sessionService.saveSession(file);

                    try {
                        JpegConverter.convertToJpeg(inputFile, outputFile);

                        ImageCheckBox newCheckBox = new ImageCheckBox(outputFile.getPath());

                        Platform.runLater(() -> {
                            imageContainerFlowPane.getChildren().add(newCheckBox);
                            imageConvertationStateProgressBar.setProgress(imageConvertationStateProgressBar.getProgress() + deltaProgress);
                        });

                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Conversion Error");
                            alert.setHeaderText("Failed to convert file");
                            alert.setContentText("An error occurred while converting: " + inputFile.getPath());
                            alert.showAndWait();
                        });

                        throw new RuntimeException("Failed to convert file: " + inputFile.getPath(), e);
                    }
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        if(event.getCode() == KeyCode.DELETE) {
            imageContainerFlowPane.getChildren().stream().map(ImageCheckBox.class::cast).filter(ImageCheckBox::isSelected)
                .forEach(el -> deleteImage(el));
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

    public void deleteAccountBtn_Click(ActionEvent event) {
        signupService.curAcc.delete();
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Account deleted!");
            alert.showAndWait();
            setMainMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void setMainMenu() throws IOException {
        Parent root =
            FXMLLoader.load(
                Objects.requireNonNull(
                    getClass().getClassLoader().getResource("pages/signup/SignupMenu.fxml")));

        Stage mainStage = (Stage) imageContainerFlowPane.getScene().getWindow();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
    }

    public void openDirectoryBtn_Click(ActionEvent event) {
        chooseDirectory();
    }

    private void chooseDirectory() {
        String pathname = directoryPathTextF.getText();
        openDirectoryChooser(pathname);
    }

    private void openDirectoryChooser(String pathname) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose directory with images");

        directoryChooser.setInitialDirectory(new File(pathname));

        try {
            String path = directoryChooser.showDialog(convertImagesBtn.getScene().getWindow())
                .getPath();
            directoryPathTextF.setText(path);
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText(STR."error opening a \{pathname} directory");
            alert.showAndWait();
            openDirectoryChooser("C:\\");
        }
    }
    private void deleteImage(ImageCheckBox imageCheckBox) {
        imageCheckBox.deleteFile();
        Platform.runLater(() -> imageContainerFlowPane.getChildren().remove(imageCheckBox));

    }

    public void logoutBtn_Click(ActionEvent event) {
        try {
            setMainMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
