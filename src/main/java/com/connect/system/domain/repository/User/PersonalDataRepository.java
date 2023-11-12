package com.connect.system.domain.repository.User;

import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long> {
    @Query("SELECT NEW com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO(d.id_personalData, d.id_account, d.identity, d.date_of_birth, d.telephone, d.cpf_person, d.rg_person, d.profile_image, d.location) FROM PersonalData d WHERE d.id = :id_personalData")
    PersonalDataDTO findInformationsById(@Param("id_personalData") Long id_personalData);
}
