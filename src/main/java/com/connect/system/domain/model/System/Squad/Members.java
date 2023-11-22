package com.connect.system.domain.model.System.Squad;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Members {

    private Long id_dashboardMembers;

    @Id
    private Long id_member;

    private Long id_squad;
    private String name_squad;
    private Long id_account;
    private String identity;
    private String name;
    private String last_name;
    private String office;
    private String seniority;
    private String sub_position;
    private String report_me;


}
