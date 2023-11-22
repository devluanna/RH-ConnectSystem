package com.connect.system.rest.controller;

import com.connect.system.domain.model.System.Squad.MemberDashboardSquad;
import com.connect.system.domain.model.System.Squad.Members;
import com.connect.system.domain.model.System.Squad.Squad;
import com.connect.system.domain.repository.System.MembersSquadRepository;
import com.connect.system.service.AccountService;
import com.connect.system.service.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/squad")
public class SquadController {

    @Autowired
    SquadService squadService;

    @Autowired
    MembersSquadRepository membersSquadRepository;

    @Autowired
    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity createSquad(@RequestBody Squad squad, Long id_squad, MemberDashboardSquad memberDashboardSquad) {

        Squad newSquad = squadService.createSquad(id_squad, squad, memberDashboardSquad);

        return ResponseEntity.status(HttpStatus.CREATED).body(newSquad);
    }

    @PostMapping("/add/member/{id_dashboardMembers}/{id_squad}/{id}")
    public ResponseEntity<Members> addMemberToSquad(@PathVariable Long id_dashboardMembers, @PathVariable Long id_squad, @PathVariable Long id, Members members){
       Members addedMembers = squadService.addMemberToSquad(id_squad, id, id_dashboardMembers, members);

       if (addedMembers != null) {
         return ResponseEntity.ok(addedMembers);
      } else {
         return ResponseEntity.notFound().build();
     }
   }

    @PutMapping("/update/{id_squad}")
    public ResponseEntity updateSquad(@PathVariable Long id_squad, @RequestBody Squad squads, Members members, Long id) {

      Squad updatedSquad = squadService.toUpdateSquad(id_squad, squads, id);
      return ResponseEntity.ok(updatedSquad);}


    @GetMapping("/squad/{id_squad}")
    public ResponseEntity<Squad> getDashboard(@PathVariable Long id_squad) {
        Squad squads = squadService.getSquadsWithMembersById(id_squad);

        if(squads == null) {
           ResponseEntity.notFound().build();}

        return ResponseEntity.ok(squads);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Squad>> findSquads() {

        List<Squad> returnSquad = squadService.getAllSquads();

        if(returnSquad == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(returnSquad);

    }

    @DeleteMapping("/members/{id_squad}/{id_member}")

    public ResponseEntity<String> removeMemberFromSquad( @PathVariable Long id_squad, @PathVariable Long id_member) {
        try {

            squadService.removeMemberFromSquad(id_squad, id_member);
            return ResponseEntity.ok("Member removed successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}

