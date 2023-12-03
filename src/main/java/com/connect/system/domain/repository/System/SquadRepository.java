package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.Squad.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SquadRepository extends JpaRepository<Squad, Integer> {
    @Query("SELECT DISTINCT s FROM Squad s LEFT JOIN FETCH s.allMembers.members WHERE s.id_squad = :id_squad")
    Squad findSquadWithMembers(@Param("id_squad") Integer id_squad);
}
