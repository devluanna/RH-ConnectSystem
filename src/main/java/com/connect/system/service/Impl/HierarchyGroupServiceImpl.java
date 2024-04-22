package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.Status;
import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.ListAssociatesHierarchy;
import com.connect.system.domain.model.System.TechnologyCommunity.HierarchyGroup;
import com.connect.system.domain.repository.System.ListAssociatesHierarchyRepository;
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

// [OK!] TODOS CASOS DE USOS FORAM FEITOS E TESTADOS!!!!!!!!!!!

    // Atualizar os campos de DIRECTOR, PRESIDENT, CEO
    // Ao atualizar os campos de PRESIDENT E CEO, atualizar o report me do DIRECTOR e PRESIDENT *Achei um jeito legal para reutilizar os metodos
    //OPERATION TYPE.

    //BUG******* >>>  VALIDAR O REPORT ME QUANDO FOR ADICIONAR ELE, POIS DA FORMA QUE ESTA ESTA DANDO ERRO DE a unique result: 2\
    // quando atualizo o DIRECTOR, O NOVO DIRECTOR RECEBE VALOR NO REPORT ME DO DIRECTOR ANTIGO E NAO ESTA ATUALIZANDO O REPORT DO HEAD
    //Vai ter que validar por meio do COMMUNITY pra atualizar!
   // Atualizar o DIRECTOR e junto atualizar o report me do HEAD e DIRECTORHR -********** FIZ!

    //o que eu fiz*****
    //Cada cargo vai herdar no REPORT ME o de cima -********** FIZ! Funcional! Teste realizado para todos cargos
    //Regra de negocio alterado para: ter que selecionar o usuario para o cargo especifico.  -********** FIZ! Teste realizado para todos cargos

    //Alterar regra do negocio sobre heranca ::: >>>>>>>>> Verificar tambem se o status do usuario esta ATIVO ou INATIVO
    // Pois quando o DIRECTOR mudar, nos mudamos esse STATUS e AUTOMATICAMENTE entra o nome do diretor pra ambas verificacoes:
    //Tanto na hora da CRIACAO como na hora de ATUALIZAR!
    //Se o sistema encontrar um DIRECTOR INATIVO continuar iteirando sobre a lista ate achar um usuario ATIVO



    @Autowired
    HierarchyGroupRepository hierarchyGroupRepository;

    @Autowired
    ListAssociatesHierarchyRepository listAssociatesHierarchyRepository;

    @Autowired
    PersonRepository personRepository;

    public enum OperationType {
        CREATE,
        UPDATE
    }

    @Override
    public HierarchyGroup createGroupHierarchy(HierarchyGroup group, Person person) {

        fieldValidator(group, person);

        HierarchyGroup groupCreated = new HierarchyGroup(
                group.getName_of_group(),
                group.getType_of_group(),
                group.getDirector(),
                group.getHr_director(),
                group.getPresident(),
                group.getName_ceo()
        );

        HierarchyGroup newGroupSaved = hierarchyGroupRepository.save(groupCreated);

        validateAndUpdateFieldsReports(newGroupSaved, group, OperationType.CREATE);/// >>>>>>> DESCOMENTAR QUANDO FICAR PRONTO

        return newGroupSaved;
    }

    @Override
    @Transactional
    public HierarchyGroup toUpdateGroup(HierarchyGroup group, Integer id_group_of_hierarchy, ListAssociatesHierarchy listAssociatesHierarchy) {

        HierarchyGroup existingGroup = hierarchyGroupRepository.findById(id_group_of_hierarchy)
                .orElseThrow(() -> new EntityNotFoundException("HierarchyGroup not found with id: " + id_group_of_hierarchy));

        HierarchyGroup updatedGroup = hierarchyGroupRepository.save(existingGroup);

        updateNewFields(updatedGroup, group);

        updateReportOfHeadDeliveryUsers(updatedGroup, group);

        validateAndUpdateFieldsReports(group, updatedGroup, OperationType.UPDATE);

        return updatedGroup;
    }



    public void updateNewFields(HierarchyGroup updatedGroup, HierarchyGroup group ) {
        validateNewDirector(group, updatedGroup);
        validateNewDirectorHr(group, updatedGroup);

        if(group.getDirector() != null) {
            updatedGroup.setDirector(group.getDirector());
        }

        if(group.getHr_director() != null){
        updatedGroup.setHr_director(group.getHr_director());
        }

        if (group.getName_of_group() != null) {
            updatedGroup.setName_of_group(group.getName_of_group());
        }

    }

    @Transactional
    public void validateNewDirector(HierarchyGroup group, HierarchyGroup updatedGroup) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users for update: " + existingUsers);

        String fieldNewDirector = group.getDirector();

        if (fieldNewDirector != null && !fieldNewDirector.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.DIRECTOR && user.getName().equalsIgnoreCase(group.getDirector()))
                    .findAny();

            matchingUser.ifPresent(user -> {
                System.out.println("User New: " + user.getName());
                updatedGroup.setDirector(user.getName() + " " + user.getLast_name());


            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with new DIRECTOR not found");
            }
        }

    }

    @Transactional
    public void validateNewDirectorHr(HierarchyGroup group, HierarchyGroup updatedGroup) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users for update: " + existingUsers);

        String fieldNewDirectorHr = group.getHr_director();

        if (fieldNewDirectorHr != null && !fieldNewDirectorHr.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.DIRECTORHR && user.getName().equalsIgnoreCase(group.getHr_director()))
                    .findAny();

            matchingUser.ifPresent(user -> {
                System.out.println("User New: " + user.getName());
                updatedGroup.setHr_director(user.getName() + " " + user.getLast_name());


            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with new DIRECTORHR not found");
            }
        }

    }

    //Metodo responsavel por: Quando o DIRECTOR for atualizado, atualizar o REPORT ME tambem do HEAD.
    @Transactional
    private void updateReportOfHeadDeliveryUsers(HierarchyGroup updatedGroup, HierarchyGroup group) {

        List<ListAssociatesHierarchy> matchingAssociates = listAssociatesHierarchyRepository.findByGroupId(updatedGroup.getId_group_of_hierarchy());


        if (!matchingAssociates.isEmpty()) {
            String newDirector = updatedGroup.getDirector();

            matchingAssociates.forEach(associate -> {
                Person existingPerson = personRepository.findById(associate.getId_account())
                        .orElseThrow(() -> new EntityNotFoundException("Person not found"));

                existingPerson.setReport_me(newDirector);
                personRepository.save(existingPerson);
            });
        } else {
            System.out.println("O HierarchyGroup atualizado não tem associados da comunidade associados a ele.");
        }
    }


    @Override
    public ListAssociatesHierarchy addGroup(ListAssociatesHierarchy listAssociatesHierarchy, HierarchyGroup groupHierarchy, Person person, Integer id) {
        Integer groupId = groupHierarchy.getId_group_of_hierarchy();

        HierarchyGroup existingGroup = hierarchyGroupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));


        ListAssociatesHierarchy newCommunityAssociate = toCheckAssociateHeadDelivery(listAssociatesHierarchy, groupHierarchy, groupId, person);

        groupHierarchy.addAssociates(newCommunityAssociate);

        hierarchyGroupRepository.save(existingGroup);

        return newCommunityAssociate;
    }

    //Metodo responsavel por validar cada usuario antes de adicionar ao grupo
    @Transactional
    public ListAssociatesHierarchy toCheckAssociateHeadDelivery(ListAssociatesHierarchy listAssociatesHierarchy, HierarchyGroup groupHierarchy, Integer groupId, Person person) {

        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users with office HEADDELIVERY: " + existingUsers);

        String fieldNameAssociate = listAssociatesHierarchy.getName_associate();

        if (fieldNameAssociate != null && !fieldNameAssociate.isEmpty()) {
            List<ListAssociatesHierarchy> matchingAssociate = existingUsers.stream()
                    .filter(user -> user.getOffice() == Office.HEADDELIVERY)
                    .map(user -> {
                        listAssociatesHierarchy.setId_group(groupId);
                        listAssociatesHierarchy.setId_account(user.getId());
                        listAssociatesHierarchy.setName_associate(user.getName() + " " + user.getLast_name());
                        listAssociatesHierarchy.setIdentity(user.getIdentityPerson());
                        listAssociatesHierarchy.setOffice(String.valueOf(user.getOffice()));
                        listAssociatesHierarchy.setSeniority(String.valueOf(user.getSeniority()));

                        Integer idAccount = user.getId();

                        updateReportFieldOfHead(groupHierarchy, idAccount, person);

                        if (user.getCommunity() != null && !user.getCommunity().isEmpty()) {
                                listAssociatesHierarchy.setName_community(user.getCommunity());
                            }

                        listAssociatesHierarchy.setGroup(groupHierarchy);

                        return listAssociatesHierarchy;


                    })
                    .collect(Collectors.toList());

            if (!matchingAssociate.isEmpty()) {
                listAssociatesHierarchyRepository.saveAll(matchingAssociate);
            } else {
                throw new IllegalArgumentException("User HEADDELIVERY not found");
            }
        }

        return listAssociatesHierarchy;
    }


    // Esse metodo valida e ATUALIZA o campo Report Me da instancia PERSON de todos os usuarios com cargo HEADDELIVERY
    public void updateReportFieldOfHead(HierarchyGroup groupHierarchy, Integer idAccount, Person person) {
        Person existingPerson = personRepository.findById(idAccount)
                                .orElseThrow(() -> new EntityNotFoundException("Person not found"));
                        if(existingPerson != null) {
                            if (existingPerson.getReport_me() == null) {
                                existingPerson.setReport_me(groupHierarchy.getDirector());
                                personRepository.save(existingPerson);
                            }
                        }
    }

    // Esse metodo verifica e adiciona os USERS aos campos de GROUP!
    public void fieldValidator(HierarchyGroup group, Person person) {
        if(group != null) {
            //validateAndAddNameCeo(group);
            //validateAndAddPresident(group);
            validateAndAddDirector(group);
            validateAndAddDirectorHR(group);

        }
    }

    public void validateAndAddDirector(HierarchyGroup group) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldDirector = group.getDirector();

        if (fieldDirector != null && !fieldDirector.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.DIRECTOR && user.getName().equalsIgnoreCase(group.getDirector()))
                    .findAny();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                group.setDirector(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with DIRECTOR not found");
            }
        }
    }

    public void validateAndAddDirectorHR(HierarchyGroup group) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldDirectorHR = group.getHr_director();

        if (fieldDirectorHR != null && !fieldDirectorHR.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.DIRECTORHR && user.getName().equalsIgnoreCase(group.getHr_director()))
                    .findAny();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                group.setHr_director(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with DIRECTORHR not found");
            }
        }
    }

    public void validateAndAddNameCeo(HierarchyGroup group) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldCeo = group.getName_ceo();

        if (fieldCeo != null && !fieldCeo.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.CEO && user.getName().equalsIgnoreCase(group.getName_ceo()))
                    .findFirst();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                group.setName_ceo(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with CEO not found");
            }
        }
    }


    public void validateAndAddPresident(HierarchyGroup group) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users: " + existingUsers);

        String fieldPresident = group.getPresident();

        if (fieldPresident != null && !fieldPresident.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            user.getOffice() == Office.PRESIDENT && user.getName().equalsIgnoreCase(group.getPresident()))
                    .findFirst();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                group.setPresident(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with PRESIDENT not found");
            }
        }
    }


    // Metodos para verificar e ATUALIZAR o campo REPORT-ME da instancia Person de cada USER de Cargo DIRECTOR pra cima! [ASSIM FUNCIONOU!]
    @Transactional
    public void validateAndUpdateFieldsReports(HierarchyGroup newGroupSaved, HierarchyGroup updateGroup, OperationType operationType) {
        if(operationType == OperationType.CREATE) {
            AddFieldReportMeDirector(newGroupSaved);
            AddFieldReportMeDirectorHR(newGroupSaved);
        }

        if(operationType == OperationType.UPDATE) {
            UpdateFieldReportMeDirectorHR(updateGroup);
            UpdateFieldReportMeDirector(updateGroup);
        }

    }


    @Transactional
    public void AddFieldReportMeDirector(HierarchyGroup newGroupSaved) {
        String nameNewPresident = newGroupSaved.getPresident();

        Person director = personRepository.findByOffice(Office.DIRECTOR, Status.AVAILABLE);

        if (director != null) {
                director.setReport_me(nameNewPresident);
                personRepository.save(director);
                System.out.println("REPORT DIRECTOR ATUALIZADO COM SUCESSO!");
            } else {
                System.out.println("Unable to save the REPORT ME field in USER DIRECTOR HR");
            }

    }

    @Transactional
    public void AddFieldReportMeDirectorHR(HierarchyGroup newGroupSaved) {
        String nameNewDirector = newGroupSaved.getDirector();

        Person directorHr = personRepository.findByOffice(Office.DIRECTORHR, Status.AVAILABLE);

        if (directorHr != null) {
            directorHr.setReport_me(nameNewDirector);
            personRepository.save(directorHr);

            System.out.println("REPORT DO DIRECTORHR ADICIONADO COM SUCESSO!");
        } else {
            System.out.println("Unable to save the REPORT ME field in USER DIRECTOR HR");
        }
    }

    @Transactional
    public void UpdateFieldReportMeDirector(HierarchyGroup updateGroup) {
        String namePresident = updateGroup.getPresident();

        Person director = personRepository.findByOffice(Office.DIRECTOR, Status.AVAILABLE);

        if (director != null) {
            director.setReport_me(namePresident);
            personRepository.save(director);
            System.out.println("REPORT DO DIRECTOR ATUALIZADO COM SUCESSO!");
        } else {
            System.out.println("Unable to save the REPORT ME field in USER DIRECTOR HR");

        }
    }

    @Transactional
    public void UpdateFieldReportMeDirectorHR(HierarchyGroup updateGroup) {
        String nameDirector = updateGroup.getDirector();

        Person directorHr = personRepository.findByOffice(Office.DIRECTORHR, Status.AVAILABLE);

        if (directorHr != null) {
                    directorHr.setReport_me(nameDirector);
                    personRepository.save(directorHr);
                    System.out.println("REPORT DO DIRECTORHR ATUALIZADO COM SUCESSO!");
                } else {
                    System.out.println("Unable to save the REPORT ME field in USER DIRECTOR HR");

        }
    }



}
