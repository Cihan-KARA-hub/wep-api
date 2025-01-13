package com.yelman.advertisementserver.api.controller.email;

import com.yelman.advertisementserver.api.dto.EmailSendDto;
import com.yelman.advertisementserver.utils.email.EmailServicesImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email/")
public class EmailController {
    private final EmailServicesImpl emailServices;

    public EmailController(EmailServicesImpl emailServices) {
        this.emailServices = emailServices;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody EmailSendDto email) {
        return emailServices.sendSimpleMail(email);
    }
}
