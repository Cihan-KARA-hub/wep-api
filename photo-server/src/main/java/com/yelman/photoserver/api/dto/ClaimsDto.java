package com.yelman.photoserver.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClaimsDto {
    private long id;
    private Role role;
}
