package com.connect.system.domain.model.System.Squad.Group;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SquadGroupAssociates {

    //Gestores e coordenadores abaixo do GESTOR CRIADOS DA SQUAD

    @Id
    private Long id_group_member;
    private Long id_squad;
    private String name_squad;

    private Long id_account;
    private String identity;
    private String name;
    private String last_name;
    private String office;
    private String occupancy_area;
    private String seniority;
    private String sub_position;
    private String report_me;

}
