package com.clinicapaw.backend_clinicapaw.service.implementation;

import com.clinicapaw.backend_clinicapaw.service.interfaces.ISendEmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService implements ISendEmailService {

    @Value("${email.user}")
    private String emailUser;

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String toUser, String subject, String messageBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailUser);
        mailMessage.setTo(toUser);
        mailMessage.setSubject(subject);
        mailMessage.setText(messageBody);

        mailSender.send(mailMessage);
    }
}
