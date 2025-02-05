package com.yelman.identityserver.util.email;

import com.yelman.identityserver.model.User;
import com.yelman.identityserver.model.VerificationToken;
import com.yelman.identityserver.repository.UserRepository;
import com.yelman.identityserver.repository.VerificationTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class VerificationService implements IVerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public VerificationService(VerificationTokenRepository tokenRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(OffsetDateTime.now().plusHours(5));
        tokenRepository.save(verificationToken);
        return token;
    }

    public boolean validateToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(OffsetDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return true;
    }

    public boolean resetPassword(String token, String newPassword) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(OffsetDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return true;
    }


}
