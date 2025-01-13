package com.yelman.identityserver.api.controller;

import com.yelman.identityserver.api.dto.AuthRequest;
import com.yelman.identityserver.api.dto.CreateUserRequest;
import com.yelman.identityserver.model.User;
import com.yelman.identityserver.model.role.Role;
import com.yelman.identityserver.services.JwtService;
import com.yelman.identityserver.services.UserService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@Slf4j
public class UserController {

    private final UserService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public UserController(UserService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "misafir mod";
    }

    @PostMapping("/addNewUser")
    public User addUser(@RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authentication.isAuthenticated()) {
           List<String> role= jwtService.getRole(request.username());
            return jwtService.generateToken(request.username(), role.get(0),role.get(1));
        }
        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        userInfo.put("Is Auth:", authentication.isAuthenticated());
        userInfo.put("detail:", authentication.getPrincipal());
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/admin")
    public String getAdminString() {
        return "This is ADMIN!";
    }
    @GetMapping("/guest")
    public String getGuestString() {
        return "This is ADMIN!";
    }
}