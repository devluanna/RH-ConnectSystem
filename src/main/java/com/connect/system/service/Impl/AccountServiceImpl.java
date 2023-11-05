package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.UpdateProfileDTO;
import com.connect.system.domain.model.Account.User.Candidate;
import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Informations.Location;
import com.connect.system.domain.model.Informations.PersonalData;
import com.connect.system.domain.model.Account.Manager.Manager;
import com.connect.system.domain.model.Account.Rh.Rh;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.connect.system.domain.repository.JobDetailsRepository;
import com.connect.system.domain.repository.PersonRepository;
import com.connect.system.domain.repository.PersonalDataRepository;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import com.connect.system.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonalDataRepository personalDataRepository;

    @Autowired
    JobDetailsRepository jobDetailsRepository;

    private String identity;

    private Map<ProfileRole, Class<? extends Person>> roleToClassMap = new HashMap<>();
    public AccountServiceImpl() {
        roleToClassMap.put(ProfileRole.USER, Candidate.class);
        roleToClassMap.put(ProfileRole.MANAGER, Manager.class);
        roleToClassMap.put(ProfileRole.RH, Rh.class);
    }

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
    public Person update(Person p) {
      return personRepository.save(p);
    }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    @Override
    @Transactional
    public Person createPerson(Person newUser)  {

        if (newUser.getIdentityPerson() == null) {
            newUser.setIdentityPerson(generateId());
        }

        if(newUser.getPassword() == null) {
            newUser.setPassword(generateRandomPassword());
        }

        Class<? extends Person> userClass = roleToClassMap.get(newUser.getProfileRole());

        if (userClass != null) {
            System.out.println("CRIOU UM " + newUser.getProfileRole());
            return createUserInstance(userClass, newUser.getName(), newUser.getLast_name(), newUser.getEmail(), newUser.getProfileRole(), newUser.getPersonalData(), newUser.getIdentityPerson(), newUser.getPassword(), newUser.getJobsDetails());
        } else {
            return null;
        }

    }

    @SneakyThrows
    @Transactional
    private Person createUserInstance(Class<? extends Person> userClass, String name, String last_name, String email, ProfileRole profileRole, PersonalData personalData, String identityPerson, String password, JobsDetails jobsDetails) {
        Constructor<? extends Person> constructor = userClass.getConstructor(String.class, String.class, String.class, ProfileRole.class, PersonalData.class, String.class, String.class, JobsDetails.class);
        return constructor.newInstance(name, last_name, email, profileRole, personalData, identityPerson, password, jobsDetails);
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


