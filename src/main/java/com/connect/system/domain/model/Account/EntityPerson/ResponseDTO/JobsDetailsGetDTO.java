package com.connect.system.domain.model.Account.EntityPerson.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobsDetailsGetDTO {
    private Long id_job_details;

    private Long id_account;
    private String identity;
    private String description;
    private String name_squad;
    private String type_of_record;
    private String seniority;
    private String office;
    private String occupancy_area;
    private int time_experience;
    private String hard_skills;
    private String soft_skills;
    private String language_primary;
    private String report_me;
    private String admission_date;
}

