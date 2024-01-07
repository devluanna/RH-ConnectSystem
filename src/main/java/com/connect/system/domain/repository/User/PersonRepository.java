package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonByOfficeHeadDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT NEW com.connect.system.domain.model.Account.ResponseDTO.PersonDTO(p.id, p.identityPerson,p.name, p.last_name, p.email, p.type_of_record, p.office, p.occupancy_area, p.seniority, p.community, p.sub_position, p.report_me, p.role, p.password, p.status, p.personalData.id, p.jobsDetails.id, p.dashboardStudies.id) FROM Person p")
    List<PersonDTO> findAllUsersWithPersonalDataIds();

    @Query("SELECT NEW com.connect.system.domain.model.Account.ResponseDTO.PersonDTO(p.id, p.identityPerson, p.name, p.last_name, p.email, p.type_of_record, p.office, p.occupancy_area, p.seniority, p.community, p.sub_position, p.report_me, p.role, p.password, p.status, p.personalData.id, p.jobsDetails.id, p.dashboardStudies.id) FROM Person p WHERE p.id = :id")
    PersonDTO findUserById(@Param("id") Integer id);

    @Query("SELECT p FROM Person p WHERE p.identityPerson = :identityPerson")
    Person findByIdentityPerson(@Param("identityPerson") String identityPerson);

    @Query("SELECT p FROM Person p WHERE p.email = :email")
    Person findByEmail(@Param("email") String email);

    @Query("SELECT NEW com.connect.system.domain.model.Account.ResponseDTO.PersonByOfficeHeadDTO(p.name, p.last_name, p.office) FROM Person p WHERE p.office = :office")
    List<PersonByOfficeHeadDTO> findUserByOffice(@Param("office") Office office);

}
