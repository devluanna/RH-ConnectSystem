package com.connect.system.domain.model.System.HRSystemCommunity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CommunityAssociatesHR {

    //Coordenadores responsaveis por terem criado grupos na comunidade do systemHR por eles mesmo

    @Id
    private Long id_associateHR;

    private Long id_squad;
    private Long id_account;

    private String identity;
    private String name;
    private String last_name;
    private String office;
    private String seniority;
    private String name_of_system_community;


}
