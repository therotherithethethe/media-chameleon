package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.domain.validation.TextValidationHandler;
import com.therotherithethethe.domain.validation.account.email.EmailValidationHandler;
import com.therotherithethethe.domain.validation.account.password.PasswordValidationHandler;
import com.therotherithethethe.domain.validation.account.username.UsernameCharacterValidationHandler;
import com.therotherithethethe.domain.validation.account.username.UsernameConsecutiveSymbolValidationHandler;
import com.therotherithethethe.domain.validation.account.username.UsernameLengthValidationHandler;
import com.therotherithethethe.domain.services.AccountService;
import com.therotherithethethe.domain.services.SignupService;
import java.net.URL;
import java.util.ArrayList;
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
/**
 * Controller class for the register view.
 */
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
    private final AccountService accountService = AccountService.getInstance();
    private boolean isFormValid = false;
    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUsernameTextFieldValidationListener();
        initializePasswordPassFieldValidationListener();
        initializeRePasswordPassFieldValidationListener();
        initializeEmailTextFieldValidationListener();
        confirmBtn.setDisable(true);
    }

    /**
     * Handles the click event for the confirm button.
     *
     * @param event the action event
     */
    @FXML
    private void handleConfirmMenu(ActionEvent event) {
        var account = new Account(null, usernameTxtF.getText(), emailTxtF.getText(),
            passwordPassF.getText());
        SignupService signupService = SignupService.getInstance();
        signupService.curAcc = account;

        if (accountService.isExistInDb(account)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("account is already in db");
            alert.showAndWait();
            return;
        }
        signupService.sendEmail();
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SignupMenuFactory.addConfirmMenu(pane);
    }

    /**
     * Updates the confirm button status based on form validation.
     */
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

    /**
     * Initializes the email text field validation listener.
     */
    private void initializeEmailTextFieldValidationListener() {
        TextValidationHandler emailValidationHandler = new EmailValidationHandler();
        textFieldSetValidationEventHandler(emailValidationHandler, emailTxtF);
    }
    /**
     * Initializes the re-password password field validation listener.
     */
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
    /**
     * Initializes the username text field validation listener.
     */
    private void initializeUsernameTextFieldValidationListener() {
        TextValidationHandler validationHandler =
            TextValidationHandler.link(
                new UsernameLengthValidationHandler(),
                new UsernameConsecutiveSymbolValidationHandler(),
                new UsernameCharacterValidationHandler());

        textFieldSetValidationEventHandler(validationHandler, usernameTxtF);
    }
    /**
     * Initializes the password password field validation listener.
     */
    private void initializePasswordPassFieldValidationListener() {
        TextValidationHandler passwordValidationHandler = new PasswordValidationHandler();
        textFieldSetValidationEventHandler(passwordValidationHandler, passwordPassF);
    }
    /**
     * Sets the validation event handler for a text field.
     *
     * @param textValidationHandler the text validation handler
     * @param textField the text field
     */
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
    /**
     * Handles the click event for the login account button.
     *
     * @param event the action event
     */
    @FXML
    private void handleLoginAccount(ActionEvent event) {
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SignupMenuFactory.addLoginMenu(pane);
    }
}
