package com.therotherithethethe.controllers.signup;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class SignupMenuController implements Initializable {

    @FXML
    public HBox menuHBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeLoginMenu();
    }

    private void initializeLoginMenu() {
        AnchorPane loginAncPane = new AnchorPane();
        try {
            loginAncPane = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/pages/signup/Login.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        menuHBox.getChildren().removeLast();
        menuHBox.getChildren().add(loginAncPane);
        HBox.setHgrow(loginAncPane, Priority.SOMETIMES);
    }
}
