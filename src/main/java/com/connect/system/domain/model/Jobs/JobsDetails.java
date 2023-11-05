package com.connect.system.domain.model.Jobs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "tb_job_details")
@JsonPropertyOrder({ "id_job_details", "id_account", "name_squad", "type_of_record", "seniority", "office", "occupancy_area", "time_experience", "hard_skills", "soft_skills", "language_primary", "report_me", "admission_date"})
public class JobsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_job_details;

    private Long id_account;
    private String identity;

    private String description;

    private String name_squad;//VERIFICAR SE ELE ESTA DENTRO DE UMA SQUAD "ALLOCATED", SE FOR VERDADE RETORNAR O NOME DA SQUAD, SE NAO "ELE NAO ESTA EM UMA SQUAD"
    private String type_of_record; //TECNOLOGIA, ADMINISTRACAO, ARQUITETURA, RH...
    private String seniority; //ENUM NIVEL JUNIOR, TRAINEE
    private String office; //CARGO: Coordenador, diretor, executivo, gestor, analista, desenvolvedor, teachlead, ENGINEER
    private String occupancy_area; //TABELA PROPRIA? (CLASS CONFORME FOR COLOCANDO, VAI ADICIONANDO NA TABLEA) TESTER(QA), MOBILE, WEB E MOBILE, WEB, JAVA, ANALISTA, ARQUITETO DE SOLUCOES, ARQUITETO AWS, ARQUITETO CLOUD.....
    private int time_experience; //tempo de experiencia
    private String hard_skills; //JAVA, SPRINGBOOT, QUARKUS, .NET, C#, AWS, CLOUD
    private String soft_skills; //COMUNICATIVO, NANANA
    private String language_primary; //PORTUGUES-BRAZIL
   // private Date time_squad; //CALCULAR TEMPO DE ENTRADA DA SQUAD ATE O TEMPO ATUAL
    private String report_me; //GESTOR ACIMA DELE (GESTOR DA SQUAD QUE ELE ESTA)
    private String admission_date; //DATA DE ADMISSAO

    public JobsDetails() {
        this.id_job_details = getId_job_details();
        this.description = "Busco constantemente o crescimento profissional e pessoal, colocando em prática minhas habilidades colaborativas";
        this.name_squad = "Rogue One";
        this.type_of_record = "Tech";
        this.seniority = "Intern";
        this.office = "Developer";
        this.occupancy_area = "MOBILE";
        this.time_experience = 3;
        this.hard_skills = "JAVA, SPRINGBOOT, .NET";
        this.soft_skills = "COMUNICATIVA";
        this.language_primary = "PORTUGUESE";
        this.report_me = "Eduardo Sanches";
        this.admission_date = "20/10/2020";

    }
}
