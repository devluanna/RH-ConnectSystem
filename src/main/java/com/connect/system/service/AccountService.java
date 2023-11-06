package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponsePersonDTO;
import com.connect.system.domain.model.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import com.connect.system.domain.model.Jobs.JobsDetails;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    ResponsePersonDTO createPerson(Person newUser,ResponsePersonDTO data) throws InvalidFormatException;

    void createInformations(PersonalData personalData, JobsDetails jobsDetails, Long id, String identityPerson);

    ResponsePersonDTO update( Person p, Long id, ResponsePersonDTO updatePersonDTO);

    List<ResponseGetDTO> getAllUsers();

    Person findById(Long id);

    ResponseGetDTO getUserById();

}
