package com.connect.system.domain.model.System.Squad;

import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;


@Entity
@Data
@NoArgsConstructor
public class Squad {

    //private String positions;
    // verificar se existe alguma vaga aberta na LIST ARRAY DE VAGAS RELACIONADAS A SQUAD, se existir positions = true

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_squad;

    private String name_squad;

    private String teach_lead; //preenchido automaticamente > se for RH OU ADMIN TEAMLEADER
    private String project_manager; //responsavel por ter criado

    private TypeOfRecord type; //TECNOLOGIA OU ADMIN/RH
    private String customer_project; //TERCERIZADO/INTERNO
    private StatusSquad status; //ATIVO ou INATIVO = se tiver vagas relacionadas

    private String area_squad; //SEGURANCA, DESENVOLVIMENTO BACKEND, DESENVOLVIMENTO FULLSTACK, PRODUCTS, (CLASSE COMMUNITY tecnologia ou RH)

    //FAZER LOGICA PRA ATRELAR UM TIPO DE TYPEOFRECORD EM UMA COMUNIDADE? PRA FILTRAR?
    // >>> SE FOR TECH > STREAM FOREACH LISTAR COMUNIDADES RELACIONADAS A ELE


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "members_id")
    private MemberDashboardSquad allMembers;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Date createdAt;


    public Squad(String name_squad, String teach_lead, String project_manager, TypeOfRecord type, String area_squad, String customer_project, Date createdAt, MemberDashboardSquad allMembers) {
        this.name_squad = name_squad;
        this.teach_lead = teach_lead;
        this.project_manager = project_manager;
        this.type = type;
        this.area_squad = area_squad;
        this.customer_project = customer_project;
        this.status = StatusSquad.valueOf("ACTIVE");
        this.createdAt = new Date();
        this.allMembers = allMembers;
    }

}
