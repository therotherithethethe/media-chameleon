package com.therotherithethethe.launcher;

import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root =
            FXMLLoader.load(
                Objects.requireNonNull(
                    getClass().getClassLoader().getResource("pages/signup/SignupMenu.fxml")));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
