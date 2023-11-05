package com.connect.system.domain.model.Account.EntityPerson.ResponseDTO;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import lombok.Data;

@Data
public class UpdateProfileDTO {
    private Long id;
    private String name;
    private String last_name;
    private String email;
    private ProfileRole profileRole;
    private Status status;
}
