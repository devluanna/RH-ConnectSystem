package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import org.springframework.stereotype.Service;

@Service
public interface TechnologyCommunityService {
    TechnologyCommunity createTechCommunity(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology, Person person);
}
