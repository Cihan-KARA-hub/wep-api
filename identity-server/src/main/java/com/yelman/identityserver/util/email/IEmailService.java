package com.yelman.identityserver.util.email;


public interface IEmailService {
     void sendVerificationEmail(String email, String token,String name);
      void sendPasswordResetEmail(String email, String token, String name);
}
