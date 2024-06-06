package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.domain.services.SignupService;
import com.therotherithethethe.presentation.controllers.main.MainMenuFactory;
import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.domain.services.AccountService;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Controller class for the login view.
 */
public class LoginController implements Initializable {

    @FXML
    public TextField passwordTxtF;
    @FXML
    public AnchorPane mainAncPane;
    @FXML
    public TextField loginTxtF;
    @FXML
    public Button loginBtn;
    @FXML
    public Button forgotPassBtn;
    private final AccountService accService = AccountService.getInstance();
    private final SignupService signupService = SignupService.getInstance();
    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing to initialize

    }
    /**
     * Handles the click event for the forgot password button.
     *
     * @param ev the action event
     */
    @FXML
    private void handleForgotPassBtn(ActionEvent ev) {
        String nameOrEmail = loginTxtF.getText();
        /*if(nameOrEmail == null || nameOrEmail.isEmpty()) {
            return;
        }*/

        if(!accService.isExistInDb(new Account(null, nameOrEmail, nameOrEmail, null)) || nameOrEmail == null || nameOrEmail.isEmpty()) {
            var alert = new Alert(AlertType.ERROR);
            alert.setContentText("Credentials are invalid or account is missing");
            alert.showAndWait();
            return;
        }

        var foundedAccount = new Account()
            .findByColumn(acc -> acc.name.equals(nameOrEmail) || acc.email.equals(nameOrEmail))
            .getFirst();
        if(Objects.isNull(foundedAccount.session)) {
            foundedAccount.session = new ArrayList<>();
        }
        SignupService signupService = SignupService.getInstance();
        signupService.curAcc = foundedAccount;
        signupService.sendEmail();
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();

        /*FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getClassLoader()
            .getResource("/pages/signup/ConfirmEmailForCreatingAccount.fxml"));

        fxmlLoader.setController(new ConfirmEmailForChangePassController());*/
        SignupMenuFactory.addConfirmEmailForChangePassword(pane);
    }
    /**
     * Checks account credentials for login.
     */
    private void checkAccountCredentials() {
        String usernameOrEmail = loginTxtF.getText();
        String password = passwordTxtF.getText();
        var optionalAccount = getAccount(usernameOrEmail);

        boolean isAccInDb = optionalAccount.isPresent();
        if(isAccInDb) {
            var foundAcc = optionalAccount.get();
            if(password.hashCode() == foundAcc.getPassword()) {
                signupService.curAcc = foundAcc;
                setMainMenu();
                return;
            }

        }
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Error. Credentials are invalid or optionalAccount is missing");
        alert.showAndWait();
    }
    /**
     * Sets the main menu after successful login.
     */
    private void setMainMenu() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("You`re Welcome!");
        alert.showAndWait();
        Parent mainMenu = MainMenuFactory.getMainMenu();
        Stage mainStage = (Stage) mainAncPane.getScene().getWindow();
        Scene scene = new Scene(mainMenu);
        mainStage.setScene(scene);
    }
    /**
     * Gets the account by username or email.
     *
     * @param usernameOrEmail the username or email
     * @return an optional containing the account or empty if not found
     */
    private static Optional<Account> getAccount(String usernameOrEmail) {
        return new Account()
            .findByColumn(
                acc -> acc.name.equals(usernameOrEmail) || acc.email.equals(usernameOrEmail))
            .stream()
            .findFirst();
    }
    /**
     * Handles the click event for creating an account.
     *
     * @param event the action event
     */
    @FXML
    private void handleCreateAccountMenu(ActionEvent event) {
        /*try {
            AnchorPane registerPane =
                FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/pages/signup/Register.fxml")));

            HBox.setHgrow(registerPane, Priority.SOMETIMES);
            Pane pane = (Pane) mainAncPane.getParent();
            pane.getChildren().removeLast();
            pane.getChildren().add(registerPane);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
      Pane pane = (Pane) mainAncPane.getParent();
      pane.getChildren().removeLast();
      SignupMenuFactory.addRegisterMenu(pane);

    }
    /**
     * Handles the click event for the login button.
     *
     * @param event the action event
     */
    public void loginBtnClick(ActionEvent event) {
        checkAccountCredentials();
    }
}
