package com.therotherithethethe.presentation.controllers.signup;

import com.therotherithethethe.domain.validation.account.password.PasswordValidationHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class NewPasswordController implements Initializable {

    @FXML
    public AnchorPane mainAncPane;
    @FXML
    public TextField passwordTextF;
    @FXML
    public TextField rePassTextF;
    @FXML
    public Button confirmNewPassBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmNewPassBtn.setDisable(true);
        passwordTextF.textProperty().addListener((_, _, newVal) -> {
            boolean valid = new PasswordValidationHandler().check(newVal);
            if(!valid) {
                confirmNewPassBtn.setDisable(true);

            }
        });
    }
}
