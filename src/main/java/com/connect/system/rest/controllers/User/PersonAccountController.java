package com.connect.system.rest.controllers.User;


import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.UpdatePersonDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.service.AccountService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonAccountController {

    @Autowired
    AccountService accountService;

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity create(@RequestBody PersonDTO data, Person newUser, PersonalData personalDataUser, JobsDetails userJobInformation, DashboardStudies dashboardStudies) {

        PersonDTO createdUser = accountService.createPerson(newUser, data, personalDataUser, userJobInformation, dashboardStudies);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable Integer id, @RequestBody UpdatePersonDTO updatePersonDTO, Members members) {

        if (id == null) {
           System.out.println("N ENCONTROU");
            return ResponseEntity.badRequest().build();
        }
        Person account = accountService.findById(id);

        UpdatePersonDTO updatedAccount = accountService.toUpdatePerson(account, id, updatePersonDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity <PersonDTO> getUserById(@PathVariable Integer id) {
        PersonDTO personUser = accountService.getUserById(id);

     if(personUser == null) {
         ResponseEntity.notFound().build();
     }

        return ResponseEntity.ok(personUser);
    }

    @GetMapping("/list-users")
    public ResponseEntity<List<PersonDTO>> getAllUsers() {

        List<PersonDTO> user = accountService.getAllUsers();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


}
