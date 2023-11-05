package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.UpdateProfileDTO;
import com.connect.system.domain.model.Informations.PersonalData;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    Person createPerson(Person newUser) throws InvalidFormatException;
    List<ResponseGetDTO> getAllUsers();
    Person findById(Long id);
    void createInformations(PersonalData personalData, JobsDetails jobsDetails, Long id, String identityPerson);
    ResponseGetDTO getUserById();
    Person update( Person p);



}
