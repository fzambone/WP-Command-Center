package com.wpcommandcenter.authentication.controller;

import com.wpcommandcenter.authentication.exception.AuthenticationException;
import com.wpcommandcenter.authentication.model.AuthenticationRequest;
import com.wpcommandcenter.authentication.model.AuthenticationResponse;
import com.wpcommandcenter.authentication.security.JwtUtil;
import com.wpcommandcenter.authentication.service.AuthenticationService;
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
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AuthenticationService firebaseAuthService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String firebaseToken = firebaseAuthService.authenticateUser(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            );

            UserDetails userDetails = new User(authenticationRequest.getEmail(), "", new ArrayList<>());
            final String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
}