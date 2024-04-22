package com.connect.system.domain.model.System.TechnologyCommunity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ListAssociatesHierarchy {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_group_of_hierarchy")
    @JsonIgnore
    private HierarchyGroup group;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_associate;

    private Integer id_group;

    //private Integer id_community;
    private String name_community;

    private Integer id_account;
    private String identity;
    private String name_associate;
    private String office;
    private String seniority;



}
