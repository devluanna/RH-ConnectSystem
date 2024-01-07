package com.connect.system.rest.controllers.System;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonByOfficeHeadDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
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
    public ResponseEntity createTechnologyCommunity(@RequestBody TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Person person) {

        TechnologyCommunity techCommunityCreated = technologyCommunityService.createTechCommunity(technologyCommunity, hierarchyGroupTechnology, person);

        return ResponseEntity.ok(techCommunityCreated);

    }

    @PostMapping("/add-associate-in-community")
    public ResponseEntity addCommunityAssociate(@RequestBody CommunityAssociates requestAssociate, @RequestParam Integer id_community, Person person, Integer id) {

        TechnologyCommunity technologyCommunity = technologyCommunityRepository.findById(id_community)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));


        CommunityAssociates associateAdded = technologyCommunityService.addCommunity(requestAssociate, technologyCommunity, person, id);

        return ResponseEntity.ok(associateAdded);
    }



    @PutMapping("/update/technology-community/{id_community}")
    public ResponseEntity updateCommunity(@PathVariable Integer id_community, @RequestBody TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Person person, CommunityAssociates associates) {

        TechnologyCommunity techCommunitySaved = technologyCommunityService.toUpdateCommunity(technologyCommunity, hierarchyGroupTechnology, id_community, person, associates);

        return ResponseEntity.ok(techCommunitySaved);
    }

    @GetMapping("/list-user/office-head")
    public List<PersonByOfficeHeadDTO> getHeadDeliveryUsers() {

        List<PersonByOfficeHeadDTO> headDeliveryUsers = personRepository.findUserByOffice(Office.HEADDELIVERY);

        return headDeliveryUsers;
    }



}
