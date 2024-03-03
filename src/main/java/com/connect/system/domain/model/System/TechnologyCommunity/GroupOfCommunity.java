package com.connect.system.domain.model.System.TechnologyCommunity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class GroupOfCommunity {

    //GRUPO CRIADO AUTOMATICAMENTE QUANDO SE CRIA A COMUNIDADE, PARA OS GESTORES DAQUELA COMUNIDADE RECEBEREM NO REPORT-ME O CAMPO DO VALOR HEAD_RESPONSIBLE


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_group_of_community;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_community")
    private TechnologyCommunity technologyCommunity;

    private Integer community_id;

    private String name_community;

    private String head_responsible;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CommunityAssociates> communityAssociate = new ArrayList<>(); //Membros (outros gestores/coordenadores desse grupo hierarquico da comunidade X)

    public void addAssociateManager(CommunityAssociates associates) {
        communityAssociate.add(associates);
    }
    public void setMembers(List<CommunityAssociates> communityAssociate) {
        this.communityAssociate = communityAssociate;
    }
    public List<CommunityAssociates> getCommunityAssociate() {
        return communityAssociate;
    }

}

