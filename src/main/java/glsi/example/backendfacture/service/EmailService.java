package glsi.example.backendfacture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ByteArrayResource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvoiceEmail(String to, String subject, String text, byte[] pdfAttachment, String attachmentFileName) {
        jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // Attacher le PDF à l'e-mail
            InputStreamSource attachmentSource = new ByteArrayResource(pdfAttachment);
            helper.addAttachment(attachmentFileName, attachmentSource);

            mailSender.send(message);

        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
