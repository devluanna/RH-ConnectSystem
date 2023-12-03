package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.TechnologyCommunityRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.TechnologyCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyCommunityServiceImpl implements TechnologyCommunityService {

    @Autowired
    TechnologyCommunityRepository technologyCommunityRepository;

    @Autowired
    PersonRepository personRepository;

    @Override
    public TechnologyCommunity createTechCommunity(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology, Person person) {
        validateHeadResponsible(data, person);

        TechnologyCommunity communityCreated = new TechnologyCommunity(
                data.getName_of_community(),
                data.getHead_responsible(),
                hierarchyGroupTechnology
        );

        TechnologyCommunity newCommunitySaved = technologyCommunityRepository.save(communityCreated);

        return newCommunitySaved;
    }
    public void validateHeadResponsible(TechnologyCommunity data, Person person) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("Lista de usuários: " + existingUsers);

        String fieldHead = data.getHead_responsible();

        if (fieldHead != null && !fieldHead.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.HEADDELIVERY && user.getName().equalsIgnoreCase(data.getHead_responsible()))
                    .findFirst();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                data.setHead_responsible(user.getName() + " " + user.getLast_name());
                System.out.println("Antes de setHead_responsible: " + data.getHead_responsible());
            });


            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("Usuário HEADDELIVERY não encontrado ou nome não corresponde");
            }
        }

        System.out.println("Depois de setHead_responsible: " + data.getHead_responsible());
    }

}
