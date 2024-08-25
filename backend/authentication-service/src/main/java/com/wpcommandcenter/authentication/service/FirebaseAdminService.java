package com.wpcommandcenter.authentication.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseAdminService {

    public void setUserRole(String uid, String role) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", role);
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
    }

    public Map<String, Object> getUserClaims(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid).getCustomClaims();
    }

}
