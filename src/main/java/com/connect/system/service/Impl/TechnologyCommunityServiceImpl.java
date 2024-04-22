package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.*;
import com.connect.system.domain.repository.System.*;
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

    // Criar comunidade > automaticamente criar grupo > campos do grupo da comunidade precisam herdar os campos da comunidade!

    //////////////////////////////////////////////////////////////////////////////////////

    //BUG: SE EXISTIR 2 HEADS ELE NAO ATUALIZA!
    // PROBLEMA REAL:  "trace": "org.springframework.dao.IncorrectResultSizeDataAccessException: query did not return a unique result: 2\r\n
    // validateFieldInHierarchyGroup


    //  "head_responsible": null | NAO ATUALIZA SE NAO PASSAR UM VALOR, FICA NULL!

    // Quando o HEAD for atualizado, ou seja MARIANA saiu e agora a HEAD e a JULIA, ou houver uma troca, enfim.
    // No Community Associates: o campo comunidade de MARIANA precisa voltar a ser NULL, resolver!

    //Quando coloco atualizo a HEAD o nome da comunidade nao vai para Person da nova Head

    //1 TESTE -> Atualizei 3x HEAD, 2 nao estava no grupo de hierarquia e nao tinha MANAGER abaixo delas na comunidade.
    //2 TESTE -> Coloquei uma MANAGER na comunidade, ou seja, abaixo de uma HEAD. E quando tentei atualizar novamente o HEAD
    // O ERRO ME RETORNOU:  "trace": "org.springframework.dao.IncorrectResultSizeDataAccessException: query did not return a unique result: 2\r\n\tat org.springframework.orm.jpa.EntityManagerFactoryUtils.
    //Ou seja, entendo que o erro esta quando o MANAGER esta abaixo de uma HEAD e tento atualizar essa HEAD da comunidade.

    //////////////////////////////////////////////////////////////////////
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
    GroupOfCommunityRepository groupOfCommunityRepository;

    @Autowired
    CommunityAssociatesRepository communityAssociatesRepository;

    //Cria a comunidade
    @Override
    public TechnologyCommunity createTechCommunity(TechnologyCommunity data, Person person, HierarchyGroup hierarchyGroup, Integer id_group_of_hierarchy, GroupOfCommunity groupOfCommunity) {

        validateAndAddHeadResponsible(data);

        TechnologyCommunity communityCreated = new TechnologyCommunity(
                data.getName_of_community(),
                data.getHead_responsible(),
                groupOfCommunity
        );

        groupOfCommunity.setTechnologyCommunity(communityCreated);

        TechnologyCommunity newCommunitySaved = technologyCommunityRepository.save(communityCreated);

        fieldGroupOfCommunity(newCommunitySaved, groupOfCommunity);

        return newCommunitySaved;
    }

    //Metodo responsavel por atribuir os valores da comunidade ao GROUP COMMUNITY (criado automaticamente ao criar uma comunidade)!
    @Transactional
    public void fieldGroupOfCommunity(TechnologyCommunity newCommunitySaved, GroupOfCommunity groupOfCommunity) {


        if(groupOfCommunity.getName_community() == null) {
            groupOfCommunity.setName_community(newCommunitySaved.getName_of_community());
        }

        if(groupOfCommunity.getHead_responsible() == null) {
            groupOfCommunity.setHead_responsible(newCommunitySaved.getHead_responsible());
        }

        groupOfCommunityRepository.save(groupOfCommunity);

    }

    //Valida se o HEADDELIVERY que esta sendo aplicado como HEAD DA COMUNIDADE existe!
    public void validateAndAddHeadResponsible(TechnologyCommunity data) {
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

               /* if(matchingUser.isPresent()) {
                    System.out.println("Usuario existe");
                    updatePersonFieldReport(user, data, idAccount, hierarchyGroup, id_group_of_hierarchy);
                }*/
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with HEADDELIVERY not found");
            }
        }
    }

    // Adiciona um associado a comunidade!
    @Override
    @Transactional
    public CommunityAssociates addCommunity(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity, Person person, Integer id, GroupOfCommunity groupOfCommunity) {
        Integer communityId = technologyCommunity.getId_community();

        TechnologyCommunity existingCommunity = technologyCommunityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada"));


        //fazer uma busca dentro do TechnologyCommunity, verificar se existe hierarchyGroupTechnology,
        // veficiar se existe communityAssociate, se existir, prosseguir e fazer
        // validacao pra verificar se existe person dentro dele, atualizar o nome da comunidade
        // do objeto person atraves disso.

        CommunityAssociates newCommunityAssociate = toCheckAssociate(communityAssociates,technologyCommunity, groupOfCommunity);
        GroupOfCommunity.addAssociateManager(newCommunityAssociate);


        //validateAndUpdateField(communityId, communityAssociates, technologyCommunity);

        technologyCommunityRepository.save(existingCommunity);

        return newCommunityAssociate;
    }

    @Transactional
    public CommunityAssociates toCheckAssociate(CommunityAssociates communityAssociates, TechnologyCommunity technologyCommunity, GroupOfCommunity groupOfCommunity) {

        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldNameAssociate = communityAssociates.getName_associate();

        if (fieldNameAssociate != null && !fieldNameAssociate.isEmpty()) {
            List<CommunityAssociates> matchingAssociate = existingUsers.stream()
                    .filter(user -> user.getOffice() == Office.MANAGER)
                    .map(user -> {
                        communityAssociates.setName_community(technologyCommunity.getName_of_community());
                        communityAssociates.setId_account(user.getId());
                        communityAssociates.setName_associate(user.getName() + " " + user.getLast_name());
                        communityAssociates.setIdentity(user.getIdentityPerson());
                        communityAssociates.setOffice(String.valueOf(user.getOffice()));
                        communityAssociates.setSeniority(String.valueOf(user.getSeniority()));

                        //PAREI AQUI
                        //VERIFICAR SE O GROUP EXISTE ANTES DE ADICIONAR!
                        // Verifica se o GroupOfCommunity já está persistido
                        if (groupOfCommunity.getId_group_of_community() == null) {

                            groupOfCommunityRepository.save(groupOfCommunity);
                        }

                        // Configurar o relacionamento bidirecional
                        communityAssociates.setGroup(groupOfCommunity);


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


    //Responsavel por vincular o NOME DA COMUNIDADE e o HEAD no campo REPORT ME do PERSON de office nivel MANAGER!
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


    // Responsavel por vincular o NOME DA COMUNIDADE do campo NAME COMMUNITY na classe CommunityAssociates
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


    @Override
    public TechnologyCommunity toUpdateCommunity(TechnologyCommunity technologyCommunity, Integer id_community, Person person, CommunityAssociates associates) {
        return null;
    }



}
