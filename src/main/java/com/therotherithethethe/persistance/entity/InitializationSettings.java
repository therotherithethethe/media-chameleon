package com.therotherithethethe.persistance.entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
/**
 * Stores initialization settings.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InitializationSettings implements Serializable {
    private static InitializationSettings instance;
    public String initializeDirectory;
    public static final String FILENAME = "initialize";
    /**
     * Serializes the InitializationSettings.
     * @throws IOException If an I/O error occurs.
     */
    public void serialize() throws IOException {
        try (FileOutputStream file = new FileOutputStream(FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(file)) {
            out.writeObject(this);
        } catch (Exception _) {}
    }
    /**
     * Deserializes the InitializationSettings.
     * @return The deserialized InitializationSettings.
     */
    private static synchronized InitializationSettings deserialize() {
        InitializationSettings settings;

        try(FileInputStream file = new FileInputStream(FILENAME);
        ObjectInputStream in = new ObjectInputStream(file))
        {
            settings = (InitializationSettings)in.readObject();
        } catch (Exception ex) {
            settings = new InitializationSettings("C:\\");
        }

        return settings;
    }
    /**
     * Retrieves the instance of InitializationSettings.
     * @return The instance of InitializationSettings.
     */
    public static InitializationSettings getInstance() {
        if(Objects.isNull(instance)) {
            instance = deserialize();
        }
        return instance;
    }
}
