package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HierarchyGroupRepository extends JpaRepository<HierarchyGroup, Integer> {


}
