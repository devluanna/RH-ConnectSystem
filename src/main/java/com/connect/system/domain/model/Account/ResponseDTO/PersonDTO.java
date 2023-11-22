package com.connect.system.domain.model.Account.ResponseDTO;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import com.connect.system.domain.model.Account.Jobs.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PersonDTO {
    private Long id;
    private String identityPerson;
    private String name;
    private String last_name;
    private String email;
    private TypeOfRecord type_of_record;
    private Office office;
    private OccupancyArea occupancy_area;
    private Seniority seniority;
    private SubPosition sub_position;
    private String report_me;
    private ProfileRole role;
    private String password;
    private Status status;
    private Long personalDataId;
    private Long jobsDetailsId;
    private Long dashboardStudiesId;

}