package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.HierarchyGroupTechnologyRepository;
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
    HierarchyGroupTechnologyRepository hierarchyGroupRepository;
    @Autowired
    PersonRepository personRepository;

    @Override
    public TechnologyCommunity createTechCommunity(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology, Person person) {

        validateHeadResponsible(data);

        TechnologyCommunity communityCreated = new TechnologyCommunity(
                data.getName_of_community(),
                data.getHead_responsible(),
                hierarchyGroupTechnology
        );

        TechnologyCommunity newCommunitySaved = technologyCommunityRepository.save(communityCreated);

        refreshFieldManagerHead(data, hierarchyGroupTechnology);
        validateFieldNameCeo(hierarchyGroupTechnology);

        return newCommunitySaved;
    }

    public void refreshFieldManagerHead(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology) {
        hierarchyGroupTechnology.setManager_head(data.getHead_responsible());
        hierarchyGroupRepository.save(hierarchyGroupTechnology);
    }


    public void validateHeadResponsible(TechnologyCommunity data) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldHead = data.getHead_responsible();

        if (fieldHead != null && !fieldHead.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.HEADDELIVERY && user.getName().equalsIgnoreCase(data.getHead_responsible()))
                    .findFirst();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                data.setHead_responsible(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with HEADDELIVERY not found");
            }
        }
    }

    public void validateFieldNameCeo(HierarchyGroupTechnology group) {
        List<PersonDTO> existingUsersCEO = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsersCEO);

        Optional<PersonDTO> matchingUserCEO = existingUsersCEO.stream()
                .filter(user -> user.getOffice() == Office.CEO)
                .findFirst();

        if (matchingUserCEO.isPresent()) {
            System.out.println("User CEO found: " + matchingUserCEO.get().getName());
            group.setName_ceo(matchingUserCEO.get().getName() + " " + matchingUserCEO.get().getLast_name());
            hierarchyGroupRepository.save(group);
        } else {
            System.out.println("User with CEO not found");
            throw new IllegalArgumentException("User with CEO not found");
        }
    }

}
