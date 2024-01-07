package com.connect.system.domain.model.System.TechnologyCommunity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class HierarchyGroupTechnology {
    // PRESIDENTE > CEO
    // DIRECTOR > PRESIDENTE
    // HEAD > DIRECTOR
    //MANAGER SENIOR RESPONDE AO GESTOR HEAD
    //PLENO > SENIOR
    //JUNIOR > PLENO OU DIRETO NO SENIOR

    //Hierarquia dentro da comunidade criada


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_group_of_community;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_community")
    private TechnologyCommunity technologyCommunity;

   private Integer community_id;

    private String name_community;

    private String type;

    private String name_ceo;

    private String president;

    private String director;

    private String manager_head;// (responsible_head da classe Community) gestor acima de outros gestores (exemplo, gestor daquela area) MUDAR DEPENDENDO DA AREA / podendo ser de mais de uma ocmunidade

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommunityAssociates> communityAssociate = new ArrayList<>(); //Membros (outros gestores/coordenadores desse grupo hierarquico da comunidade X)

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
