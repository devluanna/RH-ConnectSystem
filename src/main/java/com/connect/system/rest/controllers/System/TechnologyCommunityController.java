package com.connect.system.rest.controllers.System;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.service.TechnologyCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class TechnologyCommunityController {

    @Autowired
    TechnologyCommunityService technologyCommunityService;


    @PostMapping("/create/technology-community")
    public ResponseEntity createTechnologyCommunity(@RequestBody TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Person person) {

        TechnologyCommunity techCommunityCreated = technologyCommunityService.createTechCommunity(technologyCommunity, hierarchyGroupTechnology, person);

        return ResponseEntity.ok(techCommunityCreated);

    }



}
