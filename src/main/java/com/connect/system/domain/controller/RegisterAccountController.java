package com.connect.system.domain.controller;

import com.connect.system.domain.model.EntityPerson.PasswordPerson;
import com.connect.system.domain.model.EntityPerson.Person;
import com.connect.system.domain.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class RegisterAccountController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping("/register")
    public ResponseEntity registerPerson(@RequestBody Person person, PasswordPerson passwordPerson) {
        var userCreated = this.personRepository.save(person);


        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
