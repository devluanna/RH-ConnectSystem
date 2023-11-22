package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.PasswordDTO;
import com.connect.system.domain.model.Account.ResponseDTO.RecoveryPasswordDTO;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    Person toUpdatePassword(Person user, PasswordDTO passwordDTO, Long id);

    Person recoverPassword(Person user, RecoveryPasswordDTO passDTO, String email);
}
