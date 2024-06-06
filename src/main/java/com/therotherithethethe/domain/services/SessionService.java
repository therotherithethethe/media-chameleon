package com.therotherithethethe.domain.services;

import com.therotherithethethe.persistance.entity.Account;
import com.therotherithethethe.persistance.entity.File;
import com.therotherithethethe.persistance.entity.Session;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
/**
 * The {@code SessionService} class provides a singleton service for session management.
 */
public class SessionService {
    private static SessionService instance;
    /**
     * Returns the singleton instance of the {@code SessionService}.
     *
     * @return the singleton instance of the SessionService
     */
    public static synchronized SessionService getInstance() {
        if(Objects.isNull(instance)) {
            instance = new SessionService();
        }
        return instance;
    }
    private SessionService(){}
    private Session session;
    /**
     * Creates a new session for the given account.
     *
     * @param account the account for which to create the session
     */
    public void createSession(Account account) {
        var session = new Session(null, LocalDateTime.now());
        this.session = session;
        session.setAccount(account);
        session.save();
    }
    /**
     * Saves the given file to the current session.
     *
     * @param file the file to save to the session
     */
    public void saveSession(File file) {
        if(Objects.isNull(session.files)) {
            session.files = new ArrayList<>();
        }
        session.files.add(file);
        session.save();
    }
    /**
     * Returns the current session.
     *
     * @return the current session
     */
    public Session getSession() {
        return session;
    }
}
