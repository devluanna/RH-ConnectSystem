package com.connect.system.domain.model.System.Squad.Group;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SquadHierarchyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_group_squad; //id do grupo

    private Long id_squad; //id da squad
    private String name_squad; //nome da squad

    private String type;
    private String name_community; //nome da comunidade

    private String community_head_manager; //manager_head da comunidade
    private String group_owner; //quem criou a squad (pq ai todos os gestores "JRrs" que assumirem o cargo dele, vao ficar abaixo e receber o report_me o nome dele)

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SquadGroupAssociates> squadGroupAssociates = new ArrayList<>();

}
