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


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_group_of_hierarchy;

    private String name_of_group;

    private TypeOfRecord type_of_group;

    private String name_ceo;

    private String president;

    private String director;

    private String hr_director;


    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ListAssociatesHierarchy> communityAssociate = new ArrayList<>(); //Membros (outros HEADS desse grupo hierarquico)


    public HierarchyGroup(String name_of_group, TypeOfRecord type_of_group, String director, String hr_director, String president, String name_ceo){
        this.name_of_group = name_of_group;
        this.type_of_group = type_of_group;
        this.director = director;
        this.hr_director = hr_director;
        this.president = president;
        this.name_ceo = name_ceo;
    }


    public void addAssociates(ListAssociatesHierarchy associates) {
        communityAssociate.add(associates);
    }
    public void setMembers(List<ListAssociatesHierarchy> communityAssociate) {
        this.communityAssociate = communityAssociate;
    }
    public List<ListAssociatesHierarchy> getCommunityAssociate() {
        return communityAssociate;
    }

}
