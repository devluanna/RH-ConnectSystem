package com.connect.system.domain.model.Account.ResponseDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecoveryPasswordDTO {
    private String email;
    private String password;

}
