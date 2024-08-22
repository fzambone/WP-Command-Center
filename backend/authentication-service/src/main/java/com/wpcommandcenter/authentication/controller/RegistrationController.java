package com.wpcommandcenter.authentication.controller;

import com.wpcommandcenter.authentication.exception.RegistrationException;
import com.wpcommandcenter.authentication.model.AuthenticationResponse;
import com.wpcommandcenter.authentication.model.RegistrationRequest;
import com.wpcommandcenter.authentication.security.JwtUtil;
import com.wpcommandcenter.authentication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService userRegistrationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            String firebaseToken = userRegistrationService.registerUser(
                    registrationRequest.getEmail(),
                    registrationRequest.getPassword()
            );

            UserDetails userDetails = new User(registrationRequest.getEmail(), "", new ArrayList<>());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}

