package com.bulq.logistics.services;

import org.springframework.stereotype.Service;

import com.bulq.logistics.util.email.EmailDetails;
import com.bulq.logistics.util.email.EmailDetailsWelcome;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public Boolean sendMail(EmailDetails details){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
// String to, String firstName, String verificationLink
    public boolean sendWelcomeEmail(EmailDetailsWelcome details) throws MessagingException {
        try {
            String subject = "Welcome to Bulq Logistics";
        String content = buildEmailContent(details.getRecipient(),details.getFirstName(), details.getBody());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(details.getRecipient());
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    public boolean sendForgetPasswordEmail(EmailDetailsWelcome details) throws MessagingException {
        try {
            String subject = "Oops! Forgotten password!";
        String content = buildForgetPassword(details.getRecipient(),details.getFirstName(), details.getBody());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(details.getRecipient());
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    public boolean sendLogisticsIdMail(EmailDetailsWelcome details) throws MessagingException {
        try {
            String subject = "Booking Success";
        String content = buildDeliveryMailContent(details.getRecipient(),details.getFirstName(), details.getBody());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(details.getRecipient());
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    public boolean sendDeliveryActionsMail(EmailDetailsWelcome details) throws MessagingException {
        try {
            String subject = "Booking Success";
        String content = buildDeliveryActionsMailContent(details.getRecipient(),details.getFirstName(), details.getBody());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(details.getRecipient());
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return true;
        } catch (Exception e) {
            return false;
        }
        
    }

    private String buildEmailContent(String recipient, String firstName, String message){
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Welcome to Bulq Logistics</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f4f4f4;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".header, .footer {"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    text-align: center;"
                + "    padding: 10px 0;"
                + "}"
                + ".content {"
                + "    background-color: white;"
                + "    padding: 20px;"
                + "    margin: 20px auto;"
                + "    width: 80%;"
                + "    max-width: 600px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".button {"
                + "    display: inline-block;"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    padding: 10px 20px;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"header\">"
                + "        <h1>Welcome to Bulq Logistics</h1>"
                + "    </div>"
                + "    <div class=\"content\">"
                + "        <h2>Hi " + firstName + ",</h2>"
                + "        <p>Thank you for joining Bulq Logistics! We are thrilled to have you on board.</p>"
                + "        <p>With Bulq Logistics, you have a partner come rain, come sunshine. And Ridwan said i       should remind you that everything is gonna b alright soon. Inu adun, ayo e akun, lagbara edumare</p>"
                + "        <p>Your verification code is:</p>"
                + "        <p>" + message + "<p>"
                + "        <p>Kindly click on this link to verify.</p>"
                + "        <a href=\"https://bulq-ecommerce.vercel.app/" + "\" class=\"button\">Visit Bulq</a>"
                + "        <p>Pease note that this link will expire in 10 minutes</p>"
                + "        <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "        <p>Happy travels!</p>"
                + "        <p>Best regards,</p>"
                + "        <p>The Bulq Team</p>"
                + "    </div>"
                + "    <div class=\"footer\">"
                + "        <p>&copy; 2024 Bulq Logistics. All rights reserved.</p>"
                + "        <p>Calgary, Alberta, Canada.</p>"
                + "        <p><a href=\"ecobank.com/bulq\" style=\"color: white;\">Unsubscribe</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    private String buildForgetPassword(String recipient, String firstName, String message){
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Welcome to Bulq Logistics</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f4f4f4;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".header, .footer {"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    text-align: center;"
                + "    padding: 10px 0;"
                + "}"
                + ".content {"
                + "    background-color: white;"
                + "    padding: 20px;"
                + "    margin: 20px auto;"
                + "    width: 80%;"
                + "    max-width: 600px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".button {"
                + "    display: inline-block;"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    padding: 10px 20px;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"header\">"
                + "        <h1>Welcome to Bulq Logistics</h1>"
                + "    </div>"
                + "    <div class=\"content\">"
                + "        <h2>Hi " + firstName + ",</h2>"
                + "        <p>Forgootten password</p>"
                + "        <p>You can enter this reset password link below to reset your password</p>"
                + "        <p>" + message + "<p>"
                + "        <p>To continue on Bulq visit the link down below.</p>"
                + "        <a href=\"https://bulq-ecommerce.vercel.app/" + "\" class=\"button\">Visit Bulq</a>"
                + "        <p>Pease note that this link will expire in 10 minutes</p>"
                + "        <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "        <p>Happy Delivery!</p>"
                + "        <p>Best regards,</p>"
                + "        <p>The Bulq Team</p>"
                + "    </div>"
                + "    <div class=\"footer\">"
                + "        <p>&copy; 2024 Bulq Logistics. All rights reserved.</p>"
                + "        <p>Calgary, Alberta, Canada.</p>"
                + "        <p><a href=\"ecobank.com/bulq\" style=\"color: white;\">Unsubscribe</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    private String buildDeliveryMailContent(String recipient, String firstName, String message){
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Welcome to Bulq Logistics</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f4f4f4;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".header, .footer {"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    text-align: center;"
                + "    padding: 10px 0;"
                + "}"
                + ".content {"
                + "    background-color: white;"
                + "    padding: 20px;"
                + "    margin: 20px auto;"
                + "    width: 80%;"
                + "    max-width: 600px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".button {"
                + "    display: inline-block;"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    padding: 10px 20px;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"header\">"
                + "        <h1>Welcome to Bulq Logistics</h1>"
                + "    </div>"
                + "    <div class=\"content\">"
                + "        <h2>Hi " + firstName + ",</h2>"
                + "        <p>Thank you for booking a service with bulq logistics.</p>"
                + "        <p>Please keep your delivery code as you'd provide this to the rider on contact</p>"
                + "        <p>" + message + "<p>"
                + "        <p>To continue on Bulq visit the link down below.</p>"
                + "        <a href=\"https://bulq-ecommerce.vercel.app/" + "\" class=\"button\">Visit Bulq</a>"
                + "        <p>Pease note that this link will expire in 10 minutes</p>"
                + "        <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "        <p>Happy Delivery!</p>"
                + "        <p>Best regards,</p>"
                + "        <p>The Bulq Team</p>"
                + "    </div>"
                + "    <div class=\"footer\">"
                + "        <p>&copy; 2024 Bulq Logistics. All rights reserved.</p>"
                + "        <p>Calgary, Alberta, Canada.</p>"
                + "        <p><a href=\"ecobank.com/bulq\" style=\"color: white;\">Unsubscribe</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    private String buildDeliveryActionsMailContent(String recipient, String firstName, String message){
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "<title>Welcome to Bulq Logistics</title>"
                + "<style>"
                + "body {"
                + "    font-family: Arial, sans-serif;"
                + "    background-color: #f4f4f4;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "}"
                + ".header, .footer {"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    text-align: center;"
                + "    padding: 10px 0;"
                + "}"
                + ".content {"
                + "    background-color: white;"
                + "    padding: 20px;"
                + "    margin: 20px auto;"
                + "    width: 80%;"
                + "    max-width: 600px;"
                + "    border-radius: 8px;"
                + "    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".button {"
                + "    display: inline-block;"
                + "    background-color: #0033cc;"
                + "    color: white;"
                + "    padding: 10px 20px;"
                + "    text-decoration: none;"
                + "    border-radius: 5px;"
                + "    margin-top: 20px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "    <div class=\"header\">"
                + "        <h1>Welcome to Bulq Logistics</h1>"
                + "    </div>"
                + "    <div class=\"content\">"
                + "        <h2>Hi " + firstName + ",</h2>"
                + "        <p>Thank you for booking a service with bulq logistics.</p>"
                + "        <p>Please keep your delivery code as you'd provide this to the rider on contact</p>"
                + "        <p>" + message + "<p>"
                + "        <p>To continue on Bulq visit the link down below.</p>"
                + "        <a href=\"https://bulq-ecommerce.vercel.app/" + "\" class=\"button\">Visit Bulq</a>"
                + "        <p>Pease note that this link will expire in 10 minutes</p>"
                + "        <p>If you have any questions or need assistance, feel free to reach out to our support team.</p>"
                + "        <p>Happy Delivery!</p>"
                + "        <p>Best regards,</p>"
                + "        <p>The Bulq Team</p>"
                + "    </div>"
                + "    <div class=\"footer\">"
                + "        <p>&copy; 2024 Bulq Logistics. All rights reserved.</p>"
                + "        <p>Calgary, Alberta, Canada.</p>"
                + "        <p><a href=\"ecobank.com/bulq\" style=\"color: white;\">Unsubscribe</a></p>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }
}
