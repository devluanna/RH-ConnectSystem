package com.connect.system.domain.model.System.TechnologyCommunity;

import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyGroup {
    // PRESIDENTE > CEO
    // DIRECTOR > PRESIDENTE
    // HEAD > DIRECTOR
    //MANAGER SENIOR RESPONDE AO GESTOR HEAD
    //PLENO > SENIOR
    //JUNIOR > PLENO OU DIRETO NO SENIOR

    //TODO USUARIO COM OFFICE HEADDELIVERY JA VAI ENTRAR NESSE GROUP AUTOMATICAMENTE PARA RECEBER O REPORT ME DO VALOR DIRECTOR//
    //FAZER LOGICA PRA CADA UM IR RECEBENDO O VALOR DE REPORT-ME DO CARGO ACIMA DO DELE//

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_group_of_hierarchy;

    private String name_of_group;

    private TypeOfRecord type_of_group;

    private String name_ceo;

    private String president;

    private String director;

    private String hr_director;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommunityAssociates> communityAssociate = new ArrayList<>(); //Membros (outros HEADS desse grupo hierarquico)

    public HierarchyGroup(String name_of_group, TypeOfRecord type_of_group){
        this.name_of_group = name_of_group;
        this.type_of_group = type_of_group;
    }


    public void addAssociates(CommunityAssociates associates) {
        communityAssociate.add(associates);
    }
    public void setMembers(List<CommunityAssociates> communityAssociate) {
        this.communityAssociate = communityAssociate;
    }
    public List<CommunityAssociates> getCommunityAssociate() {
        return communityAssociate;
    }

}
