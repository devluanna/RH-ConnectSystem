package com.connect.system.domain.model.System.TechnologyCommunity;

import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_community;

    private Integer id_group_community;

    private String name_of_community;
    private TypeOfRecord type;
    private String head_responsible;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_community_id")
    private GroupOfCommunity group_of_community;

    public TechnologyCommunity(String name_of_community, String head_responsible, GroupOfCommunity group_of_community) {
        this.name_of_community = name_of_community;
        this.type = TypeOfRecord.valueOf("TECHNOLOGY");
        this.head_responsible = head_responsible;
        this.group_of_community = group_of_community;
    }


}
