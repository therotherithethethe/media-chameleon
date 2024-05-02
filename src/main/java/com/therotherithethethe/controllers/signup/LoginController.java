package com.therotherithethethe.controllers.signup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class LoginController implements Initializable {

    @FXML
    public TextField loginLbl;
    @FXML
    public TextField passwordLbl;
    @FXML
    public Label registerLbl;
    @FXML
    public AnchorPane mainAncPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void handleCreateAccount(MouseEvent event) {
        try {
            AnchorPane registerPane = FXMLLoader.load(getClass().getResource("/pages/signup/Register.fxml"));

            HBox.setHgrow(registerPane, Priority.SOMETIMES);
            Pane pane = (Pane) mainAncPane.getParent();
            pane.getChildren().removeLast();
            pane.getChildren().add(registerPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
