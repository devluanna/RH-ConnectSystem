package com.connect.system.domain.model.System.TechnologyCommunity;

import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_community;

    private String name_of_community;
    private TypeOfRecord type;
    private String head_responsible;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "hierarchyGroupTechnology_id")
    private HierarchyGroupTechnology hierarchyGroupTechnology;

    public TechnologyCommunity(String name_of_community, String head_responsible, HierarchyGroupTechnology hierarchyGroupTechnology) {
        this.name_of_community = name_of_community;
        this.type = TypeOfRecord.valueOf("TECHNOLOGY");
        this.head_responsible = head_responsible;
        this.hierarchyGroupTechnology = hierarchyGroupTechnology;
    }


}
