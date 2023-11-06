package com.connect.system.rest.controller;


import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponsePersonDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
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
    public ResponseEntity create(@RequestBody ResponsePersonDTO data, Person newUser) {

        ResponsePersonDTO createdUser = accountService.createPerson(newUser, data);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity updatePerson(@PathVariable Long id, @RequestBody ResponsePersonDTO updatePersonDTO) {

        Person account = accountService.findById(id);

        ResponsePersonDTO updatedAccount = accountService.update(account, id, updatePersonDTO);
        return ResponseEntity.ok(updatedAccount);
    }



    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseGetDTO> getUserById(@PathVariable Long id) {
        ResponseGetDTO userPerson = accountService.getUserById();

        if(userPerson == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userPerson);
    }


    @GetMapping("/list-users")
    public ResponseEntity<List<ResponseGetDTO>> getAllUsers() {
        List<ResponseGetDTO> user = accountService.getAllUsers();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }


}
