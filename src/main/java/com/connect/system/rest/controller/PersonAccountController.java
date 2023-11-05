package com.connect.system.rest.controller;


import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponsePersonDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.UpdateProfileDTO;
import com.connect.system.domain.model.Informations.Location;
import com.connect.system.domain.model.Informations.PersonalData;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.connect.system.domain.repository.PersonRepository;
import com.connect.system.domain.repository.PersonalDataRepository;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import com.connect.system.service.AccountService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/person")
public class PersonAccountController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonalDataRepository personalDataRepository;
    @Autowired
    AccountService accountService;

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

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProfileDTO> updateInfo(@PathVariable Long id, @RequestBody UpdateProfileDTO updateProfileDTO) {

        Person account = accountService.findById(id);

        account.setName(updateProfileDTO.getName());
        account.setLast_name(updateProfileDTO.getLast_name());
        account.setEmail(updateProfileDTO.getEmail());


        Person updatedAccount = accountService.update(account);

        UpdateProfileDTO responseDTO = new UpdateProfileDTO();
        responseDTO.setId(updatedAccount.getId());
        responseDTO.setName(updatedAccount.getName());
        responseDTO.setLast_name(updatedAccount.getLast_name());
        responseDTO.setEmail(updatedAccount.getEmail());
        responseDTO.setProfileRole(updatedAccount.getProfileRole());
        responseDTO.setStatus(updatedAccount.getStatus());


        return ResponseEntity.ok(responseDTO);
    }


    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity create(@RequestBody ResponsePersonDTO data) {

        PersonalData personalData = new PersonalData();
        JobsDetails aboutJob = new JobsDetails();

        Person response = new Person(data.getName(), data.getLast_name(), data.getEmail(), data.getProfileRole(), personalData, data.getIdentityPerson(), data.getPassword(), aboutJob);

        Person createdUser = accountService.createPerson(response);
        this.personRepository.save(createdUser);

        accountService.createInformations(personalData, aboutJob, createdUser.getId(), createdUser.getIdentityPerson());

        BeanUtils.copyProperties(createdUser, data);

        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }


}
