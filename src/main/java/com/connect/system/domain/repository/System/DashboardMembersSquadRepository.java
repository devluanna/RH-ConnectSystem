package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.Squad.MemberDashboardSquad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardMembersSquadRepository extends JpaRepository<MemberDashboardSquad, Integer> {
}
