package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.persistance.entity.File;
import com.therotherithethethe.persistance.entity.Session;
import java.time.LocalDateTime;
import java.util.Objects;

public class SessionService {
    private static SessionService instance;
    public static synchronized SessionService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new SessionService();
        }
        return instance;
    }
    private SessionService(){}
    private Session session;

    public void createSession(Account account) {
        var session = new Session(null, LocalDateTime.now());
        this.session = session;
        session.setAccount(account);
        session.save();
    }
    public void saveSession(File file) {
        session.files.add(file);
        session.save();
    }

    public Session getSession() {
        return session;
    }
}
