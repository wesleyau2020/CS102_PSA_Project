package com.psa.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.psa.entity.Mail;
import com.psa.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("emailService")
public class MailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserService userService;

    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "PSA"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent().replace("\n", "<br/>"), true);

            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void changePasswordEmail(int userID) {

        Mail mail = new Mail();
        mail.setMailFrom("PSA");
        mail.setMailSubject("Password Modification");
        mail.setMailTo(userService.get(userID).getEmail());
        mail.setMailContent("Dear " + userService.get(userID).getUsername() 
                + ",\n\nPassword has been successfully changed."
                + "\n\nPlease do not reply to this email");
        sendEmail(mail);
    }

    public void createNewUserEmail(User user, String password) {

        Mail mail = new Mail();
        mail.setMailFrom("PSA");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Account Creation");
        mail.setMailContent("Dear " + user.getUsername() 
                + ",\n\n Your account has been created successfully" 
                + "<ul><li>Username: " + user.getUsername() 
                + "</li><li>Password: " + password + "</li></ul>"
                + "\n\nPlease do not reply to this email");
        sendEmail(mail);
    }

    public void adminResetPasswordEmail(User user, String password) {

        Mail mail = new Mail();
        mail.setMailFrom("PSA");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Password Modification");
        mail.setMailContent("Dear " + user.getUsername()
                + ",\n\nYour password has been successfully changed to " + password
                + "\n\nPlease do not reply to this email");
        sendEmail(mail);
    }

}
