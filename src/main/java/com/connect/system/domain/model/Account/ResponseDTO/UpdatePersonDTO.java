package com.connect.system.domain.model.Account.ResponseDTO;


import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import com.connect.system.domain.model.Account.Jobs.OccupancyArea;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.Jobs.Seniority;
import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import lombok.Data;


@Data
public class UpdatePersonDTO {
    private Long id;
    private String identityPerson;
    private String name;
    private String last_name;
    private String email;
    private TypeOfRecord type_of_record;
    private Office office;
    private OccupancyArea occupancy_area;
    private Seniority seniority;
    private ProfileRole role;
    private String password;
    private Status status;

}