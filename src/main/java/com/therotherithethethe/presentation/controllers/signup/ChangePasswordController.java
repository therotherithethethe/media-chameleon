package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.domain.services.SignupService;
import com.therotherithethethe.domain.validation.TextValidationHandler;
import com.therotherithethethe.domain.validation.account.password.PasswordValidationHandler;
import com.therotherithethethe.persistance.entity.Account;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class ChangePasswordController implements Initializable {

    @FXML
    public PasswordField passwordPassF;
    @FXML
    public PasswordField rePasswordPassF;
    @FXML
    public Button confirmBtn;
    @FXML
    public Button returnToLoginMenuBtn;
    @FXML
    public AnchorPane mainAncPane;
    private boolean isFormValid = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePasswordPassFieldValidationListener();
        initializeRePasswordPassFieldValidationListener();
        confirmBtn.setDisable(true);
    }
    private void initializePasswordPassFieldValidationListener() {
        TextValidationHandler passwordValidationHandler = new PasswordValidationHandler();
        textFieldSetValidationEventHandler(passwordValidationHandler, passwordPassF);
    }
    private void initializeRePasswordPassFieldValidationListener() {
        TextValidationHandler rePasswordValidationHandler =
            new TextValidationHandler() {
                @Override
                public boolean check(String text) {
                    if (!text.equals(passwordPassF.getText())) {
                        return false;
                    }
                    return checkNext(text);
                }
            };
        textFieldSetValidationEventHandler(rePasswordValidationHandler, rePasswordPassF);
    }
    private void textFieldSetValidationEventHandler(
        TextValidationHandler textValidationHandler, TextField textField) {
        textField
            .textProperty()
            .addListener(
                (_, _, newValue) -> {
                    boolean valid = textValidationHandler.check(newValue);
                    if (!valid) {
                        textField.setStyle(
                            "-fx-text-fill: red; "
                                + "-fx-focus-color: red; "
                                + "-fx-faint-focus-color: rgba(255, 0, 0, 0.01)");
                    } else {
                        textField.setStyle("");
                    }
                    updateConfirmButtonStatus();
                });
    }
    private void updateConfirmButtonStatus() {
        isFormValid =
            passwordPassF.getText().length() >= 4
                && passwordPassF.getText().length() <= 20
                && rePasswordPassF.getText().equals(passwordPassF.getText());

        confirmBtn.setDisable(!isFormValid);
    }

    public void returnToLoginMenuBtn_Click(ActionEvent event) {
        changeToLoginPage();
    }

    private void changeToLoginPage() {
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SignupMenuFactory.addLoginMenu(pane);
    }
    @FXML
    public void confirmBtn_Click(ActionEvent event) {
        SignupService instance = SignupService.getInstance();
        Account curAcc = instance.curAcc;
        curAcc.setPassword(passwordPassF.getText());
        curAcc.save();
        var alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("The password successfully changed");
        alert.showAndWait();
        changeToLoginPage();
    }
}
