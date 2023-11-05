package com.connect.system.domain.model.Account.EntityPerson.ResponseDTO;


import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import lombok.Data;


@Data
public class ResponsePersonDTO {
    private Long id;
    private String identityPerson;
    private String name;
    private String last_name;
    private String email;
    private ProfileRole profileRole;
    private String password;
    private Status status;

}