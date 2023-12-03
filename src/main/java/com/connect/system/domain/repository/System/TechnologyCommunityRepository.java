package com.connect.system.domain.repository.System;

import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyCommunityRepository extends JpaRepository<TechnologyCommunity, Integer> {
}
