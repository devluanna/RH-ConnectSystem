package com.connect.system.domain.model.Account.EntityPerson.ResponseDTO;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseGetDTO {
    private Long id;
    private String identityPerson;
    private String name;
    private String last_name;
    private String email;
    private ProfileRole profileRole;
    private String password;
    private Status status;
    private Long personalDataId;
    private Long locationId;
    private Long jobsDetailsId;

}