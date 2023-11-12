package com.connect.system.domain.model.Account.ResponseDTO;

public record PasswordDTO(String password) {
    public String getPassword() {
        return password;
    }

}
