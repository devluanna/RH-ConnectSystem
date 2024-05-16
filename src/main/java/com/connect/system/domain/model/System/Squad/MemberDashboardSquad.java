package com.connect.system.domain.model.System.Squad;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class MemberDashboardSquad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_dashboardMembers;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_squad")
    private Squad squad;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Members> members = new ArrayList<>();

    public void addMembers(Members member) {
        if (members == null) {
            members = new ArrayList<>();
        }

        // Define o ID da squad para o membro
        member.setId_squad(this.squad.getId_squad());

        // Adiciona o membro à lista de membros da squad
        members.add(member);
    }
    public void setMembers(List<Members> members) {
        this.members = members;
    }
    public List<Members> getMembers() {
        return members;
    }

}
