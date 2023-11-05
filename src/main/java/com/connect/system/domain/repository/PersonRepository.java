package com.connect.system.domain.repository;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT NEW com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO(p.id, p.identityPerson,p.name, p.last_name, p.email, p.profileRole, p.password, p.status, p.personalData.id, p.personalData.location.id, p.jobsDetails.id) FROM Person p")
   List<ResponseGetDTO> findAllUsersWithPersonalDataIds();

    @Query("SELECT NEW com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.ResponseGetDTO(p.id, p.identityPerson,p.name, p.last_name, p.email, p.profileRole, p.password, p.status, p.personalData.id, p.personalData.location.id, p.jobsDetails.id) FROM Person p")
    ResponseGetDTO findUserById();

}
