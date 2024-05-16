package com.connect.system.domain.model.System.Squad;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Members {

    private int id_dashboardMembers;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_member;

    private int id_squad;
    private String name_squad;
    private int id_account;
    private String identity;
    private String name;
    private String last_name;
    private String office;
    private String seniority;
    private String sub_position;
    private String report_me;


}
