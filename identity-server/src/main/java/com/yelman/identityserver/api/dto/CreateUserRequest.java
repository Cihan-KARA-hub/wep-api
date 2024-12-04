package com.yelman.identityserver.api.dto;


import com.yelman.identityserver.model.role.Role;
import lombok.Builder;

import java.util.Set;


@Builder
public record CreateUserRequest(
        String name,
        String email,
        String username,
        String password,
        Set<Role> authorities
){
}