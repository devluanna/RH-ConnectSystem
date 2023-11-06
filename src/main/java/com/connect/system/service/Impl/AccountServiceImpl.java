package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponsePersonDTO;
import com.connect.system.domain.model.AccountInformation.Location;
import com.connect.system.domain.model.AccountInformation.PersonalData;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.connect.system.domain.repository.JobDetailsRepository;
import com.connect.system.domain.repository.PersonRepository;
import com.connect.system.domain.repository.PersonalDataRepository;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import com.connect.system.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonalDataRepository personalDataRepository;
    @Autowired
    JobDetailsRepository jobDetailsRepository;


   @Override
   @Transactional
    public List<ResponseGetDTO> getAllUsers() {
     return personRepository.findAllUsersWithPersonalDataIds();
  }

    @Override
    @Transactional
    public ResponseGetDTO getUserById() {
       return personRepository.findUserById();
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    @Override
    @Transactional
    public ResponsePersonDTO createPerson(Person newUser, ResponsePersonDTO data)  {

        PersonalData personalData = new PersonalData();
        JobsDetails aboutJob = new JobsDetails();

        Person response = new Person(data.getName(), data.getLast_name(), data.getEmail(), data.getIdentityPerson(), data.getPassword(), data.getProfileRole(), personalData, aboutJob);
        response.setIdentityPerson(generateId());
        response.setPassword(generateRandomPassword());

        Person createdUser = personRepository.save(response);

        createInformations(personalData, aboutJob, createdUser.getId(), createdUser.getIdentityPerson());

        BeanUtils.copyProperties(createdUser, data);

       return (data);
    }

    @Override
    public void createInformations(PersonalData personalData, JobsDetails jobsDetails, Long id, String identityPerson) {

        Optional<Person> principal = personRepository.findById(id);

        Long idAccount = principal.get().getId();
        String identity_account = principal.get().getIdentityPerson();

        if (personalData != null) {
            personalData.setId_account(idAccount);
            personalData.setIdentity(identity_account);
            Location location = personalData.getLocation();

            if (location != null) {
                location.setId_account(idAccount);
                location.setIdentity(identity_account);
            }
            personalDataRepository.save(personalData);
        }

        if(jobsDetails != null) {
            jobsDetails.setId_account(idAccount);
            jobsDetails.setIdentity(identity_account);
        }

        jobDetailsRepository.save(jobsDetails);

    }

    @Override
    public ResponsePersonDTO update(Person p, Long id, ResponsePersonDTO updatePersonDTO)  {
        Person account = findById(id);

        if(updatePersonDTO.getName() != null) {account.setName(updatePersonDTO.getName());}
        if(updatePersonDTO.getLast_name() != null) {account.setLast_name(updatePersonDTO.getLast_name());}
        if(updatePersonDTO.getEmail() != null) { account.setEmail(updatePersonDTO.getEmail());}
        if(updatePersonDTO.getEmail() != null) {account.setEmail(updatePersonDTO.getEmail());}
        if(updatePersonDTO.getStatus() != null) {account.setStatus(updatePersonDTO.getStatus());}
        if(updatePersonDTO.getStatus() != null) {account.setProfileRole(updatePersonDTO.getProfileRole());}

        Person updatedAccount = personRepository.save(account);

        BeanUtils.copyProperties(updatedAccount, updatePersonDTO);

        return (updatePersonDTO);
    }


    @Transactional
    private String generateId() {

        String characters = "0123456789";
        StringBuilder identity = new StringBuilder();
        int length = 6;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            identity.append(characters.charAt(index));
        }

        return identity.toString();
    }

    @Transactional
    protected String generateRandomPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$#";
        StringBuilder password = new StringBuilder();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }



}


