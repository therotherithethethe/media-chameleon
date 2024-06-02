package com.therotherithethethe.domain.services;

import java.util.Objects;

public class FileService {
    private static FileService instance;

    private FileService(){}

    public static synchronized FileService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new FileService();
        }
        return instance;
    }

}
