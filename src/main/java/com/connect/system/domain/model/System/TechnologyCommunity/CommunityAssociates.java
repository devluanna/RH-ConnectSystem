package com.connect.system.domain.model.System.TechnologyCommunity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommunityAssociates {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_group_of_community")
    @JsonIgnore
    private GroupOfCommunity group;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_associate;

    //private Integer id_community;
    private String name_community;

    private Integer id_account;
    private String identity;
    private String name_associate;
    private String office;
    private String seniority;

}
