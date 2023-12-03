package com.connect.system.rest.controllers.User;

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
    public ResponseEntity<PersonalDataDTO> getById(@PathVariable Integer id_personalData) {

        PersonalDataDTO personalData = informationsService.getById(id_personalData);

        if(personalData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(personalData);
    }

    @PutMapping("/personal-data/{id_personalData}")
    public ResponseEntity updateData(@PathVariable Integer id_personalData, @RequestBody PersonalData personalData) {

        PersonalData  updatedPersonalData = informationsService.toUpdatePersonalData(personalData, id_personalData);

        return ResponseEntity.ok(updatedPersonalData);
    }

    @PutMapping("/location/{id_location}")
    public ResponseEntity updateLocation(@PathVariable Integer id_location, @RequestBody Location location) {

        Location updatedLocation = informationsService.toUpdateLocation(location, id_location);

        return ResponseEntity.ok(updatedLocation);
    }



}
