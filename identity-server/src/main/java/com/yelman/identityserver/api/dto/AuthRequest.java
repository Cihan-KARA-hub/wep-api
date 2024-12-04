package com.yelman.identityserver.api.dto;

public record AuthRequest (
        String username,
        String password
){
}
