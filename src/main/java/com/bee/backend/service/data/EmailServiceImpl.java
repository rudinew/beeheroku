package com.bee.backend.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;


@Service
public class EmailServiceImpl implements EmailService {

    public JavaMailSender emailSender;
    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(mailUsername); ///!!!!
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }
    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, List<ByteArrayResource> fileAttach) throws MailException {
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setTo(to);
        simpleMessage.setFrom(mailUsername); ///!!!!
        simpleMessage.setSubject(subject);
        simpleMessage.setText(text);
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(simpleMessage.getFrom());
            helper.setTo(simpleMessage.getTo());
            helper.setSubject(simpleMessage.getSubject());
            helper.setText(simpleMessage.getText());

           // FileSystemResource file = new FileSystemResource(fileAttach);
            //helper.addAttachment(fileAttach.getFilename(), fileAttach);
            for(ByteArrayResource oneFile : fileAttach) {
                helper.addAttachment(oneFile.getFilename(), oneFile);
            }

        }catch (MessagingException e) {
            throw new MailParseException(e);
        }
        emailSender.send(mimeMessage);
    }
}
