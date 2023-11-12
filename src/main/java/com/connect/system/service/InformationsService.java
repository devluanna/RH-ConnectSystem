package com.connect.system.service;

import com.connect.system.domain.model.Account.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.Location;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import org.springframework.stereotype.Service;

@Service
public interface InformationsService {

    PersonalData findByIdPersonalData(Long id_personalData);

    PersonalDataDTO updatePersonalData(PersonalData personalData, Long id_personalData, PersonalDataDTO personalDataDTO);

    PersonalDataDTO getById(Long id_personalData);

    Location updateLocation(Location location, Long id_location);

}
