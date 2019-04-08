package org.moduleap.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.Properties;


//service for sending mails
@Service
@PropertySource("classpath:general.properties")
public class EmailServiceImpl {
    private JavaMailSender sender;

    @Value("${smtp.password:password}")
    private String smtpPassword;
    @Value("${smtp.port:587}")
    private int smtpPort;
    @Value("${smtp.username:Test}")
    private String smtpUsername;
    @Value("${verification.url:https://localhost:8080}")
    private String verificationUrl;
    @Value("${smtp.host:localhost}")
    private String smtpHost;
    @Autowired
    private MailBuilder mailBuilder;
    //Define the properties
    public EmailServiceImpl() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);

        mailSender.setUsername(smtpUsername);
        mailSender.setPassword(smtpPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        this.sender = mailSender;
    }

    //method for sending the verify email
    public void sendVerifyEmail(String to, String verify, String username){

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(to);
            messageHelper.setFrom(smtpUsername);
            messageHelper.setSubject("Please verify your E-Mail");
            //todo: change that to external config
            messageHelper.setText(mailBuilder.build("verify",verificationUrl+"/verify?key="+ verify, username), true);
        };

        sender.send(messagePreparator);

    }
}
