package com.therotherithethethe.presentation.controllers.signup;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
/**
 * Controller class for the signup menu view.
 */
public class SignupMenuController implements Initializable {

    @FXML
    public HBox menuHBox;
    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeLoginMenu();
    }
    /**
     * Initializes the login menu.
     */
    private void initializeLoginMenu() {
        AnchorPane loginAncPane = new AnchorPane();
        try {
            loginAncPane =
                FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/pages/signup/Login.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        menuHBox.getChildren().removeLast();
        menuHBox.getChildren().add(loginAncPane);
        HBox.setHgrow(loginAncPane, Priority.SOMETIMES);
    }
}
