package com.yelman.shopingserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendDto {
    private String recipient;//alıcı
    private String msgBody;//mesaj body
    private String subject;//konusu
    private String attachment;//eki
}
