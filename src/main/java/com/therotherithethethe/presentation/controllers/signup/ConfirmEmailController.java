package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.presentation.controllers.main.MainMenuFactory;
import com.therotherithethethe.domain.services.SignupServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfirmEmailController implements Initializable {

    @FXML
    public Button returnRegisterMenuBtn;
    @FXML
    public AnchorPane mainAncPane;
    @FXML
    public Label timerLbl;
    @FXML
    public Button resendCodeBtn;
    @FXML
    public Button confirmAccountBtn;
    @FXML
    public TextField verificationCodeTxtF;
    @FXML
    public void confirmAccountBtnClick(ActionEvent event) {
        Alert alert;
        SignupServiceImpl signupService = SignupServiceImpl.getInstance();
        if(signupService.getValidationCode() == Integer.parseInt(verificationCodeTxtF.getText())) {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Thanks for registering.");
            alert.showAndWait();
            signupService.curAcc.save();
            Parent mainMenu = MainMenuFactory.getMainMenu();
            Stage mainStage = (Stage) mainAncPane.getScene().getWindow();
            Scene scene = new Scene(mainMenu);
            mainStage.setScene(scene);
            return;
        }
        alert = new Alert(AlertType.ERROR);
        alert.setContentText("Wrong code. returning.....");
        alert.showAndWait();
        handleRegisterMenu();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTimer();
    }

    @FXML
    private void resendCodeBtnClick(ActionEvent event) {
        initializeTimer();
        resendCodeBtn.setDisable(true);
        SignupServiceImpl signupService = SignupServiceImpl.getInstance();
        signupService.sendEmail();
    }

    private void initializeTimer() {
        timerLbl.setText("40");
        timerLbl
            .textProperty()
            .addListener(
                (_, _, newValue) -> {
                    resendCodeBtn.setDisable(!newValue.equals("0"));
                });

        Task<Void> timerReduction =
            new Task<>() {
                @Override
                protected Void call() throws Exception {
                    int parsedInt = Integer.parseInt(timerLbl.getText());
                    while (parsedInt != 0) {
                        Thread.sleep(1000);
                        parsedInt--;
                        int finalParsedInt = parsedInt;
                        Platform.runLater(() -> timerLbl.setText(String.valueOf(finalParsedInt)));
                    }
                    return null;
                }
            };
        new Thread(timerReduction).start();
    }

    @FXML
    private void returnRegisterMenuBtnClick(ActionEvent event) {
        /*try {
            AnchorPane loginPane = FXMLLoader.load(
                getClass().getResource("/pages/signup/Register.fxml"));

            HBox.setHgrow(loginPane, Priority.SOMETIMES);
            Pane pane = (Pane) mainAncPane.getParent();
            pane.getChildren().removeLast();
            pane.getChildren().add(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        handleRegisterMenu();
    }

    private void handleRegisterMenu() {
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SignupMenuFactory.addRegisterMenu(pane);

    }
}
