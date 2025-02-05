package com.yelman.identityserver.api.controller;

import com.yelman.identityserver.api.dto.AuthRequest;
import com.yelman.identityserver.api.dto.CreateUserRequest;
import com.yelman.identityserver.model.User;
import com.yelman.identityserver.services.JwtService;
import com.yelman.identityserver.services.UserService;
import com.yelman.identityserver.util.email.EmailService;
import com.yelman.identityserver.util.email.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final VerificationService verificationService;
    private final EmailService emailService;


    public UserController(UserService service, JwtService jwtService, AuthenticationManager authenticationManager, VerificationService verificationService, EmailService emailService) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
        this.emailService = emailService;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "misafir mod";
    }

    @PostMapping("/create-user")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest request) {
        User user = service.createUser(request);
        String token = verificationService.createVerificationToken(user);
        emailService.sendVerificationEmail(user.getEmail(), token, request.name());
        return ResponseEntity.ok("Kayıt başarılı! E-postanızı doğrulamak için lütfen e-postanızı kontrol edin.");
    }

    // email verfy
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token) {
        if (verificationService.validateToken(token)) {
            return "E-posta başarıyla doğrulandı!";
        }
        return "Token geçersiz veya süresi dolmuş!";
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (authentication.isAuthenticated()) {
            List<String> role = jwtService.getRole(request.username());
            return jwtService.generateToken(request.username(), role.get(0), role.get(1));
        }
        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }

    @GetMapping("/admin")
    public String getAdminString() {
        return "This is ADMIN!";
    }

    @GetMapping("/guest")
    public String getGuestString() {
        return "This is ADMIN!";
    }

    // şifremi unuttum kısmı
    @GetMapping("/send-reset-password/{userName}")
    public ResponseEntity<String> sendResetPassword(@PathVariable String userName) {
        return service.sendResetPassword(userName);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        service.resetPassword(token, newPassword);
        return ResponseEntity.ok("Şifre başarıyla yenilendi.");
    }

    // google auth
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("authorities", authentication.getAuthorities());
        userInfo.put("Is Auth:", authentication.isAuthenticated());
        userInfo.put("detail:", authentication.getPrincipal());
        return ResponseEntity.ok(userInfo);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) {
        return  service.delete(userId);
    }

}