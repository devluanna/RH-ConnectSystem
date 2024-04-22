package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.ListAssociatesHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityAssociatesRepository extends JpaRepository<CommunityAssociates, Integer> {

    @Query("SELECT h FROM CommunityAssociates h WHERE h.id_account = :id_account")
    Optional<CommunityAssociates> findByAccountId(Integer id_account);


}
