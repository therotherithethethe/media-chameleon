package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.presentation.controllers.main.MainMenuFactory;
import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.domain.services.AccountService;
import com.therotherithethethe.domain.services.SignupServiceImpl;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // nothing to initialize

    }
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

        SignupServiceImpl signupService = SignupServiceImpl.getInstance();
        signupService.curAcc = foundedAccount;
        signupService.sendEmail();
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();

        /*FXMLLoader fxmlLoader = new FXMLLoader(getClass()
            .getClassLoader()
            .getResource("/pages/signup/ConfirmEmail.fxml"));

        fxmlLoader.setController(new ConfirmEmailForChangePassController());*/
        SignupMenuFactory.addConfirmMenu(pane);
    }
    private void checkAccountCredentials() {
        String usernameOrEmail = loginTxtF.getText();
        String password = passwordTxtF.getText();
        var account = getAccount(usernameOrEmail);

        boolean isAccInDb = account.stream().findFirst().isPresent();
        if(isAccInDb && password.hashCode() == account.get().getPassword()) {
            setMainMenu();
            return;
        }
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("Error. Credentials are invalid or account is missing");
        alert.showAndWait();
    }

    private void setMainMenu() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("You`re Welcome!");
        alert.showAndWait();
        Parent mainMenu = MainMenuFactory.getMainMenu();
        Stage mainStage = (Stage) mainAncPane.getScene().getWindow();
        Scene scene = new Scene(mainMenu);
        mainStage.setScene(scene);
    }

    private static Optional<Account> getAccount(String usernameOrEmail) {
        return new Account()
            .findByColumn(
                acc -> acc.name.equals(usernameOrEmail) || acc.email.equals(usernameOrEmail))
            .stream()
            .findFirst();
    }

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

    public void loginBtnClick(ActionEvent event) {
        checkAccountCredentials();
    }
}
