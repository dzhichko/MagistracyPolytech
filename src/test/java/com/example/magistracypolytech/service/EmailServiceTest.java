package com.example.magistracypolytech.service;

import com.example.magistracypolytech.AbstractContainerBaseTest;
import com.example.magistracypolytech.services.EmailServiceImpl;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmailServiceTest extends AbstractContainerBaseTest {

    private static GreenMail greenMail;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    private EmailServiceImpl emailService;

    @BeforeEach
    void setup() {
        greenMail = new GreenMail(new ServerSetup(3025, "localhost", "smtp"));
        greenMail.start();
        greenMail.setUser(mailUsername,password);
    }

    @AfterEach
    void teardown() {
        greenMail.stop();
    }

    @Test
    void testSendEmail() throws Exception {
        String to = "recipient@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        emailService.sendSimpleEmail(to, subject, body);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages[0].getSubject()).isEqualTo(subject);
        assertThat(receivedMessages[0].getAllRecipients()[0].toString()).isEqualTo(to);
    }
}
