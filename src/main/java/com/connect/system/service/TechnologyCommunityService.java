package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.*;
import org.springframework.stereotype.Service;

@Service
public interface TechnologyCommunityService {
    TechnologyCommunity createTechCommunity(TechnologyCommunity data, Person person, HierarchyGroup hierarchyGroup, Integer id_group_of_hierarchy, GroupOfCommunity groupOfCommunity);

    TechnologyCommunity toUpdateCommunity(TechnologyCommunity technologyCommunity, Integer id_community, Person person, CommunityAssociates associates);

    CommunityAssociates addCommunity(CommunityAssociates requestAssociate, TechnologyCommunity technologyCommunity, Person person, Integer id, GroupOfCommunity groupOfCommunity);
}
