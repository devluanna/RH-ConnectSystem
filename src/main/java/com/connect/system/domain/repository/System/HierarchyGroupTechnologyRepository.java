package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HierarchyGroupTechnologyRepository extends JpaRepository<HierarchyGroupTechnology, Integer> {

}
