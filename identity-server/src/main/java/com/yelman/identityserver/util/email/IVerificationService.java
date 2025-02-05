package com.yelman.identityserver.util.email;

import com.yelman.identityserver.model.User;

public  interface IVerificationService {


     String createVerificationToken(User user);
     boolean validateToken(String token);

}
