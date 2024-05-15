package com.therotherithethethe.presentation.controllers.signup;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class SignupMenuFactory {
    private SignupMenuFactory(){}
    public static AnchorPane addRegisterMenu(Pane paneToAdd) {
        try {
            AnchorPane registerPane = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/Register.fxml")));

            HBox.setHgrow(registerPane, Priority.SOMETIMES);
            paneToAdd.getChildren().add(registerPane);
            return registerPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AnchorPane addLoginMenu(Pane paneToAdd) {
        try {
            AnchorPane loginPane = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/Login.fxml")));

            HBox.setHgrow(loginPane, Priority.SOMETIMES);
            paneToAdd.getChildren().add(loginPane);
            return loginPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AnchorPane addConfirmMenu(Pane paneToAdd) {
        try {
            AnchorPane confirmPane = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/ConfirmEmail.fxml")));

            HBox.setHgrow(confirmPane, Priority.SOMETIMES);
            paneToAdd.getChildren().add(confirmPane);
            return confirmPane;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AnchorPane addConfirmNewPassMenu(Pane paneToAdd) {
        try {
            AnchorPane newPassword = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/NewPassword.fxml")));

            HBox.setHgrow(newPassword, Priority.SOMETIMES);
            paneToAdd.getChildren().add(newPassword);
            return newPassword;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
