package com.therotherithethethe.domain.services;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Utility class for sending emails.
 */
public class EmailSender {
    private static final String username = "MS_HwoDP1@trial-jy7zpl932p3l5vx6.mlsender.net";
    private static final String password = "h0rvGBuq3XxBHPQx";
    //private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    /**
     * Sends an email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param body the body of the email
     */
    public static void sendEmail(String to, String subject, String body) {
        new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.mailersend.net");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);
            } catch (MessagingException e) {
                //LOGGER.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();
    }
    // Private constructor to prevent instantiation
    private EmailSender() {}
}

