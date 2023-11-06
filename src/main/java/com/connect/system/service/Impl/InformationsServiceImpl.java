package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.EntityPerson.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.AccountInformation.Location;
import com.connect.system.domain.model.AccountInformation.PersonalData;
import com.connect.system.domain.repository.LocationRepository;
import com.connect.system.domain.repository.PersonalDataRepository;
import com.connect.system.service.InformationsService;


import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
public class InformationsServiceImpl implements InformationsService {

    @Autowired
    PersonalDataRepository personalDataRepository;

    @Autowired
    LocationRepository locationRepository;

    private final ModelMapper modelMapper;

    public InformationsServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PersonalData findByIdPersonalData(Long id_personalData) {
        return personalDataRepository.findById(id_personalData).orElseThrow(NoSuchElementException::new);
    }
    @Override
    public Location findLocationById(Long id_location) {
        return locationRepository.findById(id_location).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public PersonalDataDTO getById() {
        return personalDataRepository.findInformationsById();
    }

    @Override
    public LocationDTO updateLocation(Location location, Long id_location, LocationDTO locationDTO) {

        Location locationUser = findLocationById(location.getId_location());

        modelMapper.map(locationDTO, locationUser);

        modelMapper.map(locationUser, location);

        Location updatedLocation = locationRepository.save(location);

        LocationDTO updatedLocationDTO = modelMapper.map(updatedLocation, LocationDTO.class);

        return updatedLocationDTO;
    }


    @Override
    public PersonalDataDTO updatePersonalData(PersonalData personalData, Long id_personalData, PersonalDataDTO personalDataDTO) {
        PersonalData userPersonalData = findByIdPersonalData(id_personalData);

      if (personalDataDTO.getDate_of_birth() != null) {
          userPersonalData.setDate_of_birth(personalDataDTO.getDate_of_birth());}

      if (personalDataDTO.getTelephone() != null) {
          userPersonalData.setTelephone(personalDataDTO.getTelephone());}

      if(personalDataDTO.getCpf_person() != null) {
          userPersonalData.setCpf_person(personalDataDTO.getCpf_person());}


      if(personalDataDTO.getRg_person() != null) {
          userPersonalData.setRg_person(personalDataDTO.getRg_person());}


       PersonalData updateData = personalDataRepository.save(userPersonalData);

       BeanUtils.copyProperties(updateData, personalDataDTO);

       return personalDataDTO;

    }


}
