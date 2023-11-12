package com.connect.system.domain.model.Account.Jobs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JsonPropertyOrder({ "id_job_details", "id_account", "name_squad", "type_of_record", "office", "occupancy_area", "seniority", "time_experience", "hard_skills", "soft_skills", "language_primary", "report_me", "admission_date"})
public class JobsDetails {

    //Alguns campos serao liberados para o usuario atualizar, outros apenas o RH pode atualizar
    // Fazer um DTO para o usuario atualizar os campos que ele pode e outro para o RH?

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_job_details;

    private Long id_account;
    private String identity;

    private String description;
    private String name_squad;//VERIFICAR SE ELE ESTA DENTRO DE UMA SQUAD "ALLOCATED", SE FOR VERDADE RETORNAR O NOME DA SQUAD, SE NAO "ELE NAO ESTA EM UMA SQUAD"

    private String type_of_record;
    private String office;
    private String occupancy_area;
    private String seniority;

    private int time_experience; //tempo de experiencia
    private String hard_skills; //JAVA, SPRINGBOOT, QUARKUS, .NET, C#, AWS, CLOUD
    private String soft_skills; //COMUNICATIVO, NANANA
    private String language_primary; //PORTUGUES-BRAZIL
   // private Date time_squad; //CALCULAR TEMPO DE ENTRADA DA SQUAD ATE O TEMPO ATUAL
    private String report_me; //GESTOR ACIMA DELE (GESTOR DA SQUAD QUE ELE ESTA)
    private String admission_date; //DATA DE ADMISSAO

       public JobsDetails() {
        this.description = "Busco crescimento profissional";
        this.name_squad = "Rogue One";
        this.time_experience = 3;
        this.hard_skills = "JAVA, SPRINGBOOT, .NET";
        this.soft_skills = "COMUNICATIVA";
        this.language_primary = "PORTUGUESE";
        this.report_me = "Eduardo Sanches";
        this.admission_date = "20/10/2020";
    }


}
