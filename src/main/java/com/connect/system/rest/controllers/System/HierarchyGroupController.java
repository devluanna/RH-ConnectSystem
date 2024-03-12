package com.connect.system.rest.controllers.System;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.HierarchyGroupRepository;
import com.connect.system.service.HierarchyGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HierarchyGroupController {

    @Autowired
    HierarchyGroupService hierarchyGroupService;

    @Autowired
    HierarchyGroupRepository hierarchyGroupRepository;

    @PostMapping("/create/group-hierarchy")
    public ResponseEntity createGroupHierarchy(@RequestBody HierarchyGroup hierarchyGroup, Person person ) {

        HierarchyGroup GroupCreated = hierarchyGroupService.createGroupHierarchy(hierarchyGroup, person);

        return ResponseEntity.ok(GroupCreated);

    }

    @PutMapping("/update/group/{id_group_of_hierarchy}")
    public ResponseEntity updateCommunity(@PathVariable Integer id_group_of_hierarchy, @RequestBody HierarchyGroup group, CommunityAssociates communityAssociates) {

        HierarchyGroup groupExistingSaved = hierarchyGroupService.toUpdateGroup(group, id_group_of_hierarchy, communityAssociates);

        return ResponseEntity.ok(groupExistingSaved);
    }



   @PostMapping("/add-associate-in-group")
    public ResponseEntity addAssociateInGroup(@RequestBody CommunityAssociates communityAssociates, @RequestParam Integer id_group_of_hierarchy, Person person, Integer id) {

       HierarchyGroup groupHierarchy = hierarchyGroupRepository.findById(id_group_of_hierarchy)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));


        CommunityAssociates associateAdded = hierarchyGroupService.addGroup(communityAssociates, groupHierarchy, person, id);

        return ResponseEntity.ok(associateAdded);
    }


}
