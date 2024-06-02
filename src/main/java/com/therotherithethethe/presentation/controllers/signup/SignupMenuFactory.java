package com.therotherithethethe.presentation.controllers.signup;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
/**
 * Factory class for creating signup menu views.
 */
public class SignupMenuFactory {
    private SignupMenuFactory(){}
    /**
     * Adds the register menu to the specified pane.
     *
     * @param paneToAdd the pane to add the register menu to
     * @return the added register menu
     */
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
    /**
     * Adds the login menu to the specified pane.
     *
     * @param paneToAdd the pane to add the login menu to
     * @return the added login menu
     */
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
    /**
     * Adds the confirm menu to the specified pane.
     *
     * @param paneToAdd the pane to add the confirm menu to
     * @return the added confirm menu
     */
    public static AnchorPane addConfirmMenu(Pane paneToAdd) {
        try {
            AnchorPane confirmPane = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource(
                        "/pages/signup/ConfirmEmailForCreatingAccount.fxml")));

            HBox.setHgrow(confirmPane, Priority.SOMETIMES);
            paneToAdd.getChildren().add(confirmPane);
            return confirmPane;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Adds the confirm new password menu to the specified pane.
     *
     * @param paneToAdd the pane to add the confirm new password menu to
     * @return the added confirm new password menu
     */
    public static AnchorPane addConfirmEmailForChangePassword(Pane paneToAdd) {
        try {
            AnchorPane newPassword = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/ConfirmEmailForChangePassword.fxml")));

            HBox.setHgrow(newPassword, Priority.SOMETIMES);
            paneToAdd.getChildren().add(newPassword);
            return newPassword;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AnchorPane addChangePasswordMenu(Pane paneToAdd) {
        try {
            AnchorPane loginPane = FXMLLoader.load(
                Objects.requireNonNull(
                    SignupMenuFactory.class.getResource("/pages/signup/ChangePassword.fxml")));

            HBox.setHgrow(loginPane, Priority.SOMETIMES);
            paneToAdd.getChildren().add(loginPane);
            return loginPane;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
