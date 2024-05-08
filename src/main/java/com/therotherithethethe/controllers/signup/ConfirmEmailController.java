package com.therotherithethethe.controllers.signup;

import com.therotherithethethe.ApplicationData;
import com.therotherithethethe.EmailSender;
import com.therotherithethethe.model.entity.Account;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

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
    public TextField emailCodeTxtF;
    private int confirmationCode;
    @FXML
    public void confirmAccountBtnClick(ActionEvent event) {
        if(confirmationCode == Integer.parseInt(emailCodeTxtF.getText())) {
            ApplicationData.account.save();
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText("wrong input");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTimer();
        generateCode();
        Account account = ApplicationData.account;
        EmailSender.sendEmail(account.email, "Email Verification", String.valueOf(confirmationCode));
    }

    private void generateCode() {

    }

    @FXML
    private void resendCodeBtnClick(ActionEvent event) {
        initializeTimer();
        resendCodeBtn.setDisable(true);
        generateCode();
    }

    private void initializeTimer() {
        timerLbl.setText("15");
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
    private void handleRegisterMenu(ActionEvent event) {
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
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SingupMenuFactory.addRegisterMenu(pane);
    }
}
