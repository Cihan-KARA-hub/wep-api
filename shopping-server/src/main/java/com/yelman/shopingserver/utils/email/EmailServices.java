package com.yelman.shopingserver.utils.email;

import com.yelman.shopingserver.api.dto.EmailSendDto;
import org.springframework.http.ResponseEntity;

public interface EmailServices {

    ResponseEntity<String> sendSimpleMail(EmailSendDto details);


    ResponseEntity<String> sendMailWithAttachment(EmailSendDto details);
}
