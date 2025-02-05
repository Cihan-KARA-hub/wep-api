package com.yelman.identityserver.services;

import com.yelman.identityserver.api.dto.CreateUserRequest;
import com.yelman.identityserver.model.User;
import com.yelman.identityserver.repository.UserRepository;
import com.yelman.identityserver.util.email.EmailService;
import com.yelman.identityserver.util.email.VerificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerificationService verificationService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailService emailService, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.verificationService = verificationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(EntityNotFoundException::new);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request) {
        User newUser = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities(request.authorities())
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(false)
                .email(request.email())
                .accountNonLocked(true)
                .build();
        return userRepository.save(newUser);
    }

    public ResponseEntity<String> sendResetPassword(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            String token = verificationService.createVerificationToken(user.get());
            emailService.sendPasswordResetEmail(user.get().getEmail(), token, user.get().getUsername());
            return ResponseEntity.ok("Check Your Email to reset your password .");
        }
        return ResponseEntity.badRequest().build();
    }

    public void resetPassword(String token, String newPassword) {
        boolean empty = verificationService.resetPassword(token, newPassword);
        if (empty) {
            ResponseEntity.ok("Your password has been renewed");
            return;
        }
        ResponseEntity.ok("Your password could not be reset");

    }

    public ResponseEntity<String> delete(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setEnabled(false);
            userRepository.save(user.get());
            return ResponseEntity.ok("Your password has been deleted");
        }
        return ResponseEntity.badRequest().build();
    }
}