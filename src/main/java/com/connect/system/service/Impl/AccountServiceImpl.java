package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.UpdatePersonDTO;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.repository.User.DashboardStudiesRepository;
import com.connect.system.domain.repository.User.JobDetailsRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.domain.repository.User.PersonalDataRepository;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.service.AccountService;
import com.connect.system.utils.MailConfig;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    DashboardStudiesRepository dashboardStudiesRepository;

    @Autowired
    MailConfig emailService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

   @Override
   @Transactional
   public List<PersonDTO> getAllUsers() {
     return personRepository.findAllUsersWithPersonalDataIds();
  }

   @Override
   @Transactional
   public PersonDTO getUserById(Long id) {
       return personRepository.findUserById(id);
   }

    @Override
    public Person findById(Long id) {
        return personRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    @Override
    @Transactional
    public PersonDTO createPerson(Person newUser, PersonDTO data, PersonalData personalDataUser, JobsDetails userJobInformation, DashboardStudies dashboardStudies)  {

        validateExistsEmail(data);

        Person userCreated = new Person
                (data.getName(), data.getLast_name(), data.getEmail(), data.getIdentityPerson(),
                        data.getPassword(), data.getRole(), data.getType_of_record(), data.getOffice(), data.getOccupancy_area(),
                        data.getSeniority(), personalDataUser, userJobInformation, dashboardStudies);

        String passwordUser = generateRandomPassword();
        String encryptedPassword = passwordEncoder.encode(passwordUser);
        userCreated.setPassword(encryptedPassword);

        userCreated.setIdentityPerson(generateId());

        Person savedUser = personRepository.save(userCreated);

        validationOfPersonalDataFields(personalDataUser, savedUser.getId());
        validationOfJobsDetailsFields(userJobInformation, savedUser.getId());
        validationOfDasbboardStudiesFields(dashboardStudies, savedUser.getId());

        savingInformationId(data,personalDataUser, userJobInformation, dashboardStudies);

        BeanUtils.copyProperties(savedUser, data);
        //sendWelcomeEmail(data, savedUser, passwordUser);

        return (data);
    }

    @Override
    public UpdatePersonDTO update(Person p, Long id, UpdatePersonDTO updatePersonDTO)  {
        Person accountUser = findById(id);

        modelMapper.map(updatePersonDTO, accountUser);
        modelMapper.map(accountUser, p);

        Person updatedUser = personRepository.save(p);

        return modelMapper.map(updatedUser, UpdatePersonDTO.class);
    }

    private void validateExistsEmail(PersonDTO data) {
        Person existingEmail = personRepository.findByEmail(data.getEmail());

        if (existingEmail != null) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private void savingInformationId(PersonDTO data, PersonalData personalDataUser, JobsDetails userJobInformation, DashboardStudies dashboardStudies) {

        if(data.getPersonalDataId() == null) {
            data.setPersonalDataId(personalDataUser.getId_personalData());
        }

        if(data.getJobsDetailsId() == null) {
            data.setJobsDetailsId(userJobInformation.getId_job_details());
        }

        if(data.getDashboardStudiesId() == null) {
            data.setDashboardStudiesId(dashboardStudies.getId_dashboard());
        }

    }

    private void sendWelcomeEmail(PersonDTO data, Person savedUser, String passwordUser) {
        String subject = "WELCOME! Your credentials are ready!";
        String emailBody = "Hello! " + data.getName() + " " + data.getLast_name() + ",\n\nWelcome to the System!\n\n" +
                "\n\nRemember to reset your password and make your profile as updated and complete as possible.\n\n" +
                "Here is your registration information:\n\n" +
                "Login identity: " + savedUser.getIdentityPerson() + "\n" +
                "Password: " + passwordUser;

        emailService.sendEmail(data.getEmail(), subject, emailBody);
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
    private String generateRandomPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$#";
        StringBuilder password = new StringBuilder();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }

        String passwordUser = password.toString();

        System.out.println("SENHA DO USUARIO ->> " + passwordUser);
        return password.toString();
    }

    private void validationOfJobsDetailsFields(JobsDetails userJobInformation, Long id) {
        Optional<Person> accountUser = personRepository.findById(id);

        Long idAccount = accountUser.get().getId();
        String identity_account = accountUser.get().getIdentityPerson();
        String typeOfRecord = String.valueOf(accountUser.get().getType_of_record());
        String office = String.valueOf(accountUser.get().getOffice());
        String occupancyArea = String.valueOf(accountUser.get().getOccupancy_area());
        String seniorityAccount = String.valueOf(accountUser.get().getSeniority());

        if(userJobInformation != null) {
            userJobInformation.setId_account(idAccount);
            userJobInformation.setIdentity(identity_account);
            userJobInformation.setSeniority(seniorityAccount);
            userJobInformation.setType_of_record(typeOfRecord);
            userJobInformation.setOccupancy_area(occupancyArea);
            userJobInformation.setOffice(office);}

    }

    private void validationOfDasbboardStudiesFields(DashboardStudies dashboardStudies, Long id) {
        Optional<Person> account = personRepository.findById(id);

        Long idAccount = account.get().getId();
        String identity_account = account.get().getIdentityPerson();

        if (dashboardStudies != null) {
            dashboardStudies.setId_account(idAccount);
            dashboardStudies.setIdentity(identity_account);

            dashboardStudiesRepository.save(dashboardStudies);
        }

    }

    private void validationOfPersonalDataFields(PersonalData personalDataUser, Long id) {
        Optional<Person> account = personRepository.findById(id);

        Long idAccount = account.get().getId();
        String identity_account = account.get().getIdentityPerson();

        if (personalDataUser != null) {
            personalDataUser.setId_account(idAccount);
            personalDataUser.setIdentity(identity_account);

            personalDataRepository.save(personalDataUser);
        }
    }

}


