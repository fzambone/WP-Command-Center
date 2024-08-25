package com.wpcommandcenter.authentication.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.wpcommandcenter.authentication.controller.AuthenticationController;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            logger.debug("Loading user details for username: {}", email);
            return new User(userRecord.getEmail(), userRecord.getUid(), new ArrayList<>());
        } catch (FirebaseAuthException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}