package com.therotherithethethe.domain.services;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Utility class for sending emails.
 */
public class EmailSender {
    private static final String username = "3af4f874fb4c26";
    private static final String password = "3560ed875fea5f";
    //private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    /**
     * Sends an email to the specified recipient.
     *
     * @param to the recipient's email address
     * @param subject the subject of the email
     * @param body the body of the email
     */
    public static void sendEmail(String to, String subject, String body) {
        final String toTest = "wildchild250336@gmail.com";
        new Thread(() -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
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
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toTest));
                message.setSubject(subject);
                message.setText(body);
                Transport.send(message);
            } catch (MessagingException e) {
                //LOGGER.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();
       /* new Thread(() -> {
            OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType,STR."{\"from\":{\"email\":\"mailtrap@demomailtrap.com\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"\{to}\"}],\"subject\":\"You are awesome!\",\"text\":\"\{body}\",\"category\":\"Integration Test\"}");
            Request request = new Request.Builder()
                .url("https://send.api.mailtrap.io/api/send")
                .method("POST", requestBody)
                .addHeader("Authorization", "Bearer b123d92355933d3c1fa119094f72c047")
                .addHeader("Content-Type", "application/json")
                .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                response.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finally {
                assert response != null;
                response.close();
            }
        }).start();*/
    }
    // Private constructor to prevent instantiation
    private EmailSender() throws IOException {

    }
}

