package com.connect.system.service;

import com.connect.system.domain.model.System.Squad.MemberDashboardSquad;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.Squad.Squad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SquadService {
    Squad createSquad(Integer id_squad, Squad squad, MemberDashboardSquad memberDashboardSquad);

    List<Squad> getAllSquads();

    Members addMemberToSquad(Integer squad_id, Integer id, Integer id_dashboardMembers, Members members);

    Squad getSquadsWithMembersById(Integer id_squad);

    Squad toUpdateSquad(Integer id_squad, Squad squads, Integer id);

    void removeMemberFromSquad(Integer id_squad, Integer id_member);

    //Squad findSquadById(Long id_squad);



}
