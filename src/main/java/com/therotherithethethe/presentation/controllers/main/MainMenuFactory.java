package com.therotherithethethe.presentation.controllers.main;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import org.slf4j.LoggerFactory;
/**
 * Factory class for creating the main menu.
 */
public class MainMenuFactory {
    /**
     * Gets the main menu as an AnchorPane.
     * @return The main menu as an AnchorPane.
     */
    public static AnchorPane getMainMenu() {
        try {
            return FXMLLoader.load(
                Objects.requireNonNull(
                    MainMenuFactory.class.getResource("/pages/main/ConverterMenu.fxml")));
        } catch (IOException ex) {
            LoggerFactory.getLogger(MainMenuFactory.class).error(ex.getMessage());
            throw new RuntimeException(ex);
        }

    }
}

