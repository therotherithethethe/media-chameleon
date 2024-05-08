package com.therotherithethethe.launcher;

import com.therotherithethethe.EmailSender;
import com.therotherithethethe.model.HibernateUtil;
import com.therotherithethethe.model.entity.Account;
import java.util.Properties;
import javafx.application.Application;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Main {

    public static void main(String[] args) throws MessagingException {
        Application.launch(Launcher.class, args);
        //Account account = new Account(null, "boklah", "boklah@gmaul", "1212");
        //account.save();
        //var emailSender = new EmailSender("MS_HwoDP1@trial-jy7zpl932p3l5vx6.mlsender.net", "h0rvGBuq3XxBHPQx");
        //emailSender.sendEmail("blackbetch2@gmail.com", "Email Verification", "я тебе люблю");
    }

}
