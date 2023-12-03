package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.DashboardStudies.AcademicEducation;
import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.LocationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PersonalDataDTO;
import com.connect.system.domain.model.Account.AccountInformation.Location;
import com.connect.system.domain.model.Account.AccountInformation.PersonalData;
import com.connect.system.domain.repository.User.LocationRepository;
import com.connect.system.domain.repository.User.PersonalDataRepository;
import com.connect.system.service.InformationsService;


import com.connect.system.utils.Utils;
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

    public Location findLocationById(Integer id_location) {
        return locationRepository.findById(id_location).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public PersonalData findByIdPersonalData(Integer id_personalData) {
        return personalDataRepository.findById(id_personalData).orElseThrow(NoSuchElementException::new);
    }


    @Override
    @Transactional
    public PersonalDataDTO getById(Integer id_personalData) {
        return personalDataRepository.findInformationsById(id_personalData);
    }

    @Override
    public Location toUpdateLocation(Location location, Integer id_location) {

       Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       if (!authenticatedUser.getPersonalData().getLocation().getId_location().equals(id_location)) {
           throw new IllegalArgumentException("Access denied!");
        }

        Location locationUser = locationRepository.findById(id_location).orElse(null);

        if (locationUser != null) {
            Utils.copyNonNullProperties(location, locationUser);
        }

        return locationRepository.save(locationUser);
    }



    @Override
    public PersonalData toUpdatePersonalData(PersonalData personalData, Integer id_personalData) {

        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getPersonalData().getId_personalData().equals(id_personalData)) {
          throw new IllegalArgumentException("Access denied!");
        }

        PersonalData userPersonalData = findByIdPersonalData(id_personalData);

        if (userPersonalData != null) {
            Utils.copyNonNullProperties(personalData, userPersonalData);
        }

        return personalDataRepository.save(userPersonalData);

    }


}
