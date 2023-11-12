package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.Location;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.repository.User.LocationRepository;
import com.connect.system.domain.repository.User.PersonalDataRepository;
import com.connect.system.service.InformationsService;


import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
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

    public Location findLocationById(Long id_location) {
        return locationRepository.findById(id_location).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public PersonalData findByIdPersonalData(Long id_personalData) {
        return personalDataRepository.findById(id_personalData).orElseThrow(NoSuchElementException::new);
    }


    @Override
    @Transactional
    public PersonalDataDTO getById(Long id_personalData) {
        return personalDataRepository.findInformationsById(id_personalData);
    }

    @Override
    public Location updateLocation(Location location, Long id_location) {

        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       if (!authenticatedUser.getPersonalData().getLocation().getId_location().equals(id_location)) {
           throw new IllegalArgumentException("Access denied!");
        }

        location.setId_location(id_location);

        return locationRepository.save(location);

    }



    @Override
    public PersonalDataDTO updatePersonalData(PersonalData personalData, Long id_personalData, PersonalDataDTO personalDataDTO) {

        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getPersonalData().getId_personalData().equals(id_personalData)) {
           throw new IllegalArgumentException("Access denied!");
        }

        PersonalData userPersonalData = findByIdPersonalData(id_personalData);

        modelMapper.map(personalDataDTO, userPersonalData);
        modelMapper.map(userPersonalData, personalData);

        PersonalData updatedData = personalDataRepository.save(personalData);

        return modelMapper.map(updatedData, PersonalDataDTO.class);


    }


}
