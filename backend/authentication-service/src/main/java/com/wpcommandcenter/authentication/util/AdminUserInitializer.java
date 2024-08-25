package com.wpcommandcenter.authentication.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        try {
            UserRecord userRecord = null;
            try {
                userRecord = FirebaseAuth.getInstance().getUserByEmail(adminEmail);
            } catch (FirebaseAuthException e) {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(adminEmail)
                        .setPassword(adminPassword)
                        .setEmailVerified(true);
                userRecord = FirebaseAuth.getInstance().createUser(request);
            }

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", "ADMIN");
        FirebaseAuth.getInstance().setCustomUserClaims(userRecord.getUid(), claims);

        System.out.println("Admin user created: " + userRecord.getUid());
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating admin user: " + e.getMessage());
        }
    }
}
