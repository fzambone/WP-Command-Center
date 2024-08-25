package com.wpcommandcenter.authentication.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wpcommandcenter.authentication.exception.RegistrationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserManagementService {
    public void setRole(String email, String role) throws FirebaseAuthException {
        UserRecord user = FirebaseAuth.getInstance().getUserByEmail(email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", role.toUpperCase());
        FirebaseAuth.getInstance().setCustomUserClaims(user.getUid(), claims);
    }

    public Map<String, Object> registerUser(String email, String password) throws RegistrationException {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setEmailVerified(false);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);

            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", Collections.singletonList("USER"));
            FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);

            Map<String, Object> result = new HashMap<>();
            result.put("email", email);
            result.put("uid", userRecord.getUid());
            result.put("roles", Collections.singletonList("USER"));
            return result;
        } catch (FirebaseAuthException e) {
            throw new RegistrationException("Error registering user: " + e.getMessage());
        }
    }
}
