package com.connect.system.domain.model.Account.ResponseDTO;

import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobsDetailsDTO {
    private Integer id_job_details;

    private Integer id_account;
    private String identity;

    private Integer id_squad;
    private Integer id_memberOfSquad;
    private String name_squad;

    private String description;
    private String type_of_record;
    private String office;
    private String occupancy_area;
    private String seniority;
    private int time_experience;
    private String hard_skills;
    private String soft_skills;
    private String language_primary;
    private String admission_date;

}
