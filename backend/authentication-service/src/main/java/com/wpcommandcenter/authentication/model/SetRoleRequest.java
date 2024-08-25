package com.wpcommandcenter.authentication.model;

public class SetRoleRequest {

    public String email;
    public String role;

    public SetRoleRequest() {}

    public SetRoleRequest(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
