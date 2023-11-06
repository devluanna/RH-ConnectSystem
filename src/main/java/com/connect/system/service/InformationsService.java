package com.connect.system.service;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.AccountInformation.Location;
import com.connect.system.domain.model.AccountInformation.PersonalData;
import org.springframework.stereotype.Service;

@Service
public interface InformationsService {

    PersonalData findByIdPersonalData(Long id_personalData);

    PersonalDataDTO updatePersonalData(PersonalData personalData, Long id_personalData, PersonalDataDTO personalDataDTO);

    PersonalDataDTO getById();

    LocationDTO updateLocation(Location location, Long id_location, LocationDTO locationDTO);

    Location findLocationById(Long id_location);
}
