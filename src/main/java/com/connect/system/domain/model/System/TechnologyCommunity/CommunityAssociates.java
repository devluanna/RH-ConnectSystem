package com.connect.system.domain.model.System.TechnologyCommunity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CommunityAssociates {

    //Array de Gestores (q tem squads criadas por eles mesmos) e que estao abaixo dos NIVEIS MAIORES

    @Id
    private Long id_associate;
    private Long id_squad;
    private Long id_account;
    private String identity;
    private String name;
    private String last_name;
    private String office;
    private String occupancy_area;
    private String seniority;

}
