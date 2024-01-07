package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import org.springframework.stereotype.Service;

@Service
public interface TechnologyCommunityService {
    TechnologyCommunity createTechCommunity(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology, Person person);

    TechnologyCommunity toUpdateCommunity(TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Integer id_community, Person person, CommunityAssociates associates);

    CommunityAssociates addCommunity(CommunityAssociates requestAssociate, TechnologyCommunity technologyCommunity, Person person, Integer id);
}
