package com.connect.system.service.Impl;


import com.connect.system.domain.model.Account.ResponseDTO.PersonDTO;
import com.connect.system.domain.model.System.TechnologyCommunity.TechnologyCommunity;
import com.connect.system.domain.repository.System.TechnologyCommunityRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.EntityPerson.Status;

import com.connect.system.domain.model.Account.Jobs.JobsDetails;

import com.connect.system.domain.model.Account.Jobs.Office;
import com.connect.system.domain.model.Account.Jobs.SubPosition;

import com.connect.system.domain.model.System.Squad.MemberDashboardSquad;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.Squad.Squad;
import com.connect.system.domain.repository.System.DashboardMembersSquadRepository;
import com.connect.system.domain.repository.System.MembersSquadRepository;
import com.connect.system.domain.repository.System.SquadRepository;
import com.connect.system.domain.repository.User.JobDetailsRepository;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.SquadService;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SquadServiceImpl implements SquadService {

    // *** VALIDACOES ***
    // O PROJECT MANAGER, PO E TEACH LEAD PRECISAM ESTAR DENTRO DA SQUAD
    // CASOS DE USO
    // SQUAD SENDO CRIADA > METODO RESPONSAVEL POR ADICIONAR O PROJECT MANAGER COMO CRIADOR DA SQUAD > ELE ESTA SENDO ADICIONADO
    // AUTOMATICAMENTE NELA > TODAS INFORMACOES ESTAO SENDO APLICADAS CORRETAMENTE NO OBJETO PERSON E ARRAY MEMBERS
    // EDITAR SQUAD E CAMPO PROJECTMANAGER
    // ADICIONAR TEACHLEAD E EDITAR TL
    // REMOVER MEMBROS (FAZENDO)


    // FAZENDO ****** 15/05
    // QUANDO REMOVER ELE O QUE VAI ACONTECER?
    // CASOS DE USO > CAMPO MANAGER DA SQUAD PRECISA FICAR NULL (JA ESTA FICANDO)
    // REPORT-ME DOS MEMBROS ABAIXO DA SQUAD, PRECISA FICAR NULL E ALTERAR APOS ADICIONAR UM NOVO GESTOR
    // QUANDO EDITAR UM NOVO PROJECT MANAGER, PRECISA: ADICIONAR O NOVO E REMOVER O ANTIGO? OU PRIMEIRO REMOVE E DEPOIS ADICIONA UM NOVO?
    // FAZER UMA LOGICA ONDE OS DOIS CASOS FUNCIONARIA, EDITAR E ADICIONAR UM POR CIMA SUBSTITUI REPORTME DOS ABAIXO NAQUELE DASHBOARD



    // ******** BUGS RESOLVIDOS ********//
    // VALIDACAO PRA QUE ANTES DE CRIAR, ELE PRECISA ESTAR DENTRO DE UMA COMUNIDADE.
    // SO VAI SEGUIR COM A CRIACAO DA SQUAD, SE O GESTOR QUE ESTA CRIANDO, ESTIVER DENTRO DE UMA COMUNIDADE!
    // APOS ESTAR DENTRO DA COMUNIDADE, A SQUAD CRIA MAS COM O ID DO NUMERO DE TENTATIVAS Q FOI FEITO (resolvidoooooo)
    // PRECISA REFLETIR NO JOBSDETAILS DO MANAGER O ID E NOME DA SQUAD
    // ID_MEMBER_OF_SQUAD DOS MEMBROS ESTAO DE ACORDO COM ID MEMBER DO DASHBOARD DE MEMBEROS DA SQUAD
    // QUANDO REMOVE TANTO GESTOR COMO MEMBRO, OS CAMPOS FICAM NULLS DE NOVO

    @Autowired
    SquadRepository squadRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    DashboardMembersSquadRepository dashboardMembersSquadRepository;
    @Autowired
    MembersSquadRepository membersSquadRepository;
    @Autowired
    JobDetailsRepository jobDetailsRepository;
    @Autowired
    TechnologyCommunityRepository technologyCommunityRepository;


    @Override
    public List<Squad> getAllSquads() {
        return squadRepository.findAll();
    }

    @Transactional
    public Squad findSquadById(Integer id_squad) {
        return squadRepository.findById(id_squad).orElse(null);
    }

    @Transactional
    public Squad findSquadWithMembers(Integer id_squad) {
        return squadRepository.findSquadWithMembers(id_squad);
    }

    @Override
    public Squad getSquadsWithMembersById(Integer id_squad) {
        Squad squad = findSquadById(id_squad);

        if (squad != null) {
            List<Members> allMembers = membersSquadRepository.findMembersBySquadId(id_squad);
            squad.getAllMembers().setMembers(allMembers);
        }

        return squad;
    }

    //aq
    @Override
    public Squad toUpdateSquad(Integer id_squad, Squad squads, Integer id) {
        return null;
    }




    //Cria uma nova squad
    @Override
    @Transactional
    public Squad createSquad(Integer id_squad, Squad squad, MemberDashboardSquad memberDashboardSquad) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            throw new AccessDeniedException("Apenas usuários com a ROLE MANAGER podem criar squads");
        }

        Person authenticatedUser = (Person) authentication.getPrincipal();

        squadValidatorField(squad);

        Squad squadNewCreated = new Squad(
                squad.getName_squad(), squad.getTeach_lead(), squad.getProject_manager(), squad.getType(), squad.getArea_squad(), squad.getCustomer_project(),
                squad.getCreatedAt(), memberDashboardSquad, squad.getCommunity_name());

        memberDashboardSquad.setSquad(squadNewCreated);

        validateAndAddProjectManager(squadNewCreated);

        if(squadNewCreated.getProject_manager() != null){
            Squad newSquadSaved = squadRepository.save(squadNewCreated);

            Optional<Person> projectManagerOptional = personRepository.findByFullName(newSquadSaved.getProject_manager());

            List<Members> membersOfSquad = newSquadSaved.getAllMembers().getMembers();

            addProjectManagerToSquad(projectManagerOptional, newSquadSaved, memberDashboardSquad, membersOfSquad);

            return newSquadSaved;
        } else {
            throw new IllegalStateException("Não foi possível criar a squad devido a um erro de validação do PROJECT MANAGER");
        }

    }

    // Metodo responsavel por validar campos
    public void squadValidatorField(Squad squad) {
        if (squad != null) {
            validateNameCommunity(squad);
        }
    }

    // Verifica se existe a comunidade
    // Adiciona a comunidade
    // Valida o campo COMMUNITY
    private void validateNameCommunity(Squad squadNew) {
        String communityName = squadNew.getCommunity_name();

        if (communityName != null && !communityName.isEmpty()) {
            List<TechnologyCommunity> existingCommunity = technologyCommunityRepository.findByNameCommunity(communityName);

            if (!existingCommunity.isEmpty()) {
                TechnologyCommunity matchingCommunity = existingCommunity.get(0);
                System.out.println("Community: " + matchingCommunity.getName_of_community());
                squadNew.setCommunity_name(matchingCommunity.getName_of_community());
            } else {
                throw new IllegalArgumentException("Community not found");
            }
        } else {
            throw new IllegalArgumentException("Community name cannot be null or empty");
        }
    }

    //  Metodo responsavel por validar se o PROJECT MANAGER existe no sistema e se existir, adicionar na squad automaticamente!
    // UMA IDEIA DE LISTAR APENAS USUARIOS COM A ROLE OU SUBPOSITION XXXXX pra nao pesar tanto
    public boolean validateAndAddProjectManager(Squad squadNewCreated) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();

        String fieldProjectManager = squadNewCreated.getProject_manager();

        if (fieldProjectManager != null && !fieldProjectManager.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            (user.getSub_position() == SubPosition.PROJECTMANAGER || user.getOffice() == Office.MANAGER) &&
                                    (user.getName() + " " + user.getLast_name()).equalsIgnoreCase(squadNewCreated.getProject_manager()))
                    .findAny();

            if (matchingUser.isPresent()) {
                PersonDTO user = matchingUser.get();
                if (user.getCommunity() != null) {
                    squadNewCreated.setProject_manager(user.getName() + " " + user.getLast_name());
                } else {
                    throw new IllegalArgumentException("O PROJECT MANAGER precisa estar em uma comunidade");
                }
            } else {
                throw new IllegalArgumentException("User with MANAGER PROJECT not found");
            }
        }

        return false;
    }

    // Método responsável por adicionar o Project Manager à squad como membro
    private void addProjectManagerToSquad(Optional<Person> projectManagerOptional, Squad newSquadSaved, MemberDashboardSquad allMembers, List<Members> members) {
        if (projectManagerOptional.isPresent()) {
            Person person = projectManagerOptional.get();

            JobsDetails jobsDetails = person.getJobsDetails();

        Members newMemberProjectManager = new Members();
        newMemberProjectManager.setId_squad(newSquadSaved.getId_squad());
        newMemberProjectManager.setId_dashboardMembers(allMembers.getId_dashboardMembers());
        newMemberProjectManager.setName_squad(newSquadSaved.getName_squad());
        newMemberProjectManager.setId_account(person.getId());
        newMemberProjectManager.setIdentity(person.getIdentityPerson());
        newMemberProjectManager.setName(person.getName());
        newMemberProjectManager.setLast_name(person.getLast_name());
        newMemberProjectManager.setOffice(String.valueOf(person.getOffice()));
        newMemberProjectManager.setSeniority(String.valueOf(person.getSeniority()));
        newMemberProjectManager.setSub_position(String.valueOf(person.getSub_position()));
        newMemberProjectManager.setReport_me(person.getReport_me());

        allMembers.addMembers(newMemberProjectManager);
        //dashboardMembersSquadRepository.save(allMembers);

        Members memberSaved = membersSquadRepository.save(newMemberProjectManager);

        validateFieldNameSquadUser(jobsDetails, newMemberProjectManager, memberSaved);

        } else {
            throw new IllegalArgumentException("Project Manager not found");
        }

    }


    // Atualiza os campos de JOBS DETAILS do membro adicionado
    private void validateFieldNameSquadUser(JobsDetails jobsDetails, Members newMember, Members memberSaved) {
        if(jobsDetails != null) {
            if (jobsDetails.getName_squad() == null) {
                jobsDetails.setName_squad(newMember.getName_squad());
            }

            if (jobsDetails.getId_squad() == null) {
                jobsDetails.setId_squad(newMember.getId_squad());
            }

            if (jobsDetails.getId_memberOfSquad() == null) {
                jobsDetails.setId_memberOfSquad(memberSaved.getId_member());
            }

            jobDetailsRepository.save(jobsDetails);
        }
    }



    //  Metodo responsavel por validar se o TEACHLEAD existe no sistema
    // UMA IDEIA DE LISTAR APENAS USUARIOS COM A ROLE OU SUBPOSITION XXXXX pra nao pesar tanto
    public void validateAndAddTeachLead(Squad squad) {
        List<PersonDTO> existingUsers = personRepository.findAllUsersWithPersonalDataIds();
        System.out.println("List of users in System: " + existingUsers);

        String fieldTeachLead = squad.getProject_manager();

        if (fieldTeachLead != null && !fieldTeachLead.isEmpty()) {
            Optional<PersonDTO> matchingUser = existingUsers.stream()
                    .filter(user ->
                            (user.getSub_position() == SubPosition.TEACHLEAD) &&
                                    (user.getName() + " " + user.getLast_name()).equalsIgnoreCase(squad.getTeach_lead()))
                    .findAny();

            matchingUser.ifPresent(user -> {
                System.out.println("User: " + user.getName());
                squad.setTeach_lead(user.getName() + " " + user.getLast_name());
            });

            if (!matchingUser.isPresent()) {
                throw new IllegalArgumentException("User with TEACHLEAD not found");
            }
        }
    }

    // Adiciona membro na squad
    @Override
    @Transactional
    public Members addMemberToSquad(Integer squad_id, Integer id, Integer id_dashboardMembers, Members members) {
        Optional<Squad> squadOptional = squadRepository.findById(squad_id);
        Optional<Person> personOptional = personRepository.findById(id);
        Optional<MemberDashboardSquad> allMembersOptional = dashboardMembersSquadRepository.findById(id_dashboardMembers);

        if (squadOptional.isPresent() && personOptional.isPresent() && allMembersOptional.isPresent()) {
            Squad squad = squadOptional.get();
            Person person = personOptional.get();
            MemberDashboardSquad allMembers = allMembersOptional.get();

            if (isMemberAlreadyInSquad(allMembers, id)) {
                throw new IllegalArgumentException("Membro já adicionado a squad.");
            }

            changeSubPositionJobs(person);

            Members newMember = createNewMember(squad, allMembers, person);
            saveNewMember(newMember, squad, person);

            return newMember;
        } else {
            throw new IllegalArgumentException("Squad, Person, ou MemberDashboardSquad não encontrado.");
        }

    }

    // Quando o membro e adicionado, alguns campos no DASHBOARD sao atualizados de acordo com PERSON.
    private Members createNewMember(Squad squad, MemberDashboardSquad allMembers, Person person ) {
        JobsDetails jobsDetails = person.getJobsDetails();

        Members newMember = new Members();
        newMember.setId_squad(squad.getId_squad());
        newMember.setId_dashboardMembers(allMembers.getId_dashboardMembers());
        newMember.setName_squad(squad.getName_squad());
        newMember.setId_account(person.getId());
        newMember.setIdentity(person.getIdentityPerson());
        newMember.setName(person.getName());
        newMember.setLast_name(person.getLast_name());
        newMember.setOffice(String.valueOf(person.getOffice()));
        newMember.setSeniority(String.valueOf(person.getSeniority()));
        newMember.setSub_position(String.valueOf(person.getSub_position()));
        newMember.setReport_me(squad.getProject_manager());

        changeStatusAndReportMe(person, squad);

        allMembers.addMembers(newMember);
        Members memberSaved = membersSquadRepository.save(newMember);

        validateFieldNameSquadUser(jobsDetails, newMember, memberSaved);

        squad.setAllMembers(allMembers);

        return newMember;
    }

    // Salva os campos de JOBS DETAILS
    private void saveNewMember(Members newMember, Squad squad, Person person) {
        JobsDetails jobsDetails = person.getJobsDetails();
        //validateFieldNameSquadUser(jobsDetails, newMember, memberSaved);
        jobDetailsRepository.save(jobsDetails);
        squadRepository.save(squad);

    }


    // Verifica se o usuario ja esta na squad
    private boolean isMemberAlreadyInSquad(MemberDashboardSquad allMembers, Integer member_id) {
        return allMembers.getMembers().stream()
                .anyMatch(member -> member_id.equals(member.getId_account()));
    }

    // Atualiza a SUBPOSITION do membro
    private void changeSubPositionJobs(Person person) {
        if (person.getSub_position().equals(SubPosition.valueOf("NOTAPPLICABLE"))) {
            person.setSub_position(SubPosition.valueOf(String.valueOf(SubPosition.valueOf(String.valueOf(SubPosition.valueOf("MEMBER"))))));
            personRepository.save(person);
        }
    }

    // Atualiza o STATUS e REPORT ME do membro
    private void changeStatusAndReportMe(Person person, Squad squad) {
        person.setReport_me(squad.getProject_manager());
        person.setStatus(Status.valueOf("ALLOCATED"));
        personRepository.save(person);
    }


    //REMOVE UM MEMBRO DA SQUAD
    @Override
    public void removeMemberFromSquad(Integer id_squad, Integer id_member) {
        try {
            Squad squad = squadRepository.findById(id_squad).orElseThrow(() -> new IllegalArgumentException("Squad not found"));
            Members memberToRemove = membersSquadRepository.findById(id_member).orElseThrow(() -> new IllegalArgumentException("Member not found"));

            MemberDashboardSquad memberDashboardSquad = squad.getAllMembers();
            memberDashboardSquad.getMembers().remove(memberToRemove);

            Person person = personRepository.findById(memberToRemove.getId_account()).orElse(null);

            if (person != null) {
                if(person.getSub_position().equals(SubPosition.valueOf("PROJECTMANAGER")) || person.getOffice().equals(Office.valueOf("MANAGER"))) {
                    squad.setProject_manager(null);
                    person.setStatus(Status.valueOf("AVAILABLE"));
                    person.setSub_position(SubPosition.valueOf("PROJECTMANAGER"));
                } else if(person.getSub_position().equals(SubPosition.valueOf("TEACHLEAD"))) {
                    squad.setTeach_lead(null);
                    person.setStatus(Status.valueOf("AVAILABLE"));
                    person.setSub_position(SubPosition.valueOf("NOTAPPLICABLE"));
                } else {
                    person.setStatus(Status.valueOf("AVAILABLE"));
                    person.setSub_position(SubPosition.valueOf("NOTAPPLICABLE"));}
                personRepository.save(person);

                JobsDetails jobsDetails = person.getJobsDetails();

                if (jobsDetails != null) {
                    jobsDetails.setName_squad(null);
                    jobsDetails.setId_squad(null);
                    jobsDetails.setId_memberOfSquad(null);
                    jobDetailsRepository.save(jobsDetails);
                }
            }

            squadRepository.save(squad);
            membersSquadRepository.delete(memberToRemove);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



}


