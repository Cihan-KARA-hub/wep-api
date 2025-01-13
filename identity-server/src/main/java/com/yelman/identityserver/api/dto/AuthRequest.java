package com.yelman.identityserver.api.dto;

import com.yelman.identityserver.model.role.Role;

public record AuthRequest (
        String username,
        String password
){
}
