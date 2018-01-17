package com.bee.backend.service.data;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;

import java.util.List;

/**
 * Created by Rudoman on 01.08.2017.
 */
public interface EmailService {
    public void sendSimpleMessage(String to, String subject, String text) throws MailException;
    public void sendMessageWithAttachment(String to, String subject, String text, List<ByteArrayResource> fileAttach) throws MailException;
}
