package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.CommunityAssociatesRepository;
import com.connect.system.domain.repository.System.HierarchyGroupRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.HierarchyGroupService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HierarchyGroupServiceImpl implements HierarchyGroupService {

    @Autowired
    HierarchyGroupRepository hierarchyGroupRepository;

    @Autowired
    CommunityAssociatesRepository communityAssociatesRepository;

    @Autowired
    PersonRepository personRepository;

    @Override
    public HierarchyGroup createGroupHierarchy(HierarchyGroup group ) {

        HierarchyGroup groupCreated = new HierarchyGroup(
                group.getName_of_group(),
                group.getType_of_group()
        );

        HierarchyGroup newGroupSaved = hierarchyGroupRepository.save(groupCreated);

        groupFieldValidator(groupCreated);

        return newGroupSaved;
    }

    @Override
    @Transactional
    public HierarchyGroup toUpdateGroup(HierarchyGroup group, Integer id_group_of_hierarchy) {

        HierarchyGroup existingGroup = hierarchyGroupRepository.findById(id_group_of_hierarchy)
                .orElseThrow(() -> new EntityNotFoundException("HierarchyGroup not found with id: " + id_group_of_hierarchy));

        HierarchyGroup updatedGroup = hierarchyGroupRepository.save(existingGroup);

       updateAndValidateFields(updatedGroup, group);

        return updatedGroup;
    }

    public void updateAndValidateFields(HierarchyGroup updatedGroup, HierarchyGroup group ) {
        if (group.getName_of_group() != null) {
            updatedGroup.setName_of_group(group.getName_of_group());
        }

    }

    @Override
    public CommunityAssociates addGroup(CommunityAssociates communityAssociates, HierarchyGroup groupHierarchy, Person person, Integer id) {
        Integer communityId = groupHierarchy.getId_group_of_hierarchy();

        HierarchyGroup existingGroup = hierarchyGroupRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));


        CommunityAssociates newCommunityAssociate = toCheckAssociateHeadDelivery(communityAssociates, groupHierarchy);

        groupHierarchy.addAssociates(newCommunityAssociate);

        hierarchyGroupRepository.save(existingGroup);

        return newCommunityAssociate;
    }

    @Transactional
    public CommunityAssociates toCheckAssociateHeadDelivery(CommunityAssociates communityAssociates, HierarchyGroup groupHierarchy) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users with office HEADDELIVERY: " + existingUsers);

        String fieldNameAssociate = communityAssociates.getName_associate();

        if (fieldNameAssociate != null && !fieldNameAssociate.isEmpty()) {
            List<CommunityAssociates> matchingAssociate = existingUsers.stream()
                    .filter(user -> user.getOffice() == Office.HEADDELIVERY)
                    .map(user -> {
                        communityAssociates.setId_account(user.getId());
                        communityAssociates.setName_associate(user.getName() + " " + user.getLast_name());
                        communityAssociates.setIdentity(user.getIdentityPerson());
                        communityAssociates.setOffice(String.valueOf(user.getOffice()));
                        communityAssociates.setSeniority(String.valueOf(user.getSeniority()));


                        if (user.getCommunity() != null && !user.getCommunity().isEmpty()) {
                            communityAssociates.setName_community(user.getCommunity());
                        }

                        return communityAssociates;

                    })
                    .collect(Collectors.toList());

            if (!matchingAssociate.isEmpty()) {
                communityAssociatesRepository.saveAll(matchingAssociate);
            } else {
                throw new IllegalArgumentException("User HEADDELIVERY not found");
            }
        }

        return communityAssociates;
    }


    public void groupFieldValidator(HierarchyGroup group) {
        if(group != null) {
        validateFieldDirector(group);
        //validateFieldDirectorHR(group);
        //validateFieldNameCeo(group);
        //validateFieldPresident(group);
        }
    }

    public void validateFieldDirector(HierarchyGroup group) {
        List<PersonDTO> existingUserDirector = personRepository.findAllUsersWithPersonalDataIds();

        Optional<PersonDTO> matchingUserDirector = existingUserDirector.stream()
                .filter(user -> user.getOffice() == Office.DIRECTOR)
                .findFirst();

        if(matchingUserDirector.isPresent()) {
            System.out.println("User DIRECTOR found: " + matchingUserDirector.get().getName());
            group.setDirector(matchingUserDirector.get().getName() + " " + matchingUserDirector.get().getLast_name());
            hierarchyGroupRepository.save(group);
        } else {
            System.out.println("User with DIRECTOR not found");
            throw new IllegalArgumentException("User with DIRECTOR not found");
        }
    }


    public void validateFieldDirectorHR(HierarchyGroup group) {
        List<PersonDTO> existingUserDirectorHR = personRepository.findAllUsersWithPersonalDataIds();

        Optional<PersonDTO> matchingUserDirectorHR = existingUserDirectorHR.stream()
                .filter(user -> user.getOffice() == Office.DIRECTORHR)
                .findFirst();

        if(matchingUserDirectorHR.isPresent()) {
            System.out.println("User DIRECTOR HR found: " + matchingUserDirectorHR.get().getName());
            group.setHr_director(matchingUserDirectorHR.get().getName() + " " + matchingUserDirectorHR.get().getLast_name());
            hierarchyGroupRepository.save(group);
        } else {
            System.out.println("User with DIRECTOR HR not found");
            throw new IllegalArgumentException("User with DIRECTOR HR not found");
        }
    }

    public void validateFieldNameCeo(HierarchyGroup group) {
        List<PersonDTO> existingUsersCEO = personRepository.findAllUsersWithPersonalDataIds();

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

    public void validateFieldPresident(HierarchyGroup group) {
        List<PersonDTO> existingUserPresident = personRepository.findAllUsersWithPersonalDataIds();

        Optional<PersonDTO> matchingUserPresident = existingUserPresident.stream()
                .filter(user -> user.getOffice() == Office.PRESIDENT)
                .findFirst();

        if(matchingUserPresident.isPresent()) {
            System.out.println("User PRESIDENT found: " + matchingUserPresident.get().getName());
            group.setPresident(matchingUserPresident.get().getName() + " " + matchingUserPresident.get().getLast_name());
            hierarchyGroupRepository.save(group);
        } else {
            System.out.println("User with PRESIDENT not found");
            throw new IllegalArgumentException("User with PRESIDENT not found");
        }
    }



}
