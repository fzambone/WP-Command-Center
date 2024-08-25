package com.wpcommandcenter.authentication.controller;

import com.wpcommandcenter.authentication.exception.RegistrationException;
import com.wpcommandcenter.authentication.model.AuthenticationResponse;
import com.wpcommandcenter.authentication.model.RegistrationRequest;
import com.wpcommandcenter.authentication.model.SetRoleRequest;
import com.wpcommandcenter.authentication.security.JwtUtil;
import com.wpcommandcenter.authentication.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User Management APIs")
@SecurityRequirement(name = "bearer-jwt")
public class UserManagementController {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Registration failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        try {
            Map<String, Object> userClaims = userManagementService.registerUser(
                    registrationRequest.getEmail(),
                    registrationRequest.getPassword()
            );


            final String jwt = jwtUtil.generateToken(userClaims);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (RegistrationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/set-role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set role for a user", description = "Sets the role for a specific user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role set successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Error setting role"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> setRole(@RequestBody SetRoleRequest setRoleRequest) {

        String email = setRoleRequest.getEmail();
        String role = setRoleRequest.getRole();
        try {
            userManagementService.setRole(email, role);
            return ResponseEntity.ok(role +" role set successfully for " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error setting role: " + role + " for " + email + ". " + e.getMessage());
        }
    }

}
