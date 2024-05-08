package com.therotherithethethe.viewmodel;

import com.therotherithethethe.EmailSender;
import com.therotherithethethe.model.entity.Account;
import java.util.Random;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class AccountViewModel {
    private final StringProperty verificationCode = new SimpleStringProperty();
    public StringProperty username = new SimpleStringProperty();
    public StringProperty email = new SimpleStringProperty();
    public StringProperty password = new SimpleStringProperty();


    public void generateVerificationCode() {
        int confirmationCode = new Random().nextInt(99999);
        System.out.println(confirmationCode);
        verificationCode.set(String.valueOf(confirmationCode));
    }

    public void createAccount(String username, String email, String password) {
        Task<Account> task = new Task<>() {
            @Override
            protected Account call() {
                return new Account(null, username, email, password);
            }
        };
        task.setOnSucceeded(_ -> {
            var account = task.getValue();
            this.username.set(account.name);
            this.email.set(account.email);
            this.password.set(String.valueOf(account.getPassword()));
            sendEmailVerification();
        });
        new Thread(task).start();
    }

    private void sendEmailVerification() {
        String code = verificationCode.get();
        /*Account acc = account.get();
        if (acc != null && code != null) {
            EmailSender.sendEmail(acc.email, "Email Verification", code);
        }*/
    }
}

