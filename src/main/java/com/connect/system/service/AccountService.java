package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.UpdatePersonDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;

import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.model.System.Squad.Members;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {

    PersonDTO createPerson(Person newUser, PersonDTO data, PersonalData personalDataUser, JobsDetails userJobInformation, DashboardStudies dashboardStudies) throws InvalidFormatException;
    UpdatePersonDTO toUpdatePerson(Person person, Long id, UpdatePersonDTO updatePersonDTO);
    List<PersonDTO> getAllUsers();
    Person findById(Long id);
    PersonDTO getUserById(Long id);

}
