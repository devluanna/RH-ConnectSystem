package com.connect.system.service;

import com.connect.system.domain.model.Account.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.Location;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import org.springframework.stereotype.Service;

@Service
public interface InformationsService {

    PersonalData findByIdPersonalData(Integer id_personalData);

    PersonalData toUpdatePersonalData(PersonalData personalData, Integer id_personalData);

    PersonalDataDTO getById(Integer id_personalData);

    Location toUpdateLocation(Location location, Integer id_location);

}
