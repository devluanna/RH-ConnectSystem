package com.connect.system.domain.model.System.HRSystemCommunity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class HierarchyGroupHR {

    // Se o usuario "COORDENADOR" criado for um RH ele automaticamente entra aqui dentro? >ANALISAR<
    //Hierarquia para a parte de administracao/RH

    @Id
    @GeneratedValue
    private Long id_group_of_system;

    private String ceo; //estatico

    private String president;//estatico

    private String general_director;//estatico

    private String hr_director; //estatico

    private String hr_manager; //podendo mudar em algum momento

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommunityAssociatesHR> communityAssociatesHR = new ArrayList<>();

}
