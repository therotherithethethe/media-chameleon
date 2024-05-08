package com.therotherithethethe.controllers.signup;

import com.therotherithethethe.ApplicationData;
import com.therotherithethethe.EmailSender;
import com.therotherithethethe.model.HibernateUtil;
import com.therotherithethethe.model.entity.Account;
import com.therotherithethethe.model.entity.validation.TextValidationHandler;
import com.therotherithethethe.model.entity.validation.account.username.UsernameCharacterValidationHandler;
import com.therotherithethethe.model.entity.validation.account.username.UsernameConsecutiveSymbolValidationHandler;
import com.therotherithethethe.model.entity.validation.account.username.UsernameLengthValidationHandler;
import com.therotherithethethe.viewmodel.AccountViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    public AnchorPane mainAncPane;
    private boolean isFormValid = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUsernameTextFieldValidationListener();
        initializePasswordPassFieldValidationListener();
        initializeRePasswordPassFieldValidationListener();
        initializeEmailTextFieldValidationListener();
        confirmBtn.setDisable(true);
    }


    @FXML
    private void handleConfirmMenu(ActionEvent event) {
    }
    /// copypaste but whatever
    private void updateConfirmButtonStatus() {
        isFormValid =
            !usernameTxtF.getText().isEmpty()
                && emailTxtF
                .getText()
                .matches(
                    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
                && passwordPassF.getText().length() >= 4
                && passwordPassF.getText().length() <= 20
                && rePasswordPassF.getText().equals(passwordPassF.getText());

        confirmBtn.setDisable(!isFormValid);
    }


    private void initializeEmailTextFieldValidationListener() {
        TextValidationHandler emailValidationHandler =
            new TextValidationHandler() {
                @Override
                public boolean check(String text) {
                    if (!text.matches(
                        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        return false;
                    }
                    return checkNext(text);
                }
            };
        textFieldSetValidationEventHandler(emailValidationHandler, emailTxtF);
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

    private void initializeUsernameTextFieldValidationListener() {
        TextValidationHandler validationHandler =
            TextValidationHandler.link(
                new UsernameLengthValidationHandler(),
                new UsernameConsecutiveSymbolValidationHandler(),
                new UsernameCharacterValidationHandler());

        textFieldSetValidationEventHandler(validationHandler, usernameTxtF);
    }

    private void initializePasswordPassFieldValidationListener() {
        TextValidationHandler passwordValidationHandler =
            new TextValidationHandler() {
                @Override
                public boolean check(String text) {
                    if (!(text.length() >= 4 && text.length() <= 20)) {
                        return false;
                    }
                    return checkNext(text);
                }
            };
        textFieldSetValidationEventHandler(passwordValidationHandler, passwordPassF);
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

    @FXML
    private void handleLoginAccount(ActionEvent event) {
        /*try {
            AnchorPane loginPane = FXMLLoader.load(
                getClass().getResource("/pages/signup/Login.fxml"));

            HBox.setHgrow(loginPane, Priority.SOMETIMES);
            Pane pane = (Pane) mainAncPane.getParent();
            pane.getChildren().removeLast();
            pane.getChildren().add(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SingupMenuFactory.addLoginMenu(pane);
    }
}
