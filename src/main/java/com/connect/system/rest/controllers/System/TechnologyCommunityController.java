package com.connect.system.rest.controllers.System;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonByOfficeHeadDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.TechnologyCommunityRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.TechnologyCommunityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class TechnologyCommunityController {

    @Autowired
    TechnologyCommunityService technologyCommunityService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TechnologyCommunityRepository technologyCommunityRepository;


    @PostMapping("/create/technology-community")
    public ResponseEntity createTechnologyCommunity(@RequestBody TechnologyCommunity technologyCommunity, Person person,  HierarchyGroup hierarchyGroup, Integer id_group_of_hierarchy) {

        TechnologyCommunity techCommunityCreated = technologyCommunityService.createTechCommunity(technologyCommunity, person, hierarchyGroup, id_group_of_hierarchy);

        return ResponseEntity.ok(techCommunityCreated);

    }

    @PostMapping("/add-associate-in-community")
    public ResponseEntity addCommunityAssociate(@RequestBody CommunityAssociates requestAssociate, @RequestParam Integer id_community, Person person, Integer id, HierarchyGroup hierarchyGroup) {

        TechnologyCommunity technologyCommunity = technologyCommunityRepository.findById(id_community)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));


        CommunityAssociates associateAdded = technologyCommunityService.addCommunity(requestAssociate, technologyCommunity, person, id, hierarchyGroup );

        return ResponseEntity.ok(associateAdded);
    }



    @PutMapping("/update/technology-community/{id_community}")
    public ResponseEntity updateCommunity(@PathVariable Integer id_community, @RequestBody TechnologyCommunity technologyCommunity, Person person, CommunityAssociates associates) {

        TechnologyCommunity techCommunitySaved = technologyCommunityService.toUpdateCommunity(technologyCommunity, id_community, person, associates);

        return ResponseEntity.ok(techCommunitySaved);
    }

 /*   @GetMapping("/list-user/office-head")
    public List<PersonByOfficeHeadDTO> getHeadDeliveryUsers() {

        List<PersonByOfficeHeadDTO> headDeliveryUsers = personRepository.findUserByOffice(Office.HEADDELIVERY);

        return headDeliveryUsers;
    }*/



}
