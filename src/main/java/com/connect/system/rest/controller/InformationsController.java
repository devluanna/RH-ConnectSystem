package com.connect.system.rest.controller;

import com.connect.system.domain.model.Account.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.Location;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.service.InformationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/api/info")
public class InformationsController {

    @Autowired
    InformationsService informationsService;

    @GetMapping("/user/{id_personalData}")
    public ResponseEntity<PersonalDataDTO> getById(@PathVariable Long id_personalData) {

        PersonalDataDTO personalData = informationsService.getById(id_personalData);

        if(personalData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personalData);
    }

    @PutMapping("/personal-data/{id_personalData}")
    public ResponseEntity updateData(@PathVariable Long id_personalData, @RequestBody PersonalDataDTO personalDataDTO) {

        PersonalData personalData = informationsService.findByIdPersonalData(id_personalData);

        PersonalDataDTO updatedPersonalData = informationsService.updatePersonalData(personalData, id_personalData, personalDataDTO);

        return ResponseEntity.ok(updatedPersonalData);
    }

    @PutMapping("/location/{id_location}")
    public ResponseEntity updateLocation(@PathVariable Long id_location, @RequestBody Location location) {

        Location updatedLocation = informationsService.updateLocation(location, id_location);

        return ResponseEntity.ok(updatedLocation);
    }



}
