package com.connect.system.rest.controllers.System;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonByOfficeHeadDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.TechnologyCommunityService;
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


    @PostMapping("/create/technology-community")
    public ResponseEntity createTechnologyCommunity(@RequestBody TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Person person) {

        TechnologyCommunity techCommunityCreated = technologyCommunityService.createTechCommunity(technologyCommunity, hierarchyGroupTechnology, person);

        return ResponseEntity.ok(techCommunityCreated);

    }

    @GetMapping("/list-user/office-head")
    public List<PersonByOfficeHeadDTO> getHeadDeliveryUsers() {

        List<PersonByOfficeHeadDTO> headDeliveryUsers = personRepository.findUserByOffice(Office.HEADDELIVERY);

        return headDeliveryUsers;
    }



}
