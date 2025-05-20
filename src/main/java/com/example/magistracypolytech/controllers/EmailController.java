package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.services.EmailServiceImpl;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@Slf4j
@RequestMapping("/email")
public class EmailController {

    @Autowired
    EmailServiceImpl emailService;

    @GetMapping(value = "/simple-email/{user-email}")
    public @ResponseBody ResponseEntity sendSimpleEmail(@PathVariable("user-email") String email) {
        try {
            emailService.sendSimpleEmail(email, "Welcome", "This is a welcome email for your!!");
        } catch (MailException mailException) {
            log.error("Error while sending out email..{}", mailException.getStackTrace());
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Please check your inbox", HttpStatus.OK);
    }

    @GetMapping(value = "/simple-order-email/{user-email}")
    public @ResponseBody ResponseEntity sendEmailAttachment(@PathVariable("user-email") String email) {

        try {
            emailService.sendEmailWithAttachment(email, "Order Confirmation", "Thanks for your recent order",
                    "classpath:purchase_order.pdf");
        } catch (MessagingException | FileNotFoundException mailException) {
            log.error("Error while sending out email..{}", mailException.getStackTrace());
            return new ResponseEntity<>("Unable to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Please check your inbox for order confirmation", HttpStatus.OK);
    }

}