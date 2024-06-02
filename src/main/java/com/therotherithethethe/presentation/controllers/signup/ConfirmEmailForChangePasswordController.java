package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.domain.services.SignupService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Controller class for the confirm email view.
 */
public class ConfirmEmailForChangePasswordController implements Initializable {

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
    /**
     * Handles the click event for the confirm account button.
     *
     * @param event the action event
     */
    @FXML
    public void confirmAccountBtnClick(ActionEvent event) {
        Alert alert;
        SignupService signupService = SignupService.getInstance();
        try {
            if(signupService.getValidationCode() == Integer.parseInt(verificationCodeTxtF.getText())) {
                Pane pane = (Pane) mainAncPane.getParent();
                pane.getChildren().removeLast();
                SignupMenuFactory.addChangePasswordMenu(pane);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        alert = new Alert(AlertType.ERROR);
        alert.setContentText("Wrong code. returning.....");
        alert.showAndWait();
        handleLoginMenu();


    }
    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTimer();
    }
    /**
     * Handles the click event for the resend code button.
     *
     * @param event the action event
     */
    @FXML
    private void resendCodeBtnClick(ActionEvent event) {
        initializeTimer();
        resendCodeBtn.setDisable(true);
        SignupService signupService = SignupService.getInstance();
        signupService.sendEmail();
    }
    /**
     * Initializes the timer for the resend code button.
     */
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
    /**
     * Handles the click event for the return register menu button.
     *
     * @param event the action event
     */
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
        handleLoginMenu();
    }
    /**
     * Handles the action to return to the register menu.
     */
    private void handleLoginMenu() {
        Pane pane = (Pane) mainAncPane.getParent();
        pane.getChildren().removeLast();
        SignupMenuFactory.addLoginMenu(pane);

    }
}
