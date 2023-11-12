package com.connect.system.rest.controller;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.AuthenticationDTO;
import com.connect.system.domain.model.Account.ResponseDTO.PasswordDTO;
import com.connect.system.domain.model.Account.ResponseDTO.RecoveryPasswordDTO;
import com.connect.system.domain.model.Account.ResponseDTO.ResponseTokenDTO;
import com.connect.system.infra.security.TokenService;
import com.connect.system.service.AccountService;
import com.connect.system.service.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    TokenService tokenService;

    @Autowired
    PasswordService passwordService;

    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data, Person person) {
        var auth = new UsernamePasswordAuthenticationToken(data.identityPerson(), data.password());
        var authentication = manager.authenticate(auth);

        var token = tokenService.generateToken((Person) authentication.getPrincipal());

        return ResponseEntity.ok(new ResponseTokenDTO(token));
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<String> updatingPassword(@PathVariable Long id, @RequestBody @Valid PasswordDTO passwordDTO) {
        Person user = accountService.findById(id);

        user.setPassword(passwordDTO.getPassword());
        passwordService.updatePassword(user, passwordDTO, id);

        return ResponseEntity.ok("Password updated successfully!");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody RecoveryPasswordDTO passDTO, String email, Person user) {

        passwordService.recoverPassword(user, passDTO, email);

        return ResponseEntity.ok("Please check your email. The new password has been sent!");
    }

}
