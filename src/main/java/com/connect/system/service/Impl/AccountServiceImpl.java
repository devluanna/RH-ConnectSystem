package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ProfileRole;
import com.connect.system.domain.model.Account.Jobs.SubPosition;
import com.connect.system.domain.model.Account.ResponseDTO.UpdatePersonDTO;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.model.Account.DashboardStudies.DashboardStudies;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.repository.System.MembersSquadRepository;
import com.connect.system.domain.repository.User.DashboardStudiesRepository;
import com.connect.system.domain.repository.User.JobDetailsRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.domain.repository.User.PersonalDataRepository;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.service.AccountService;
import com.connect.system.utils.MailConfig;
import jakarta.persistence.EntityNotFoundException;
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
    DashboardStudiesRepository dashboardStudiesRepository;

    @Autowired
    MembersSquadRepository membersSquadRepository;

    @Autowired
    JobDetailsRepository jobDetailsRepository;

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


    private void validateCreateRoleManager(PersonDTO data) {
        if (ProfileRole.valueOf(String.valueOf(data.getRole())) == ProfileRole.MANAGER) {
            data.setSub_position(SubPosition.valueOf(SubPosition.PROJECTMANAGER.name()));
        }
    }

    private void validateCreateRoleUser(PersonDTO data) {
        if (ProfileRole.valueOf(String.valueOf(data.getRole())) == ProfileRole.USER) {
            data.setSub_position(SubPosition.valueOf(SubPosition.NOTAPPLICABLE.name()));
        }
    }

    private void validateCreateRoleRh(PersonDTO data) {
        if (ProfileRole.valueOf(String.valueOf(data.getRole())) == ProfileRole.RH) {
            data.setSub_position(SubPosition.valueOf(SubPosition.ADMINISTRATIVEMANAGEMENT.name()));
        }
    }


    @Override
    @Transactional
    public PersonDTO createPerson(Person newUser, PersonDTO data, PersonalData personalDataUser, JobsDetails userJobInformation, DashboardStudies dashboardStudies)  {

        validateExistsEmail(data);
        validateCreateRoleManager(data);
        validateCreateRoleUser(data);
        validateCreateRoleRh(data);

        Person userCreated = new Person
                (data.getName(), data.getLast_name(), data.getEmail(), data.getIdentityPerson(),
                        data.getPassword(), data.getRole(), data.getType_of_record(), data.getOffice(), data.getOccupancy_area(),
                        data.getSeniority(), data.getSub_position(), data.getReport_me(), personalDataUser, userJobInformation, dashboardStudies);


        //String passwordUser = generateRandomPassword();
        String passwordUser = "12345678";
        String encryptedPassword = passwordEncoder.encode(passwordUser);
        userCreated.setPassword(encryptedPassword);

        userCreated.setIdentityPerson(generateId());

        Person savedUser = personRepository.save(userCreated);

        validationOfPersonalDataFields(personalDataUser, savedUser.getId());
        validationOfJobsDetailsFields(userJobInformation, savedUser.getId());
        validationOfDashboardStudiesFields(dashboardStudies, savedUser.getId());

        savingInformationId(data,personalDataUser, userJobInformation, dashboardStudies);

        BeanUtils.copyProperties(savedUser, data);
        //sendWelcomeEmail(data, savedUser, passwordUser);
        return (data);
    }


    @Override
    @Transactional
    public UpdatePersonDTO toUpdatePerson(Person person, Long id, UpdatePersonDTO updatePersonDTO) {
        Person accountUser = findById(id);

        modelMapper.map(updatePersonDTO, accountUser);
        modelMapper.map(accountUser, person);

        Person updatedUser = personRepository.save(person);

        processToGetMemberID(accountUser);

        return modelMapper.map(updatedUser, UpdatePersonDTO.class);
    }

    private void processToGetMemberID(Person accountUser) {
        JobsDetails jobsDetails = accountUser.getJobsDetails();

        if (jobsDetails != null) {
            Long idMemberOfSquad = jobsDetails.getId_memberOfSquad();

            if (idMemberOfSquad != null) {
                Members members = membersSquadRepository.findById(idMemberOfSquad)
                        .orElseThrow(() -> new IllegalArgumentException("Members not found: " + idMemberOfSquad));

                Long idAccount = members.getId_account();

                if (idAccount != null && idAccount.equals(accountUser.getId())) {

                    updateSubPositionInMembers(members, accountUser);

                    System.out.println("SubPosition updated successfully.");
                } else {
                    System.out.println("id_account in Members does not match the provided id.");
                }
            } else {
                updateUserInfo(accountUser);
            }
        } else {
            System.out.println("JobsDetails is null.");
        }
    }


    private void updateUserInfo(Person accountUser) {
       accountUser.setSub_position(SubPosition.valueOf(String.valueOf(accountUser.getSub_position())));
       personRepository.save(accountUser);
    }

    @Transactional
    private void updateSubPositionInMembers(Members members, Person accountUser) {
        if (members != null) {
            members.setSub_position(String.valueOf(accountUser.getSub_position()));
            membersSquadRepository.save(members);
        }
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
            userJobInformation.setOffice(office);
        }
    }

    private void validationOfDashboardStudiesFields(DashboardStudies dashboardStudies, Long id) {
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


