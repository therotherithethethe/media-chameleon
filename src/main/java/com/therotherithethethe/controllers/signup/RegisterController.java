package com.therotherithethethe.controllers.signup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class RegisterController implements Initializable {

    @FXML
    public TextField usernameTxtF;
    @FXML
    public TextField emailTxtF;
    @FXML
    public PasswordField passwordPassF;
    @FXML
    public PasswordField rePasswordPassF;
    @FXML
    public Button confirmBtn;
    @FXML
    public Label loginLbl;
    @FXML
    public AnchorPane mainAncPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void handleLoginAccount(MouseEvent event) {
        try {
            AnchorPane loginPane = FXMLLoader.load(getClass().getResource("/pages/signup/Login.fxml"));

            HBox.setHgrow(loginPane, Priority.SOMETIMES);
            Pane pane = (Pane) mainAncPane.getParent();
            pane.getChildren().removeLast();
            pane.getChildren().add(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
