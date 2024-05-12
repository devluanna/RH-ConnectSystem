package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnologyCommunityRepository extends JpaRepository<TechnologyCommunity, Integer> {


    @Query("SELECT c FROM TechnologyCommunity c WHERE c.name_of_community = :name_of_community")
    List<TechnologyCommunity> findByNameCommunity(String name_of_community);

}
