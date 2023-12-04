package com.connect.system.domain.model.System.TechnologyCommunity;

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
    private int id_group_of_community;

    private String community; // comunidade de seguranca, engenharia, desenvolvimento, arquitetura, cloud > Cada comunidade vai ter um grupo

    private String type; //TECNOLOGIA, ADMIN?

    private String responsible; //responsavel por criar o grupo (pode ser um coord do rh)

    private String name_ceo; //CEO dono da porra toda ESTATICO

    private String president; // presidente de todos ESTATICO

    private String director; // diretor de todos ESTATICO

    private String manager_head; // (responsible_head da classe Community) gestor acima de outros gestores (exemplo, gestor daquela area) MUDAR DEPENDENDO DA AREA / podendo ser de mais de uma ocmunidade

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommunityAssociates> communityAssociate = new ArrayList<>(); //Membros (outros gestores/coordenadores desse grupo hierarquico da comunidade X)


}
