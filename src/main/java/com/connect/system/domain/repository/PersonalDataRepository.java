package com.connect.system.domain.repository;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.AccountInformation.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {


    @Query("SELECT NEW com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.PersonalDataDTO(d.id_personalData, d.id_account, d.identity, d.date_of_birth, d.telephone, d.cpf_person, d.rg_person, d.profile_image, d.location) FROM PersonalData d")
    PersonalDataDTO findInformationsById();
}
