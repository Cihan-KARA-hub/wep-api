package com.yelman.advertisementserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendDto implements Serializable {
    private String recipient;//alıcı
    private String msgBody;//mesaj body
    private String subject;//konusu
    private String attachment;//eki
}
