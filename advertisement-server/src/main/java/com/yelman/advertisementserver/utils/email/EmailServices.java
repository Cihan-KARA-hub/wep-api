package com.yelman.advertisementserver.utils.email;

import com.yelman.advertisementserver.api.dto.EmailSendDto;
import org.springframework.http.ResponseEntity;

public interface EmailServices {

    ResponseEntity<String> sendSimpleMail(EmailSendDto details);


    ResponseEntity<String>  sendMailWithAttachment(EmailSendDto details);
}
