package se.dmolinsky.webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String toEmail, String orderDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Din orderbekräftelse - GreenShop");
        message.setText("Tack för din beställning!\n\n" + orderDetails + "\n\nMed vänliga hälsningar,\nGreenShop");

        mailSender.send(message);
    }
}