package com.connect.system.service;

import com.connect.system.domain.model.System.Squad.MemberDashboardSquad;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.Squad.Squad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SquadService {
    Squad createSquad(Long id_squad, Squad squad, MemberDashboardSquad memberDashboardSquad);

    List<Squad> getAllSquads();

    Members addMemberToSquad(Long squad_id, Long id, Long id_dashboardMembers, Members members);

    Squad getSquadsWithMembersById(Long id_squad);

    Squad toUpdateSquad(Long id_squad, Squad squads, Long id);

    void removeMemberFromSquad(Long id_squad, Long id_member);

    //Squad findSquadById(Long id_squad);



}
