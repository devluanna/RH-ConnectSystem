package com.connect.system.domain.model.Account.EntityPerson;


public enum Status {

    ALLOCATED("ALLOCATED"),
    TEMPORARY("IS ON VACATION"),
    UNAVAILABLE("UNAVAILABLE, SPEAK TO HR"),
    AVAILABLE("AVAILABLE");

    private String status_description;
    private Status(String status_description) {
        this.status_description = status_description;
    }

    public String getStatus_description() {
        return status_description;
    }

}
