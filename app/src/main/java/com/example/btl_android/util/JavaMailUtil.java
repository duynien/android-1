package com.example.btl_android.util;

import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";  // 587 for TLS, 465 for SSL
    private static final String SMTP_USER = "duynien128@gmail.com";  // Sender's email
    private static final String SMTP_PASSWORD = "dxbo eoeg pvmm owrh";  // Sender's email password

    // To send an email with the verification code

    public static void sendVerificationEmail(final String recipientEmail, final String verificationCode) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.put("mail.smtp.host", SMTP_HOST);
                    properties.put("mail.smtp.port", SMTP_PORT);
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.socketFactory.port", SMTP_PORT);
                    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    properties.put("mail.smtp.socketFactory.fallback", "false");

                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
                        }
                    });

                    // Compose the message
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(SMTP_USER));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                    message.setSubject("Email Verification Code");
                    message.setText("Your verification code is: " + verificationCode);

                    // Send the message
                    Transport.send(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
