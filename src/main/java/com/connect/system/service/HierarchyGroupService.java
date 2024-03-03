package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import org.springframework.stereotype.Service;

@Service
public interface HierarchyGroupService {
    HierarchyGroup createGroupHierarchy(HierarchyGroup hierarchyGroup);

    HierarchyGroup toUpdateGroup(HierarchyGroup group, Integer id_group_of_hierarchy);

    CommunityAssociates addGroup(CommunityAssociates communityAssociates, HierarchyGroup groupHierarchy, Person person, Integer id);
}
