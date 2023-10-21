package com.connect.system.domain.model.EntityPerson;

public enum ProfileRole {
    RH("RH"),
    USER("user"),
    MANAGER("manager");

    private String role;

    ProfileRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
