package com.connect.system.domain.model.System.TechnologyCommunity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupOfCommunity {

    //GRUPO CRIADO AUTOMATICAMENTE QUANDO SE CRIA A COMUNIDADE
    //PARA OS GESTORES DAQUELA COMUNIDADE RECEBEREM NO REPORT-ME O CAMPO DO VALOR HEAD_RESPONSIBLE

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_group_of_community;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_community")
    private TechnologyCommunity technologyCommunity;

    private String name_community;

    private String head_responsible;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private static List<CommunityAssociates> communityAssociate = new ArrayList<>();
    //Criar outro LIST pra comunidade!
    //Membros (outros gestores/coordenadores desse grupo hierarquico da comunidade X)

    public static void addAssociateManager(CommunityAssociates associates) {
        communityAssociate.add(associates);
    }
    public void setAssociateManager(List<CommunityAssociates> communityAssociate) {
        this.communityAssociate = communityAssociate;
    }
    public List<CommunityAssociates> getCommunityAssociate() {
        return communityAssociate;
    }

}

