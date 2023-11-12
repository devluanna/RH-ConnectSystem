package com.connect.system.domain.model.Account.ResponseDTO;


public record AuthenticationDTO (String identityPerson, String password) {

    public String identityPerson() {
        return identityPerson;
    }


    public String password() {
        return password;
    }
}
