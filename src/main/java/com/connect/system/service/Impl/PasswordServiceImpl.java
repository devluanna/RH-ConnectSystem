package com.connect.system.service.Impl;

import com.connect.system.domain.model.Account.EntityPerson.Person;
import com.connect.system.domain.model.Account.ResponseDTO.PasswordDTO;
import com.connect.system.domain.model.Account.ResponseDTO.RecoveryPasswordDTO;
import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.AccountService;
import com.connect.system.service.PasswordService;
import com.connect.system.utils.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MailConfig emailService;

    @Autowired
    AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Person recoverPassword(Person user, RecoveryPasswordDTO passDTO, String email) {

        Person existingUser = personRepository.findByEmail(passDTO.getEmail());

        if (existingUser != null) {
            String passwordUser = generateNewPassword();

            String encryptedPassword = passwordEncoder.encode(passwordUser);
            existingUser.setPassword(encryptedPassword);
            personRepository.save(existingUser);

            sendEmailWithNewPassword(existingUser, passwordUser);

            return existingUser;
        } else {
            throw new IllegalArgumentException("Email does not match any user's email.");
        }
    }

    public Person toUpdatePassword(Person updatedNewPassword, PasswordDTO passwordDTO, Integer id) {
        Person authenticatedUser = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!authenticatedUser.getId().equals(id)) {
            throw new IllegalArgumentException("Access denied!");
        }

        String newPassword = updatedNewPassword.getPassword();

        if (newPassword == null || newPassword.length() < 10) {
            throw new IllegalArgumentException("Password must be at least 10 characters.");
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        updatedNewPassword.setPassword(encryptedPassword);

        return personRepository.save(updatedNewPassword);
    }

    private void sendEmailWithNewPassword(Person savedUser, String passwordUser) {
        String subject = "Password recovery!";
        String emailBody = "Hello! " + savedUser.getName() + " " + savedUser.getLast_name() + ",\n\nWelcome back!\n\n" +
                "\n\nRemember to reset your password\n\n" +
                "Here is your new generated password:\n\n" +
                "Login identity: " + savedUser.getIdentityPerson() + "\n" +
                "Password: " + passwordUser;

        emailService.sendEmail(savedUser.getEmail(), subject, emailBody);
    }

    private String generateNewPassword() {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$#";
        StringBuilder password = new StringBuilder();
        int length = 10;

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }

        String passwordUser = password.toString();

        System.out.println("SENHA DO USUARIO ->> " + passwordUser);

        return password.toString();
    }

}
