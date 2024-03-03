package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.Jobs.TypeOfRecord;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.CommunityAssociates;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.CommunityAssociatesRepository;
import com.connect.system.domain.repository.System.HierarchyGroupRepository;
import com.connect.system.domain.repository.System.TechnologyCommunityRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.TechnologyCommunityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TechnologyCommunityServiceImpl implements TechnologyCommunityService {

    // PARA CADA CARGO, O REPORT ME DELE PRECISA SER O CARGO ACIMA -- FAZER
    // DELETAR ASSOCIADO DE DENTRO DA COMUNIDADE -- nao fiz

    ///////////////////////////////////////
    // QUANDO HEADDELIVERY ENTRA NA COMUNIDADE ATUALIZAR O CAMPO COMMUNITY DO ASSOCIATE.HIERARCHY, TANTO ANTES DE ENTRAR COMO DEPOIS -- **FIZ**
    // Quando atualizar o HEAD RESPONSIBLE atualizar em tudo, nao e o que esta acontecendo  -- **FIZ**
    // Quando um manager entrar na comunidade o REPORT-ME dele deve ser o HEAD! -- **FIZ**

    //ATUALIZAR NOME DA COMUNIDADE, ATUALIZA PERSON TBM -- **FIZ**
    //PAREI NO BUG DO CAMPO COMMUNITY DO PERSON QUE E HEADDELIVERY -- (QUANDO ATUALIZA NAME COMMUNITY NAO ATUALIZA NO PERSON)  **FIZ**

    //fazer uma busca dentro do TechnologyCommunity, verificar se existe hierarchyGroupTechnology,
    // veficiar se existe communityAssociate, se existir, prosseguir e fazer
    // validacao pra verificar se existe person dentro dele, atualizar o nome da comunidade
    // do objeto person atraves disso.


    @Autowired
    TechnologyCommunityRepository technologyCommunityRepository;

    @Autowired
    HierarchyGroupRepository hierarchyGroupRepository;
    @Autowired
    PersonRepository personRepository;

    @Autowired
    CommunityAssociatesRepository communityAssociatesRepository;

    @Override
    public TechnologyCommunity createTechCommunity(TechnologyCommunity data, Person person, HierarchyGroup hierarchyGroup) {

        validateAndAddHeadResponsible(data, hierarchyGroup);

        TechnologyCommunity communityCreated = new TechnologyCommunity(
                data.getName_of_community(),
                data.getHead_responsible()
        );

        TechnologyCommunity newCommunitySaved = technologyCommunityRepository.save(communityCreated);

        //refreshFieldManagerHead(data);
        //groupFieldValidator(data);

       /* hierarchyGroupTechnology.setCommunity_id(newCommunitySaved.getId_community());
        System.out.println("ID DA COMUNIDADE" + hierarchyGroupTechnology.getCommunity_id());*/

       // updatePersonFieldReport(person, newCommunitySaved, data);

        return newCommunitySaved;
    }


    public void validateAndAddHeadResponsible(TechnologyCommunity data, HierarchyGroup hierarchyGroup) {
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

                Integer idAccount = user.getId();

                updatePersonWithCommunity(user, data);
                validateFieldInHierarchyGroup(data, idAccount);
            });


            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with HEADDELIVERY not found");
            }
        }
    }

    public void validateFieldInHierarchyGroup(TechnologyCommunity data, Integer idAccount) {
        Optional<CommunityAssociates> existingAssociate = communityAssociatesRepository.findByAccountId(idAccount);

        if (existingAssociate.isPresent()) {
            CommunityAssociates associate = existingAssociate.get();
            associate.setName_community(data.getName_of_community());
            communityAssociatesRepository.save(associate);
        } else {
            System.out.println("ID NOT FOUND ");
        }
    }

    //VERIFICAR SE EXISTE A COMUNIDADE
    //SE EXISTIR, VERIFICAR SE EXISTE UM USUARIO COM OFFICE HEADDELIVERY
    // SE EXISTIR, COLOCAR O REPORT ME

    /*private void updatePersonFieldReport(Person user, TechnologyCommunity newCommunitySaved, TechnologyCommunity data, HierarchyGroup hierarchyGroup) {

        if (user != null && user.getId() != null) {
            Person existingPerson = personRepository.findById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Person not found"));

            if (existingPerson.getOffice() == Office.HEADDELIVERY) {
                // Verificar se o DIRECTOR está definido no HierarchyGroupTechnology
                if (hierarchyGroup.getDirector() != null && !hierarchyGroup.getDirector().isEmpty()) {
                    existingPerson.setReport_me(hierarchyGroup.getDirector());
                    System.out.println("O HEADDELIVERY REPORTA-SE Ao DIRECTOR: " + existingPerson.getReport_me());
                } else {
                    throw new IllegalArgumentException("DIRECTOR não está definido no HierarchyGroupTechnology");
                }
            }

            personRepository.save(existingPerson);
        } else {
            throw new IllegalArgumentException("ID do usuário é nulo");
        }
    }*/

    @Override
    @Transactional
    public TechnologyCommunity toUpdateCommunity(TechnologyCommunity technologyCommunity, Integer id_community, Person person, CommunityAssociates associates) {

        TechnologyCommunity existingCommunity = technologyCommunityRepository.findById(id_community)
                .orElseThrow(() -> new EntityNotFoundException("TechnologyCommunity not found with id: " + id_community));

        String communityName = existingCommunity.getName_of_community();

        TechnologyCommunity updatedCommunity = technologyCommunityRepository.save(existingCommunity);

        updateAndValidateFields(updatedCommunity, technologyCommunity);

       // HierarchyGroup existingHierarchyGroup = hierarchyGroupRepository.findByCommunityName(communityName);

        CommunityAssociates existingCommunityAssociates = communityAssociatesRepository.findByCommunityName(communityName);

           if(existingCommunityAssociates != null) {
               existingCommunityAssociates.setName_community(updatedCommunity.getName_of_community());
               communityAssociatesRepository.save(existingCommunityAssociates);
                updateFieldNameCommunityPerson(associates, technologyCommunity, updatedCommunity);

            }

          // refreshFieldNewManagerHead(technologyCommunity, hierarchyGroupTechnology);
          // updateAndValidateFieldsGroup(technologyCommunity, existingCommunity, hierarchyGroupTechnology, updatedCommunity);

        return updatedCommunity;
    }


   public void updateAndValidateFields(TechnologyCommunity technologyCommunity, TechnologyCommunity updatedCommunity ) {
       validateNewHeadResponsible(updatedCommunity, updatedCommunity);

        if (technologyCommunity.getName_of_community() != null) {
            technologyCommunity.setName_of_community(updatedCommunity.getName_of_community());
        }

        if (technologyCommunity.getHead_responsible() != null) {
            technologyCommunity.setHead_responsible(updatedCommunity.getHead_responsible());
        }
    }
    @Transactional
    public void validateNewHeadResponsible(TechnologyCommunity data, TechnologyCommunity updatedCommunity) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users for update: " + existingUsers);

        String fieldHead = data.getHead_responsible();

        if (fieldHead != null && !fieldHead.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.HEADDELIVERY && user.getName().equalsIgnoreCase(data.getHead_responsible()))
                    .findFirst();

                    matchingUser.ifPresent(user -> {
                    System.out.println("User New: " + user.getName());
                    data.setHead_responsible(user.getName() + " " + user.getLast_name());

                    Integer idAccount = user.getId();

                Person existingPerson = personRepository.findById(user.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Person not found"));

                validateFieldInHierarchyGroup(data, idAccount);

                if (existingPerson.getCommunity() != null && !existingPerson.getCommunity().isEmpty()) {
                    existingPerson.setCommunity(updatedCommunity.getName_of_community());
                    System.out.println("NOME DA COMUNIDADE DEPOIS DE ATUALIZAR " + existingPerson.getCommunity());

                        personRepository.save(existingPerson);
                }

            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with new HEADDELIVERY not found");
            }
        }
        
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



    @Override
    @Transactional
    public CommunityAssociates addCommunity(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity, Person person, Integer id, HierarchyGroup hierarchyGroup) {
        Integer communityId = technologyCommunity.getId_community();

        TechnologyCommunity existingCommunity = technologyCommunityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));

        //HierarchyGroupTechnology hierarchyGroupTechnology = existingCommunity.getHierarchyGroupTechnology();

        CommunityAssociates newCommunityAssociate = toCheckAssociate(communityAssociates,technologyCommunity);
        
       // hierarchyGroupTechnology.addAssociates(newCommunityAssociate);
        validateAndUpdateField(communityId, communityAssociates, technologyCommunity);

        technologyCommunityRepository.save(existingCommunity);

        return newCommunityAssociate;
    }

    //Criar um metodo aqui para que quando o USUARIO for ADICIONADO DEPOIS DE ESTAR EM UM GRUPO DE HIERARQUIA
    //Preencher o campo COMMUNITY da classe ASSOCIATE.HIERARCHYGROUP.COMMUNITY


    
    @Transactional
    public CommunityAssociates toCheckAssociate(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity) {

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

            if (communityName != null && !communityName.isEmpty()) {
                existingPerson.setCommunity(communityName);

                if(existingPerson.getOffice() == Office.MANAGER) {
                    existingPerson.setReport_me(technologyCommunity.getHead_responsible());
                }

            }
            personRepository.save(existingPerson);
        }

    }


    public void validateAndUpdateField(Integer communityId, CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity) {
       // communityAssociates.setId_community(communityId);
        communityAssociates.setName_community(technologyCommunity.getName_of_community());

        communityAssociatesRepository.save(communityAssociates);
    }



}
