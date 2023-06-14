//package com.popit.popitproject.user.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class EmailService {
//
//    private final JavaMailSender emailSender;
//
//    @Value("${mail.username}")
//    private String emailUsername;
//
//    @Value("${mail.password}")
//    private String emailPassword;
//
//    public void sendEmail(String to, String subject, String content) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(emailUsername);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(content);
//        emailSender.send(message);
//    }
//}