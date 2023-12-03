package com.connect.system.service.Impl;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
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

import com.connect.system.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SquadServiceImpl implements SquadService {


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


    @Override
    @Transactional
    public Squad createSquad(Integer id_squad, Squad squad, MemberDashboardSquad memberDashboardSquad) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            throw new AccessDeniedException("Apenas usuários com a ROLE MANAGER podem criar squads");
        }

        Person authenticatedUser = (Person) authentication.getPrincipal();
        Squad squadNew = new Squad(
                squad.getName_squad(), squad.getTeach_lead(), squad.getProject_manager(), squad.getType(), squad.getArea_squad(), squad.getCustomer_project(),
                squad.getCreatedAt(), memberDashboardSquad);


        memberDashboardSquad.setSquad(squadNew);
        Squad savedSquad = squadRepository.save(squadNew);

        Members newMember = createNewMember(savedSquad, memberDashboardSquad, authenticatedUser);
        membersSquadRepository.save(newMember);

        memberDashboardSquad.setSquad(savedSquad);
        dashboardMembersSquadRepository.save(memberDashboardSquad);

        squadNew.getAllMembers();
        validateAndUpdateProjectManagerField(squadNew);

        BeanUtils.copyProperties(savedSquad, squad);

        return squad;
    }

    private void validateAndUpdateProjectManagerField(Squad squadNew ) {
        if(squadNew.getProject_manager() == null) {
            for (Members member : squadNew.getAllMembers().getMembers()) {
                if (SubPosition.PROJECTMANAGER.name().equalsIgnoreCase(member.getSub_position())) {
                    squadNew.setProject_manager(member.getName() + " " + member.getLast_name());
                    squadRepository.save(squadNew);
                    break;
                }
            }
        }
    }

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
                throw new IllegalArgumentException("Member already added to the squad.");
            }

            changeSubPositionJobs(person);

            Members newMember = createNewMember(squad, allMembers, person);
            saveNewMember(newMember, squad, person);

            return newMember;
        } else {
            throw new IllegalArgumentException("Squad, Person, ou MemberDashboardSquad não encontrado.");
        }
    }


    private Members createNewMember(Squad squad, MemberDashboardSquad allMembers, Person person ) {
        JobsDetails jobsDetails = person.getJobsDetails();

        Members newMember = new Members();
        newMember.setId_member(person.getId());
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

        validateFieldNameSquadUser(jobsDetails, newMember);
        allMembers.addMembers(newMember);
        membersSquadRepository.save(newMember);

        squad.setAllMembers(allMembers);

        return newMember;
    }

    private void saveNewMember(Members newMember, Squad squad, Person person) {
        JobsDetails jobsDetails = person.getJobsDetails();
        validateFieldNameSquadUser(jobsDetails, newMember);
        jobDetailsRepository.save(jobsDetails);
        squadRepository.save(squad);

    }

    private boolean isMemberAlreadyInSquad(MemberDashboardSquad allMembers, Integer member_id) {
        return allMembers.getMembers().stream()
                .anyMatch(member -> member_id.equals(member.getId_account()));
    }


    private void changeSubPositionJobs(Person person) {
        if (person.getSub_position().equals(SubPosition.valueOf("NOTAPPLICABLE"))) {
            person.setSub_position(SubPosition.valueOf(String.valueOf(SubPosition.valueOf(String.valueOf(SubPosition.valueOf("MEMBER"))))));
            personRepository.save(person);
        }
    }

    private void changeStatusAndReportMe(Person person, Squad squad) {
        person.setReport_me(squad.getProject_manager());
        person.setStatus(Status.valueOf("ALLOCATED"));
        personRepository.save(person);
    }


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



    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Squad toUpdateSquad(Integer id_squad, Squad squads, Integer id) {

        Squad existingSquad = squadRepository.findById(id_squad).orElse(null);

        if (existingSquad != null) {

            String oldSquadName = existingSquad.getName_squad();
            Utils.copyNonNullProperties(squads, existingSquad);

            for (Members member : existingSquad.getAllMembers().getMembers()) {
                member.setName_squad(existingSquad.getName_squad());
                membersSquadRepository.save(member);

                Person person = personRepository.findById(member.getId_account()).orElse(null);

                editFieldTeachLeadSquad(existingSquad, id_squad);
                editFieldProjectManagerSquad(existingSquad, id_squad, person);

                if (person != null) {
                    JobsDetails jobsDetails = person.getJobsDetails();

                    if (jobsDetails != null && jobsDetails.getName_squad() != null && jobsDetails.getName_squad().equals(oldSquadName)) {
                        jobsDetails.setName_squad(existingSquad.getName_squad());
                        jobDetailsRepository.save(jobsDetails);
                    }
                }
            }
        }
        return squadRepository.save(existingSquad);
    }

    private void editFieldTeachLeadSquad(Squad squads, Integer id_squad) {
        Squad existingSquad = squadRepository.findById(id_squad).orElse(null);

        if (existingSquad != null) {
            String teachLeadName = squads.getTeach_lead();

            if (teachLeadName != null && !teachLeadName.isEmpty()) {
                boolean isTeachLeadValid = existingSquad.getAllMembers().getMembers()
                        .stream()
                        .anyMatch(member -> teachLeadName.equals(member.getName() + " " + member.getLast_name()));

                if (isTeachLeadValid) {
                    existingSquad.setTeach_lead(teachLeadName);
                } else {
                    throw new IllegalArgumentException("The provided teach_lead is not a valid squad member");
                }
            } else {
                throw new IllegalArgumentException("The teach_lead field is mandatory for the update");
            }
        }
    }

    private void editFieldProjectManagerSquad(Squad squads, Integer id_squad, Person person) {
        Squad existingSquad = squadRepository.findById(id_squad).orElse(null);

        if (existingSquad != null) {
            String projectManagerName = squads.getProject_manager();

            if (projectManagerName != null && !projectManagerName.isEmpty()) {
                boolean isProjectManagerValid = existingSquad.getAllMembers().getMembers()
                        .stream()
                        .anyMatch(member -> projectManagerName.equals(member.getName() + " " + member.getLast_name())
                                && member.getSub_position().equals(String.valueOf(SubPosition.PROJECTMANAGER)));

                if (isProjectManagerValid) {
                    existingSquad.setProject_manager(projectManagerName);


                    existingSquad.getAllMembers().getMembers()
                            .forEach(member -> {
                                if ("MEMBER".equals(member.getSub_position()) || "TEACHLEAD".equals(member.getSub_position())) {
                                    member.setReport_me(projectManagerName);

                                    Person account = personRepository.findById(member.getId_account()).orElse(null);
                                    if (account != null) {
                                        account.setReport_me(projectManagerName);
                                        personRepository.save(account);
                                    }
                                }
                            });
                } else {
                    throw new IllegalArgumentException("The provided project_manager is not a valid squad member");
                }
            } else {
                throw new IllegalArgumentException("The project_manager field is mandatory for the update");
            }
        }
    }


    private void validateFieldNameSquadUser(JobsDetails jobsDetails, Members newMember) {
        if(jobsDetails != null) {
            if (jobsDetails.getName_squad() == null) {
                jobsDetails.setName_squad(newMember.getName_squad());
            }

            if (jobsDetails.getId_squad() == null) {
                jobsDetails.setId_squad(newMember.getId_squad());
            }

            if (jobsDetails.getId_memberOfSquad() == null) {
                jobsDetails.setId_memberOfSquad(newMember.getId_member());
            }

            jobDetailsRepository.save(jobsDetails);
        }
    }

/*    private void validateAndUpdateTechLeadField(Squad squad) {
        if (squad.getTeach_lead() == null) {
        for (Members member : squad.getAllMembers().getMembers()) {
           if (SubPosition.TEACHLEAD.name().equalsIgnoreCase(member.getSub_position())) {
               squad.setTeach_lead(member.getName() + " " + member.getLast_name());
               squadRepository.save(squad);
               break;
           }
       }
   }}*/

}


