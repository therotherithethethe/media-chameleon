package com.therotherithethethe.launcher;

import com.therotherithethethe.domain.utils.HibernateUtil;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Launcher extends Application {


    @Override
    public void init() {
        new Thread(HibernateUtil::buildSessionFactory).start();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root =
            FXMLLoader.load(
                Objects.requireNonNull(
                    getClass().getClassLoader().getResource("pages/signup/SignupMenu.fxml")));
        Screen primaryScreen = Screen.getPrimary();
        double width = primaryScreen.getBounds().getWidth() / 2.5;
        double height = primaryScreen.getBounds().getHeight() / 2.1;
        primaryStage.setMinWidth(width);
        primaryStage.setMinHeight(height);
        primaryStage.setTitle(" media-chameleon");
        Image applicationIcon = new Image(
            Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/fileconverter_white_icon.png")));
        primaryStage.getIcons().add(applicationIcon);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
