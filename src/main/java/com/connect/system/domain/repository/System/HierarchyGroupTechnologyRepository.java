package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HierarchyGroupTechnologyRepository extends JpaRepository<HierarchyGroupTechnology, Integer> {

    @Query("SELECT h FROM HierarchyGroupTechnology h WHERE h.name_community = :name_community")
    HierarchyGroupTechnology findByCommunityName(@Param("name_community") String name_community);

}
