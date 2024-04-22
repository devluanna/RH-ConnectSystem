package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.ListAssociatesHierarchy;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import org.springframework.stereotype.Service;

@Service
public interface HierarchyGroupService {
    HierarchyGroup createGroupHierarchy(HierarchyGroup hierarchyGroup, Person person);

    HierarchyGroup toUpdateGroup(HierarchyGroup group, Integer id_group_of_hierarchy, ListAssociatesHierarchy listAssociatesHierarchy);

    ListAssociatesHierarchy addGroup(ListAssociatesHierarchy listAssociatesHierarchy, HierarchyGroup groupHierarchy, Person person, Integer id);


}
