package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.JobsDetails;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroupTechnology;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.CommunityAssociatesRepository;
import com.connect.system.domain.repository.System.HierarchyGroupTechnologyRepository;
import com.connect.system.domain.repository.System.TechnologyCommunityRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.TechnologyCommunityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TechnologyCommunityServiceImpl implements TechnologyCommunityService {

    //PAREI NO BUG DO CAMPO COMMUNITY DO PERSON QUE E HEADDELIVERY -- ** FAZENDO **

    // Adicionar o HEAD no Community Associates e ai depois incluir como head? -- nao fiz
    // Quando atualizar o head atualizar em tudo, nao e o que esta acontecendo  -- nao fiz

    //ATUALIZAR NOME DA COMUNIDADE, ATUALIZA PERSON TBM -- **FIZ**

    //O MANAGER_HEAD PRECISA ENTRAR DENTRO DO GRUPO TAMBEM -- nao fiz

    // DELETAR ASSOCIADO DE DENTRO DA COMUNIDADE -- nao fiz

    //fazer uma busca dentro do TechnologyCommunity, verificar se existe hierarchyGroupTechnology,
    // veficiar se existe communityAssociate, se existir, prosseguir e fazer
    // validacao pra verificar se existe person dentro dele, atualizar o nome da comunidade
    // do objeto person atraves disso.


    @Autowired
    TechnologyCommunityRepository technologyCommunityRepository;
    @Autowired
    HierarchyGroupTechnologyRepository hierarchyGroupRepository;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    CommunityAssociatesRepository communityAssociatesRepository;

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
        groupFieldValidator(hierarchyGroupTechnology, data);

        hierarchyGroupTechnology.setCommunity_id(newCommunitySaved.getId_community());
        System.out.println("ID DA COMUNIDADE" + hierarchyGroupTechnology.getCommunity_id());

        hierarchyGroupRepository.save(hierarchyGroupTechnology);


        return newCommunitySaved;
    }


   @Override
   @Transactional
    public TechnologyCommunity toUpdateCommunity(TechnologyCommunity technologyCommunity, HierarchyGroupTechnology hierarchyGroupTechnology, Integer id_community, Person person, CommunityAssociates associates) {

        TechnologyCommunity existingCommunity = technologyCommunityRepository.findById(id_community)
                .orElseThrow(() -> new EntityNotFoundException("TechnologyCommunity not found with id: " + id_community));

        String communityName = existingCommunity.getName_of_community();

        updateAndValidateFields(technologyCommunity, existingCommunity, hierarchyGroupTechnology);

        TechnologyCommunity updatedCommunity = technologyCommunityRepository.save(existingCommunity);

        HierarchyGroupTechnology existingHierarchyGroup = hierarchyGroupRepository.findByCommunityName(communityName);

        CommunityAssociates existingCommunityAssociates = communityAssociatesRepository.findByCommunityName(communityName);

       if (existingHierarchyGroup != null) {
            existingHierarchyGroup.setName_community(updatedCommunity.getName_of_community());
            hierarchyGroupRepository.save(existingHierarchyGroup);

           if(existingCommunityAssociates != null) {
               existingCommunityAssociates.setName_community(updatedCommunity.getName_of_community());
               communityAssociatesRepository.save(existingCommunityAssociates);
                updateFieldNameCommunityPerson(associates,technologyCommunity, updatedCommunity);

            }
        }
        //*updateAndValidateFieldsGroup(technologyCommunity, existingCommunity, hierarchyGroupTechnology, updatedCommunity);

        return updatedCommunity;
    }


   @Transactional
    public void updateFieldNameCommunityPerson(CommunityAssociates associates, TechnologyCommunity technologyCommunity, TechnologyCommunity updatedCommunity) {
        String updatedCommunityName = updatedCommunity.getName_of_community();
        if (updatedCommunityName == null) {
            throw new IllegalArgumentException("O nome da comunidade atualizada não pode ser nulo.");
        }

        List<CommunityAssociates> communityAssociatesList = communityAssociatesRepository.findByNameCommunity(updatedCommunityName);


        if (communityAssociatesList.isEmpty()) {
            System.out.println("Não há associados à comunidade: " + updatedCommunityName);
            return;
        }

        for (CommunityAssociates communityAssociates : communityAssociatesList) {
            updatePersonCommunity(communityAssociates, updatedCommunityName);
        }
    }


    private void updatePersonCommunity(CommunityAssociates communityAssociates, String updatedCommunityName) {
        Integer accountId = communityAssociates.getId_account();
        if (accountId != null) {
            Person existingPerson = personRepository.findById(accountId)
                    .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + accountId));

            existingPerson.setCommunity(updatedCommunityName);
            personRepository.save(existingPerson);
        }
    }

    public void updateHeadResponsible(TechnologyCommunity data) {
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

                updatePersonWithCommunity(user, data);
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with HEADDELIVERY not found");
            }
        }
    }

    @Override
    @Transactional
    public CommunityAssociates addCommunity(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity, Person person, Integer id) {
        Integer communityId = technologyCommunity.getId_community();

        TechnologyCommunity existingCommunity = technologyCommunityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));

        HierarchyGroupTechnology hierarchyGroupTechnology = existingCommunity.getHierarchyGroupTechnology();

        CommunityAssociates newCommunityAssociate = toCheckAssociate(communityAssociates,technologyCommunity, person);
        
        hierarchyGroupTechnology.addAssociates(newCommunityAssociate);
        validateAndUpdateField(communityId, communityAssociates, technologyCommunity);

        technologyCommunityRepository.save(existingCommunity);

        return newCommunityAssociate;
    }
    
    
    @Transactional
    public CommunityAssociates toCheckAssociate(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity, Person person) {

        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldNameAssociate = communityAssociates.getName_associate();

        if (fieldNameAssociate != null && !fieldNameAssociate.isEmpty()) {
            List<CommunityAssociates> matchingAssociate = existingUsers.stream()
                    .filter(user -> user.getOffice() == Office.MANAGER)
                    .map(user -> {
                        communityAssociates.setId_account(user.getId());
                        communityAssociates.setName_associate(user.getName() + " " + user.getLast_name());
                        communityAssociates.setIdentity(user.getIdentityPerson());
                        communityAssociates.setOffice(String.valueOf(user.getOffice()));
                        communityAssociates.setSeniority(String.valueOf(user.getSeniority()));

                        updatePersonWithCommunity(user, technologyCommunity);

                        return communityAssociates;

                    })
                    .collect(Collectors.toList());

            if (!matchingAssociate.isEmpty()) {
                communityAssociatesRepository.saveAll(matchingAssociate);
            } else {
                throw new IllegalArgumentException("User MANAGER not found");
            }
        }

        return communityAssociates;
    }

    private void updatePersonWithCommunity(PersonDTO user, TechnologyCommunity technologyCommunity) {
        Person existingPerson = personRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        if (existingPerson.getCommunity() == null || existingPerson.getCommunity().isEmpty()) {
            String communityName = technologyCommunity.getName_of_community();
            System.out.println("COMUNIDADE ENCONTRADA: " + communityName);

            if (communityName != null && !communityName.isEmpty()) {
                existingPerson.setCommunity(communityName);
                System.out.println("COMUNIDADE ADICIONADA: " + existingPerson.getCommunity());
                personRepository.save(existingPerson);
            }
        }
    }


    public void validateAndUpdateField(Integer communityId, CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity) {
        communityAssociates.setId_community(communityId);
        communityAssociates.setName_community(technologyCommunity.getName_of_community());

        communityAssociatesRepository.save(communityAssociates);
    }


    public void updateAndValidateFields(TechnologyCommunity updatedData, TechnologyCommunity existingCommunity, HierarchyGroupTechnology hierarchyGroupTechnology) {
        validateHeadResponsible(updatedData); //COMECAR POR AQUI

        if (updatedData.getName_of_community() != null) {
            existingCommunity.setName_of_community(updatedData.getName_of_community());
        }

        if (updatedData.getHead_responsible() != null) {
            existingCommunity.setHead_responsible(updatedData.getHead_responsible());
        }
    }


    public void groupFieldValidator(HierarchyGroupTechnology hierarchyGroupTechnology, TechnologyCommunity data) {
        /*validateFieldDirector(hierarchyGroupTechnology);
        validateFieldNameCeo(hierarchyGroupTechnology);
        validateFieldPresident(hierarchyGroupTechnology);*/
        refreshFieldTypeGroup(hierarchyGroupTechnology);
        refreshFieldNameCommunityGroup(data, hierarchyGroupTechnology);
    }
    public void refreshFieldManagerHead(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology) {
        hierarchyGroupTechnology.setManager_head(data.getHead_responsible());
        hierarchyGroupRepository.save(hierarchyGroupTechnology);
    }

    public void refreshFieldTypeGroup(HierarchyGroupTechnology hierarchyGroupTechnology) {
        hierarchyGroupTechnology.setType(String.valueOf(TypeOfRecord.TECHNOLOGY));
        hierarchyGroupRepository.save(hierarchyGroupTechnology);

    }

    public void refreshFieldNameCommunityGroup(TechnologyCommunity data, HierarchyGroupTechnology hierarchyGroupTechnology) {
        hierarchyGroupTechnology.setName_community(data.getName_of_community());
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

                updatePersonWithCommunity(user, data);
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with HEADDELIVERY not found");
            }
        }
    }

    public void validateFieldNameCeo(HierarchyGroupTechnology group) {
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

    public void validateFieldPresident(HierarchyGroupTechnology group) {
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

    public void validateFieldDirector(HierarchyGroupTechnology group) {
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

}
