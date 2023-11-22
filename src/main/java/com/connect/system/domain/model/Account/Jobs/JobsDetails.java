package com.connect.system.domain.model.Account.Jobs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JsonPropertyOrder({ "id_job_details", "id_account", "identity", "id_squad", "id_memberOfSquad", "name_squad", "type_of_record", "office", "occupancy_area", "seniority", "time_experience", "hard_skills", "soft_skills", "language_primary", "admission_date"})
public class JobsDetails {

    //MANAGER >>>>>>>>> SQUAD PRA HIERARQUIA DELES
    //VALIDACOES DA SQUAD AINDA:
    // APENAS RH E MANAGERS -> ADICIONAR USUARIOS; >SECURITY
    // QUANDO O RH FOR ADICIONAR UM USUARIO NA SQUAD APARECER UMA LISTA DE TODOS OS GESTORES DISPONIVEIS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_job_details;

    private Long id_account;
    private String identity;

    private Long id_squad;
    private Long id_memberOfSquad;
    private String name_squad;

    private String description;

    private String type_of_record;
    private String office;
    private String occupancy_area;
    private String seniority;

    private int time_experience;
    private String hard_skills;
    private String soft_skills;
    private String language_primary;
    private String admission_date;


}
