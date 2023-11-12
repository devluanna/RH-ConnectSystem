package com.connect.system.service.Impl;

import com.connect.system.domain.repository.User.PersonRepository;
import com.connect.system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements UserDetailsService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = personRepository.findByIdentityPerson(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }


    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        return accountService.findById(id);
    }
}
