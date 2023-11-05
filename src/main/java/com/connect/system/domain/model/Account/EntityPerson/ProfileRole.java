package com.connect.system.domain.model.Account.EntityPerson;

public enum ProfileRole {
    RH("RH"),
    USER("USER"),
    MANAGER("MANAGER");

    private String role;

    ProfileRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
