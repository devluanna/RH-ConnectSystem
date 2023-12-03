package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.Account.DashboardStudies.Certificates;
import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.Squad.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembersSquadRepository extends JpaRepository<Members, Integer> {

    @Query("SELECT s.allMembers.members FROM Squad s WHERE s.id_squad = :id_squad")
    List<Members> findMembersBySquadId(@Param("id_squad") Integer id_squad);


}
