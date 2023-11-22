package com.connect.system.domain.model.System.HRSystemCommunity;


import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HRSystemCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_system_community;

    private String name_of_system_community; //Recrutamento e Seleção, depart. pessoal...

    private TypeOfRecord type; //ADMINISTRATION OR HUMAN RESOURCES

    //Pra colocar aqui ele precisa existir com um cargo XXXXXX
    // Aqui vai ser o hr_manager do SystemHierarchyGroupHR
    private String head_responsible;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HierarchyGroupHR hierarchyGroupHR;

    public HRSystemCommunity(String name_of_system_community, TypeOfRecord type, String head_responsible, HierarchyGroupHR hierarchyGroupHR) {
        this.name_of_system_community = name_of_system_community;
        this.type = type;
        this.head_responsible = head_responsible;
        this.hierarchyGroupHR = hierarchyGroupHR;
    }

}
