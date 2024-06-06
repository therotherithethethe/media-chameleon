package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.File;
import java.util.Objects;
import java.util.Optional;
/**
 * The {@code FileService} class provides a singleton service for file operations.
 */
public class FileService {
    private static FileService instance;
    private static final File dao = new File();

    private FileService(){}
    /**
     * Returns the singleton instance of the {@code FileService}.
     *
     * @return the singleton instance of the FileService
     */
    public static synchronized FileService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new FileService();
        }
        return instance;
    }
    /**
     * Retrieves a file by its path.
     *
     * @param path the path of the file to retrieve
     * @return an optional containing the file if found, otherwise empty
     */
    public Optional<File> getFileByPath(String path) {
        return dao.findByColumn(file -> file.path.equals(path)).stream().findFirst();
    }
}
